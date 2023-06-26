<template>
    <el-breadcrumb class="app-breadcrumb" separator="/">
        <TransitionGroup name="breadcrumb">
            <el-breadcrumb-item  v-for="(item, index) in breadcrumbList" :key="item.path">
                <span v-if="item.redirect == 'noRedirect' || index == breadcrumbList.length - 1" class="no-redirect">
                    {{ item.meta.title }}
                </span>
                <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
            </el-breadcrumb-item>
        </TransitionGroup>
    </el-breadcrumb>
</template>

<script setup lang="ts">
import router from '@/router';
import { onBeforeMount, ref, watch } from "vue";
import { RouteLocationMatched, useRoute } from "vue-router";

const breadcrumbList = ref([] as Array<RouteLocationMatched>);
const currentRoute = useRoute();
// 生成面包屑导航
const getBreadCrumb = () => {
    let matched = currentRoute.matched.filter(item => item.name);
    const first = matched[0];
    if (!isDashboard(first)) {
        // 添加首页进去
        matched = [{ path: "/index", meta: { title: "首页" } } as any].concat(matched);
    }
    breadcrumbList.value = matched.filter(item => item.meta && item.meta.title);
};
// 判断某条路由是否为 /index 首页路由。
const isDashboard = (route: RouteLocationMatched) => {
  const name = route && route.name;
  if (!name) {
    return false;
  }
  return name.toString().trim() === 'Index';
};
// 点击某条路由时,进行路由导航 (重定向 或 普通路由导航)
const handleLink = (item: any) => {
  const { redirect, path } = item;
  if (redirect) {
    router.push(redirect);
    return;
  }
  router.push(path);
};
// 监听路由变化
watch(
  () => currentRoute.path,
  path => {
    if (path.startsWith('/redirect/')) {
      return;
    }
    getBreadCrumb();
  }
);
// 路由导航前调用
onBeforeMount(() => {
  getBreadCrumb();
});

</script>

<style lang="scss" scoped>
</style>
