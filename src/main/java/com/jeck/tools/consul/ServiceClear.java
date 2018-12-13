package com.jeck.tools.consul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ServiceClear {

    private static Logger log = Logger.getLogger(ServiceClear.class);

    public static final String CONSUL_HOST = "http://127.0.0.1:8500";

    public static String listChecks() throws IOException {

        String checkList = "/v1/agent/checks";


        String content = Request.Get(CONSUL_HOST + checkList)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnContent().asString();
        // JSONArray jsonArray = JSON.parseArray("");
        //
        // JSONObject jsonObject = jsonArray.getJSONObject(0);
        //
        // jsonObject.getString("");

        System.out.println(content);

        log.info("check list: " + content);

        return content;
    }

    public static void main(String[] args) throws IOException {

        listChecks();

    }

}
