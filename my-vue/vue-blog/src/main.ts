// 基础样式
import "@/assets/fonts/font.css";
import "@/assets/styles/index.scss";
import SvgIcon from "@/components/SvgIcon/index.vue"
// 自定义指令, 可视区
import * as directive from "@/directive";
// md编辑器
import createKatexPlugin from "@kangc/v-md-editor/lib/plugins/katex/cdn";
import createTodoListPlugin from "@kangc/v-md-editor/lib/plugins/todo-list/index";
import "@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css";
import VMdPreview from "@kangc/v-md-editor/lib/preview";
import "@kangc/v-md-editor/lib/theme/style/vuepress.css";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress.js";
// vite虚拟icon注册
import "virtual:svg-icons-register"
// pinia持久化管理
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import { createApp, Directive } from 'vue'
// 引入路由
import router from "@/router";
// 引入nativeUI
import naive from "naive-ui";
// 引入动态标签名
import { titleChange } from "@/utils/title";
// swiper
import "swiper/css";
import "swiper/css/autoplay";
import "swiper/css/mousewheel";
import "swiper/css/navigation";
import "swiper/css/pagination";
//代码高亮
import Prism from "prismjs";
// 虚假进度条
import "nprogress/nprogress.css";
import "@/permission";
// 图片预览器
import VueViewer from "v-viewer";
import "viewerjs/dist/viewer.css";
import { VueMasonryPlugin } from "vue-masonry";

import App from './App.vue'
// 懒加载
import error from "./assets/images/404.gif";
import loading from "./assets/images/loading.gif";
import lazyPlugin from "vue3-lazy";

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
VMdPreview.use(vuepressTheme, {
    Prism,
})
  .use(createTodoListPlugin())
  .use(createKatexPlugin());
app.use(VMdPreview);
app.use(router);
app.use(naive);
app.use(VueViewer);
app.use(pinia);
app.use(lazyPlugin, {
    loading,
    error,
});
app.use(VueMasonryPlugin);
app.component("svg-icon", SvgIcon);
app.mount('#app');
titleChange();
