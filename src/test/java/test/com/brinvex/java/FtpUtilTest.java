package test.com.brinvex.java;

import com.brinvex.java.net.FtpUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FtpUtilTest {

    @Test
    void fetch(@TempDir Path dir) throws IOException {
        Path file = Files.writeString(dir.resolve("my file.txt"), "hello");
        assertEquals("hello", FtpUtil.fetch(file.toUri().toString()));
    }

    @Test
    void fetch_missingFile(@TempDir Path dir) {
        String url = dir.resolve("missing.txt").toUri().toString();
        assertThrows(UncheckedIOException.class, () -> FtpUtil.fetch(url));
    }
}
