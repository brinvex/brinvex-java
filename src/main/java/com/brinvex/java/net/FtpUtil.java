package com.brinvex.java.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLConnection;

public class FtpUtil {

    public static String fetch(String url) {
        try {
            URLConnection urlConnection = URI.create(url).toURL().openConnection();
            try (InputStream is = urlConnection.getInputStream()) {
                String content = new String(is.readAllBytes());
                return content;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
