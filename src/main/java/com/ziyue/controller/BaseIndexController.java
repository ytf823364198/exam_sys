package com.ziyue.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ziyue.entity.BaseUser;
import com.ziyue.service.BaseMenuService;
import com.ziyue.service.BaseUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BaseIndexController {
	@Autowired
	private Producer producer;
    @Autowired
    BaseUserService baseUserService;
	@Autowired
	private BaseMenuService baseMenuService;
    
    @RequestMapping(value ="/")
    public String root(){
		BaseUser user = (BaseUser)SecurityUtils.getSubject().getPrincipal();
		if(null == user ) {
			return "login";
		}
		return "redirect:/index";
    }
    
    @RequestMapping(value = "/index")
    public String index(HttpSession session,Model model){
    	//获取菜单
    	BaseUser user = (BaseUser)session.getAttribute("loginUser");
    	model.addAttribute("menus",baseMenuService.findMenuByUserId(user.getId()));
    	return "index";
    }

	@PostMapping(value = "/login")
	public String login(String username,String password,String vrifyCode,Model model){
		try{
			String kaptcha = (String)SecurityUtils.getSubject().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if(null == vrifyCode ||  !vrifyCode.equalsIgnoreCase(kaptcha)){
				model.addAttribute("msg","验证码不正确");
	            //return  "login";
			}
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (Exception e) {
			model.addAttribute("msg",e.getMessage());
	        return  "login";
		}
		//登陆成功，防止表单重复提交，可以重定向到主页
	    return "redirect:/index";	
	}
	
	
	@RequestMapping(value = "/main")
    public String main(){
    	return "main";
    }
	
	
	
	//谷歌kaptcha验证码
	@RequestMapping("/kaptcha")
	public void kaptcha(HttpServletResponse response)throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到 session
        SecurityUtils.getSubject().getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
		out.flush();
		out.close();
	}
	
	@RequestMapping(value = "/unauthorized")
    public String unauthorized(){
    	log.debug("turn unauthorized.html page");
    	return "public/unauthorized";
    }
	
	@RequestMapping(value="logout")
	public String logout(HttpServletRequest request){
		
		//subject的实现类DelegatingSubject的logout方法，将本subject对象的session清空了
		//即使session托管给了redis ，redis有很多个浏览器的session
		//只要调用退出方法，此subject的、此浏览器的session就没了
		try {
			//退出
			SecurityUtils.getSubject().logout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login";
 
	}	
	
}
