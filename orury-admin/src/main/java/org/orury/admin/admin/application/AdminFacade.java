package org.orury.admin.admin.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.admin.domain.AdminService;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.post.domain.PostService;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminFacade {
    private final AdminService adminService;
    private final PostService postService;

    public AdminDto getAdmin(Long adminId) {
        return adminService.getAdmin(adminId);
    }

    public List<AdminDto> getAdmins() {
        return adminService.getAdmins();
    }

    public List<UserDto> getUsers() {
        return adminService.getUsers();
    }

    public void deletePost(Long postId) {
        var post = postService.getPostDtoById(postId);
        postService.deletePost(post);
    }

    public void deleteUser(Long userId) {
        adminService.deleteUser(userId);
    }

    public List<PostDto> getPosts() {
        return postService.getPosts();
    }
}