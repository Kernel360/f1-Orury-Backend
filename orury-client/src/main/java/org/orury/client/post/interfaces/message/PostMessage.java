package org.orury.client.post.common.message;

import lombok.Getter;

@Getter
public enum PostMessage {
    POST_CREATED("게시글이 작성되었습니다."),
    POST_READ("게시글이 조회되었습니다."),
    POSTS_READ("게시글 목록이 조회되었습니다."),
    POST_UPDATED("게시글이 수정되었습니다."),
    POST_DELETED("게시글이 삭제되었습니다."),
    POST_LIKE_CREATED("게시글 좋아요가 생성되었습니다."),
    POST_LIKE_DELETED("게시글 좋아요가 삭제되었습니다.");

    private final String message;

    PostMessage(String message) {
        this.message = message;
    }
}
