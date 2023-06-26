package com.example.Store.dao.interfaces;

import com.example.Store.entity.video.LocationOnThePage;
import com.example.Store.entity.video.VideoOnPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VideoOnPageDAO {

    void save(VideoOnPage video);
    Mono<Void> update(VideoOnPage video);
    Mono<Void> delete(Integer id);
    Mono<VideoOnPage> findById(Integer id);
    Flux<VideoOnPage> findAllVideoOnPage();
    Flux<String> getAllFileNameFromMyResourcesDirectory();
    Mono<VideoOnPage> findByLocationOnThePage(LocationOnThePage locationOnThePage);
}
