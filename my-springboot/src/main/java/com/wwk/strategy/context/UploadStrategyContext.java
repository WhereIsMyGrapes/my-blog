package com.wwk.strategy.context;

import com.wwk.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import static com.wwk.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略上下文
 *
 * @author WWK
 * @program: my-springboot
 */
@Service
public class UploadStrategyContext {
    /**
     * 上传模式 （yml配置）
     */
    @Value("${upload.strategy}")
    private String uploadStrategy;

    @Autowired
    private Map<String, UploadStrategy> uploadStrategyMap;

    public String executeUploadStrategy(MultipartFile file, String path){
        return uploadStrategyMap.get(getStrategy(uploadStrategy)).uploadFile(file,path);
    }
}
