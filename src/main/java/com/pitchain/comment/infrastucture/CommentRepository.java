package com.pitchain.comment.infrastucture;

import com.pitchain.bm.domain.Bm;
import com.pitchain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT DISTINCT c FROM Comment c " +
            "LEFT JOIN FETCH c.member " +
            "WHERE c.parentComment IS NULL " +
            "AND c.bm = :bm")
    List<Comment> findAllByBm(@Param("bm") Bm bm);

    @Query("SELECT c FROM Comment c " +
           "LEFT JOIN FETCH c.member " +
           "WHERE c.parentComment = :parentComment")
    List<Comment> findByParentComment(@Param("parentComment") Comment parentComment);

}
