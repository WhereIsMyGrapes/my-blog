package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.SiteConfig;
import org.springframework.web.multipart.MultipartFile;

/**
 * (SiteConfig)表服务接口
 *
 * @author makejava
 */
public interface SiteConfigService extends IService<SiteConfig> {

    /**
     * 获取网站配置
     *
     * @return 网站配置
     */
    SiteConfig getSiteConfig();

    /**
     * 更新网站配置
     *
     * @param siteConfig 网站配置
     */
    void updateSiteConfig(SiteConfig siteConfig);

    /**
     * 上传网站配置图片
     *
     * @param file 图片
     * @return 图片路径
     */
    String uploadSiteImg(MultipartFile file);
}

