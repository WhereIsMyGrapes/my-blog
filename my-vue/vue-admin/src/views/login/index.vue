<template>
    <div class="login">
        <el-form ref="ruleFormRef" :model="loginForm" :rules="rules" class="login-form">
            <h3 class="title">博客后台管理系统</h3>
            <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" size="large" placeholder="账号">
                    <template #prefix><svg-icon icon-class="user"></svg-icon></template>
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" show-password size="large" placeholer="密码"
                    @keyup.enter="handleLogin(ruleFormRef)">
                    <template #prefix><svg-icon icon-class="password"></svg-icon></template>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button :loading="loading" type="primary" 
                    @click.prevent="handleLogin(ruleFormRef)"
                    style="width:100%">
                    <span v-if="!loading">登录</span>
                    <span v-else>登陆中...</span>
                </el-button>
            </el-form-item>
        </el-form>
        <!-- 底部 -->
        <div class="el-login-footer">
            <span>Copyright © 2023 - {{ new Date().getFullYear() }} By 凉拌葡萄  </span>
        </div>
    </div>
</template>

<script lang="ts" setup>
import router from "@/router";
import useStore from '@/store';

import {FormInstance, FormRules} from "element-plus"
import { userInfo } from "os";
import { reactive, ref } from 'vue';

const { user } = useStore();
const ruleFormRef = ref<FormInstance>();
const loading = ref(false)

const loginForm = reactive({
    username:"test@qq.com",
    password:"123456"
})

const rules = reactive<FormRules>({
    username:[{required:true,message:"请输入用户名",trigger:"blur"}],
    password:[{required:true,message:"请输入密码",trigger:"blur"}, { min: 6, message: "密码不能少于6位", trigger: "blur" }]
})

const handleLogin = async (formEl:FormInstance | undefined)=>{
    if(!formEl) return;
    await formEl.validate(valid=>{
        if (valid) {
            loading.value = true;
            user.LogIn(loginForm).then(() => {
                router.push({ path: "/" });
                loading.value = false;
            }).catch(() => {
                loading.value = false;
            });
        } else {
            return false;
        }
    })
}

</script>

<style lang="scss" scoped>
.login{
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("https://static.cloverkitty.love/config/502fad10dade6101f592b350de965b94.jpg");
    background-size: cover;

    .title{
        margin: 0px auto 30px auto;
        text-align: center;
        color: #ffffffa1;
    }

    .login-form{
        border-radius: 6px;
        width: 400px;
        padding: 25px 25px 5px 25px;
    }

    .el-login-footer{
        height: 40px;
        line-height: 40px;
        position: fixed;
        bottom: 0;
        width: 100%;
        text-align: center;
        color: #fff;
        font-family: Arial;
        font-size: 12px;
        
    }

}


</style>
