package com.example.Store.service;

import com.example.Store.dao.VideoOnPageDAOImpl;
import com.example.Store.entity.form.VideoOnThePageForm;
import com.example.Store.entity.video.LocationOnThePage;
import com.example.Store.entity.video.VideoOnPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class VideoOnPageService {

    private static final Logger logger = LoggerFactory.getLogger(VideoOnPageService.class);

    private final VideoOnPageDAOImpl videoOnPageDAO;

    public VideoOnPageService(VideoOnPageDAOImpl videoOnPageDAO){
        this.videoOnPageDAO = videoOnPageDAO;
    }

    //создать запись
    //обновить запись
    //удалить запись

    public Mono<Void> saveAVideo(VideoOnThePageForm form){ //сохранить запись о видео в памяти
        if(form.getFileName() == null || form.getLocationOnThePage() == null){
            logger.warn("File name and location on the page can not be null!");
            return Mono.empty();
        }
        return Mono.create(event -> {
            try{
                var videoOnThePage = new VideoOnPage();
                var id = ThreadLocalRandom.current().nextInt(); //создаем рандомнй id
                var filePath = "/raw/video/" + form.getFileName(); //создаем путь к файлу

                videoOnThePage.setId(id);
                videoOnThePage.setFileName(filePath);
                videoOnThePage.setLocationOnThePage(form.getLocationOnThePage());

                event.success(videoOnThePage);

            }catch (Exception e){
                logger.error("Exception post a video: {}", e.getMessage());
                event.error(e);
            }
        }).flatMap(result -> {
            var video = (VideoOnPage) result;
            return videoOnPageDAO.update(video);
        });
    }

    public Mono<VideoOnPage> getTopVideoOnThePage(){ //вернуть видео для верха страницы
        return videoOnPageDAO.findByLocationOnThePage(LocationOnThePage.TOP);
    }

    public Mono<VideoOnPage> getBottomVideoOnThePage(){ //вернуть видко для низа страницы
        return videoOnPageDAO.findByLocationOnThePage(LocationOnThePage.BOTTOM);
    }
}
