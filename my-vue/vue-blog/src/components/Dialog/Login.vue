<template>
  <n-modal class="bg" v-model:show="dialogVisible" preset="dialog" :show-icon="false"
    transform-origin="center" :block-sroll="false">
    <n-input class="mt-11" v-model:value="loginForm.username" placeholder="邮箱号" @keyup.enter="handlelogin"></n-input>
    <n-input class="mt-11" v-model:value="loginForm.password" type="password" show-password-on="click" placeholder="密码"
      @keyup.enter="handlelogin"></n-input>
    <n-button class="mt-11" color="#ed6ea0" style="width: 100%" :loading="loading" @click="handlelogin">
      登 录
    </n-button>
    <div class="mt-10 login-tip">
      <span class="colorFlag" @click="handleRegister">立即注册</span>
      <span class="colorFlag" @click="handleForget">忘记密码?</span>
    </div>
  </n-modal>    
</template>

<script setup lang="ts">
import { login } from "@/api/login";
import { LoginForm } from "@/api/login/types";
import config from "@/assets/js/config";
import useStore from "@/store";
import { setToken } from "@/utils/token";
const { app, user, blog } = useStore();
const route = useRoute();
const loading = ref(false);
const loginForm = ref<LoginForm>({
  username: "",
  password: "",
});
const dialogVisible = computed({
  get: () => app.loginFlag,
  set: (value) => (app.loginFlag = value),
});
const showLogin = computed(
  () => (type: string) => blog.blogInfo.siteConfig.loginList.includes(type)
);
const handleRegister = () => {
  app.setLoginFlag(false);
  app.setRegisterFlag(true);
  console.log(app.registerFlag);
};
const handleForget = () => {
  app.setLoginFlag(false);
  app.setForgetFlag(true);
};

const handlelogin = () => {
  let reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
  if (!reg.test(loginForm.value.username)) {
    window.$message?.warning("邮箱格式不正确");
    return;
  }
  if (loginForm.value.password.trim().length == 0) {
    window.$message?.warning("密码不能为空");
    return;
  }
  loading.value = true;
  login(loginForm.value).then(({ data }) => {
    if (data.flag) {
      setToken(data.data);
      user.GetUserInfo();
      window.$message?.success("登录成功");
      loginForm.value = {
        username: "",
        password: "",
      };
      app.setLoginFlag(false);
    }
    loading.value = false;
  });
};
</script>

<style scoped >
.login-tip {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.social-login-title {
  margin-top: 1rem;
  color: #b5b5b5;
  font-size: 0.75rem;
  text-align: center;
}

.social-login-title::before {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}

.social-login-title::after {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}

.social-login-wrapper {
  text-align: center;
  margin-top: 1.4rem;
}

.icon {
  margin: 0 0.3rem;
}
</style>
