package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.dashboard.security.shiro.realm.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Component
public class NacosDynamicAccountsServiceImpl {
    @Value("${nacos.serveraddr:127.0.0.1:8848}")
    private String nacosServerAddr;
    private List<User> users;

    @PostConstruct
    public void init(){
        nacosDynamicAccountsListener("Dynamic-Accounts-Service","DEFAULT_GROUP");
    }

    public void nacosDynamicAccountsListener(String dataId, String group){
        try {
            ConfigService configService= NacosFactory.createConfigService(nacosServerAddr);
            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String config) {
                    users = JSON.parseArray(config,User.class);
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("admin");
        user.setPassword("f5a7dfcb78ba72cdd79a13cda8e31daf");
        user.setId("4028ea815a3d2a8c015a3d2f8d2a0002");
        user.setRealname("系统管理员");
        user.setSalt("b6046f1752d7b6951ad4df9e2f6d2952");

        User user1 = new User();
        user1.setUsername("test");
        user1.setPassword("2ebb8bee885791cb053cfe142353cb8a");
        user1.setId("40288ab85ce3c20a015ce3ca6df60000");
        user1.setRealname("测试用户");
        user1.setSalt("5a9d6207da81bd4c1cca29d5e3b6695b");

        users.add(user);
        users.add(user1);
        String json = JSON.toJSONString(users);
        System.out.println(json);
    }
}
