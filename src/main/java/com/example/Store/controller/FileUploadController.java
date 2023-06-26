package com.example.Store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload/")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static final Path pathImages = Paths.get("resource/images/");
    private static final Path pathVideo = Paths.get("resource/video/");

    @PostMapping("/image/single")
    public Mono<Void> upload(@RequestPart("file") Mono<FilePart> filePartMono){
        return filePartMono
                .doOnNext(fp -> logger.info("Received file : {}", fp.filename()))
                .flatMap(fp -> fp.transferTo(pathImages.resolve(fp.filename())))
                .then();
    }

    @PostMapping("/image/multi")
    public Mono<Void> uploadMulti(@RequestPart("files") Flux<FilePart> filePartMono){
        return filePartMono
                .doOnNext(fp -> logger.info("Received file : {}", fp.filename()))
                .flatMap(fp -> fp.transferTo(pathImages.resolve(fp.filename())))
                .then();
    }

    @PostMapping("/video/single")
    public Mono<Void> uploadVideo(@RequestPart Mono<FilePart> filePartMono){
        return filePartMono
                .doOnNext(fp -> logger.info("Received file : {}", fp.filename()))
                .flatMap(fp -> fp.transferTo(pathVideo.resolve(fp.filename())))
                .then();
    }

    @PostMapping("/video/multi")
    public Mono<Void> uploadMultiVideo(@RequestPart("files") Flux<FilePart> filePartMono){
        return filePartMono
                .doOnNext(fp -> logger.info("Received file : {}", fp.filename()))
                .flatMap(fp -> fp.transferTo(pathVideo.resolve(fp.filename())))
                .then();
    }
}
