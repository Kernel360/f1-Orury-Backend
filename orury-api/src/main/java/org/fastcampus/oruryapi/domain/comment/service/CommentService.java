package org.fastcampus.oruryapi.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.comment.converter.dto.CommentDto;
import org.fastcampus.oruryapi.domain.comment.converter.request.CommentCreateRequest;
import org.fastcampus.oruryapi.domain.comment.converter.request.CommentUpdateRequest;
import org.fastcampus.oruryapi.domain.comment.converter.response.ChildCommentResponse;
import org.fastcampus.oruryapi.domain.comment.converter.response.CommentResponse;
import org.fastcampus.oruryapi.domain.comment.converter.response.CommentsWithCursorResponse;
import org.fastcampus.oruryapi.domain.comment.db.model.Comment;
import org.fastcampus.oruryapi.domain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.oruryapi.domain.comment.db.repository.CommentRepository;
import org.fastcampus.oruryapi.domain.comment.error.CommentErrorCode;
import org.fastcampus.oruryapi.domain.comment.util.CommentMessage;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.post.db.repository.PostRepository;
import org.fastcampus.oruryapi.domain.post.error.PostErrorCode;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.constants.NumberConstants;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
}
