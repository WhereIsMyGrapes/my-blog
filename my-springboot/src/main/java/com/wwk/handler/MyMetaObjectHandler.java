package com.wwk.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.wwk.enums.ZoneEnum.SHANGHAI;

/**
 * mp 自动填充
 *
 * @author WWK
 * @program: my-springboot
 */
@Log4j2
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充, 插入");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class,LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充, 更新");
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class,LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }
}