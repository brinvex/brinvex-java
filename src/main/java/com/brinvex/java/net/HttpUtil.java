package com.brinvex.java.net;

import java.net.HttpCookie;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        if (cookies.isEmpty()) {
            return;
        }
        String cookieHeader = cookies.stream()
                .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .collect(Collectors.joining("; "));
        reqBuilder.header("Cookie", cookieHeader);
    }

    /**
     * Get the Charset from the Content-type header.
     * <p>
     * See jdk.internal.net.http.common.Utils#charsetFrom(java.net.http.HttpHeaders)
     */
    public static Charset detectCharset(HttpHeaders headers, Charset defaultCharset) {
        String s = headers.firstValue("Content-type").orElse(null);
        if (s == null) {
            return defaultCharset;
        }
        int i = s.indexOf("charset=");
        if (i >= 0) {
            s = s.substring(i + 8);
            i = s.indexOf(";");
            if (i >= 0) {
                s = s.substring(0, i);
            }
            s = s.strip();
            if (s.length() >= 2 && (s.charAt(0) == '"' || s.charAt(0) == '\'') && s.charAt(s.length() - 1) == s.charAt(0)) {
                s = s.substring(1, s.length() - 1);
            }
            return Charset.forName(s);
        } else {
            return defaultCharset;
        }
    }

    public static String detectEncoding(HttpHeaders headers) {
        return headers.firstValue("Content-Encoding").orElse(null);
    }

}
