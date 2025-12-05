package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.media.MediaInitRequest;
import com.apiece.springboot_sns_sample.api.media.MediaInitResponse;
import com.apiece.springboot_sns_sample.api.media.MediaResponse;
import com.apiece.springboot_sns_sample.config.auth.AuthUser;
import com.apiece.springboot_sns_sample.domain.media.Media;
import com.apiece.springboot_sns_sample.domain.media.MediaPresignedUrl;
import com.apiece.springboot_sns_sample.domain.media.MediaService;
import com.apiece.springboot_sns_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/api/v1/media/init")
    public MediaInitResponse initMedia(
            @RequestBody MediaInitRequest request,
            @AuthUser User user
    ) {
        MediaPresignedUrl result = mediaService.initMedia(request.mediaType(), user);
        return MediaInitResponse.from(result.media(), result.presignedUrl());
    }

    @GetMapping("/api/v1/media/{id}")
    public MediaResponse getMediaById(@PathVariable Long id) {
        Media media = mediaService.getMediaById(id);
        return MediaResponse.from(media);
    }

    @GetMapping("/api/v1/users/{userId}/media")
    public List<MediaResponse> getMediaByUserId(@PathVariable Long userId) {
        return mediaService.getMediaByUserId(userId).stream()
                .map(MediaResponse::from)
                .toList();
    }

    @DeleteMapping("/api/v1/media/{id}")
    public void deleteMedia(
            @PathVariable Long id,
            @AuthUser User user
    ) {
        mediaService.deleteMedia(id, user);
    }
}
