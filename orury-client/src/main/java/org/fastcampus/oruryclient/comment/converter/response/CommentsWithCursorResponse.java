package org.fastcampus.oruryclient.domain.comment.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.global.constants.NumberConstants;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentsWithCursorResponse(
        List<CommentResponse> comments,
        Long cursor
) {
    public static CommentsWithCursorResponse of(List<CommentResponse> comments) {

        Long cursor = (comments.isEmpty())
                ? NumberConstants.LAST_CURSOR
                : comments.get(comments.size() - 1).id();

        return new CommentsWithCursorResponse(comments, cursor);
    }
}
