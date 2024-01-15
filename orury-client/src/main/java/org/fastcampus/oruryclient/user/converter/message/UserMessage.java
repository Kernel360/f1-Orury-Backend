package org.fastcampus.oruryclient.user.converter.message;

import lombok.Getter;

@Getter
public enum UserMessage {

    USER_DELETED("회원 탈퇴 되었습니다."),
    USER_UPDATED("유저 정보가 수정되었습니다."),
    USER_PROFILEIMAGE_UPDATED("프로필 사진이 수정되었습니다."),
    USER_READ("마이페이지를 조회했습니다.");

    private final String message;

    UserMessage(String message) {
        this.message = message;
    }


}
