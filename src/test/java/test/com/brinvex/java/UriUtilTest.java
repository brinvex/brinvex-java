package test.com.brinvex.java;

import com.brinvex.java.net.UriUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("HttpUrlsUsage")
public class UriUtilTest {

    @Test
    public void extractDomainName() {
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("https://brinvex.com/path/"));
        assertEquals("dev.brinvex.com", UriUtil.extractDomainName("http://dev.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://brinvex.com"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://brinvex.com:18445/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("brinvex.com"));

        assertEquals("brinvex.com", UriUtil.extractDomainName("http://brinvex.com/path/"));

        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://WWW.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://WwW.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("https://www.brinvex.com/path/"));
        assertEquals("dev.brinvex.com", UriUtil.extractDomainName("http://www.dev.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www.brinvex.com"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www.brinvex.com:18445/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("www.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("www.brinvex.com"));

        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www2.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("https://www3.brinvex.com/path/"));
        assertEquals("dev.brinvex.com", UriUtil.extractDomainName("http://www4.dev.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www5.brinvex.com"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("http://www66.brinvex.com:18445/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("wwwABC.brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractDomainName("www123.brinvex.com"));
    }

    @Test
    public void extractSchemaAndHost() {
        assertEquals("http://brinvex.com", UriUtil.extractSchemeAndHost("http://brinvex.com/path/"));
        assertEquals("https://brinvex.com", UriUtil.extractSchemeAndHost("https://brinvex.com/path/"));
        assertEquals("http://dev.brinvex.com", UriUtil.extractSchemeAndHost("http://dev.brinvex.com/path/"));
        assertEquals("http://brinvex.com", UriUtil.extractSchemeAndHost("http://brinvex.com"));
        assertEquals("http://brinvex.com", UriUtil.extractSchemeAndHost("http://brinvex.com:18445/path/"));
        assertEquals("brinvex.com", UriUtil.extractSchemeAndHost("brinvex.com/path/"));
        assertEquals("brinvex.com", UriUtil.extractSchemeAndHost("brinvex.com"));

        assertEquals("http://brinvex.com", UriUtil.extractSchemeAndHost("http://brinvex.com/path/"));

        assertEquals("http://www.brinvex.com", UriUtil.extractSchemeAndHost("http://www.brinvex.com/path/"));
        assertEquals("http://WWW.brinvex.com", UriUtil.extractSchemeAndHost("http://WWW.brinvex.com/path/"));
        assertEquals("http://WwW.brinvex.com", UriUtil.extractSchemeAndHost("http://WwW.brinvex.com/path/"));
        assertEquals("https://www.brinvex.com", UriUtil.extractSchemeAndHost("https://www.brinvex.com/path/"));
        assertEquals("http://www.dev.brinvex.com", UriUtil.extractSchemeAndHost("http://www.dev.brinvex.com/path/"));
        assertEquals("http://www.brinvex.com", UriUtil.extractSchemeAndHost("http://www.brinvex.com"));
        assertEquals("http://www.brinvex.com", UriUtil.extractSchemeAndHost("http://www.brinvex.com:18445/path/"));
        assertEquals("www.brinvex.com", UriUtil.extractSchemeAndHost("www.brinvex.com/path/"));
        assertEquals("www.brinvex.com", UriUtil.extractSchemeAndHost("www.brinvex.com"));

        assertEquals("http://www2.brinvex.com", UriUtil.extractSchemeAndHost("http://www2.brinvex.com/path/"));
        assertEquals("https://www3.brinvex.com", UriUtil.extractSchemeAndHost("https://www3.brinvex.com/path/"));
        assertEquals("http://www4.dev.brinvex.com", UriUtil.extractSchemeAndHost("http://www4.dev.brinvex.com/path/"));
        assertEquals("https://www5.brinvex.com", UriUtil.extractSchemeAndHost("https://www5.brinvex.com"));
        assertEquals("https://www66.brinvex.com", UriUtil.extractSchemeAndHost("https://www66.brinvex.com:18445/path/"));
        assertEquals("wwwABC.brinvex.com", UriUtil.extractSchemeAndHost("wwwABC.brinvex.com/path/"));
        assertEquals("www123.brinvex.com", UriUtil.extractSchemeAndHost("www123.brinvex.com"));
    }
}
