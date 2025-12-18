package test.com.brinvex.java;

import com.brinvex.java.net.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
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
}
