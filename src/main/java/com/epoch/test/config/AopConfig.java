package com.epoch.test.config;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.UserIn;
import com.epoch.test.controller.UserController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

@Aspect
@Configuration
public class AopConfig {

//    private static final Log log = LogFactory.getLog(UserController.class);
//
//    @Autowired
//    HttpServletRequest request;
//    @Pointcut("execution (* com.epoch.test.controller..*(..))")
//    public void initAllAop(){};
//    @Before("initAllAop()")
//    public void beforeInit()throws Exception{
//        log.info("进入beforeInit方法");
//        UserIn loginUser = (UserIn) request.getSession().getAttribute("USER");
//        if ( loginUser == null ){
//            throw new Exception("用户身份已过期，请重新登录！");
//        }
//    };
}
