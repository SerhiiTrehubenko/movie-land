package com.tsa.movieland.service.parallel;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class ResultExtractor {
    private final List<Future<Object>> futures;

    @SneakyThrows
    public <T> T getObject(Class<T> requiredObject) {
        int count = 0;
        for (Future<Object> executedTask : futures) {
            if (notNull(executedTask ) && isRequiredType(executedTask, requiredObject)) {
                Object result = executedTask.get();
                    futures.set(count, null);
                    return requiredObject.cast(result);
            }
            count++;
        }
        throw new RuntimeException("Required Object was not found");
    }
    @SneakyThrows
    private <T> boolean isRequiredType(Future<Object> executedTask, Class<T> requiredObject) {
        Object result = executedTask.get();
        return requiredObject.isAssignableFrom(result.getClass());
    }

    private boolean notNull(Future<Object> executedTask) {
        return Objects.nonNull(executedTask);
    }

    @SneakyThrows
    public <U> List<U> getList(Class<U> requiredEntry) {
        int count = 0;
        for (Future<Object> executedTask : futures) {
            if (notNull(executedTask) && isList(executedTask)) {
                List<?> resultList = (List<?>) executedTask.get();
                if (hasEntry(resultList) && entryCoincides(resultList, requiredEntry)) {
                    futures.set(count, null);
                    @SuppressWarnings("unchecked")
                    List<U> requiredList = (List<U>) resultList;
                    return requiredList;
                }
            }
            count++;
        }
        return Collections.emptyList();
    }

    @SneakyThrows
    private boolean isList(Future<Object> executedTask) {
        Object result = executedTask.get();
        return List.class.isAssignableFrom(result.getClass());
    }

    private boolean hasEntry(List<?> resultList) {
        return resultList.size() != 0;
    }

    private <U> boolean entryCoincides(List<?> resultList, Class<U> requiredEntry) {
        Object entryFrom = resultList.get(0);
        return requiredEntry.isAssignableFrom(entryFrom.getClass());
    }
}
