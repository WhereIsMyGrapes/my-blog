<template>
    <div class="imgs">
      <ul>
        <li class="item" v-for="(image, index) in imageList" :key="index" :style="{
          'background-image': 'url(' + image + ')'
        }">
        </li>
      </ul>
    </div>
</template>

<script setup lang="ts">
const imageList = [
    "https://static.cloverkitty.love/article/359813f3c32c5d94660672dc0a681c81.jpg",
    "https://static.cloverkitty.love/article/37dd544c5b5a25d5eb2cd2241ff98b67.jpg",
    "https://static.cloverkitty.love/article/f86bce5c123eb3c07d2c05251b5905d2.png",
    "https://static.cloverkitty.love/article/9ff6602644ce641064ebee67c536e257.jpg",
    "https://static.cloverkitty.love/article/5db8cba021babd29ec5fd0193d5131d5.png"
]

</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";

.imgs {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    z-index: -9;
    background-color: #363636;
    overflow: hidden;

    .item {
        @include absolute;
        width: 100%;
        height: 100%;
        background: no-repeat 50% 50% / cover;
        opacity: 0;
        animation: imageAnimation 36s linear infinite 0s;
        backface-visibility: hidden;
        transform-style: preserve-3d;

        &:nth-child(2) {
        animation-delay: 6s;
        }

        &:nth-child(3) {
        animation-delay: 12s;
        }

        &:nth-child(4) {
        animation-delay: 18s;
        }

        &:nth-child(5) {
        animation-delay: 24s;
        }

        // &:nth-child(6) {
        // animation-delay: 30s;
        // }
    }

    &::before {
        content: '';
        display: block;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, .2);
        transition: all .2s ease-in-out 0s;
    }
}

@keyframes imageAnimation {
    0% {
        opacity: 0;
        animation-timing-function: ease-in;
    }

    2% {
        opacity: 1;
    }

    8% {
        opacity: 1;
        transform: scale(1.05);
        animation-timing-function: ease-out;
    }

    17% {
        opacity: 1;
        transform: scale(1.1);
    }

    25% {
        opacity: 0;
        transform: scale(1.1);
    }

    100% {
        opacity: 0;
    }
}
</style>