package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.security.shiro.filter.authc.FormAuthenticationFilter;
import com.alibaba.csp.sentinel.dashboard.security.shiro.realm.UserRealm;
import com.alibaba.csp.sentinel.dashboard.util.UserUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class LoginController {

	@GetMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request, HttpServletRequest response, Model model) {
		// 我的电脑有缓存问题
		UserRealm.Principal principal = UserUtils.getPrincipal(); // 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return new ModelAndView("redirect:/index.htm");
		}
		String useruame = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		return new ModelAndView("login");
	}

    @PostMapping(value = "/login")
    public ModelAndView login(@RequestParam Map<String,String> registerParams,HttpServletRequest request, HttpServletRequest response, Model model) {
		return new ModelAndView("login");
    }

}
