package org.fastcampus.oruryclient.post.converter.message;

import lombok.Getter;

@Getter
public enum PostMessage {
    POST_CREATED("게시글이 정상적으로 작성되었습니다."),
    POST_READ("게시글을 정상적으로 조회했습니다."),
    POSTS_READ("게시글 카테고리를 정상적으로 조회했습니다."),
    POST_UPDATED("게시글 수정이 완료되었습니다."),
    POST_DELETED("게시글 삭제가 완료되었습니다."),
    POST_LIKE_CREATED("게시글 좋아요가 완료되었습니다."),
    POST_LIKE_DELETED("게시글 좋아요를 취소했습니다.");

    private final String message;

    PostMessage(String message) {
        this.message = message;
    }
}
