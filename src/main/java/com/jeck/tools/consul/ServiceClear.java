package com.jeck.tools.consul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ServiceClear {

    // public static final String CONSUL_HOST = "http://127.0.0.1:8500";
    public static final String CONSUL_HOST = "http://10.129.57.89:8500";

    public static String listChecks(String serviceName, String status) throws IOException {
        String checkList = "/v1/agent/checks";
        String checkDeregister = "/v1/agent/check/deregister/";

        String serviceList = "/v1/agent/services";
        String serviceDeregister = "/v1/agent/service/deregister/";

        String content = Request.Get(CONSUL_HOST + checkList)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnContent().asString();

        System.out.println(content);
        JSONObject jsonObject = JSON.parseObject(content);
        for (String key : jsonObject.keySet()) {
            JSONObject check = jsonObject.getJSONObject(key);
            String srvName = check.getString("ServiceName");
            String srvId = check.getString("ServiceID");
            String stt = check.getString("Status");
            System.out.println("ServiceName=" + srvName + ", ServiceID=" + srvId + ", Status=" + stt);
            // System.out.println("ServiceName=" + check.getString("Service") + ", ServiceID=" + check.getString("ID") + ", Status=" + check.getString("Status"));
            if (srvName.equals(serviceName) && stt.equals(status)) {
                // String deRegisterContent = Request.Put(CONSUL_HOST + checkDeregister + check.getString("CheckID")).execute().returnContent().asString();
                HttpResponse response = Request.Put(CONSUL_HOST + serviceDeregister + srvId).execute().returnResponse();
                System.out.println("return content: " + response.getStatusLine());
                System.out.println("Deregister service of " + srvId);
            } else {
                System.out.println("Should not deregister service of " + srvId);
            }
        }

        return content;
    }

    public static Set<String> getAvailableServices(String serviceName) throws IOException {
        String status = "passing";
        Set<String> serviceIds = new HashSet<>();
        String checkList = "/v1/agent/checks";

        String content = Request.Get(CONSUL_HOST + checkList)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnContent().asString();

        // System.out.println(content);
        JSONObject jsonObject = JSON.parseObject(content);
        for (String key : jsonObject.keySet()) {
            JSONObject check = jsonObject.getJSONObject(key);
            String srvName = check.getString("ServiceName");
            String srvId = check.getString("ServiceID");
            String stt = check.getString("Status");
            System.out.println("ServiceName=" + srvName + ", ServiceID=" + srvId + ", Status=" + stt);
            if (srvName.equals(serviceName) && stt.equals(status)) {
                serviceIds.add(srvId);
                System.out.println("PASSING SERVICEID: " + srvId);
            } else {
                System.out.println("CRITICAL SERVICEID: " + srvId);
            }
        }

        return serviceIds;
    }

    public static String deregisterUnavailableServices(String serviceName, String hostName) throws IOException {
        String serviceList = "/v1/agent/services";
        String serviceDeregister = "/v1/agent/service/deregister/";

        String content = Request.Get(CONSUL_HOST + serviceList)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnContent().asString();

        Set<String> availableServiceIds = getAvailableServices(serviceName);
        JSONObject jsonObject = JSON.parseObject(content);
        for (String key : jsonObject.keySet()) {
            JSONObject check = jsonObject.getJSONObject(key);
            String srvName = check.getString("Service");
            String srvId = check.getString("ID");
            String address = check.getString("Address");
            System.out.println("ServiceName=" + srvName + ", ServiceID=" + srvId + ", Address=" + address);
            if (srvName.equals(serviceName)) {
                if (StringUtils.isNotBlank(hostName)) {
                    if (!address.equals(hostName.trim())) {
                        System.out.println("omit by host");
                        continue;
                    }
                }
                if (availableServiceIds.contains(srvId)) {
                    System.out.println("service available: " + srvId);
                } else {
                    System.out.println("Deregister service: " + srvId);
                    HttpResponse response = Request.Put(CONSUL_HOST + serviceDeregister + srvId).execute().returnResponse();
                    System.out.println("return content: " + response.getStatusLine());
                }
            } else {
                System.out.println("Should not deregister service of " + srvId);
            }
        }

        return content;
    }

    public static void main(String[] args) throws IOException {
        String serviceName = "";
        String hostName = "";
        deregisterUnavailableServices(serviceName, hostName);
    }

}
