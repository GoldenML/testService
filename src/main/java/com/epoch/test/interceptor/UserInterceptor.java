package com.epoch.test.interceptor;

import com.epoch.test.bean.UserIn;
import com.epoch.test.bean.UserOut;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            UserIn loginUserIn =(UserIn)request.getSession().getAttribute("USER");
            if(loginUserIn != null){
                return true;
            }
            response.sendRedirect(request.getContextPath()+"/user/home");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
