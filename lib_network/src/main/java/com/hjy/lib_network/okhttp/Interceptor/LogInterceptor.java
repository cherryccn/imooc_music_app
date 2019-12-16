/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hjy.lib_network.okhttp.Interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hjy.lib_network.BuildConfig;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public final class LogInterceptor implements Interceptor {

    private static class Holder {
        static final LogInterceptor INSTANCE = new LogInterceptor();
    }

    public static LogInterceptor get() {
        return Holder.INSTANCE;
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Gson mGson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public enum Level {
        NONE,
        BASIC,
        COMPLETE
    }

    public interface Logger {
        void log(String message);

        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    Log.d("OKHttpLogger", message);
                }
            }
        };
    }

    private LogInterceptor() {
        this(Logger.DEFAULT);
    }

    private LogInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Set<String> headersToRedact = Collections.emptySet();

    private volatile Level level = Level.BASIC;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;
        StringBuilder builder = new StringBuilder("OkHttp Logger\n");

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logHeaders = level == Level.COMPLETE;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        builder.append(requestStartMessage).append("\n");


        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                builder.append("Content-Type: " + requestBody.contentType()).append("\n");
            }
            if (requestBody.contentLength() != -1) {
                builder.append("Content-Length: " + requestBody.contentLength()).append("\n");
            }
        }

        if (logHeaders) {
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name)
                        && !"Content-Length".equalsIgnoreCase(name)) {
                    logHeader(builder, headers, i);
                }
            }
        } else {
            Headers headers = request.headers();
            if (headers.get("Authorization") != null) {
                builder.append("Authorization: " + headers.get("Authorization")).append("\n");
            }
        }

        if (!hasRequestBody) {
            builder.append("[no request body]").append("\n");
        } else if (bodyHasUnknownEncoding(request.headers())) {
            builder.append("[encoded body omitted]").append("\n");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                builder.append("[request body] ");
                assert charset != null;
                builder.append(buffer.readString(charset)).append("\n");
            } else {
                builder.append("[binary " + requestBody.contentLength() + "-byte body omitted]").append("\n");
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            builder.append("<-- HTTP FAILED: "
                    + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
                    + "ms "
                    + e).append("\n");
            logger.log(builder.toString());

//            if (e instanceof IOException) {
//                throw new HttpException(e, builder.toString());
//            }

            throw e;
        }

        ResponseBody responseBody = response.body();
        boolean hasBody = responseBody != null;

        builder.append("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : " " + response.message())
                + " "
                + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs) + "ms")
                .append("\n");

        Headers headers = response.headers();
        if (logHeaders) {
            for (int i = 0, count = headers.size(); i < count; i++) {
                logHeader(builder, headers, i);
            }
        } else {
            if (hasBody && responseBody.contentType() != null) {
                builder.append("Content-Type: " + responseBody.contentType()).append("\n");
            }
        }

        if (!hasBody || !HttpHeaders.hasBody(response)) {
            builder.append("[no response body]").append("\n");
        } else if (bodyHasUnknownEncoding(response.headers())) {
            builder.append("[encoded body omitted]").append("\n");
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                try (GzipSource gzipSource = new GzipSource(buffer.clone())) {
                    buffer = new Buffer();
                    buffer.writeAll(gzipSource);
                }
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                builder.append("[binary " + buffer.size() + "-byte body omitted]").append("\n");
                logger.log(builder.toString());
                return response;
            }
            long contentLength = responseBody.contentLength();
            if (contentLength != 0) {
                assert charset != null;
                String bodyString = buffer.clone().readString(charset);
                if (bodyString.startsWith("{") && bodyString.endsWith("}")) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(bodyString);
                        bodyString = mGson.toJson(element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (bodyString.startsWith("<!DOCTYPE html>")) {
                    bodyString = "[response body is html]";
                }
                builder.append(bodyString).append("\n");
            }
        }
        logger.log(builder.toString());

        return response;
    }

    private void logHeader(StringBuilder builder, Headers headers, int i) {
        String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
        builder.append(headers.name(i) + ": " + value).append("\n");
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }

}
