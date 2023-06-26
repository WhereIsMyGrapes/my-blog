<template>
  <div :class="classObj" class="app-wrapper">
    <!-- 手机模式打开左侧边栏后, 右边的半透明遮罩 -->
    <div v-if="(device === 'mobile' && !app.isCollapse)" 
    class="drawer-bg"  
    @click="handleClickOutside" />
    <!-- 左侧边栏 -->
    <SideBar class="sidebar-container"></SideBar>
    <div :class="{hasTagsView: needTagView}" class="main-container">
      <div :class="{'fixed-header': fixedHeader}">
          <!-- 头部导航栏 -->
          <NavBar @setLayout="setLayout"></NavBar>
          <!-- 历史标签栏 -->
          <TagView v-if="needTagView"></TagView>
      </div>
      <AppMain></AppMain>
      <!-- 设置 -->
      <Settings ref="settingRef"></Settings>
    </div>

  </div>
</template>

<script setup lang="ts">
import useStore from '@/store';
import {computed,ref,watchEffect,onMounted} from 'vue';
import {useWindowSize} from "@vueuse/core"
import SideBar from "./components/SideBar/index.vue";
import NavBar from "./components/NavBar/index.vue";
import AppMain from "./components/AppMain/index.vue";
import Settings from "@/components/Settings/index.vue";


const settingRef = ref();
const {app , setting} = useStore();
const {width} =useWindowSize();
const WIDTH = 992;
const device = computed(()=>app.device)
// 面包屑历史导航
const needTagView = computed(() => setting.tagView);
// 是否固定头部
const fixedHeader = computed(() => setting.fixedHeader);
const classObj = computed(() => ({
  hideSidebar: app.isCollapse,
  openSidebar: !app.isCollapse,
  mobile: device.value === "mobile",
}));

watchEffect(()=>{
  if(width.value - 1 < WIDTH){
    app.toggleDevice("mobile");
    app.changeCollapse(true);
    // console.log(app.$state.device);
  }else {
    app.toggleDevice("desktop");
  }
});

const handleClickOutside = ()=>{
  app.changeCollapse(true);
}

const setLayout = () => {
  settingRef.value.openSetting();
}

onMounted(() => {
  // console.log(app.$state.isCollapse)
})

</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header{
  position:fixed;
  top: 0;
  right: 0;
  z-index: 40;
  width: calc(100% - #{$sideBarWidth});
  transition: width 0.28s;
}

.hideSidebar .fixed-header{
  width: calc(100% - 64px);
}

.sidebarHide .fixed-header{
  width: 100%;
}

.mobile .fixed-header{
  width: 100%;
}

</style>