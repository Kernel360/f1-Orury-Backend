package org.orury.client.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.global.image.ImageAsyncStore;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.orury.common.util.S3Folder.USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;
    private final ImageStore imageStore;
    private final ImageAsyncStore imageAsyncStore;
    private final PostStore postStore;
    private final CommentStore commentStore;
    private final ReviewStore reviewStore;
    private final GymStore gymStore;

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long id) {
        User user = userReader.findUserById(id)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND));
        return UserDto.from(user);
    }

    @Override
    @Transactional
    public void updateProfileImage(UserDto userDto, MultipartFile image) throws BusinessException {
        //기존에 저장된 요소 삭제
        imageStore.delete(USER, userDto.profileImage());
        imageUploadAndSave(userDto, image);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDto userDto) {
        userStore.save(userDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteUser(UserDto userDto) {
        gymStore.deleteGymLikesByUserId(userDto.id());
        commentStore.deleteCommentLikesByUserId(userDto.id());
        postStore.deletePostLikesByUserId(userDto.id());
        postStore.deletePostsByUserId(userDto.id());
        reviewStore.deleteReviewReactionsByUserId(userDto.id());
        var user = userDto.toEntity();
        user.setStatus(UserStatus.LEAVE);
        user.setProfileImage(null);
        imageStore.delete(USER, userDto.profileImage());
        userStore.save(user);
    }

    /**
     * 빈 파일이 들어왔는지에 대해서는 upload 메소드에서 유효성 검사해줌.
     */
    private void imageUploadAndSave(UserDto userDto, MultipartFile file) {
        String image = imageAsyncStore.upload(USER, file);
        var user = userDto.toEntity();
        user.setProfileImage(image);
        userStore.save(user);
    }
}
