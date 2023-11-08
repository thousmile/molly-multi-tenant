<script lang="ts" setup name="SvgLoading"></script>

<template>
  <svg class="circular" viewbox="25 25 50 50">
    <circle class="path" cx="50" cy="50" r="20" fill="none" />
  </svg>
</template>

<style lang="scss" scoped>
.circular {
  width: 20px;
  height: 20px;
  animation: rotate 2s linear infinite;
  margin: auto;
}

.path {
  stroke-dasharray: 1, 200;
  stroke-dashoffset: 0;
  stroke: #de9393;
  stroke-width: 5px;
  animation: dash 1.5s ease-in-out infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 200;
    stroke-dashoffset: 0;
  }

  50% {
    stroke-dasharray: 89, 200;
    stroke-dashoffset: -35px;
  }

  100% {
    stroke-dasharray: 89, 200;
    stroke-dashoffset: -124px;
  }
}
</style>
