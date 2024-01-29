package org.fastcampus.oruryclient.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.S3Folder;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Repository s3Repository;
    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    public UserDto getUserDtoById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND));
        String imgUrl = s3Repository.getUrls(S3Folder.USER.getName(), user.getProfileImage())
                .get(0);
        return UserDto.from(user, imgUrl);
    }

    @Transactional
    public void updateProfileImage(UserDto userDto, MultipartFile image) throws BusinessException {
        //기존에 저장된 요소 삭제
        s3Repository.delete(S3Folder.USER.getName(), userDto.profileImage());
        var url = s3Repository.upload(S3Folder.USER.getName(), image);

        //MultipartFile이 오지 않은 경우 -> 유저의 프로필 이미지를 기본 이미지로 변경
        //MultipartFile이 오는 경우 -> 유저의 프로필 이미지를 업로드한 이미지로 변경
        String newProfileImage = url.isEmpty() ? defaultImage : url.get(0);
        userRepository.save(userDto.toEntity(newProfileImage));
    }

    @Transactional
    public void updateUserInfo(UserDto userDto) {
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void deleteUser(UserDto userDto) {
        s3Repository.delete(S3Folder.USER.getName(), userDto.profileImage());
        userRepository.delete(userDto.toEntity());
    }
}
