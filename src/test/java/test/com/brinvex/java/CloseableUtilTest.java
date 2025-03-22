package test.com.brinvex.java;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.brinvex.java.CloseableUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;

class CloseableUtilTest {

    @Test
    void applyAndClose_successful() throws Exception {
        // Create a mock AutoCloseable resource
        AutoCloseable resource = mock(AutoCloseable.class);
        String expected = "result";

        // Call the method with a function that returns a result
        String result = CloseableUtil.applyAndClose(resource, res -> expected);
        assertEquals(expected, result);

        // Verify that close() was called exactly once
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndClose_functionThrows_exception() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        RuntimeException functionException = new RuntimeException("Function exception");

        // When the function throws an exception, close() should still be called.
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.applyAndClose(resource, res -> {
                    throw functionException;
                })
        );
        // The exception from the function should be propagated
        assertSame(functionException, thrown);
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndClose_closeThrows_exception() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        String expected = "result";
        Exception closeException = new Exception("Close exception");

        // When close() fails and no main exception occurred, a RuntimeException should be thrown.
        doThrow(closeException).when(resource).close();

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.applyAndClose(resource, res -> expected)
        );
        assertTrue(thrown.getMessage().contains("Exception caught while closing resource"));
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndClose_functionAndCloseBothThrow_exceptionSuppressed() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        RuntimeException functionException = new RuntimeException("Function exception");
        Exception closeException = new Exception("Close exception");

        // Configure close() to throw an exception
        doThrow(closeException).when(resource).close();

        // When both the function and close() throw, the close exception is suppressed.
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.applyAndClose(resource, res -> {
                    throw functionException;
                })
        );
        assertSame(functionException, thrown);
        Throwable[] suppressed = thrown.getSuppressed();
        assertEquals(1, suppressed.length);
        assertEquals(closeException, suppressed[0]);
        verify(resource, times(1)).close();
    }

    @Test
    void useAndClose_successful() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);

        // Consumer that does nothing
        CloseableUtil.useAndClose(resource, res -> {
            // normal execution
        });
        verify(resource, times(1)).close();
    }

    @Test
    void useAndClose_consumerThrows_exception() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        RuntimeException consumerException = new RuntimeException("Consumer exception");

        // When the consumer throws an exception, close() should still be called.
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.useAndClose(resource, res -> {
                    throw consumerException;
                })
        );
        assertSame(consumerException, thrown);
        verify(resource, times(1)).close();
    }

    @Test
    void useAndClose_closeThrows_exceptionSuppressed() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        RuntimeException consumerException = new RuntimeException("Consumer exception");
        Exception closeException = new Exception("Close exception");

        // Make close() throw an exception
        doThrow(closeException).when(resource).close();

        // The consumer throws and the close exception is suppressed
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.useAndClose(resource, res -> {
                    throw consumerException;
                })
        );
        assertSame(consumerException, thrown);
        Throwable[] suppressed = thrown.getSuppressed();
        assertEquals(1, suppressed.length);
        assertEquals(closeException, suppressed[0]);
        verify(resource, times(1)).close();
    }

    @Test
    void useAndClose_closeThrows_noConsumerException() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        Exception closeException = new Exception("Close exception");

        // When no exception occurs in the consumer but close() fails,
        // a RuntimeException should be thrown.
        doThrow(closeException).when(resource).close();

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.useAndClose(resource, res -> {
                    // consumer does nothing
                })
        );
        assertTrue(thrown.getMessage().contains("Exception caught while closing resource"));
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndCloseThrowing_noException() throws Exception {
        // Arrange: Create a mock AutoCloseable resource.
        AutoCloseable resource = mock(AutoCloseable.class);

        // Act: Execute an action that returns a value without throwing an exception.
        String result = CloseableUtil.applyAndCloseThrowing(resource, r -> "success");

        // Assert: Verify that the returned value is correct and that resource.close() was called.
        assertEquals("success", result);

        verify(resource, times(1)).close();
    }

    @Test
    void useAndCloseThrowing_noException() throws Exception {
        // Arrange: Create a mock AutoCloseable resource.
        AutoCloseable resource = mock(AutoCloseable.class);

        // Act: Execute an action that does nothing.
        CloseableUtil.useAndCloseThrowing(resource, r -> { /* No exception thrown */ });

        // Assert: Verify that resource.close() was called.
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndCloseThrowing_actionThrows() throws Exception {
        // Arrange: Create a mock AutoCloseable resource.
        AutoCloseable resource = mock(AutoCloseable.class);
        IOException actionException = new IOException("Action failed");

        // Act & Assert: Expect the action exception to be thrown.
        IOException thrown = assertThrows(IOException.class, () ->
                CloseableUtil.applyAndCloseThrowing(resource, r -> {
                    throw actionException;
                })
        );
        assertEquals("Action failed", thrown.getMessage());

        // Verify that resource.close() was still called.
        verify(resource, times(1)).close();
    }

    @Test
    void useAndCloseThrowing_actionThrows() throws Exception {
        // Arrange: Create a mock AutoCloseable resource.
        AutoCloseable resource = mock(AutoCloseable.class);
        IOException actionException = new IOException("Action failed");

        // Act & Assert: Expect the action exception to be thrown.
        IOException thrown = assertThrows(IOException.class, () ->
                CloseableUtil.useAndCloseThrowing(resource, r -> {
                    throw actionException;
                })
        );
        assertEquals("Action failed", thrown.getMessage());

        // Verify that resource.close() was called.
        verify(resource, times(1)).close();
    }

    @Test
    void applyAndCloseThrowing_closeThrows() throws Exception {
        // Arrange: Create a mock AutoCloseable resource and simulate a close() failure.
        AutoCloseable resource = mock(AutoCloseable.class);
        IOException closeException = new IOException("Close failed");
        doThrow(closeException).when(resource).close();

        // Act & Assert: When the action succeeds but close() fails, the close exception should be thrown.
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.applyAndCloseThrowing(resource, r -> "result")
        );
        assertTrue(thrown.getMessage().endsWith("Close failed"));
        verify(resource, times(1)).close();
    }

    @Test
    void useAndCloseThrowing_closeThrows() throws Exception {
        // Arrange: Create a mock AutoCloseable resource and simulate a close() failure.
        AutoCloseable resource = mock(AutoCloseable.class);
        IOException closeException = new IOException("Close failed");
        doThrow(closeException).when(resource).close();

        // Act & Assert: Expect the close exception to be thrown when the action succeeds.
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                CloseableUtil.useAndCloseThrowing(resource, r -> { /* Action succeeds */ })
        );
        assertTrue(thrown.getMessage().endsWith("Close failed"));
        verify(resource, times(1)).close();
    }

    @Test
    void suppressedException_whenActionAndCloseThrow() throws Exception {
        // Arrange: Create a mock AutoCloseable resource.
        AutoCloseable resource = mock(AutoCloseable.class);
        IOException actionException = new IOException("Action failed");
        IOException closeException = new IOException("Close failed");

        // Configure resource.close() to throw an exception.
        doThrow(closeException).when(resource).close();

        // Act & Assert: Expect the action exception to be thrown, with the close exception added as suppressed.
        IOException thrown = assertThrows(IOException.class, () ->
                CloseableUtil.useAndCloseThrowing(resource, r -> {
                    throw actionException;
                })
        );
        assertEquals("Action failed", thrown.getMessage());
        Throwable[] suppressed = thrown.getSuppressed();
        assertEquals(1, suppressed.length);
        assertEquals("Close failed", suppressed[0].getMessage());

        // Optionally, verify the order of operations.
        InOrder inOrder = inOrder(resource);
        inOrder.verify(resource).close();
    }
}
