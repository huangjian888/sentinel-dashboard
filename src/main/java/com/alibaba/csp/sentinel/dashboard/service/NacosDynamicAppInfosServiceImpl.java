package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Component
public class NacosDynamicAppInfosServiceImpl {
    @Value("${nacos.serveraddr:127.0.0.1:8848}")
    private String nacosServerAddr;
    private Map<String,List<String>> appInfos = new HashMap<>();

    @PostConstruct
    public void init(){
        nacosDynamicAppInfosListener("Dynamic-AppInfos-Service","DEFAULT_GROUP");
    }

    public void nacosDynamicAppInfosListener(String dataId, String group){
        try {
            ConfigService configService= NacosFactory.createConfigService(nacosServerAddr);
            configService.addListener(dataId, group, new Listener() {

                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String config) {
                    appInfos = JSON.parseObject(config,Map.class);
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getAppInfos() {
        return appInfos;
    }

    public static void main(String[] args) {
        Map<String,List<String>> appInfos = new HashMap<>();
        List<String> admin_value = new ArrayList<>();
        List<String> test_value = new ArrayList<>();

        admin_value.add("sentinel-dashboard");
        admin_value.add("appA");
        test_value.add("appB");

        appInfos.put("admin",admin_value);
        appInfos.put("test",test_value);
        String json = JSON.toJSONString(appInfos);

        System.out.println(json);

    }
}
