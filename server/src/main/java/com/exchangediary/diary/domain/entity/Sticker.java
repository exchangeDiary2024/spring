package com.exchangediary.diary.domain.entity;

import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.exchangediary.global.domain.entity.BaseEntity;
import com.exchangediary.global.domain.entity.StaticImage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor(access = PRIVATE)
public class Sticker extends BaseEntity {
    @Id
    @Column(name = "sticker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final Double coordX;
    private final Double coordY;
    private final Integer coordZ;
    private final Double width;
    private final Double height;
    private final Double rotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private final Diary diary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private final StaticImage staticImage;

    public static Sticker of(
            StickerRequest stickerRequest,
            int coordZ,
            Diary diary,
            StaticImage staticImage) {
        return Sticker.builder()
                .coordX(stickerRequest.coordX())
                .coordY(stickerRequest.coordY())
                .coordZ(coordZ)
                .width(stickerRequest.width())
                .height(stickerRequest.height())
                .rotation(stickerRequest.rotation())
                .diary(diary)
                .staticImage(staticImage)
                .build();
    }
}
