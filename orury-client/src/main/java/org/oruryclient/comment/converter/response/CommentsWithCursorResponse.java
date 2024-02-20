package org.oruryclient.comment.converter.response;

import org.orurydomain.global.constants.NumberConstants;

import java.util.Comparator;
import java.util.List;

public record CommentsWithCursorResponse(
        List<CommentResponse> comments,
        Long cursor
) {
    public static CommentsWithCursorResponse of(List<CommentResponse> comments) {

        Long cursor = comments.stream()
                .filter(commentResponse -> commentResponse.parentId() == NumberConstants.PARENT_COMMENT)
                .map(CommentResponse::id)
                .max(Comparator.naturalOrder())
                .orElse(NumberConstants.LAST_CURSOR);

        return new CommentsWithCursorResponse(comments, cursor);
    }
}
