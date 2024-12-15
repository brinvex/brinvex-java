package com.brinvex.java.net;

import java.net.HttpCookie;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpUtil {

    public static List<HttpCookie> extractCookiesFromRespHeaders(Map<String, List<String>> headers) {
        if (headers == null) {
            return Collections.emptyList();
        }
        List<String> setCookieHeaders = headers.get("Set-Cookie");
        if (setCookieHeaders == null) {
            return Collections.emptyList();
        }
        return setCookieHeaders
                .stream()
                .map(HttpCookie::parse)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<HttpCookie> extractCookiesFromRespHeaders(HttpHeaders headers) {
        if (headers == null) {
            return Collections.emptyList();
        }
        return extractCookiesFromRespHeaders(headers.map());
    }

    public static void appendCookies(HttpRequest.Builder reqBuilder, Collection<HttpCookie> cookies) {
        for (HttpCookie cookie : cookies) {
            reqBuilder.header("Cookie", cookie.getName() + "=" + cookie.getValue());
        }
    }

    /**
     * Get the Charset from the Content-encoding header. Defaults to UTF_8
     * <p>
     * See jdk.internal.net.http.common.Utils#charsetFrom(java.net.http.HttpHeaders)
     */
    public static Charset detectCharset(HttpHeaders headers) {
        Charset defaultCharset = StandardCharsets.UTF_8;
        Optional<String> typeOpt = headers.firstValue("Content-type");
        if (typeOpt.isEmpty()) {
            return defaultCharset;
        }
        String type = typeOpt.get();
        int i = type.indexOf(";");
        if (i >= 0) {
            type = type.substring(i + 1);
        }
        i = type.indexOf("charset=");
        if (i >= 0) {
            String value = type.substring(i + 8);
            return Charset.forName(value);
        } else {
            return defaultCharset;

        }
    }

    public static String detectEncoding(HttpHeaders headers) {
        return headers.firstValue("Content-Encoding").orElse(null);
    }

}
