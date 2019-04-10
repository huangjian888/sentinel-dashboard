package com.alibaba.csp.sentinel.dashboard.interceptor;

import com.alibaba.csp.sentinel.dashboard.security.shiro.realm.UserRealm;
import com.alibaba.csp.sentinel.dashboard.util.SpringContextHolder;
import com.alibaba.csp.sentinel.dashboard.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 公共拦截器
 */
public class WebInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Environment env = SpringContextHolder.getBean(Environment.class);
        String staticPath = env.getProperty("company.staticPath");
        String adminPath = env.getProperty("company.admin.url.prefix");
        //加入公用参数的
        String ctx = request.getServletContext().getContextPath();
        request.setAttribute("ctx",ctx);
        request.setAttribute("adminPath",ctx + adminPath);
        request.setAttribute("staticPath",ctx + staticPath);
        request.setAttribute("platformName", "platformName");
        request.setAttribute("platformCopyright", "platformCopyright");
        request.setAttribute("platformVersion", "platformVersion");
        UserRealm.Principal principal = UserUtils.getPrincipal(); // 如果已经登录，则跳转到管理首页
        if (principal == null){
//            response.sendRedirect("/");
//            return false;
        }
        request.setAttribute("loginUser",UserUtils.getUser());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

}
