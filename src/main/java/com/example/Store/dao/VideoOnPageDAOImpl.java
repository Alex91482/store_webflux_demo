package com.example.Store.dao;

import com.example.Store.dao.interfaces.VideoOnPageDAO;
import com.example.Store.entity.video.AllVideo;
import com.example.Store.entity.video.LocationOnThePage;
import com.example.Store.entity.video.VideoOnPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class VideoOnPageDAOImpl implements VideoOnPageDAO {

    private static final Logger logger = LoggerFactory.getLogger(VideoOnPageDAOImpl.class);
    private static final Path VIDEO_XML_PATH = Paths.get("resources/config/video_conf.xml");
    private static final Path VIDEO_PATH = Paths.get("resources/video/");
    private static final Path IMAGES_PATH = Paths.get("resources/images/");


    @Override
    public void save(VideoOnPage video) {
        update(video).subscribe(complete -> logger.info("Save video to xml file, id = {}, video name = {}", video.getId(), video.getFileName()),
                error -> logger.error("Video save failure, id = {}, video name = {}", video.getId(), video.getFileName()));
    }

    @Override
    public Mono<Void> update(VideoOnPage video) {
        return Mono.create(event -> {
            try{
                var context = JAXBContext.newInstance(AllVideo.class);
                var um = context.createUnmarshaller();
                var allVideo = (AllVideo) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(VIDEO_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                var videoMap = allVideo.getVideoOnPageMap() != null ? allVideo.getVideoOnPageMap() : new HashMap<Integer, VideoOnPage>();
                //проверка есть ли на данной позиции видео
                VideoOnPage removeVideo = null;
                for(VideoOnPage videoInMap : videoMap.values()){
                    if(videoInMap.getLocationOnThePage().equals(video.getLocationOnThePage())){
                        removeVideo = videoInMap;
                    }
                }
                //если на такой позиции видео уже есть то удаляем его
                if(removeVideo != null){
                    videoMap.remove(removeVideo.getId());
                    var file = new File(Paths.get(VIDEO_PATH + removeVideo.getFileName()).toUri());
                    deleteFile(file);
                }
                //запись в map
                videoMap.put(video.getId(), video);
                allVideo.setVideoOnPageMap(videoMap);
                //запись в файл
                var marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(allVideo , new File(VIDEO_XML_PATH.toUri()));

                event.success(); //выполнено успешно

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error save or update : {}", e.getMessage());
                event.error(e); //неудача, передаем ошибку
            }
        });
    }

    @Override
    public Mono<Void> delete(Integer id) {
        if(id == null){
            return Mono.empty();
        }
        return Mono.create(event -> {
            try{
                var context = JAXBContext.newInstance(AllVideo.class);
                var um = context.createUnmarshaller();
                var allVideo = (AllVideo) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(VIDEO_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //основная обработка
                var videoMap = allVideo.getVideoOnPageMap();
                //удаление файла
                var video = videoMap.get(id);
                if(video != null){
                    var file = new File(Paths.get(VIDEO_PATH + video.getFileName()).toUri());
                    deleteFile(file);
                }
                videoMap.remove(id);
                allVideo.setVideoOnPageMap(videoMap);
                //запись в файл
                var marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(allVideo , new File(VIDEO_XML_PATH.toUri()));

                event.success(); //выполнено успешно

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error delete, id : {}, error : {}", id, e.getMessage());
                event.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<VideoOnPage> findById(Integer id) {
        if(id == null){
            return Mono.empty();
        }
        return Mono.create(event -> {
            var video = new VideoOnPage();
            try{
                var context = JAXBContext.newInstance(AllVideo.class);
                var um = context.createUnmarshaller();
                var allVideo = (AllVideo) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(VIDEO_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //извлекаем по id продукт из map
                video = allVideo.getVideoOnPageMap().get(id);

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e));
            }
            event.success(video); //возвращаем результат (может вернуть пустой видос?)
        });
    }

    @Override
    public Mono<VideoOnPage> findByLocationOnThePage(LocationOnThePage locationOnThePage){
        if(locationOnThePage == null){
            return Mono.empty();
        }
        return Mono.create(event -> {
            var video = new VideoOnPage();
            try{
                var context = JAXBContext.newInstance(AllVideo.class);
                var um = context.createUnmarshaller();
                var allVideo = (AllVideo) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(VIDEO_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                for(VideoOnPage videoInMap : allVideo.getVideoOnPageMap().values()){
                    if(videoInMap.getLocationOnThePage().equals(locationOnThePage)){
                        video = videoInMap;
                        break;
                    }
                }

                event.success(video);

            }catch (Exception e){
                logger.error("Error in method find by location: {}", e.getMessage());
                event.error(e);
            }
            event.success(video); //возвращаем результат (может вернуть пустой видос?)
        });
    }

    @Override
    public Flux<VideoOnPage> findAllVideoOnPage() {
        return Flux.create(event -> {
            var list = new ArrayList<VideoOnPage>();
            try{
                var context = JAXBContext.newInstance(AllVideo.class);
                var um = context.createUnmarshaller();
                var allVideo = (AllVideo) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(VIDEO_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                list = new ArrayList<>(allVideo.getVideoOnPageMap().values());
                for (VideoOnPage video : list){
                    event.next(video); //перебираем и отдаем все видосы которые есть
                }

                event.complete(); //сигнал о завершении последовательности

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e)); //сигнал о ошибке
            }
        });
    }

    @Override
    public Flux<String> getAllFileNameFromMyResourcesDirectory(){ //посмотреть список всех файлов в директории
        return Flux.create(event -> {
            try {
                var dirVideo = new File(VIDEO_PATH.toUri()); //path указывает на директорию с видео
                var dirImages = new File(IMAGES_PATH.toUri()); //path указывает на директорию с изображениями
                var arrFilesVideo = dirVideo.listFiles();
                var arrFilesImages = dirImages.listFiles();
                var list = new ArrayList<File>();
                if(arrFilesVideo != null) {
                    Collections.addAll(list, arrFilesVideo);
                }
                if(arrFilesImages != null) {
                    Collections.addAll(list, arrFilesImages);
                }
                for(File file : list){
                    event.next(file.getName());
                }

                event.complete();

            }catch (Exception e){
                logger.error("Exception to read file in directory: {}", e.getMessage());
                event.error(e);
            }
        });
    }

    private void deleteFile(File file){
        if(file.delete()){
            logger.info("File delete: {}", file.getName());
        }else{
            logger.warn("File delete failed: {}", file.getName());
        }
    }
}
