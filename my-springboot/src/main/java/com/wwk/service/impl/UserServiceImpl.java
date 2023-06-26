package com.wwk.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Menu;
import com.wwk.entity.Role;
import com.wwk.entity.User;
import com.wwk.entity.UserRole;
import com.wwk.enums.FilePathEnum;
import com.wwk.enums.RoleMenu;
import com.wwk.mapper.*;
import com.wwk.model.dto.*;
import com.wwk.model.vo.*;
import com.wwk.service.RedisService;
import com.wwk.service.UserService;
import com.wwk.strategy.context.UploadStrategyContext;
import com.wwk.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.wwk.constant.CommonConstant.*;
import static com.wwk.constant.RedisConstant.*;
import static com.wwk.utils.PageUtils.getLimit;
import static com.wwk.utils.PageUtils.getSize;

/**
 * @author WWK
 * @program: my-springboot
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 修改后台密码
     * @param password 密码
     */
    @Override
    public void updateAdminPassword(PasswordDTO password) {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 判断用户存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId));
        Assert.notNull(user, "用户不存在");
        // 检查旧密码
        Assert.isTrue(SecurityUtils.checkPw(user.getPassword(), password.getOldPassword()),
                "旧密码错误");
        // 修改密码
        String newPassword = SecurityUtils.sha256Encrypt(password.getNewPassword());
        user.setPassword(newPassword);
        baseMapper.updateById(user);
    }

    @Override
    public UserBackInfoVo getUserBackInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查用户头像
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().
                select(User::getAvatar).eq(User::getId, userId));
        // 查用户角色列表
        List<String> roleList = StpUtil.getRoleList();
//        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
//                .select(Role::getRoleName)
//                .eq(Role::getId, userId));
//        List<String> roleNameList = roleList.stream()
//                                    .map(Role::getRoleName)
//                                    .collect(Collectors.toList());
        // 查角色权限列表
        List<String> permissionList = StpUtil.getPermissionList().stream().
                filter(string -> !string.isEmpty()).
                distinct()
                .collect(Collectors.toList());
