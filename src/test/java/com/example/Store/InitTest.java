package com.example.Store;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class InitTest {

    //@Test
    public void createDirectoryTest(){
        var configList = Arrays.asList("resources/config/product_list.xml", "resources/config/video_conf.xml");
        for(String path : configList){
            System.out.println(path + " " + Files.notExists(Paths.get(path)));
        }
    }
}
