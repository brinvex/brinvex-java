package test.com.brinvex.java;

import com.brinvex.java.net.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpUtilTest {

    @Test
    void detectCharset() {
        {
            HttpHeaders headers = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel; charset=windows-1250")), (k, v) -> true);
            assertEquals(Charset.forName("windows-1250"), HttpUtil.detectCharset(headers));
        }
        {
            HttpHeaders headers = HttpHeaders.of(Map.of("Content-type", List.of("application/msexcel; charset=windows-1250; name='Obchody.csv'")), (k, v) -> true);
            assertEquals(Charset.forName("windows-1250"), HttpUtil.detectCharset(headers));
        }
    }
}
