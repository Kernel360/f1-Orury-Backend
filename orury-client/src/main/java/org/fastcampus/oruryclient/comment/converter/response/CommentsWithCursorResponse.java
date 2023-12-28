package org.fastcampus.oruryclient.comment.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.oruryclient.global.constants.NumberConstants;

import java.util.Comparator;
import java.util.List;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentsWithCursorResponse(
        List<CommentResponse> comments,
        Long cursor
) {
    public static CommentsWithCursorResponse of(List<CommentResponse> comments) {

        Long cursor = comments.stream()
                .filter(commentResponse -> commentResponse.parentId() == 0)
                .map(CommentResponse::id)
                .max(Comparator.naturalOrder())
                .orElse(NumberConstants.LAST_CURSOR);

        return new CommentsWithCursorResponse(comments, cursor);
    }
}
