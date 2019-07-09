/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.dao.repository;

import com.mtons.mblog.entity.jpa.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * @author langhsu
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
	Page<Comment> findAll(Pageable pageable);
	Page<Comment> findAllByPostId(Pageable pageable, long postId);
	Page<Comment> findAllByAuthorId(Pageable pageable, long authorId);
	List<Comment> removeByIdIn(Collection<Long> ids);
	List<Comment> removeByPostId(long postId);
	long countByAuthorIdAndPostId(long authorId, long postId);
}
