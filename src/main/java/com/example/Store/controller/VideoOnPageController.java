package com.example.Store.controller;

import com.example.Store.entity.video.VideoOnPage;
import com.example.Store.service.VideoOnPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/videoOnPage/")
public class VideoOnPageController {

    private final VideoOnPageService videoOnPageService;

    public VideoOnPageController(VideoOnPageService videoOnPageService){
        this.videoOnPageService = videoOnPageService;
    }

    @GetMapping("/top")
    public Mono<ResponseEntity<VideoOnPage>> getTopVideo(){
        Mono<VideoOnPage> video = videoOnPageService.getTopVideoOnThePage();
        return video.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/bottom")
    public Mono<ResponseEntity<VideoOnPage>> getBottomVideo(){
        Mono<VideoOnPage> video = videoOnPageService.getBottomVideoOnThePage();
        return video.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
