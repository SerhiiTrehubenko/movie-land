package com.tsa.movieland.service.parallel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class ThreadExecutor {
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private final List<Callable<Object>> callables = Collections.synchronizedList(new ArrayList<>());

    public <T, U> void addMethodTwoParam(BiFunction<T, U, Object> biFunction, T firstParam, U secondParam) {
        callables.add(() -> biFunction.apply(firstParam, secondParam));
    }

    public <T> void addMethodOneParam(Function<T, Object> function, T param) {
        callables.add(() -> function.apply(param));
    }

    @SneakyThrows
    public ResultExtractor executeTasks() {
        List<Future<Object>> futures = Collections.synchronizedList(EXECUTOR_SERVICE.invokeAll(callables, 5, TimeUnit.SECONDS));
        return new ResultExtractor(futures);
    }
}
