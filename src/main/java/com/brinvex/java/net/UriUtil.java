package com.brinvex.java.net;

@SuppressWarnings("DuplicatedCode")
public class UriUtil {

    public static String extractSchemeAndHost(String url) {
        int start = url.indexOf("://");
        if (start < 0) {
            start = 0;
        } else {
            start += 3;
        }
        int end = url.indexOf('/', start);
        if (end < 0) {
            end = url.length();
        }
        String schemaAndhost = url.substring(0, end);

        int port = schemaAndhost.indexOf(':', start);
        if (port >= 0) {
            schemaAndhost = schemaAndhost.substring(0, port);
        }
        return schemaAndhost;
    }

    public static String extractDomainName(String url) {
        int start = url.indexOf("://");
        if (start < 0) {
            start = 0;
        } else {
            start += 3;
        }
        int end = url.indexOf('/', start);
        if (end < 0) {
            end = url.length();
        }
        String domainName = url.substring(start, end);

        int port = domainName.indexOf(':');
        if (port >= 0) {
            domainName = domainName.substring(0, port);
        }
        domainName = domainName.replaceFirst("^[wW]{3}.*?\\.", "");
        return domainName;
    }


}
