package com.exchangediary.comment.domain.entity;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.domain.entity.BaseEntity;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.reply.domain.entity.Reply;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor(access = PRIVATE)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @NotNull
    private final Double xPosition;
    @NotNull
    private final Double yPosition;
    @Lob
    @JdbcType(LongVarcharJdbcType.class)
    @NotNull
    private final String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", foreignKey = @ForeignKey(name = "comment_diary_id_fkey"))
    @NotNull
    private final Diary diary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "comment_member_id_fkey"))
    @NotNull
    private final Member member;
    @OneToMany(mappedBy = "comment")
    @OrderBy("created_at ASC")
    private List<Reply> replys;
}
