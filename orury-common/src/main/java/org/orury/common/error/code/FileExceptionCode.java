package org.orury.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileExceptionCode implements ErrorCode {
    FILE_UPLOAD_ERROR(880, "파일 업로드 오류"),
    FILE_DOWNLOAD_ERROR(888, "파일 다운로드 오류"),
    FILE_DELETE_ERROR(889, "파일 삭제 오류"),
    FILE_NOT_FOUND(890, "파일을 찾을 수 없습니다."),
    ;
    private final int status;
    private final String message;
}
