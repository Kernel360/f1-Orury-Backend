package org.orury.client.global;

import org.orury.domain.global.constants.NumberConstants;

import java.util.List;

public record WithCursorResponse<T extends IdIdentifiable>(
        List<T> list,
        Long cursor
) {
    public static <T extends IdIdentifiable> WithCursorResponse<T> of(List<T> list, Long cursor) {
        Long newCursor = (list.isEmpty())
                ? determineCursorWhenEmpty(cursor)
                : list.get(list.size() - 1).id();
        return new WithCursorResponse<>(list, newCursor);
    }

    private static Long determineCursorWhenEmpty(Long cursor) {
        return cursor.equals(NumberConstants.FIRST_CURSOR)
                ? NumberConstants.NOTHING_CURSOR
                : NumberConstants.LAST_CURSOR;
    }
}
