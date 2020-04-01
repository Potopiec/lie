package dev.hangedman.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;


@Controller
public class MainController {

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

//    @PostMapping("/encrypt")
//    public String encript(@RequestParam("img") Image image, @RequestParam("addText") String string, Model model){
//        model.addAttribute("text",string);
//        model.addAttribute("img",image);
//
//        return "encript";
//    }


    @PostMapping(value = "/encrypt",consumes = {"multipart/form-data"},produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[]
    encript(@RequestParam("img") MultipartFile file, @RequestParam("addText") String message, Model model){


        BufferedImage image = null;
        byte[] bytes = null;
        int imageHight = 0;
        int imageWidth = 0;

        model.addAttribute("text",file.getName());

        if (!(file.getContentType().equals("image/png"))||(message.isEmpty()))
            return null;

        try {
            image = ImageIO.read(file.getInputStream());

            imageHight = image.getData().getHeight();
            imageWidth = image.getData().getWidth();

            WritableRaster raster = image.getRaster();

            int RetriveSizeOfTheMessageCounter = 0;

            StringBuilder stringBuilder = new StringBuilder();

            String basicMessageSizeInBinary = Integer.toBinaryString(message.length());

            if (basicMessageSizeInBinary.length() <= 18){
                int i = 18 - basicMessageSizeInBinary.length();
                 while (i>0){
                     stringBuilder.append("2");
                     i--;
                 }
                 stringBuilder.append(basicMessageSizeInBinary);
            }

            char[]lettersInMessage = message.toCharArray();
            char[]fullMessageSizeInBinary = stringBuilder.toString().toCharArray();

            int[] tobi = Stream.of(message.split("")).mapToInt(Integer::parseInt).toArray();
            for (int c:tobi){
               System.out.println(c);
            }




            for (int i = 0;i < imageWidth;i++){
                for (int j = 0; j < imageHight;j++){
                    int[] pixels = raster.getPixel(i, j, (int[]) null);

                    if (RetriveSizeOfTheMessageCounter < 6){

                        pixels[0] = pixels[0] / 10 * 10 + Integer.parseInt(String.valueOf(fullMessageSizeInBinary[0 + 3 *RetriveSizeOfTheMessageCounter]));
                        pixels[1] = pixels[1] / 10 * 10 + Integer.parseInt(String.valueOf(fullMessageSizeInBinary[1 + 3 *RetriveSizeOfTheMessageCounter]));
                        pixels[2] = pixels[2] / 10 * 10 + Integer.parseInt(String.valueOf(fullMessageSizeInBinary[2 + 3 *RetriveSizeOfTheMessageCounter]));
                        raster.setPixel(i, j, pixels);

                        RetriveSizeOfTheMessageCounter ++;
                    }else {



                        pixels[0] = pixels[0] / 10 * 10;
                        pixels[1] = pixels[1] / 10 * 10;
                        pixels[2] = pixels[2] / 10 * 10;;
                        raster.setPixel(i, j, pixels);


                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image,"png",out);
            out.flush();
            bytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @PostMapping("/decript")
    public String decript(Model model,@RequestParam("img") ImageInputStream img){
        model.addAttribute("text","result");
        return "decript";

    }


}
