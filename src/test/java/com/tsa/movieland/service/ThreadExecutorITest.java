package com.tsa.movieland.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ThreadExecutorITest {
    private final ThreadExecutor executor = new ThreadExecutor();

    @SneakyThrows
    private void sleep(long second) {
        Thread.sleep(second);
    }

    @Test
    void shouldReturnObjectResultOfOneExecutedTask() {
        Function<String, Object> method = (paramOne) -> String.join("", "\"", paramOne, "\"");
        executor.addMethodOneParam(method, "Hello World");

        final ResultExtractor resultExtractor = executor.executeTasks();
        final String result = resultExtractor.getObject(String.class);

        assertEquals("\"Hello World\"", result);
    }

    @Test
    void shouldReturnThrowExceptionWhenAskedObjectButListOfExecutionTasksIsEmpty() {
        final ResultExtractor resultExtractor = executor.executeTasks();
        assertThrows(RuntimeException.class, () -> resultExtractor.getObject(Integer.class));
    }

    @Test
    void shouldReturnThrowExceptionWhenObjectResultOfOneExecutedTaskIsAbsent() {
        Function<String, Object> method = (paramOne) -> String.join("", "\"", paramOne, "\"");
        executor.addMethodOneParam(method, "Hello World");

        final ResultExtractor resultExtractor = executor.executeTasks();

        assertThrows(RuntimeException.class, () -> resultExtractor.getObject(Integer.class));
    }

    @Test
    void shouldReturnObjectResultsOfTwoExecutedTask() {
        Function<String, Object> methodString = (paramOne) -> String.join("", "\"", paramOne, "\"");
        Function<Integer, Object> methodInteger = (paramOne) -> paramOne * 20;

        executor.addMethodOneParam(methodString, "Hello World");
        executor.addMethodOneParam(methodInteger, 20);

        final ResultExtractor resultExtractor = executor.executeTasks();
        final String resultString = resultExtractor.getObject(String.class);
        final Integer resultInteger = resultExtractor.getObject(Integer.class);

        assertEquals("\"Hello World\"", resultString);
        assertEquals(400, resultInteger);
    }

    @Test
    void shouldReturnListResultOfOneExecutedTask() {
        String param = "Hello World";
        Function<String, Object> method = (paramOne) -> List.of(paramOne, paramOne, paramOne);
        executor.addMethodOneParam(method, param);

        final ResultExtractor resultExtractor = executor.executeTasks();
        final List<String> result = resultExtractor.getList(String.class);

        assertEquals(3, result.size());
        assertEquals(param, result.get(0));
        assertEquals(param, result.get(1));
        assertEquals(param, result.get(2));
    }

    @Test
    void shouldReturnListResultOfTwoExecutedTask() {
        String paramString = "Hello World";
        Integer paramInteger = 10;
        Function<String, Object> methodListString = (paramOne) -> List.of(paramOne, paramOne, paramOne);
        Function<Integer, Object> methodIntegerString = (paramOne) -> List.of(paramOne, paramOne, paramOne);
        executor.addMethodOneParam(methodListString, paramString);
        executor.addMethodOneParam(methodIntegerString, paramInteger);

        final ResultExtractor resultExtractor = executor.executeTasks();
        final List<String> resultListString = resultExtractor.getList(String.class);

        assertEquals(3, resultListString.size());
        assertEquals(paramString, resultListString.get(0));
        assertEquals(paramString, resultListString.get(1));
        assertEquals(paramString, resultListString.get(2));

        final List<Integer> result = resultExtractor.getList(Integer.class);

        assertEquals(3, result.size());
        assertEquals(paramInteger, result.get(0));
        assertEquals(paramInteger, result.get(1));
        assertEquals(paramInteger, result.get(2));
    }

    @Test
    void shouldReturnObjectAndListResultOfExecutedTasks() {
        String param = "Hello World";
        Function<String, Object> method = (paramOne) -> List.of(paramOne, paramOne, paramOne);
        Function<String, Object> methodString = (paramOne) -> String.join("", "\"", paramOne, "\"");
        Function<Integer, Object> methodInteger = (paramOne) -> paramOne * 20;

        executor.addMethodOneParam(method, param);
        executor.addMethodOneParam(methodString, param);
        executor.addMethodOneParam(methodInteger, 20);

        final ResultExtractor resultExtractor = executor.executeTasks();
        final String resultString = resultExtractor.getObject(String.class);
        final Integer resultInteger = resultExtractor.getObject(Integer.class);
        final List<String> resultList = resultExtractor.getList(String.class);

        assertEquals("\"Hello World\"", resultString);
        assertEquals(400, resultInteger);

        assertEquals(3, resultList.size());
        assertEquals(param, resultList.get(0));
        assertEquals(param, resultList.get(1));
        assertEquals(param, resultList.get(2));
    }

    @Test
    void shouldThrowExceptionWhenExecutionTimeOfTasksExceedsFiveSecond() {
        long fiveSeconds = 5000;
        long sixSeconds = 6000;
        Function<Integer, Object> methodInteger =
                (paramOne) -> {
                    sleep(sixSeconds);
                    return paramOne * 20;
                };

        executor.addMethodOneParam(methodInteger, 20);

        final long start = System.currentTimeMillis();
        final ResultExtractor resultExtractor = executor.executeTasks();
        assertThrows(CancellationException.class, () -> resultExtractor.getObject(Integer.class));
        final long finish = System.currentTimeMillis();
        long resultTime = finish - start;
        assertTrue(resultTime >= fiveSeconds && resultTime < sixSeconds);
    }
}