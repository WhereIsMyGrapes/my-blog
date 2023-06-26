package com.wwk.satoken;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.wwk.mapper.MenuMapper;
import com.wwk.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-22 17:58:26
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     *
     * @param loginId   登录用户id
     * @param loginType 登录账号类型
     * @return 权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 声明权限集合
        List<String> permissionList = new ArrayList<>();
        // 遍历角色列表, 查询拥有的权限码
        for (String roleId : getRoleList(loginId, loginType)){
            SaSession roleSession = SaSessionCustomUtil.getSessionById("role-" + roleId);
            List<String> list = roleSession.get("Permission_List",()->menuMapper.selectPermissionByRoleId(roleId));
            permissionList.addAll(list);
        }
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的可用角色标识集合
     *
     * @param loginId   登录用户id
     * @param loginType 登录账号类型
     * @return 角色集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 获取账号id为loginId的Session, 并决定在Session尚未创建时，是否新建并返回
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        // 取值 (若无值则执行参数方法, 之后将结果保存到此键名下,并返回此结果   若有值则直接返回, 无需执行参数方法)]
        return session.get("Role_List",roleMapper.selectRoleListByUserId(loginId));
    }
}
