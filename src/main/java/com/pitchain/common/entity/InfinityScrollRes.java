package com.pitchain.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
public class InfinityScrollRes<T> {
    @Getter
    private List<T> content;
    @Getter
    private Long lastElementId;
    private boolean hasNext;

    public static <T> InfinityScrollRes<T> createRes(List<T> data, Long lastElementId, boolean hasNext) {
        return new InfinityScrollRes<>(data, lastElementId, hasNext);
    }

    public boolean hasNext() {
        return hasNext;
    }
}
