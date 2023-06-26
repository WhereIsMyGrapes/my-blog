package com.wwk.satoken;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwk.entity.User;
import com.wwk.mapper.UserMapper;
import com.wwk.model.vo.OnlineVO;
import com.wwk.service.UserService;
import com.wwk.utils.IpUtils;
import com.wwk.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static com.wwk.constant.CommonConstant.ONLINE_USER;
import static com.wwk.enums.ZoneEnum.SHANGHAI;


/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-18 22:31:26
 */
@Component
public class MySaTokenListener implements SaTokenListener {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;


    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        // 查询用户昵称
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar,User::getNickname)
                .eq(User::getId,loginId));
        // 解析browser和os
        Map<String,String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 获取登录ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取登录地址(省份)
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 获取登录时间
        LocalDateTime loginTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
        OnlineVO onlineVO = OnlineVO.builder()
                .id((Integer) loginId)
                .token(tokenValue)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .os(userAgentMap.get("os"))
                .browser(userAgentMap.get("browser"))
                .loginTime(loginTime)
                .build();
        // 更新用户登录信息
        User newUser = User.builder()
                .id((Integer) loginId)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .loginTime(loginTime)
                .build();
        userMapper.updateById(newUser);
        // 用户在线信息存入tokenSession
        SaSession tokenSession = StpUtil.getTokenSessionByToken(tokenValue);
        tokenSession.set(ONLINE_USER, onlineVO);
    }

    /**
     * 每次注销登出时触发
     * @param loginType
     * @param loginId
     * @param tokenValue
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        // 删除缓存中的用户信息
        StpUtil.logoutByTokenValue(tokenValue);
    }

    @Override
    public void doRegisterComponent(String comtName, Object comtObj) {

    }

    @Override
    public void doSetStpLogic(StpLogic stpLogic) {

    }

    @Override
    public void doSetConfig(SaTokenConfig config) {

    }


    @Override
    public void doKickout(String s, Object o, String s1) {

    }

    @Override
    public void doReplaced(String s, Object o, String s1) {

    }

    @Override
    public void doDisable(String s, Object o, String s1, int i, long l) {

    }

    @Override
    public void doUntieDisable(String s, Object o, String s1) {

    }

    @Override
    public void doOpenSafe(String s, String s1, String s2, long l) {

    }

    @Override
    public void doCloseSafe(String s, String s1, String s2) {

    }

    @Override
    public void doCreateSession(String s) {

    }

    @Override
    public void doLogoutSession(String s) {

    }

    @Override
    public void doRenewTimeout(String s, Object o, long l) {

    }
}
