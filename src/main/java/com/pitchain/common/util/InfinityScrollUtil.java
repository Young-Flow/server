package com.pitchain.common.util;

import java.util.List;
import java.util.Optional;

public class InfinityScrollUtil {
    public static <T> Optional<T> getLastElement(List<T> content) {
        if (content.isEmpty()) return Optional.empty();

        T t = content.get(content.size() - 1);

        return Optional.ofNullable(t);
    }

    public static boolean hasNext(int contentSize, int pageSize) {
        return contentSize > pageSize;
    }
}
