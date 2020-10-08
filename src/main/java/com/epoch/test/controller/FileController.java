package com.epoch.test.controller;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.FileBean;
import com.epoch.test.bean.UserIn;
import com.epoch.test.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;
    @RequestMapping(value = "uploadzc", method = RequestMethod.POST)
    public DataResult uploadzc(@RequestParam("file") MultipartFile file, HttpServletRequest request)throws Exception{
        DataResult dataResult = new DataResult();
        UserIn loginUserIn = (UserIn)request.getSession().getAttribute("USER");
        FileBean fileBean = new FileBean();
        System.out.println(request.getParameter("uuid") != null);
        System.out.println(request.getParameter("uuid").trim() != "");
        System.out.println(request.getParameter("uuid"));
        if (loginUserIn != null) {
            try {
                dataResult = fileService.uploadzc(file, request.getParameter("uuid"), request.getParameter("fileType"), request);
            } catch (Exception e) {
                e.printStackTrace();
                dataResult.setSuccess(false);
                dataResult.setMsg("文件上传出错！");
            }
        }else {
            dataResult.setSuccess(false);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }
        return dataResult;
    }
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public DataResult upload(@RequestBody FileBean fileBean, HttpServletRequest request)throws Exception{
        DataResult dataResult = new DataResult();
        UserIn loginUserIn = (UserIn)request.getSession().getAttribute("USER");
        if (loginUserIn != null) {
            try {
                dataResult = fileService.upload(fileBean);
            } catch (Exception e) {
                e.printStackTrace();
                dataResult.setSuccess(false);
                dataResult.setMsg("保存出错！");
            }
        }else {
            dataResult.setRet(2);
            dataResult.setSuccess(false);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }
        return dataResult;
    }
    @RequestMapping(value = "filecx", method = RequestMethod.POST)
    public DataResult filecx(HttpServletRequest request, @RequestBody FileBean fileBean)throws Exception{
        DataResult dataResult = new DataResult();
        try {
            dataResult = fileService.filecx(fileBean, request);
        } catch (Exception e) {
            e.printStackTrace();
            dataResult.setSuccess(false);
            dataResult.setMsg("文件查询出错！");
        }
        return dataResult;
    }
    @RequestMapping(value = "allfilecx", method = RequestMethod.POST)
    public DataResult allFileCx(HttpServletRequest request, @RequestBody FileBean fileBean)throws Exception{
        DataResult dataResult = new DataResult();
        try {
            dataResult = fileService.allFileCx(request, fileBean);
        } catch (Exception e) {
            e.printStackTrace();
            dataResult.setSuccess(false);
            dataResult.setMsg("文件查询出错！");
        }
        return dataResult;
    }
    @RequestMapping(value = "filedel", method = RequestMethod.POST)
    public DataResult delete(@RequestBody FileBean fileBean, HttpServletRequest request) {
        DataResult dataResult = new DataResult();
        UserIn loginUserIn = (UserIn)request.getSession().getAttribute("USER");
        if (loginUserIn != null) {
            try {
                dataResult = fileService.filedel(fileBean);
            } catch (Exception e) {
                e.printStackTrace();
                dataResult.setSuccess(false);
                dataResult.setMsg("文件删除出错！");
            }
        }else {
            dataResult.setRet(2);
            dataResult.setSuccess(false);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }
        return dataResult;
    }

    @RequestMapping(value = "cz", method = RequestMethod.POST)
    public DataResult cz(HttpServletRequest request, @RequestBody FileBean fileBean) throws Exception{
        DataResult dataResult = new DataResult();
        try {
            dataResult = fileService.cz(request, fileBean);
        }catch (Exception e) {
            e.printStackTrace();
            dataResult.setSuccess(false);
            dataResult.setMsg("操作失败！");
        }
        return dataResult;
    }


}
