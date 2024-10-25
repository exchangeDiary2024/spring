package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.group.id = :groupId AND YEAR(d.createdAt) = :year AND MONTH(d.createdAt) = :month ORDER BY d.createdAt")
    List<Diary> findAllByGroupYearAndMonth(Long groupId, int year, int month);
    @Query("SELECT d.id FROM Diary d WHERE d.group.id = :groupId AND CAST(d.createdAt AS DATE) = :date")
    Optional<Long> findIdByGroupAndDate(Long groupId, LocalDate date);
    @Query("SELECT d FROM Diary d WHERE d.group.id = :groupId AND CAST(d.createdAt AS DATE) = CURRENT_DATE")
    Optional<Diary> findTodayDiaryInGroup(Long groupId);
    @Query("SELECT count(d.id) > 0 FROM Diary d WHERE d.group.id = :groupId AND CAST(d.createdAt AS DATE) = CURRENT_DATE")
    Boolean existsTodayDiaryInGroup(Long groupId);
    void deleteByMemberId(Long memberId);
}
