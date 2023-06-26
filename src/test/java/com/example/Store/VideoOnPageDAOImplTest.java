package com.example.Store;

import com.example.Store.dao.VideoOnPageDAOImpl;
import com.example.Store.entity.form.VideoOnThePageForm;
import com.example.Store.entity.video.LocationOnThePage;
import com.example.Store.entity.video.VideoOnPage;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class VideoOnPageDAOImplTest {

    //@Test
    public void saveTest(){
        var dao = new VideoOnPageDAOImpl();
        var video = new VideoOnPage();
        var fileName = "New 2023 EM1 e_ Electric Scooter.mp4";
        var sb = new StringBuilder()
                .append("..").append(File.separator)
                .append("raw").append(File.separator)
                .append("video").append(File.separator).append(fileName);
        video.setId(2);
        video.setFileName(sb.toString());
        video.setLocationOnThePage(LocationOnThePage.BOTTOM);

        dao.save(video);
    }

    //@Test
    public void fileToDirTest(){
        try {
            var dao = new VideoOnPageDAOImpl();
            dao.getAllFileNameFromMyResourcesDirectory().subscribe(
                    System.out::println
            );

            Thread.sleep(1000L);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //@Test
    public void mySaveEventTest(){
        VideoOnThePageForm form = new VideoOnThePageForm();
        form.setFileName("video1.mp4");
        form.setLocationOnThePage(LocationOnThePage.TOP);

        Mono<VideoOnPage> mono = Mono.create(event -> {
            try{
                var videoOnThePage = new VideoOnPage();
                var id = ThreadLocalRandom.current().nextInt(); //создаем рандомнй id
                videoOnThePage.setId(id);
                videoOnThePage.setFileName(form.getFileName());
                videoOnThePage.setLocationOnThePage(form.getLocationOnThePage());

                event.success(videoOnThePage);

            }catch (Exception e){
                e.printStackTrace();
                event.error(e);
            }
        }).flatMap(result -> {
            var video = (VideoOnPage) result;
            //return videoOnPageDAO.update(video);
            return Mono.create(event -> {
                video.setFileName("video1Update.mp4");
                event.success(video);
            });
        });

        mono.subscribe(
                event -> System.out.println(event.getFileName())
        );

        try{
            Thread.sleep(500L);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
