package com.jeck.tools.bimface;

import com.alibaba.fastjson.JSONObject;
import com.bimface.sdk.BimfaceClient;
import com.bimface.sdk.bean.request.translate.TranslateSource;
import com.bimface.sdk.bean.response.FileBean;
import com.bimface.sdk.bean.response.TranslateBean;
import com.bimface.sdk.exception.BimfaceException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SampleTest {
    private static Logger log = LoggerFactory.getLogger(SampleTest.class);

    private static String callback = "http://pcop.test.com";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static String appKey = "xxx";
    private static String appSecert = "xxx";

    private static BimfaceClient bimfaceClient = new BimfaceClient(appKey, appSecert);

    public static FileBean upload(String fileName, String filePath) {
        log.info("start to upload: {}", dateFormat.format(new Date()));
        FileBean fileBean = null;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                fileBean = bimfaceClient.upload(fileName, file.length(), inputStream);
                log.info("respond file bean: {}", (new Gson()).toJson(fileBean));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (BimfaceException e) {
                e.printStackTrace();
            }
        }
        log.info("finish to upload: {}", dateFormat.format(new Date()));

        try {
            TranslateRequestBody requestBody = new TranslateRequestBody();
            requestBody.setCallback(callback);
            requestBody.setPriority(2);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("texture", true);
            requestBody.setConfig(jsonObject);

            TranslateSource translateSource = new TranslateSource();
            translateSource.setFileId(fileBean.getFileId());
            translateSource.setCompressed(false);
            requestBody.setSource(translateSource);

            // TranslateBean translateBean = bimfaceClient.translate(fileBean.getFileId());
            TranslateBean translateBean = bimfaceClient.translate(requestBody);
            log.info("translate bean: {}", translateBean);
        } catch (BimfaceException e) {
            e.printStackTrace();
        }
        log.info("finish translate: {}", dateFormat.format(new Date()));

        return fileBean;
    }

    public static void main(String[] args) {
        upload("car-t1.rvt", "G:\\data\\bim\\car.rvt");
    }

}
