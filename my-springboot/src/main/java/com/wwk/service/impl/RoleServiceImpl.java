package com.wwk.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Role;
import com.wwk.entity.User;
import com.wwk.entity.UserRole;
import com.wwk.mapper.RoleMapper;
import com.wwk.mapper.RoleMenuMapper;
import com.wwk.mapper.UserRoleMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.RoleDTO;
import com.wwk.model.dto.RoleStatusDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.RoleVO;
import com.wwk.model.vo.UserRoleVO;
import com.wwk.service.RoleService;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.wwk.constant.CommonConstant.ADMIN;
import static com.wwk.constant.CommonConstant.TRUE;

/**
 * @author WWK
 * @program: my-springboot
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public PageResult<RoleVO> listRoleVO(ConditionDTO condition) {
        Long count = roleMapper.selectCountRoleVO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询角色列表
        List<RoleVO> roleVOList = roleMapper.selectRoleVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(roleVOList, count);
    }

    @Override
    public void addRole(RoleDTO role) {
        // 查重
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, role.getRoleName()));
        Assert.isNull(existRole, "角色已存在");
        // 添加角色
        Role newRole = Role.builder().roleName(role.getRoleName()).roleDesc(role.getRoleDesc()).isDisable(role.getIsDisable()).build();
        baseMapper.insert(newRole);
        // 添加权限
        roleMenuMapper.insertRoleMenu(newRole.getId(), role.getMenuIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(List<String> roleIdList) {
        Assert.isFalse(roleIdList.contains(ADMIN), "不能删除管理员");
        // 查角色是否分配给了某个用户
        Long count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList));
        Assert.isFalse(count > 0, "该角色已分配");
        // 删除角色
        roleMapper.deleteBatchIds(roleIdList);
        // 批量删除角色关联的菜单权限
        roleMenuMapper.deleteRoleMenu(roleIdList);
        // 删除Redis缓存中的菜单权限
        roleIdList.forEach(roleId -> {
            SaSession sessionById = SaSessionCustomUtil.getSessionById("role-" + roleId, false);
            Optional.ofNullable(sessionById).ifPresent(saSession -> saSession.delete("Permission_List"));
        });
    }

    @Override
    public void updateRole(RoleDTO role) {
        Assert.isFalse(role.getId().equals(ADMIN) && role.getIsDisable().equals(TRUE), "不允许禁用管理员角色");
        // 查重名字
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, role.getRoleName()));
        Assert.isFalse(Objects.nonNull(existRole) && !existRole.getId().equals(role.getId()));
        // 更新角色信息
        Role newRole = Role.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .roleDesc(role.getRoleDesc())
                .isDisable(role.getIsDisable())
                .build();
        roleMapper.updateById(newRole);
        // 先删除角色关联的菜单权限
        roleMenuMapper.deleteRoleMenuByRoleId(newRole.getId());
        // 再添加角色菜单权限
        roleMenuMapper.insertRoleMenu(newRole.getId(), role.getMenuIdList());
        // 删除Redis缓存中的菜单权限
        SaSession sessionById = SaSessionCustomUtil.getSessionById("role-", false);
        Optional.ofNullable(sessionById).ifPresent(saSession -> saSession.delete("Permission_List"));
    }

    @Override
    public void updateRoleStatus(RoleStatusDTO roleStatus) {
        Assert.isFalse(roleStatus.getId().equals(ADMIN), "不允许禁用管理员角色");
        Role newRole = Role.builder()
                .id(roleStatus.getId())
                .isDisable(roleStatus.getIsDisable())
                .build();
        baseMapper.updateById(newRole);
    }

    @Override
    public List<Integer> listRoleMenuTree(String roleId) {
        return roleMenuMapper.selectMenuByRoleId(roleId);
    }
}
