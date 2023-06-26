import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: () => import("@/views/Home/index.vue"),
    meta: {
      title: "首页",
    },
  },
  {
    path: "/user",
    component: () => import("@/views/User/index.vue"),
    meta: {
      title: "个人中心",
    },
  },
  {
    name: "talk",
    path: "/talk",
    component: () => import("@/views/Talk/TalkList.vue"),
    meta: {
      title: "说说",
    },
  },
  {
    name: "talkInfo",
    path: "/talk/:id",
    component: () => import("@/views/Talk/Talk.vue"),
    meta: {
      title: "说说",
    },
  },
  {
    name: "article",
    path: "/article/:id",
    component: () => import("@/views/Article/Article.vue"),
    meta: {
      title: "文章",
    },
  },
  {
    path: "/category",
    component: () => import("@/views/Category/index.vue"),
    meta: {
      title: "分类",
    },
  },
  {
    path: "/category/:id",
    component: () => import("@/views/Category/ArticleList.vue"),
  },
  {
    path: "/archive",
    component: () => import("@/views/Archive/index.vue"),
    meta: {
      title: "归档",
    },
  },
  {
    path: "/tag",
    component: () => import("@/views/Tag/index.vue"),
    meta: {
      title: "标签",
    },
  },
  {
    path: "/tag/:tagId",
    component: () => import("@/views/Tag/ArticleList.vue"),
  },
  {
    path: "/message",
    component: () => import("@/views/Message/index.vue"),
    meta: {
      title: "留言",
    },
  },
  {
    name: "friend",
    path: "/friend",
    component: () => import("@/views/Friend/index.vue"),
    meta: {
      title: "友链",
    },
  },
  {
    path: "/album",
    component: () => import("@/views/Album/Album.vue"),
    meta: {
      title: "相册",
    },
  },
  {
    path: "/album/:albumId",
    component: () => import("@/views/Album/Photo.vue"),
  },
  {
    path: "/404",
    component: () => import("@/views/404/index.vue"),
    meta: {
      title: "404",
    },
  },
  { path: "/:catchAll(.*)", redirect: "/404" },

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
  
export default router;
