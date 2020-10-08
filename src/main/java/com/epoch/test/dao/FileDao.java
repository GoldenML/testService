package com.epoch.test.dao;

import com.epoch.test.bean.DataResult;
import com.epoch.test.bean.FileBean;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface FileDao {
    @Insert("insert into file(upload_time, contributor, filename, file_path, filetype, fid, uuid, filezt) values(#{uploadTime}, #{contributor}, #{fileName}, #{filePath}, #{fileType} ,#{fid}, #{uuid}, 0)")
    void uploadzc(FileBean fileBean) throws Exception;

    @Update("update file set filezt = 1 where uuid = #{uuid}")
    void upload(@RequestParam String uuid)throws Exception;

    @Select({"<script>"
            + "select fid, filename, contributor ,name xingming,file_path filepath, upload_time uploadtime, star_num starnum, good_num goodNum, bad_num badNum, share_num sharenum, fid from file f inner join user u on f.contributor = u.username where filezt = 1 "
            + "<if test='contributor!=null'> and contributor=#{contributor} </if>"
            + "<if test='fileType!=null'> and #{fileType} like concat('%',filetype, '%') </if>"
            + "<if test='startTime!=null and endTime!=null'> and upload_time between #{startTime} and #{endTime} </if>"
            + " order by upload_time desc"
            + " </script>"})
    List<FileBean> fileCx(FileBean fileBean);

    @Select({"<script>"
            + "select f.fid, upload_time uploadTime,contributor , name xingming, filename, file_path filepath, star_num starNum, good_num goodNum, bad_num badNum, filetype, cz, cz_time czTime, star, star_time starTime from file f left join cz c on f.fid = c.fid and c.username = #{username} inner join user u on f.contributor = u.username  where filezt = 1"
            + "<if test='contributor!=null'> and name like concat('%',#{contributor},'%') </if>"
            + "<if test='fileType!=null'> and #{fileType} like concat('%',filetype, '%') </if>"
            + "<if test='startTime!=null and endTime!=null'> and upload_time between #{startTime} and #{endTime} </if>"
            + " order by upload_time desc"
            + " </script>"})
    List<FileBean> allFileCx(FileBean fileBean);
    @Delete("delete from file where fid = #{fid}")
    void filedel(@RequestParam String fid);

    @SelectProvider(type = FileProvider.class, method = "filecx")
    List<FileBean> filecx2(FileBean fileBean);

    @Insert("insert into cz(fid, username, cz, cz_time) values(#{fid}, #{username}, #{cz}, #{czTime})")
    void cz(FileBean fileBean);
    @Update("update cz set cz = #{cz}, cz_time = #{czTime} where fid = #{fid} and username = #{username}")
    void czchg(FileBean fileBean);
    @Select("select * from cz where fid = #{fid} and username = #{username}")
    FileBean czcx(FileBean fileBean);
    @Update("update cz set cz = '', cz_time = '' where fid = #{fid} and username = #{username}")
    void czdel(FileBean fileBean);

    @Update("update file set star_num = star_num - 1 where fid = #{fid}")
    void fileCzStarSub(FileBean fileBean);
    @Update("update file set good_num = good_num - 1 where fid = #{fid}")
    void fileCzGoodSub(FileBean fileBean);
    @Update("update file set bad_num = bad_num - 1 where fid = #{fid}")
    void fileCzBadSub(FileBean fileBean);
    @Update("update file set star_num = star_num + 1 where fid = #{fid}")
    void fileCzStarAdd(FileBean fileBean);
    @Update("update file set good_num = good_num + 1 where fid = #{fid}")
    void fileCzGoodAdd(FileBean fileBean);
    @Update("update file set bad_num = bad_num + 1 where fid = #{fid}")
    void fileCzBadAdd(FileBean fileBean);
    @Insert("insert into cz(fid, username, star, star_time) values(#{fid}, #{username}, #{cz}, #{starTime})")
    void star(FileBean fileBean);
    @Update("update cz set star = '', star_time = '' where fid = #{fid} and username = #{username}")
    void starDel(FileBean fileBean);
    @Update("update cz set star = #{cz}, star_time = #{starTime} where fid = #{fid} and username = #{username}")
    void starChg(FileBean fileBean);

    class FileProvider {
        public String filecx(FileBean fileBean) {
            String sql = "select *from file where 1 = 1 ";
            if (fileBean.getContributor() != null && fileBean.getContributor().trim() != "") {
                sql += "and contributor" + fileBean.getContributor() + " ";
            }
            if (fileBean.getFileType() != null && fileBean.getFileType().trim() != "") {
                String[] s = fileBean.getFileType().split(",");
                if (s.length == 1) {
                    sql += "and filetype" + fileBean.getFileType() + " ";
                } else {
                    for (int i = 0; i < s.length; i++) {
                        if (i == 0) {
                            sql += " ";
                        }

                    }
                }
            }
            return sql;
        }
    }
}
