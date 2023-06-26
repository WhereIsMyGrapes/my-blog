package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Menu;
import com.wwk.entity.Role;
import com.wwk.enums.RoleMenu;
import com.wwk.mapper.MenuMapper;
import com.wwk.mapper.RoleMapper;
import com.wwk.mapper.RoleMenuMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MenuDTO;
import com.wwk.model.vo.MenuOption;
import com.wwk.model.vo.MenuTree;
import com.wwk.model.vo.MenuVO;
import com.wwk.service.MenuService;
import com.wwk.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wwk.constant.CommonConstant.PARENT_ID;

/**
 * @author WWK
 * @program: my-springboot
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuVO> listMenuVO(ConditionDTO condition) {
        // 查询当前菜单列表
        List<MenuVO> menuVOList = menuMapper.selectMenuVOList(condition);
        Set<Integer> menuIdList = menuVOList.stream()
                .map(MenuVO::getId)
                .collect(Collectors.toSet());
        // 生成菜单id列表
        return menuVOList.stream()
                .map(menuVO -> {
                    Integer parentId = menuVO.getParentId();
                    // parentId不在当前菜单id列表，说明为父级菜单id，根据此id作为递归的开始条件节点
                    if (!menuIdList.contains(parentId)) {
                        menuIdList.add(parentId);
                        return recurMenuList(parentId, menuVOList);
                    }
                    return new ArrayList<MenuVO>();
                })
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    @Override
    public void addMenu(MenuDTO menu) {
        // 名字查重
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        Assert.isNull(existMenu, existMenu + "菜单已存在");
        Menu newMenu = BeanCopyUtils.copyBean(menu, Menu.class);
        baseMapper.insert(newMenu);
    }

    @Override
    public void delMenu(Integer menuId) {
        // 是否存在子菜单
        Long menuCount = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getId, menuId));
        Assert.isFalse(menuCount > 0, "存在子菜单");
        // 是否分配给了角色
        Long roleCount = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        Assert.isFalse(roleCount > 0, "已分配角色");
        baseMapper.deleteById(menuId);
    }

    @Override
    public void updateMenu(MenuDTO menu) {
        // 查重
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        // 存在就通过, 不存在且id不相等就断言
        Assert.isFalse(Objects.nonNull(existMenu) && !existMenu.getId().equals(menu.getId())
                , menu.getMenuName() + "菜单已经存在");
        // copy
        Menu newMenu = BeanCopyUtils.copyBean(menu, Menu.class);
        baseMapper.updateById(newMenu);
    }

    @Override
    public MenuDTO editMenu(Integer menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    @Override
    public List<MenuTree> listMenuTree() {
        List<MenuTree> menuTreeList = menuMapper.selectMenuTree();
        return recurMenuTreeList(PARENT_ID, menuTreeList);
    }

    @Override
    public List<MenuOption> listMenuOption() {
        List<MenuOption> menuOptionList = menuMapper.selectMenuOptions();
        return recurMenuOptionList(PARENT_ID, menuOptionList);
    }

    /**
     * 递归生成菜单列表
     *
     * @param parentId  父菜单id
     * @param menuList  菜单列表
     * @return {@link List< MenuVO>}  菜单列表
     */
    private List<MenuVO> recurMenuList(Integer parentId, List<MenuVO> menuList) {
        return menuList.stream()
                .filter(menuVO -> menuVO.getParentId().equals(parentId))
                .peek(menuVO -> menuVO.setChildren(recurMenuList(menuVO.getId(), menuList)))
                .collect(Collectors.toList());
    }


    /**
     * 递归生成菜单下拉树
     *
     * @param parentId     父菜单id
     * @param menuTreeList 菜单树列表
     * @return 菜单列表
     */
    private List<MenuTree> recurMenuTreeList(Integer parentId, List<MenuTree> menuTreeList) {
        return menuTreeList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(recurMenuTreeList(menu.getId(), menuTreeList)))
                .collect(Collectors.toList());
    }

    /**
     * 递归生成菜单选项树
     *
     * @param parentId       父菜单id
     * @param menuOptionList 菜单树列表
     * @return 菜单列表
     */
    private List<MenuOption> recurMenuOptionList(Integer parentId, List<MenuOption> menuOptionList) {
        return menuOptionList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(recurMenuOptionList(menu.getValue(), menuOptionList)))
                .collect(Collectors.toList());
    }


}
