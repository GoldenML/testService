package com.epoch.test.service;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.FileBean;
import com.epoch.test.bean.UserIn;
import com.epoch.test.controller.UserController;
import com.epoch.test.dao.FileDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    FileDao fileDao;
    private static final Log log = LogFactory.getLog(UserController.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public DataResult uploadzc(MultipartFile file, String uuid,String fileType, HttpServletRequest request) throws Exception{
        DataResult dataResult = new DataResult();
        UserIn loginUser = (UserIn) request.getSession().getAttribute("USER");
        FileBean fileBean = new FileBean();
        fileBean.setUuid(uuid);
        fileBean.setFileType(fileType);
        log.info("fileBean="+fileBean);
        fileBean.setUploadTime(sdf.format(new Date()));
        fileBean.setContributor(loginUser.getUsername());
        try {
            String filePath = upload2(file, request);
            fileBean.setFilePath(filePath);
            fileBean.setFileName(filePath.substring(filePath.lastIndexOf("\\") + 1));
            String fid = "";
            for (int i = 0; i < 10; i++) {
                fid += (int)(10 * Math.random());
            }
            fileBean.setFid(fid);
            System.out.println("filebens" + fileBean.getUuid());
            if (fileBean.getUuid() != null && !("".equals(fileBean.getUuid().trim()))) {

            } else {
                fileBean.setUuid(UUID.randomUUID().toString());
            }
            fileDao.uploadzc(fileBean);
            dataResult.setData(fileBean.getUuid());
            dataResult.setSuccess(true);
            dataResult.setMsg("上传成功！");
        }catch (Exception e) {
            throw e;
        }
        return dataResult;
    }
    public DataResult upload(FileBean fileBean) throws Exception {
        DataResult dataResult = new DataResult();
        try {
            fileDao.upload(fileBean.getUuid());
            dataResult.setSuccess(true);
            dataResult.setMsg("上传成功！");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }
    public DataResult filecx(FileBean fileBean, HttpServletRequest request) throws Exception{
        DataResult dataResult = new DataResult();
        UserIn loginUser = (UserIn)request.getSession().getAttribute("USER");
        fileBean.setContributor(loginUser.getUsername());
        try {
            PageHelper.startPage(fileBean.getPage(), fileBean.getSize());
            List<FileBean> list = fileDao.fileCx(fileBean);
            PageInfo<FileBean> pageInfo = new PageInfo<>(list,5);
            dataResult.setSuccess(true);
            dataResult.setTotalCount((int) pageInfo.getTotal());
            dataResult.setResultList(list);
            dataResult.setMsg("查询成功！");
        }catch (Exception e) {
            throw e;
        }
        return dataResult;
    }

    public DataResult allFileCx(HttpServletRequest request, FileBean fileBean) throws Exception{
        DataResult dataResult = new DataResult();
        UserIn userIn = (UserIn)request.getSession().getAttribute("USER");
        if (userIn != null) {
            try {
                fileBean.setUsername(userIn.getUsername());
                PageHelper.startPage(fileBean.getPage(), fileBean.getSize());
                List<FileBean> list = fileDao.allFileCx(fileBean);
                PageInfo<FileBean> pageInfo = new PageInfo<>(list,5);
                dataResult.setSuccess(true);
                dataResult.setTotalCount((int) pageInfo.getTotal());
                dataResult.setResultList(list);
                dataResult.setMsg("查询成功！");
            }catch (Exception e) {
                throw e;
            }
        } else {
            dataResult.setRet(2);
            dataResult.setSuccess(false);
            dataResult.setMsg("用户身份已过期，请重新登录！");
        }

        return dataResult;
    }
    public String upload2(MultipartFile file, HttpServletRequest request)throws Exception {
        // 获得项目的路径
        ServletContext sc = request.getSession().getServletContext();
        log.info(sc.getRealPath(""));
        // 上传位置
        String path = sc.getRealPath("/file") + File.separatorChar;
        File f = new File(path);
        if (!f.exists()){
            f.mkdirs();
        }
        // 获得原始文件名
        String fileName = file.getOriginalFilename();
        log.info("原始文件名:" + fileName);
        // 新文件名
        String newFileName = fileName;
        if (!file.isEmpty()) {
            try {
                FileOutputStream fos = new FileOutputStream(path
                        + newFileName);
                InputStream in = file.getInputStream();
                int b = 0;
                while ((b = in.read()) != -1) {
                    fos.write(b);
                }
                fos.close();
                in.close();
            } catch (Exception e) {
                throw e;
            }
        }
        log.info("上传文件到:" + path + newFileName);
        return path + fileName;
    }

    public DataResult filedel(FileBean fileBean) {
        DataResult dataResult = new DataResult();
        try{
            fileDao.filedel(fileBean.getFid());
            dataResult.setSuccess(true);
            dataResult.setMsg("删除成功！");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dataResult;
    }

    public DataResult cz(HttpServletRequest request, FileBean fileBean) throws Exception{
        DataResult dataResult = new DataResult();
        UserIn userIn = (UserIn)request.getSession().getAttribute("USER");
        fileBean.setUsername(userIn.getUsername());
        try{
            String time = sdf.format(new Date());
            fileBean.setCzTime(time);
            fileBean.setStarTime(time);
            FileBean fileBean1 = new FileBean();
            fileBean1 = fileDao.czcx(fileBean);
            if ("star".equals(fileBean.getCz())) {
                if (fileBean1 != null) {
                    if (fileBean1.getStar().equals(fileBean.getCz())) {
                        fileDao.starDel(fileBean);
                        fileDao.fileCzStarSub(fileBean);
                    } else {
                        fileDao.starChg(fileBean);
                        fileDao.fileCzStarAdd(fileBean);
                    }
                } else {
                    if ("star".equals(fileBean.getCz())) {
                        fileDao.star(fileBean);
                        fileDao.fileCzStarAdd(fileBean);
                    }
                }
            } else{
                if (fileBean1 != null) {
                    if (fileBean1.getCz().equals(fileBean.getCz())) {
                        if ("good".equals(fileBean1.getCz())) {
                            fileDao.czdel(fileBean);
                            fileDao.fileCzGoodSub(fileBean);
                        } else if ("bad".equals(fileBean1.getCz())){
                            fileDao.czdel(fileBean);
                            fileDao.fileCzBadSub(fileBean);
                        }

                    } else {
                        if ("good".equals(fileBean1.getCz())) {
                            fileDao.fileCzGoodSub(fileBean);
                        } else if ("bad".equals(fileBean1.getCz())){
                            fileDao.fileCzBadSub(fileBean);
                        }
                        if ("good".equals(fileBean.getCz())) {
                            fileDao.czchg(fileBean);
                            fileDao.fileCzGoodAdd(fileBean);
                        } else if ("bad".equals(fileBean.getCz())){
                            fileDao.czchg(fileBean);
                            fileDao.fileCzBadAdd(fileBean);
                        }
                    }
                } else {
                    if ("good".equals(fileBean.getCz())) {
                        fileDao.cz(fileBean);
                        fileDao.fileCzGoodAdd(fileBean);
                    } else if ("bad".equals(fileBean.getCz())) {
                        fileDao.cz(fileBean);
                        fileDao.fileCzBadAdd(fileBean);
                    }
                }
            }
            dataResult.setSuccess(true);
            dataResult.setMsg("操作成功！");
        }catch (Exception e) {
            throw e;
        }
        return dataResult;
    }
}
