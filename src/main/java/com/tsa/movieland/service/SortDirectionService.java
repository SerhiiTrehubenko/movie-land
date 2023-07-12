package com.tsa.movieland.service;

import com.tsa.movieland.domain.SortDirection;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SortDirectionService {

    public SortDirection getSortDirection(Map<String, String> params) {
        if (!params.isEmpty()) {
            return params.entrySet().stream()
                    .map(this::extract)
                    .findFirst()
                    .get();
        }
        return getEmptySortDirection();
    }

    private SortDirection extract(Map.Entry<String, String> entry) {
        return SortDirection.builder()
                .typeOfSorting(entry.getKey())
                .direction(entry.getValue())
                .build();
    }

    private SortDirection getEmptySortDirection() {
        return SortDirection.builder()
                .typeOfSorting("empty")
                .direction("empty")
                .build();
    }
}
