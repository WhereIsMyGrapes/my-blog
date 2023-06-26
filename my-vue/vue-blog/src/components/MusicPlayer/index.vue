<template>
  <meting-js v-if="blog.blogInfo.siteConfig.isMusic" server="netease" type="playlist"
    :id="blog.blogInfo.siteConfig.musicId" mini="true" fixed="true" theme="#e9546b"></meting-js>
</template>

<script setup lang="ts">
import useStore from "@/store";
const { blog } = useStore();
</script>

<style scoped></style>
