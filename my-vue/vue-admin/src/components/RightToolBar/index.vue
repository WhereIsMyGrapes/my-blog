<template>
    <div class="top-right-btn" :style="style">
        <el-row>
            <el-tooltip class="item" effect="dark" 
                :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top"
                v-if="search">
                <el-button circle icon="Search" @click="toggleSearch()" />
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="刷新" placement="top">
                <el-button circle icon="Refresh" @click="refresh()" />
            </el-tooltip>
            <!-- <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="columns">
                <el-button circle icon="Menu" @click="showColumn()" />
            </el-tooltip> -->
        </el-row>
        <!-- <el-dialog :title="title" v-model="open" append-to-body>
            <el-transfer :titles="['显示', '隐藏']" v-model="value" :data="columns" @change="dataChange"></el-transfer>
        </el-dialog> -->
    </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
const props = defineProps({
    showSearch: {
        type: Boolean,
        default: true,
    },
    columns: {
        type: Array,
    },
    search: {
        type: Boolean,
        default: true,
    },
    gutter: {
        type: Number,
        default: 10,
    },
});
const emits = defineEmits(['update:showSearch', 'queryTable']);

// 显隐数据
const value = ref([]);
// 弹出层标题
const title = ref("显示/隐藏");
// 是否显示弹出层
const open = ref(false);

// 如果接收到gutter, 动态返回样式
const style = computed(() => {
    const ret = {};
    if(props.gutter) {
        // ret.marginRight = `${props.gutter / 2}px`;
    }
    return ret;
})

// 搜索
function toggleSearch() {
    emits("update:showSearch", !props.showSearch);
}

// 刷新
function refresh() {
    emits("queryTable");
}



</script>

<style lang="scss" scoped>

</style>
