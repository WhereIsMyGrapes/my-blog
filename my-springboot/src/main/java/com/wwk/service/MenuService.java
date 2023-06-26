package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Menu;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MenuDTO;
import com.wwk.model.vo.MenuOption;
import com.wwk.model.vo.MenuTree;
import com.wwk.model.vo.MenuVO;

import java.util.List;

/**
 * (Menu)表服务接口
 *
 * @author makejava
 * @since 2023-04-22 22:27:01
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查看菜单列表
     *
     * @return {@link MenuVO} 菜单列表
     */
    List<MenuVO> listMenuVO(ConditionDTO condition);

    /**
     * 添加菜单
     *
     * @param menu 菜单
     */
    void addMenu(MenuDTO menu);

    /**
     * 删除菜单
     *
     * @param menuId
     */
    void delMenu(Integer menuId);

    /**
     * 修改菜单
     *
     * @param menu 菜单
     */
    void updateMenu(MenuDTO menu);

    /**
     * 编辑菜单
     *
     * @param menuId 菜单id
     * @return {@link MenuDTO} 菜单
     */
    MenuDTO editMenu(Integer menuId);

    /**
     * 获取菜单下拉树
     *
     * @return 菜单下拉树
     */
    List<MenuTree> listMenuTree();

    /**
     * 获取菜单选项树
     *
     * @return 菜单选项树
     */
    List<MenuOption> listMenuOption();
}

