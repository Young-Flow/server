package com.pitchain.repository;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Comment;
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
