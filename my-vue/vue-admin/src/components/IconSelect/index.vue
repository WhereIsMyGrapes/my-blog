<template>
  <div class="icon-select">
    <el-input v-model="iconName" clearable placeholder="请输入图标名称" @clear="filterIcons" @input="filterIcons">
      <template #suffix><i class="el-icon-search el-input__icon" /></template>
    </el-input>
    <div class="icon-select__list">
      <div v-for="(item, index) in iconList" :key="index" @click="selectedIcon(item)">
        <svg-icon color="#999" :icon-class="item" style="height: 30px; width: 16px; margin-right: 5px" />
        <span>{{ item }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const icons = [] as string[];
const modules = import.meta.glob("../../assets/icons/*.svg");
// 通过切割获取无前后缀的名字
for (const path in modules) {
    const p = path.split('assets/icons/')[1].split(".svg")[0];
    icons.push(p);
};
const iconList = ref(icons);
const iconName = ref("");
// 定义触发事件
const emit = defineEmits(["selected"]);
// 默认先将 iconList 设置为原始的 icons 数组,然后判断如果 iconName 有值,则从 icons 数组中过滤出名称包含 iconName 值的图标,更新 iconList。
const filterIcons = () => {
    iconList.value = icons;
    if(iconName.value) {
        iconList.value = icons.filter(item => item.indexOf(iconName.value) !== -1);
    }
};
// 点击某个图标时触发,Emit 一个 selected 事件,并把所选图标的名称作为参数传出去,同时调用 document.body.click() 来关闭下拉框。
const selectedIcon = (name: string) => {
  emit('selected', name);
  document.body.click();
};
const reset = () => {
  iconName.value = '';
  iconList.value = icons;
};
// 定义 reset 方法用于重置,并通过 defineExpose 暴露给外部使用。
defineExpose({
  reset
});


</script>

<style lang="scss" scoped>

.icon-select {
  width: 100%;
  padding: 10px;

  &__list {
    height: 200px;
    overflow-y: scroll;

    div {
      height: 30px;
      line-height: 30px;
      margin-bottom: -5px;
      cursor: pointer;
      width: 33%;
      float: left;
    }

    span {
      display: inline-block;
      vertical-align: -0.15em;
      fill: currentColor;
      overflow: hidden;
    }
  }
}
</style>



