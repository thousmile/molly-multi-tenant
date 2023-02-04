<template>
  <div><h1>趋势分析</h1></div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue"
const data1 = ref({})
console.log("data1 :>> ", data1)
onMounted(() => {
  console.log("1.-组件挂载到页面之后执行")
})
</script>

<style lang="scss" scoped></style>
