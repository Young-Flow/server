package com.pitchain.common.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class InfinityScrollRes<T> {
    @NotNull
    private List<T> content;
    @NotNull
    private Long lastElementId;
    @NotNull
    private Boolean hasNext;

    public static <T> InfinityScrollRes<T> createRes(List<T> data, Long lastElementId, boolean hasNext) {
        return new InfinityScrollRes<>(data, lastElementId, hasNext);
    }

    public boolean hasNext() {
        return hasNext;
    }
}
