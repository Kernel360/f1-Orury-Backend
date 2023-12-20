package org.fastcampus.oruryapi.domain.post.db.repository;

import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByIdDesc(int category, Pageable pageable);

    List<Post> findByCategoryAndIdLessThanOrderByIdDesc(int category, Long id, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String titleSearchWord, String contentSearchWord, Pageable pageable);

    List<Post> findByTitleContainingOrContentContainingAndIdLessThanOrderByIdDesc(String titleSearchWord, String contentSearchWord, Long id, Pageable pageable);
}