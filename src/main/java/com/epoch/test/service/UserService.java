package com.epoch.test.service;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.UserIn;
import com.epoch.test.bean.UserOut;
import com.epoch.test.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;
    public DataResult login(String username, String password) throws Exception{
        DataResult dataResult = new DataResult();
        try {
            int count = userDao.findByUsernameAndPassword(username, password);
            if (count > 0) {
                dataResult.setSuccess(true);
                dataResult.setMsg("登陆成功！");
            } else {
                dataResult.setSuccess(false);
                dataResult.setMsg("账号或用户名错误！");
            }
            return dataResult;
        } catch (Exception e) {
            throw e;
        }
    }

    public DataResult findJbxx(UserIn loginUserIn) throws Exception{
        DataResult dataResult = new DataResult();
        try {
            UserOut userOut = userDao.findJbxx(loginUserIn.getUsername());
            if (userOut != null) {
                dataResult.setData(userOut);
                dataResult.setSuccess(true);
                dataResult.setMsg("查询成功");
            } else {
                dataResult.setSuccess(false);
                dataResult.setMsg("未查到用户信息，请重新登录！");
            }
        } catch (Exception e) {
            throw e;
        }
        return dataResult;
    }
    public String findPassword(UserIn loginUserIn) throws Exception{
        String  password = "";
        try {
            password = userDao.findPassword(loginUserIn.getUsername());
        } catch (Exception e) {
            throw e;
        }
        return password;
    }
    public DataResult updatePassword(String username, String  newPssword) throws Exception{
        DataResult dataResult = new DataResult();
        try {
            userDao.updatePassword(username, newPssword);
            dataResult.setSuccess(true);
            dataResult.setMsg("修改成功");
        } catch (Exception e) {
            throw e;
        }
        return dataResult;
    }
}