//        List<RoleMenu> permList = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>()
//                .select(RoleMenu::getMenuId)
//                .eq(RoleMenu::getRoleId, userId.toString()));
//        List<Integer> temp = permList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
//        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
//                .select(Menu::getPerms)
//                .in(Menu::getId, temp));
//        List<String> perms = menuList.stream()
//                .map(Menu::getPerms)
//                .filter(string -> !string.isEmpty())
//                .distinct()
//                .collect(Collectors.toList());
        // 返回出去
        return UserBackInfoVo.builder()
                .id(userId)
                .avatar(user.getAvatar())
                .roleList(roleList)
                .permissionList(permissionList)
                .build();
    }

    /**
     * 获取后台登录用户的权限菜单信息
     *
     * @param
     * @return {@link List< RouterVO>}
     */
    @Override
    public List<RouterVO> getUserMenu() {
        // 查询用户菜单
        List<UserMenuVO> userMenuVOList = menuMapper.selectMenuByUserId(StpUtil.getLoginIdAsInt());
        // 递归生成路由, parentId 为 0
        return this.recurRoutes(PARENT_ID, userMenuVOList);
    }

    @Override
    public PageResult<OnlineVO> listOnlineUser(ConditionDTO condition) {
        // 查询所有会话token
        List<String> tokenList = StpUtil.searchTokenSessionId("", 0, -1, false);
        List<OnlineVO> onlineVOList = tokenList.stream()
                .map(token -> {
                    // 获取tokenSession
                    SaSession sessionBySessionId = StpUtil.getSessionBySessionId(token);
                    return (OnlineVO) sessionBySessionId.get(ONLINE_USER);
                })
                .filter(onlineVO -> StringUtils.isEmpty(condition.getKeyword()) || onlineVO.getNickname().contains(condition.getKeyword()))
                .sorted(Comparator.comparing(OnlineVO::getLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = getLimit().intValue();
        int size = getSize().intValue();
        int toIndex = onlineVOList.size() - fromIndex > size ? fromIndex + size : onlineVOList.size();
        List<OnlineVO> userOnlineList = onlineVOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, (long) onlineVOList.size());
    }

    @Override
    public void kickOutUser(String token) {
        StpUtil.logoutByTokenValue(token);
    }

    @Override
    public PageResult<UserBackVO> listUserBackVO(ConditionDTO condition) {
        // 数量
        Long count = userMapper.countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查内容
        List<UserBackVO> userBackVOList = userMapper.listUserBackVO(getLimit(), getSize(), condition);
        return new PageResult<>(userBackVOList, count);
    }

    @Override
    public List<UserRoleVO> listUserRoleDTO() {
        // 查用户的角色列表
        return roleMapper.selectUserRoleList();
    }

    @Override
    public void updateUser(UserRoleDTO user) {
        // 更新基础信息
        User newUser = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
        // 删除用户角色
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, user.getId()));
        // 重新添加用户角色
        userRoleMapper.insertUserRole(user.getId(), user.getRoleIdList());
        // 移除redis中的缓存
        SaSession sessionByLoginId = SaSessionCustomUtil.getSessionById(user.getId().toString(), false);
        Optional.ofNullable(sessionByLoginId).ifPresent(saSession -> saSession.delete("Role_List"));
    }

    @Override
    public void updateUserStatus(DisableDTO disable) {
        // 更新状态
        User newUser = User.builder()
                .id(disable.getId())
                .isDisable(disable.getIsDisable())
                .build();
        userMapper.updateById(newUser);
        if (disable.getIsDisable().equals(TRUE)) {
            // 先踢下线
            StpUtil.logout(disable.getId());
            // 再封禁账号
            StpUtil.disable(disable.getId(), 86400);
        } else {
            // 解除封禁
            StpUtil.untieDisable(disable.getId());
        }
    }

    @Override
    public UserInfoVO getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar, User::getUsername, User::getWebSite,
                        User::getIntro, User::getEmail, User::getLoginType)
                .eq(User::getId, userId));
        Set<Object> articleLikeSet = redisService.getSet(USER_ARTICLE_LIKE + userId);
        Set<Object> commentLikeSet = redisService.getSet(USER_COMMENT_LIKE + userId);
        Set<Object> talkLikeSet = redisService.getSet(USER_TALK_LIKE + userId);
        return UserInfoVO
                .builder()
                .id(userId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .webSite(user.getWebSite())
                .intro(user.getIntro())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .loginType(user.getLoginType())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserEmail(EmailDTO email) {
        verifyCode(email.getEmail(), email.getCode());
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .email(email.getEmail())
                .build();
        userMapper.updateById(newUser);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户头像
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(avatar)
                .build();
        userMapper.updateById(newUser);
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoDTO userInfo) {
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .nickname(userInfo.getNickname())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .build();
        userMapper.updateById(newUser);
    }

    @Override
    public void updatePassword(UserDTO user) {
        verifyCode(user.getUsername(), user.getCode());
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, user.getUsername()));
        Assert.notNull(existUser, "邮箱尚未注册！");
        // 根据用户名修改密码
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, SecurityUtils.sha256Encrypt(user.getPassword()))
                .eq(User::getUsername, user.getUsername()));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     */
    private void verifyCode(String username, String code) {
        String sysCode = redisService.getObject(CODE_KEY + username);
        Assert.notBlank(sysCode, "验证码未发送或已过期！");
        Assert.isTrue(sysCode.equals(code), "验证码错误，请重新输入！");
    }

    /**
     * 递归生成路由列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return {@link java.util.List<com.wwk.model.vo.RouterVO>} 路由列表
     */
    private List<RouterVO> recurRoutes(Integer parentId, List<UserMenuVO> menuList) {
        ArrayList<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterVO routeVO = new RouterVO();
                    routeVO.setName(menu.getMenuName());
                    routeVO.setName(menu.getMenuName());
                    routeVO.setPath(getRouterPath(menu));
                    routeVO.setComponent(getComponent(menu));
                    routeVO.setMeta(MetaVO.builder()
                            .title(menu.getMenuName())
                            .icon(menu.getIcon())
                            .hidden(menu.getIsHidden().equals(TRUE))
                            .build());
                    if(menu.getMenuType().equals(TYPE_DIR)){
                        List<RouterVO> children = recurRoutes(menu.getId(),menuList);
                        if(CollectionUtil.isNotEmpty(children)){
                            routeVO.setAlwaysShow(true);
                            routeVO.setRedirect("noRedirect");
                        }
                        routeVO.setChildren(children);
                    } else if (isMenuFrame(menu)){
                        routeVO.setMeta(null);
                        List<RouterVO> childrenList = new ArrayList<>();
                        RouterVO children = new RouterVO();
                        children.setName(menu.getMenuName());
                        children.setPath(menu.getPath());
                        children.setComponent(menu.getComponent());
                        children.setMeta(MetaVO.builder()
                                .title(menu.getMenuName())
                                .icon(menu.getIcon())
                                .hidden(menu.getIsHidden().equals(TRUE))
                                .build());
                        childrenList.add(children);
                        routeVO.setChildren(childrenList);
                    }
                    list.add(routeVO);
                })
        );
        return list;
    }

    /**
     * 让前端动态获取路由地址
     *
     * @param menu 菜单信息
     * @return {@link java.lang.String} 路由地址
     */
    public String getRouterPath(UserMenuVO menu) {
        String routerPath = menu.getPath();
        // 一级目录 M
        if (menu.getParentId().equals(PARENT_ID) && menu.getMenuType().equals(TYPE_DIR)) {
            routerPath = "/" + routerPath;
        }
        // 一级菜单 C
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return {@link java.lang.String} 组件信息
     */
    public String getComponent(UserMenuVO menu) {
        String component = LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isNotEmpty(menu.getComponent()) && isParentView(menu)) {
            component = PARENT_VIEW;
        }
        return component;
    }


    /**
     * 判断是否为菜单内部跳转, vue单页面模式
     *
     * @param menu 菜单信息
     * @return {@link boolean}
     */
    public boolean isMenuFrame(UserMenuVO menu) {
        return menu.getParentId().equals(PARENT_ID) && TYPE_MENU.equals(menu.getMenuType());
    }

    /**
     * 判断是否为parent_view组件 (可能多余)
     *
     * @param menu 菜单信息
     * @return {@link boolean}
     */
    public boolean isParentView(UserMenuVO menu) {
        return !menu.getParentId().equals(PARENT_ID) && TYPE_DIR.equals(menu.getMenuType());
    }


}
