package com.exchangediary.comment.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT count(c) > 0 FROM Comment c WHERE c.diary.id = :diaryId AND c.member.id = :memberId")
    Boolean existsByDiaryAndMember(Long memberId, Long diaryId);
}
