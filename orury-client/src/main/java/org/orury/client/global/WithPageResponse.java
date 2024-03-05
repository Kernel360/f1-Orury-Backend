package org.orury.client.global;

import java.util.List;

public record WithPageResponse<T>(
        List<T> list,
        int nextPage
) {
    public static <T> WithPageResponse of(List<T> list, int nextPage) {
        return new WithPageResponse(list, nextPage);
    }
}
