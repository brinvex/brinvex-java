package test.com.brinvex.java;

import com.brinvex.java.net.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpUtilTest {

    @Test
    void detectCharset() {
        Charset win1250 = Charset.forName("windows-1250");
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel; charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250; application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel;charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250;application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel; charset=windows-1250; name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel; name='history.csv'; charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250; application/msexcel; name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250; name='history.csv'; application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("name='history.csv'; application/msexcel; charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("name='history.csv'; charset=windows-1250; application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel;charset=windows-1250;name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel;name='history.csv';charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250;application/msexcel;name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("charset=windows-1250;name='history.csv';application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("name='history.csv';application/msexcel;charset=windows-1250")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("name='history.csv';charset=windows-1250;application/msexcel")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
    }

    @Test
    void detectCharset_quotedOrPadded() {
        Charset win1250 = Charset.forName("windows-1250");
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("text/html; charset=\"windows-1250\"")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("text/html; charset='windows-1250'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("text/html; charset=\"windows-1250\"; name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("text/html; charset=windows-1250 ; name='history.csv'")), (k, v) -> true);
            assertEquals(win1250, HttpUtil.detectCharset(h, UTF_8));
        }
        {
            HttpHeaders h = HttpHeaders.of(Map.of("Content-type", List.of("text/html")), (k, v) -> true);
            assertEquals(UTF_8, HttpUtil.detectCharset(h, UTF_8));
        }
    }

    @Test
    void appendCookies() {
        URI uri = URI.create("https://example.com");
        {
            HttpRequest.Builder b = HttpRequest.newBuilder(uri);
            HttpUtil.appendCookies(b, List.of(new HttpCookie("a", "1"), new HttpCookie("b", "2")));
            assertEquals(List.of("a=1; b=2"), b.build().headers().allValues("Cookie"));
        }
        {
            HttpRequest.Builder b = HttpRequest.newBuilder(uri);
            HttpUtil.appendCookies(b, List.of());
            assertEquals(List.of(), b.build().headers().allValues("Cookie"));
        }
    }
}
