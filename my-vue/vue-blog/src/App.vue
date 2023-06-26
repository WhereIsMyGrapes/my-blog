<template>
  <Provider>
    <div class="app-wrapper">
      <Header></Header>
      <main class="main-wrapper">
        <router-view v-slot="{ Component, route }">
          <keep-alive>
            <component :is="Component" :key="route.path" />
          </keep-alive>
        </router-view>
      </main>
      <Drawer></Drawer>
      <Footer></Footer>
      <Tool></Tool>
      <Login></Login>
      <Register></Register>
      <Search></Search>
      <Email></Email>
      <Forget></Forget>
      <MusicPlayer></MusicPlayer>
    </div>
  </Provider>

</template>

<script setup lang="ts">
import { getBlogInfo, report } from "@/api/blogInfo";
import useStore from '@/store';
const { blog } = useStore();

onMounted(() => {
  console.log(
    "%c UEA %c By 阿恺 %c",
    "background:#e9546b ; padding: 1px; border-radius: 3px 0 0 3px;  color: #fff; padding:5px 0;",
    "background:#ec8c69 ; padding: 1px; border-radius: 0 3px 3px 0;  color: #000; padding:5px 0;",
    "background:transparent"
  );
  getBlogInfo().then(({ data }) => {
    blog.setBlogInfo(data.data);
  });
  report();
})
</script>



<style scoped>
.app-wrapper {
  position: relative;
  min-height: 100vh;
}

.main-wrapper {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0 0 8rem;
}

</style>
