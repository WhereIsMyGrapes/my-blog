import "@/assets/styles/index.scss";
// 自定义事件
import * as directive from "@/directive";
// echart可视化
import CalendarHeatmap from "@/components/CalendarHeatmap/index.vue";
import Echarts from "@/components/Echarts/index.vue";
// 引入组件
import SvgIcon from '@/components/svgIcon/index.vue' 
import Pagination from "@/components/Pagination/index.vue";
import RightToolbar from "@/components/RightToolbar/index.vue";
// md编辑器
import VMdEditor from "@kangc/v-md-editor";
import "@kangc/v-md-editor/lib/plugins/emoji/emoji.css";
import createEmojiPlugin from "@kangc/v-md-editor/lib/plugins/emoji/index";
import createTodoListPlugin from "@kangc/v-md-editor/lib/plugins/todo-list/index";
import "@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import "@kangc/v-md-editor/lib/theme/style/vuepress.css";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress.js";
// 语法高亮
import Prism from "prismjs";
// 持久化管理工具
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
// 自定义路由
import router from "@/router/index";
// 自定义未登录路由拦截(鉴权)
import "@/permission.ts"
// elementPlus
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import 'element-plus/dist/index.css'
import "element-plus/theme-chalk/index.css";
import "element-plus/theme-chalk/dark/css-vars.css";
// 虚假进度条
import "nprogress/nprogress.css"; 
// 图片查看器
import VueViewer from "v-viewer";
import "viewerjs/dist/viewer.css";
//svg
import "virtual:svg-icons-register"; // 引入注册脚本
import App from './App.vue'
import { createApp, Directive } from 'vue'

const app = createApp(App);
const pinia = createPinia();

VMdEditor.use(vuepressTheme, { Prism }).use(createEmojiPlugin()).use(createTodoListPlugin());

Object.keys(directive).forEach((key) => {
    app.directive(key, (directive as { [key: string]: Directive })[key]);
});

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(router);
app.use(VMdEditor);
// app.use(VueViewer);
app.use(ElementPlus);
app.component("CalendarHeatmap", CalendarHeatmap);
app.component("svg-icon", SvgIcon);
app.component("Pagination", Pagination);
app.component("RightToolbar", RightToolbar);
app.component("Echarts", Echarts);
app.mount('#app');
