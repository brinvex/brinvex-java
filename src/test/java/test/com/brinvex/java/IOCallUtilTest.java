package test.com.brinvex.java;

import com.brinvex.java.IOCallUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IOCallUtilTest {

    @Test
    public void wrapToUncheckedIO_nonExceptional() {
        Path path = Paths.get("./");
        List<Path> childPaths = IOCallUtil.uncheckedIO(Files::list, path).collect(Collectors.toList());
        assertNotNull(childPaths);
    }

    @Test
    public void wrapToUncheckedIO_exceptional() {
        Path path = Paths.get("./nonexistent");
        try {
            IOCallUtil.uncheckedIO(Files::list, path);
            Assertions.fail();
        } catch (UncheckedIOException ioException) {
            String expectedMsg = String.format(
                    "IOException occurred: type=[java.nio.file.NoSuchFileException], msg=[%s], input=[%s]", path, path);
            assertEquals(expectedMsg, ioException.getMessage());
        }
    }
}
