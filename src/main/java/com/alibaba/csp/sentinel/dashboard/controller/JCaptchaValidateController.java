package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.util.jcaptcha.JCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * jcaptcha 验证码验证
 *
 */

@RestController
@RequestMapping("/admin")
public class JCaptchaValidateController {

	@GetMapping(value = "/jcaptcha.jpg")
	public void jqueryValidationEngineValidate(HttpServletRequest request,
                                               HttpServletResponse response) throws ServletException, IOException {
		response.setDateHeader("Expires", 0L);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		String id = request.getSession().getId();
		BufferedImage bi = JCaptcha.captchaService.getImageChallengeForID(id);
		ServletOutputStream out = response.getOutputStream();
		try {
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} finally {
			out.close();
		}
	}
}
