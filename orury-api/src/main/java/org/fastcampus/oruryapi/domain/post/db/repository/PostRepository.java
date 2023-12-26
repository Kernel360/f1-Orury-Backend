package org.fastcampus.oruryapi.domain.post.db.repository;

import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByIdDesc(int category, Pageable pageable);

    List<Post> findByCategoryAndIdLessThanOrderByIdDesc(int category, Long id, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String titleSearchWord, String contentSearchWord, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingAndIdLessThanOrderByIdDesc(String titleSearchWord, String contentSearchWord, Long id, Pageable pageable);

    @Modifying
    @Query("UPDATE post SET viewCount = viewCount + 1 WHERE id = :id")
    void updateViewCount(Long id);
}