package com.apiece.springboot_sns_sample.domain.media;

import com.apiece.springboot_sns_sample.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "media")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @Column(nullable = false, length = 2000)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus status;

    @Column(name = "user_id")
    private Long userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> attributes;

    public static Media create(MediaType mediaType, String path, Long userId) {
        Media media = new Media();
        media.mediaType = mediaType;
        media.path = path;
        media.status = MediaStatus.INIT;
        media.userId = userId;
        return media;
    }

    public void updateStatus(MediaStatus status) {
        this.status = status;
    }

    public void updatePath(String path) {
        this.path = path;
    }

    public void updateAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
