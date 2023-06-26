package com.example.Store.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class Init implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Init.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var pathsList = Arrays.asList("resource/", "resource/config/", "resource/images/", "resource/video/");
        var configList = Arrays.asList("product_list.xml", "video_conf.xml");
        searchDirectory(pathsList);
        searchAndCreateConfigFile(configList);
    }

    /**
     * Поиск директории, если отсутствует то создать
     */
    private void searchDirectory(List<String> pathsList){
        try{
            for(String path : pathsList) {
                if(Files.notExists(Path.of(path))){
                    logger.info("Create directory: {}", path);
                    createDirectory(path);
                }
                logger.info("Path is valid: {}", path.toString());
            }
        }catch (Exception e){
            logger.error("Initialization exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Создать директорию
     * @param path путо до директории
     */
    private void createDirectory(String path){
        try{
            if(new File(path).mkdir()){
                logger.info("Directory is create: {}", path);
            }
        }catch (Exception e){
            logger.error("Exception while creating directory: {}, Exception : {}", path, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Проверить существование файлов конфигурации если нет то создать
     */
    private void searchAndCreateConfigFile(List<String> configFilePath){
        try {
            for(String fileName : configFilePath){
                var path1 = Paths.get("resource/config/" + fileName);
                var flag = false;

                if(Files.notExists(path1)){
                    flag = path1.toFile().createNewFile();
                    logger.info("Create configuration: {}, {}", flag, fileName);
                }
                if(flag){
                    fillingInTheInitialConfiguration(path1, fileName);
                }
            }
        }catch (Exception e){
            logger.error(" {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * запись в файл конфигурации заголовков
     * @param pathToConfig путь к файлу конфигурации
     * @param configName имя файла конфигурации
     */
    private void fillingInTheInitialConfiguration(Path pathToConfig, String configName){

        if(Files.notExists(pathToConfig)){
            return;
        }
        var encoding= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        var start = "";
        var end = "";

        if(configName.equals("product_list.xml")) {
            start = "<ns2:market xmlns:ns2=\"market\">";
            end = "</ns2:market>";
        }
        if(configName.equals("video_conf.xml")) {
            start = "<ns2:allVideo xmlns:ns2=\"AllVideo\">";
            end = "</ns2:allVideo>";
        }

        try(var writer = new FileWriter(pathToConfig.toFile(), false)) {

            writer.write(encoding);
            writer.append('\n');
            writer.write(start);
            writer.append('\n');
            writer.write(end);
            writer.flush();

        }catch(IOException ex){
            ex.printStackTrace();
        }
        logger.info("Config file written successfully: {}", configName);
    }
}
