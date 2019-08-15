package com.ziyue.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ziyue.bar.BarCodeUtil;
import com.ziyue.bar.QRCodeUtil;

@Controller
@RequestMapping("/bar")
public class BaseBarController {
	@RequestMapping("/qr")
	public void  qr(String code,HttpServletResponse response) throws Exception{
		response.setContentType("image/png");
		// 接收卡片条形码编号
        OutputStream out = response.getOutputStream();
		//二维码中间的小图标，resource设置为null，则不生成小图标
        Resource resource = new ClassPathResource("public/images/logo/zxing.jpg");
		BufferedImage  image = QRCodeUtil.createImage(code, resource.getInputStream());
		ImageIO.write(image, "PNG",out);
		if(null != out) {
			out.close();
		}    
	}
	
	/**
	 * 创建条码
	 * @param code 条码编号
	 * @param isFont 是否在条码下面创建字体 ,isFont=n 为不创建
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/bar")
	public void  bar(String code,String isFont, HttpServletResponse response) throws Exception{
		response.setContentType("image/png");
		// 接收卡片条形码编号
        OutputStream out = response.getOutputStream();
        if(null != isFont && "n".equals(isFont)){
        	 BarCodeUtil.encode(code, out, false);
        }else {
        	 BarCodeUtil.encode(code, out, true);
        }
       
		if(null != out) {
			out.close();
		}    
	}

	
}
