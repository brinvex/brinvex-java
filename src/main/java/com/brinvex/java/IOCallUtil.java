package com.brinvex.java;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

public class IOCallUtil {

    public interface NoArgIOCall<O> {
        O call() throws IOException;
    }

    public interface IOCall<I, O> {
        O call(I input) throws IOException;
    }

    public static <I, O> O uncheckedIO(IOCall<I, O> ioCall, I input) {
        try {
            return ioCall.call(input);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("IOException occurred: type=[%s], msg=[%s], input=[%s]",
                    e.getClass().getName(), e.getMessage(), input), e);
        }
    }

    public static <I, O> Function<I, O> uncheckedIO(IOCall<I, O> fnc) {
        return input -> {
            try {
                return fnc.call(input);
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("IOException occurred: type=[%s], msg=[%s]",
                        e.getClass().getName(), e.getMessage()), e);
            }
        };
    }

    public static <I, O> O uncheckedIO(NoArgIOCall<O> ioCall) {
        try {
            return ioCall.call();
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("IOException occurred: type=[%s], msg=[%s]",
                    e.getClass().getName(), e.getMessage()), e);
        }
    }

}
