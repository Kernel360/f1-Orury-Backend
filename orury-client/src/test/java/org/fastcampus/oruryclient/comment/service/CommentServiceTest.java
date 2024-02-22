package org.fastcampus.oruryclient.comment.service;

import org.fastcampus.orurycommon.error.code.CommentErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.ImageUtils;
import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 댓글 테스트")
@ActiveProfiles("test")
class CommentServiceTest {

    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;
    private PostRepository postRepository;
    private ImageUtils imageUtils;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentLikeRepository = mock(CommentLikeRepository.class);
        postRepository = mock(PostRepository.class);
        imageUtils = mock(ImageUtils.class);
        commentService = new CommentService(commentRepository, commentLikeRepository, postRepository, imageUtils);
    }

    @Test
    @DisplayName("댓글이 생성되고 게시글의 댓글수가 증가되어야 한다")
    void should_CreateCommentAndIncreaseCommentCountOfPost() {
        // given
        Comment comment = createComment();

        // when
        commentService.createComment(CommentDto.from(comment));

        // then
        verify(commentRepository, times(1))
                .save(comment);
        verify(postRepository, times(1))
                .increaseCommentCount(comment.getPost()
                        .getId());
    }

    @Test
    @DisplayName("게시글에 따른 부모댓글과 자녀댓글이 정렬된 채로 조회되어야 한다.")
    void should_GetSortedCommentsOfPost() {
        // given
        Post post = createPost();
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, 10);

        Comment comment1 = createComment(1L, 0L);
        Comment comment2 = createComment(2L, 0L);
        Comment comment3 = createComment(3L, 0L);
        Comment comment1_1 = createComment(4L, 1L);
        Comment comment1_2 = createComment(5L, 1L);
        Comment comment3_1 = createComment(6L, 3L);
        Comment comment3_2 = createComment(7L, 3L);
        Comment comment3_3 = createComment(8L, 3L);
        Comment comment1_3 = createComment(9L, 1L);

        List<Comment> parentComments = List.of(
                comment1,
                comment2,
                comment3
        );
        List<Comment> childCommentsFor1 = List.of(
                comment1_1,
                comment1_2,
                comment1_3
        );
        List<Comment> childCommentsFor3 = List.of(
                comment3_1,
                comment3_2,
                comment3_3
        );
        String commentUserImage = "userProfileImage";
        List<CommentDto> expectedCommentDtos = List.of(
                CommentDto.from(comment1, commentUserImage),
                CommentDto.from(comment1_1, commentUserImage),
                CommentDto.from(comment1_2, commentUserImage),
                CommentDto.from(comment1_3, commentUserImage),
                CommentDto.from(comment2, commentUserImage),
                CommentDto.from(comment3, commentUserImage),
                CommentDto.from(comment3_1, commentUserImage),
                CommentDto.from(comment3_2, commentUserImage),
                CommentDto.from(comment3_3, commentUserImage)
        );

        when(commentRepository.findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(post.getId(), NumberConstants.PARENT_COMMENT, cursor, pageable))
                .thenReturn(parentComments);
        when(commentRepository.findByParentIdOrderByIdAsc(1L))
                .thenReturn(childCommentsFor1);
        when(commentRepository.findByParentIdOrderByIdAsc(2L))
                .thenReturn(List.of());
        when(commentRepository.findByParentIdOrderByIdAsc(3L))
                .thenReturn(childCommentsFor3);
        when(imageUtils.getUserImageUrl(commentUserImage))
                .thenReturn(commentUserImage);

        // when
        List<CommentDto> commentDtos = commentService.getCommentDtosByPost(PostDto.from(post), cursor, pageable);

        // then
        assertEquals(expectedCommentDtos, commentDtos);

        verify(commentRepository, times(1))
                .findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(post.getId(), NumberConstants.PARENT_COMMENT, cursor, pageable);
        verify(commentRepository, times(3))
                .findByParentIdOrderByIdAsc(anyLong());
    }

    @Test
    @DisplayName("cursor가 -1로 전체 목록 조회가 끝났음을 알리는 인자로 들어오면, 빈 배열을 응답한다.")
    void when_LastCursorComes_ReturnEmptyCommentList() {
        // given
        Post post = createPost();
        Long cursor = NumberConstants.LAST_CURSOR;
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<CommentDto> commentDtos = commentService.getCommentDtosByPost(PostDto.from(post), cursor, pageable);

        // then
        assertEquals(0, commentDtos.size());

        verify(commentRepository, never())
                .findByPostIdAndParentIdAndIdGreaterThanOrderByIdAsc(post.getId(), NumberConstants.PARENT_COMMENT, cursor, pageable);
        verify(commentRepository, never())
                .findByParentIdOrderByIdAsc(anyLong());
    }

    @Test
    @DisplayName("댓글이 수정되어야 한다")
    void should_UpdateComment() {
        // given
        Comment comment = createComment();

        // when
        commentService.updateComment(CommentDto.from(comment));

        // then
        verify(commentRepository, times(1))
                .save(comment);
    }

    @Test
    @DisplayName("댓글을 삭제처리하고, 그에 따른 댓글좋아요 삭제와 게시글의 댓글 수 감소가 정상적으로 수행되어야 한다")
    void should_DeleteCommentAndDeleteCommentLikeAndDecreaseCommentCountOfPost() {
        // given
        Comment comment = createComment();

        // when
        commentService.deleteComment(CommentDto.from(comment));

        // then
        verify(commentRepository, times(1))
                .save(comment);
        verify(commentLikeRepository, times(1))
                .deleteByCommentLikePK_CommentId(comment.getId());
        verify(postRepository, times(1))
                .decreaseCommentCount(comment.getId());
    }

    @Test
    @DisplayName("유저가 댓글 작성자인지 검증해야 한다.")
    void should_ValidateWhetherUserIsCommentCreator() {
        // given
        User user = createUser();
        Comment comment = createComment(user);

        // when
        commentService.isValidate(CommentDto.from(comment), UserDto.from(user));

        // then
        assertEquals(comment.getUser()
                .getId(), user.getId());
    }

    @Test
    @DisplayName("유저가 댓글 작성자가 아니면, FORBIDDEN 예외를 발생시킨다.")
    void when_UserIsNotCommentCreator_ForbiddenException() {
        // given
        User commentCreatorUser = createUser(1L);
        Comment comment = createComment(commentCreatorUser);
        User justUser = createUser(2L);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.isValidate(CommentDto.from(comment), UserDto.from(justUser)));

        assertEquals(CommentErrorCode.FORBIDDEN.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("검증할 댓글좋아요의 댓글이 유효한지 검증해야 한다.")
    void should_ValidateCommentOfCommentLike() {
        // given
        CommentLike commentLike = createCommentLike(createCommentLikePK());
        Comment comment = createComment();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));

        // when
        commentService.isValidate(CommentLikeDto.from(commentLike));

        // then
        verify(commentRepository, times(1))
                .findById(commentLike.getCommentLikePK()
                        .getCommentId());
    }

    @Test
    @DisplayName("검증할 댓글좋아요의 댓글이 존재하지 않으면, NOT_FOUND 예외를 발생시킨다.")
    void when_CommentOfCommentLikeDoesNoTExists_Then_NotFoundException() {
        // given
        CommentLike commentLike = createCommentLike(createCommentLikePK());

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.isValidate(CommentLikeDto.from(commentLike)));

        assertEquals(CommentErrorCode.NOT_FOUND.getStatus(), exception.getStatus());

        verify(commentRepository, times(1))
                .findById(commentLike.getCommentLikePK()
                        .getCommentId());
    }

    @Test
    @DisplayName("검증할 댓글좋아요의 댓글이 삭제처리 됐다면, FORBIDDEN 예외를 발생시킨다.")
    void when_CommentOfCommentLikeIsDeleted_Throw_ForbiddenException() {
        // given
        CommentLike commentLike = createCommentLike(createCommentLikePK());
        Comment comment = Comment.of(
                1L,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.isValidate(CommentLikeDto.from(commentLike)));

        assertEquals(CommentErrorCode.FORBIDDEN.getStatus(), exception.getStatus());

        verify(commentRepository, times(1))
                .findById(commentLike.getCommentLikePK()
                        .getCommentId());
    }

    @Test
    @DisplayName("부모 댓글 id가 0L이면, 아무런 수행을 하지 않는다.")
    void when_ParentCommentIdEqualsTo0L_Then_DoNothing() {
        // given
        Long parentCommentId = NumberConstants.PARENT_COMMENT;

        // when
        commentService.validateParentComment(parentCommentId);

        // then
        verify(commentRepository, never())
                .findById(parentCommentId);
    }

    @Test
    @DisplayName("부모 댓글 id가 유효한 값이면, 아무런 수행을 하지 않는다.")
    void when_ValidParentCommentId_Then_DoNothing() {
        // given
        Comment parentComment = createComment(1L, 0L);
        Long parentCommentId = parentComment.getId();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(parentComment));

        // when
        commentService.validateParentComment(parentCommentId);

        // then
        verify(commentRepository, times(1))
                .findById(parentCommentId);
    }

    @Test
    @DisplayName("부모 댓글 id가 존재하는 값이 아니면, NOT_FOUND 예외 발생")
    void when_NotExistingParentCommentId_Throw_NotFoundException() {
        // given
        Long parentCommentId = 2L;

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.validateParentComment(parentCommentId));

        assertEquals(CommentErrorCode.NOT_FOUND.getStatus(), exception.getStatus());

        verify(commentRepository, times(1))
                .findById(parentCommentId);
    }

    @Test
    @DisplayName("부모 댓글 id가 존재하지만 해당 id의 댓글이 자녀댓글로 판명나면, BAD_REQUEST 예외 발생")
    void when_ParentCommentIdTurnedOutToChildComment_Throw_BadRequestException() {
        // given
        Comment parentComment = createComment(2L, 1L);
        Long parentCommentId = parentComment.getId();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(parentComment));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.validateParentComment(parentCommentId));

        assertEquals(CommentErrorCode.BAD_REQUEST.getStatus(), exception.getStatus());

        verify(commentRepository, times(1))
                .findById(parentCommentId);
    }

    @Test
    @DisplayName("존재하는 댓글 id에 대해, CommentDto를 반환한다.")
    void when_ExistingCommentId_Then_ReturnCommentDto() {
        // given
        Comment comment = createComment();
        Long id = comment.getId();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));

        // when
        CommentDto commentDto = commentService.getCommentDtoById(id);

        // then
        assertEquals(id, commentDto.id());

        verify(commentRepository, times(1))
                .findById(id);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 id에 대해, NOT_FOUND 예외를 발생시킨다.")
    void when_NotExistingCommentId_Throw_NotFoundException() {
        // given
        Comment comment = createComment();
        Long id = comment.getId();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.getCommentDtoById(id));

        assertEquals(CommentErrorCode.NOT_FOUND.getStatus(), exception.getStatus());

        verify(commentRepository, times(1))
                .findById(id);
    }

    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서일 때 findByUserIdOrderByIdDesc을 실행한다.")
    void given_FirstCursor_when_getMyComments_then_successfully() {
        //given
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE);

        List<Comment> comments = Arrays.asList(
                createComment(10L, NumberConstants.PARENT_COMMENT),
                createComment(9L, NumberConstants.PARENT_COMMENT),
                createComment(8L, NumberConstants.PARENT_COMMENT),
                createComment(7L, NumberConstants.PARENT_COMMENT),
                createComment(6L, NumberConstants.PARENT_COMMENT),
                createComment(5L, NumberConstants.PARENT_COMMENT),
                createComment(4L, NumberConstants.PARENT_COMMENT),
                createComment(3L, NumberConstants.PARENT_COMMENT),
                createComment(2L, NumberConstants.PARENT_COMMENT),
                createComment(1L, NumberConstants.PARENT_COMMENT)
        );

        List<CommentDto> expectCommentDtos = comments.stream()
                .map(CommentDto::from)
                .toList();

        given(commentRepository.findByUserIdOrderByIdDesc(anyLong(), any())).willReturn(comments);

        //when
        List<CommentDto> resultCommentDtos = commentService.getCommentDtosByUserId(NumberConstants.USER_ID, cursor, pageable);

        //then
        assertEquals(expectCommentDtos, resultCommentDtos);
        then(commentRepository).should(times(1))
                .findByUserIdOrderByIdDesc(anyLong(), any());
        then(commentRepository).should(times(0))
                .findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서가 아닐 때 findByUserIdAndIdLessThanOrderByIdDesc을 실행한다.")
    void given_NotFirstCursor_when_getMyComments_then_successfully() {
        //given
        Long cursor = 20L;
        Pageable pageable = PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE);

        List<Comment> comments = Arrays.asList(
                createComment(10L, NumberConstants.PARENT_COMMENT),
                createComment(9L, NumberConstants.PARENT_COMMENT),
                createComment(8L, NumberConstants.PARENT_COMMENT),
                createComment(7L, NumberConstants.PARENT_COMMENT),
                createComment(6L, NumberConstants.PARENT_COMMENT),
                createComment(5L, NumberConstants.PARENT_COMMENT),
                createComment(4L, NumberConstants.PARENT_COMMENT),
                createComment(3L, NumberConstants.PARENT_COMMENT),
                createComment(2L, NumberConstants.PARENT_COMMENT),
                createComment(1L, NumberConstants.PARENT_COMMENT)
        );

        List<CommentDto> expectCommentDtos = comments.stream()
                .map(CommentDto::from)
                .toList();

        given(commentRepository.findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any())).willReturn(comments);

        //when
        List<CommentDto> resultCommentDtos = commentService.getCommentDtosByUserId(NumberConstants.USER_ID, cursor, pageable);

        //then
        assertEquals(expectCommentDtos, resultCommentDtos);
        then(commentRepository).should(times(0))
                .findByUserIdOrderByIdDesc(anyLong(), any());
        then(commentRepository).should(times(1))
                .findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    private static User createUser() {
        return User.of(
                1L,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static User createUser(Long id) {
        return User.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static Post createPost() {
        return Post.of(
                1L,
                "postTitle",
                "postContent",
                0,
                0,
                0,
                List.of(),
                1,
                createUser(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Comment createComment() {
        return Comment.of(
                1L,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Comment createComment(Long id, Long parentId) {
        return Comment.of(
                id,
                "commentContent",
                parentId,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.of(2024, 1, 1, 11, 50),
                LocalDateTime.of(2024, 1, 1, 11, 50)
        );
    }

    private static Comment createComment(User user) {
        return Comment.of(
                1L,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(),
                user,
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static CommentLikePK createCommentLikePK() {
        return CommentLikePK.of(
                1L,
                1L
        );
    }

    private static CommentLike createCommentLike(CommentLikePK commentLikePK) {
        return CommentLike.of(commentLikePK);
    }
}
