package com.tsa.movieland.common;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
@Slf4j
@Service
public class AsyncExecutor {
    @Async
    @SneakyThrows
    public <V> CompletableFuture<V> executeTask(Supplier<V> supplier) {
        log.trace(Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(supplier).orTimeout(5, TimeUnit.SECONDS);
    }
}
