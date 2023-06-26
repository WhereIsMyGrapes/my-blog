import { RouteLocationNormalized, RouteRecordRaw } from "vue-router";

export interface TagView extends Partial<RouteLocationNormalized> {
  title?: string;
}

export interface TagViewState {
  visitedViews: TagView[];
}
/**
 * 应用
 */
export interface AppState{
    /**
     * 侧边栏开关
     */
    isCollapse:boolean;
    /**
     * 响应式操作
     */
    device:string;
    /**
     * 大小
     */
    size:string;
}

/**
 * 设置
 */
export interface SettingState {
    /**
     * 是否显示 tagView
     */
    tagView: boolean;
    /**
     * 是否固定头部
     */
    fixedHeader: boolean;
    /**
     * 是否显示Logo
     */
    sidebarLogo: boolean;
  }


export interface PermissionState{
    /**
     * 路由
     */
    routes:RouteRecordRaw[];
}

/**
 * 用户
 */
export interface UserState {
  /**
   * 用户id
   */
  id: number | null;
  /**
   * 用户头像
   */
  avatar: string;
  /**
   * 角色
   */
  roleList: string[];
  /**
   * 权限
   */
  permissionList: string[];
}
