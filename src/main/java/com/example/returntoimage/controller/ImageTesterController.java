package com.example.returntoimage.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class ImageTesterController {


    final String apiURL = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?crs=EPSG:4326&w=320&h=320&center=127.03071880565923,37.492360550712895&level=15&format=jpg&markers=type:d|size:mid|pos:127.03071880565923 37.492360550712895|viewSizeRatio:0.6";
    String clientId = "clientId";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "clientSecret";//애플리케이션 클라이언트 시크릿값";

    @GetMapping("/")
    public String main(){
        return "/main";
    }

    @GetMapping("/getText")
    @ResponseBody
    public String getText(){
        return "getText";
    }

    @GetMapping(value = "/getImageEncodingBase64")
    @ResponseBody
    public String getImageEncodingBase64() throws IOException {

        ResponseEntity<byte[]> response = getNaverResponse();

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(response.getBody()));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", os);

        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    @GetMapping(value = "/getImageWithMediaType",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getImageWithMediaType() throws IOException {

        ResponseEntity<byte[]> response = getNaverResponse();

        return response.getBody();
    }

    public ResponseEntity<byte[]> getNaverResponse(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.add("X-NCP-APIGW-API-KEY", clientSecret);

        return new RestTemplate().exchange(apiURL, HttpMethod.GET, new HttpEntity(headers), byte[].class);
    }
}
