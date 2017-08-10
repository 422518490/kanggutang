package com.kanggutang.common;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by ztz on 2016/6/22.
 */
public class AccessApiInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AccessApiInterceptor.class);

    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.redis.keytimeout}")
    private long keytimeout;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AccessRequired annotation = method.getAnnotation(AccessRequired.class);
        try {
            if (annotation != null) {
                String accessToken = request.getHeader("accessToken");
                response.setContentType("text/html;charset=utf-8");
/*          String permissionCode = request.getHeader("per_code");
            String URI = request.getRequestURI();
            String requestMethod = request.getMethod();*/
                String methodName = method.getName();
                JSONObject jsonObject = new JSONObject();
                //如果token不为空,则取值权限，为空则返回token为空请求参数错误400
                if (accessToken != null) {
                    List<String> value = (List<String>) redisUtil.get(accessToken);

                    if(value != null){
                        if(value.size() > 0){
                            if (value.contains(methodName)) {
                                redisUtil.updateExprieTime(accessToken, keytimeout);
                                return true;
                            } else {
                                jsonObject.put("code", HttpStatus.UNAUTHORIZED);
                                jsonObject.put("msg", "该角色没有权限访问或系统未添加此权限.");
                                response.getWriter().print(jsonObject.toString());
                                return false;
                            }
                        }else if(value.size() == 0){
                            jsonObject.put("code", HttpStatus.UNAUTHORIZED);
                            jsonObject.put("msg", "该角色没有赋予任何权限访问.");
                            response.getWriter().print(jsonObject.toString());
                            return false;
                        }else{
                            jsonObject.put("code", HttpStatus.UNAUTHORIZED);
                            jsonObject.put("msg", "未知的权限错误,请联系管理员.");
                            response.getWriter().print(jsonObject.toString());
                            return false;
                        }
                    }else{
                        jsonObject.put("code", 99412);
                        jsonObject.put("msg", "令牌失效,请重新登录.");
                        response.getWriter().print(jsonObject.toString());
                        return false;
                    }
                } else {
                    jsonObject.put("code", 99400);
                    jsonObject.put("msg", "未通过鉴权,请求令牌有误.");
                    response.getWriter().print(jsonObject.toString());
                    return false;
                }
            }
        }catch(Exception e){
            logger.error("Error from AccessApiInterceptor!",e);
            e.printStackTrace();
        }
        // 没有注解通过拦截
        return true;
    }

}
