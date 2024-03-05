package org.orury.admin.admin.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.admin.admin.application.AdminFacade;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
@RestController
public class AdminController {
    private final AdminFacade adminFacade;

    @Operation(summary = "어드민 조회", description = "adminId에 담긴 값으로 어드민을 조회합니다.")
    @GetMapping("/{adminId}")
    public AdminDto getAdmin(
            @PathVariable Long adminId
    ) {
        return adminFacade.getAdmin(adminId);
    }

    @Operation(summary = "어드민 전체 조회", description = "어드민을 전체 조회합니다.")
    @GetMapping
    public List<AdminDto> getAdmins() {
        return adminFacade.getAdmins();
    }

    @Operation(summary = "유저 전체 조회", description = "유저들을 전체 조회합니다.")
    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return adminFacade.getUsers();
    }

    @Operation(summary = "유저 삭제", description = "userId에 담긴 값으로 유저를 삭제합니다.")
    @DeleteMapping("/users/{userId}")
    public void deleteUser(
            @PathVariable Long userId
    ) {
        adminFacade.deleteUser(userId);
    }

    @Operation(summary = "게시글 삭제", description = "postId에 담긴 값으로 게시글을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public void deletePost(
            @PathVariable Long postId
    ) {
        adminFacade.deletePost(postId);
    }

    @Operation(summary = "게시글 전체 조회", description = "게시글을 전체 조회합니다.")
    @GetMapping("/posts")
    public List<PostDto> getPosts() {
        return adminFacade.getPosts();
    }

}
