package com.example.site.utils;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.EXCEPTION.BusinessException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class qiniu {
    private static final Logger LOGGER = LoggerFactory.getLogger(qiniu.class);

    @Value("${qiniu.accesskey}")
    private String ACCESS_KEY;
    @Value("${qiniu.serectkey}")
    private String SECRET_KEY;
    @Value("${qiniu.bucket}")
    private String BUCKET;
    @Value("${qiniu.cdn.url}")
    public String QINIU_UPLOAD_SITE;
    public Configuration cfg;

    public qiniu() {
        //构造一个带指定Zone对象的配置类
        cfg = new Configuration(Region.huadongZheJiang2());

    }

    public String upload(MultipartFile file, String fileName) {

        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = null;

            response = uploadManager.put(file.getInputStream(), fileName, upToken, null, null);

            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(r.toString());
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(ex.getMessage());
        } catch (IOException e) {
            LOGGER.error("file upload failed", e);
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(e.getMessage());
        }
    }


    public void delete(String path) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET, path);
        } catch (Exception e) {
            //如果遇到异常，说明删除失败
            e.printStackTrace();
        }
    }
}
