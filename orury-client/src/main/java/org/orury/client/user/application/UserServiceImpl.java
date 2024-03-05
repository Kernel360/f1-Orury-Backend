package org.orury.client.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.ImageUrlConverter;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;
    private final ImageReader imageReader;
    private final ImageStore imageStore;
    private final PostStore postStore;
    private final CommentStore commentStore;
    private final ReviewStore reviewStore;
    private final GymStore gymStore;

    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long id) {
        User user = userReader.findUserById(id)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND));
        return transferUserDto(user);
    }

    @Override
    @Transactional
    public void updateProfileImage(UserDto userDto, MultipartFile image) throws BusinessException {
        //기존에 저장된 요소 삭제
        imageStore.delete(userDto.profileImage());
        imageUploadAndSave(userDto, image);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDto userDto) {
        userStore.save(userDto.toEntity(ImageUrlConverter.splitUrlToImage(userDto.profileImage())));
    }

    @Override
    @Transactional
    public void deleteUser(UserDto userDto) {
        gymStore.deleteGymLikesByUserId(userDto.id());
        commentStore.deleteCommentLikesByUserId(userDto.id());
        postStore.deletePostLikesByUserId(userDto.id());
        postStore.deletePostsByUserId(userDto.id());
        reviewStore.deleteReviewReactionsByUserId(userDto.id());
        imageStore.delete(userDto.profileImage());
        var deletingUser = userDto.toEntity().delete(defaultImage);
        userStore.save(deletingUser);
    }

    @Override
    public List<UserDto> getUsers() {
        return userReader.findAll().stream().map(this::transferUserDto).toList();
    }

    private UserDto transferUserDto(User user) {
        var profileLink = imageReader.getUserImageLink(user.getProfileImage());
        return UserDto.from(user, profileLink);
    }

    private void imageUploadAndSave(UserDto userDto, MultipartFile file) {
        // 빈 파일이 들어왔는지에 대해서는 upload 메소드에서 유효성 검사해줌.
        String image = imageStore.upload(file);
        userStore.save(userDto.toEntity(image));
    }
}
