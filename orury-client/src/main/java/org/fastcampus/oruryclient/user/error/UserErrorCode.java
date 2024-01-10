<<<<<<< HEAD:orury-common/src/main/java/org/fastcampus/orurycommon/error/code/UserErrorCode.java
package org.fastcampus.orurycommon.error.code;
=======
package org.fastcampus.oruryclient.user.error;
>>>>>>> 05549cbcd075e2a92b992f0f6f75aa4db3352544:orury-client/src/main/java/org/fastcampus/oruryclient/user/error/UserErrorCode.java

import lombok.AllArgsConstructor;
import org.fastcampus.oruryclient.global.error.code.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저를 찾을 수 없습니다."),
    INVALID_USER(HttpStatus.NOT_FOUND.value(), "유효하지 않은 계정입니다."),
    DUPLICATED_USER(HttpStatus.CONFLICT.value(), "이미 등록된 아이디입니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
