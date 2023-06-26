<template>
  <div class="page-header">
    <h1 class="page-title">页面不存在</h1>
    <img class="page-cover" src="https://static.cloverkitty.love/config/26ae397511046b1e777ab36069dcfa2c.jpg"
      alt="">
    <Waves></Waves>
  </div>
  <div class="bg">
    <div class="page-container">
      <div class="notfound">404! 页面不见了
        <router-link :to="`/`">
          <n-button  color="#3e999f" style="margin-bottom: 10px" >
          返回首页
          </n-button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.notfound {
  width: 18.75rem;
  height: 22.625rem;
  /* background: url("https://static.cloverkitty.love/config/5407c7c027676c19b98daa50698550e1.jpg") no-repeat bottom; */
  text-align: center;
  margin: 6.25rem auto;
  font-size:  2rem;
}
</style>