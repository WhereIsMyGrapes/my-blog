<template>
  <div class="page-header">
    <h1 class="page-title">个人中心</h1>
    <img class="page-cover" src="https://static.cloverkitty.love/talk/7b2c3eb3ad288253dcc481763f794db5.jpg"
      alt="">
    <Waves></Waves>
  </div>
  <div class="bg">
    <div class="title" style="text-align: center">基本信息</div>
    <div class="info-container">
      <UserAvatar class="avatar"></UserAvatar>
      <div class="info mt-4">
        <n-form ref="formInstRef" label-align="left" :label-width="80" :model="userForm" :rules="rules">
          <n-form-item label="昵称" label-style="color: var(--text-color);" path="nickname">
            <n-input placeholder="输入您的昵称" v-model:value="userForm.nickname" />
          </n-form-item>
          <n-form-item label="个人网站" label-style="color: var(--text-color);" path="website">
              <n-input placeholder="请输入个人网站" v-model:value="userForm.webSite" />
            </n-form-item>
            <n-form-item label="简介" label-style="color: var(--text-color);" path="intro">
              <n-input placeholder="介绍一下自己吧" v-model:value="userForm.intro" />
            </n-form-item>
            <n-form-item label="邮箱" label-style="color: var(--text-color);" path="email">
              <n-input-group>
                <n-input placeholder="请输入邮箱" disabled v-model:value="user.email"></n-input>
                <n-button color="#49b1f5" v-if="user.email" @click="app.emailFlag = true">
                  修改邮箱
                </n-button>
                <n-button color="#49b1f5" v-else @click="app.emailFlag = true">
                  绑定邮箱
                </n-button>
              </n-input-group>
            </n-form-item>
        </n-form>
        <n-button color="#3e999f" style="margin-bottom: 10px" @click="handleUpdate">
            修改
        </n-button>
      </div>    
    </div>
  </div>
</template>

<script setup lang="ts">
import { updateUserInfo } from '@/api/user';
import { UserInfo } from "@/api/user/types";
import useStore from "@/store";
import { FormInst } from 'naive-ui';
const formInstRef = ref<FormInst | null>(null)
const { user, app } = useStore();
const router = useRouter();
const rules = {
    nickname: {
        require: true,
        message: "昵称不能为空",
    },
};
const userForm = ref<UserInfo>({
  nickname: user.nickname,
  intro: user.intro,
  webSite: user.webSite,
});
const handleUpdate = () => {
  formInstRef.value?.validate((errors) => {
    // 发请求更新
    updateUserInfo(userForm.value).then(({ data }) => {
      if (data.flag) {
        // 将更新后的结果持久化到pinia
        user.updateUserInfo(userForm.value);
        window.$message?.success("修改成功");
      }
    });
  })
};
onMounted(() => {
  if (!user.id) {
    router.push("/");
  }
})
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";

.title {
  font-size: 1.25rem;
  font-weight: 700;
}

.info-container {
  @include flex;
  flex-wrap: wrap;
  margin-top: 1rem;

  .avatar {
    display: inline-flex;
    width: 230px;
    height: 140px;
  }

  .info {
    width: 530px;
  }
}

@media (max-width: 850px) {
  .avatar {
    justify-content: center;
  }
}
</style>
