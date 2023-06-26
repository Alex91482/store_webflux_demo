package com.example.Store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final String PATH_TO_EXTERNAL_RESOURCE = "resource/images/";

    public ImageService(){
        try{
            Path path = Paths.get(PATH_TO_EXTERNAL_RESOURCE);
            logger.info("Path is valid: {}", path.toString());
        }catch (Exception e){
            logger.error("ImageService fatal Error during initialization: {}", e.getMessage());
        }
    }



    public Mono<byte[]> getImage(String imageName){
        return Mono.create(event -> {
            try {
                var byteArray = Files.readAllBytes(Paths.get(PATH_TO_EXTERNAL_RESOURCE + imageName));

                event.success(byteArray);

            }catch (Exception e){
                logger.error("Exception while sending image: {}", e.getMessage());
                event.error(e);
            }
        });
    }

    public boolean searchFileToDirectory(String fileName){

        File[] arrExternal = new File(PATH_TO_EXTERNAL_RESOURCE).listFiles();
        if(arrExternal != null) {
            for(File file : arrExternal){
                if(file.getName().equals(fileName)){
                    return true;
                }
            }
        }
        return false;
    }
}
