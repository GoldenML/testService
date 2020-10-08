package com.epoch.test.controller;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.UserOut;
import com.epoch.test.bean.UserIn;
import com.epoch.test.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private static final Log log = LogFactory.getLog(UserController.class);

    @RequestMapping(value = "home", method = RequestMethod.POST)
    public DataResult home(HttpServletRequest request){
        DataResult dataResult = new DataResult();
        dataResult.setSuccess(false);
        dataResult.setMsg("用户身份已过期，请重新登录！");
        return dataResult;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public DataResult login(HttpServletRequest request, @RequestBody UserIn user) throws Exception{
        DataResult dataResult = new DataResult();
        try {
            dataResult = userService.login(user.getUsername(), user.getPassword());
            if (dataResult.getSuccess()) {
                request.getSession().setAttribute("USER", user);
            }
            log.info("用户登陆！");
        } catch (Exception e) {
            dataResult.setMsg("服务调用出错！");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return dataResult;
    }

    @RequestMapping(value = "exit", method = RequestMethod.POST)
    public void exit(HttpServletRequest request) throws Exception{
        UserIn loginUserIn = (UserIn)request.getSession().getAttribute("USER");
        if (loginUserIn != null) {
            request.getSession().removeAttribute("USER");
        }
        log.info("用户退出！");
    }

    @RequestMapping(value = "jbxx", method = RequestMethod.POST)
    public DataResult getJbxx(HttpServletRequest request) throws Exception{
        DataResult dataResult = new DataResult();
        UserIn loginUserIn = (UserIn)request.getSession().getAttribute("USER");
        if (loginUserIn != null) {
            try {
                dataResult = userService.findJbxx(loginUserIn);
            } catch (Exception e) {
                dataResult.setSuccess(false);
                dataResult.setMsg("用户身份查询失败，请重新登录！");
                e.printStackTrace();
            }
        } else {
            dataResult.setSuccess(false);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }
        return dataResult;
    }
    @RequestMapping(value = "xgmm", method = RequestMethod.POST)
    public DataResult xgmm(HttpServletRequest request, @RequestBody UserIn user)throws Exception {
        DataResult dataResult = new DataResult();
        UserIn loginUserIn = (UserIn) request.getSession().getAttribute("USER");
        if (loginUserIn != null) {
            try {
                String password = userService.findPassword(loginUserIn);
                if (password.equals(user.getOldPassword())) {
                    try {
                        dataResult = userService.updatePassword(loginUserIn.getUsername(), user.getNewPassword());
                        loginUserIn.setPassword(loginUserIn.getNewPassword());
                        request.getSession().setAttribute("USER", loginUserIn);
                    } catch (Exception e) {
                        dataResult.setSuccess(false);
                        dataResult.setRet(1);
                        dataResult.setMsg("密码更新失败，请重试！");
                        e.printStackTrace();
                    }
                } else {
                    dataResult.setSuccess(false);
                    dataResult.setRet(1);
                    dataResult.setMsg("原密码不正确，请重新输入！");
                }
            } catch (Exception e) {
                dataResult.setSuccess(false);
                dataResult.setRet(2);
                dataResult.setMsg("用户身份查询失败，请重新登录！");
                e.printStackTrace();
            }
        } else {
            dataResult.setSuccess(false);
            dataResult.setRet(2);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }
        return dataResult;
    }
}
