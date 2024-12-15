package test.com.brinvex.java;

import com.brinvex.java.LazyConstant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/*
todo 1 - Avoid a (harmless) Warning: Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
https://github.com/mockito/mockito/issues/2590
https://github.com/mockito/mockito/issues/3111
 */
/*
todo 1 - Upgrade Mockito (and ByteBuddy) to support Java version 21+
*/
public class LazyConstantTest {

    @EnabledForJreRange(max = JRE.JAVA_20)
    @Test
    public void whenCalledMultipleTimes_thenShouldBeCalledOnlyOnce() {

        @SuppressWarnings("unchecked") Supplier<String> mockedExpensiveFunction = Mockito.mock(Supplier.class);
        Mockito.when(mockedExpensiveFunction.get())
                .thenReturn("ok");
        LazyConstant<String> testee = LazyConstant.nonThreadSafe(mockedExpensiveFunction);
        Mockito.verify(mockedExpensiveFunction, Mockito.never())
                .get();
        testee.get();
        testee.get();
        Mockito.verify(mockedExpensiveFunction, Mockito.times(1))
                .get();
    }

    @EnabledForJreRange(max = JRE.JAVA_20)
    @Test
    public void whenCalledMultipleTimesConcurrently_thenShouldBeCalledMultipleTimes() throws InterruptedException {
        @SuppressWarnings("unchecked") Supplier<String> mockedExpensiveFunction = Mockito.mock(Supplier.class);
        Mockito.when(mockedExpensiveFunction.get())
                .thenAnswer((Answer<?>) invocation -> {
                    Thread.sleep(1000L);
                    return "ok";
                });
        LazyConstant<String> testee = LazyConstant.nonThreadSafe(mockedExpensiveFunction);
        Mockito.verify(mockedExpensiveFunction, Mockito.never())
                .get();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.invokeAll(List.of(testee::get, testee::get));
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        Mockito.verify(mockedExpensiveFunction, Mockito.times(2))
                .get();
    }

    @EnabledForJreRange(max = JRE.JAVA_20)
    @Test
    public void whenCalledMultipleTimesConcurrently_thenShouldBeCalledOnlyOnce() throws InterruptedException {
        @SuppressWarnings("unchecked") Supplier<String> mockedExpensiveFunction = Mockito.mock(Supplier.class);
        Mockito.when(mockedExpensiveFunction.get())
                .thenAnswer((Answer<?>) invocation -> {
                    Thread.sleep(1000L);
                    return "ok";
                });
        LazyConstant<String> testee = LazyConstant.threadSafe(mockedExpensiveFunction);
        Mockito.verify(mockedExpensiveFunction, Mockito.never())
                .get();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.invokeAll(List.of(testee::get, testee::get));
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        Mockito.verify(mockedExpensiveFunction, Mockito.times(1))
                .get();
    }
}
