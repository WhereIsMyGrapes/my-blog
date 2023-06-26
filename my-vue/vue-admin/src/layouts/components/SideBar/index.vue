<template>
    <div :class="{ 'has-logo': showLogo }">
        <!-- Logo -->
        <Logo v-if="showLogo" :collapse="isCollapse" />
        
        <el-scrollbar wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu" 
                :unique-opened="true" 
                :collapse="isCollapse" 
                :collapse-transition="false"
                :background-color="variables.menuBg" 
                :text-color="variables.menuText"
                :active-text-color="variables.menuActiveText"
            >
            
            <SidebarItem v-for="route in routes" :item="route" :key="route.path" :base-path="route.path" ></SidebarItem>
            
            </el-menu>

        </el-scrollbar>
    </div>
</template>

<script lang="ts" setup>
import variables from '@/assets/styles/variables.module.scss';
import useStroe from "@/store";
import { useRoute } from 'vue-router';
import {computed, onMounted} from "vue";
import Logo from ".//Logo.vue";
import SidebarItem from './SidebarItem.vue';

const {app, setting, permission}  = useStroe();

const route = useRoute();
const routes = computed(() => permission.routes);
const activeMenu = computed(() => route.path);

const isCollapse = computed(() => app.isCollapse);
const showLogo = computed(() => setting.sidebarLogo);

onMounted(()=>{
// console.log("routes",routes);
});

</script>

<style lang="scss" scoped>

</style>