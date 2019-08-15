package com.ziyue.util;
import org.springframework.ui.Model;

/**
 * 请求响应结构
 */
public class HttpResult {
    private Model model; // 定义 数据对象
    private String status; // 响应业务状态 ok：成功,error：错误,可自行定义
    private String msg ;// 响应消息
    
    public HttpResult(String status, String msg, Model model) {
        this.status = status;
        this.msg = msg;
        this.model = model;
    }

    public HttpResult(Model model) {
        this.status = "ok";
        this.msg = "ok";
        this.model = model;
    }

    /**
     * 构建自定义的请求响应结构
     * @param String status http状态
     * @param String msg 返回的提示信息 
     * @param Model model 返回的数据集合
     * @return HttpResult 规范的接口
     */
    public static HttpResult build(String status, String msg, Model model) {
        return new HttpResult(status, msg, model);
    }
    
    /**
     * 构建自定义的请求响应结构
     * @param String status http状态
     * @param String msg 返回的提示信息 
     * @return HttpResult 规范的接口
     */
    public static HttpResult build(String status, String msg) {
        return new HttpResult(status, msg, null);
    }
    
    /**
     * 默认处理成功，并返回数据
     * @param Model model 返回的数据集合
     * @return HttpResult 规范的接口
     */
    public static HttpResult success(Model model) {
        return new HttpResult(model);
    }
    /**
     * 默认处理成功 ,构建自定义的请求响应结构
     * @param String msg 返回的提示信息 
     * @return HttpResult 规范的接口
     */
    public static HttpResult success(String msg) {
    	 return new HttpResult("ok", msg, null);
    }
    
    /**
     * 默认处理错误，并返回错误信息
     * @param String errorMsg 错误信息
     * @return HttpResult 规范的接口
     */
    public static HttpResult error(String errorMsg) {
        return new HttpResult("error", errorMsg, null); 
    }

    /**
     * 默认处理成功，不返回任何数据
     * @return HttpResult 规范的接口
     */
    public static HttpResult success() {
        return new HttpResult(null);
    }

    public HttpResult() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
