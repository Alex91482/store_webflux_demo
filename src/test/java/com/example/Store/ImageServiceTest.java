package com.example.Store;

import com.example.Store.service.ImageService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageServiceTest {

    //@Test
    public void searchFileToDirectoryTest(){
        try {
            ImageService imageService = new ImageService();
            byte[] arr = imageService.getImage("images1.png").block();
            Thread.sleep(100);
            System.out.println(arr.length);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
