package com.wwk.strategy.context;

import com.wwk.enums.LoginTypeEnum;
import com.wwk.model.dto.CodeDTO;
import com.wwk.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录策略上下文
 *
 * @author WWK
 * @program: my-springboot
 */
@Service
public class SocialLoginStrategyContext {
    @Autowired
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    public String executeLoginStrategy(CodeDTO data, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

}
