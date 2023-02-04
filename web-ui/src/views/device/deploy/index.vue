<template>
  <div>
    <h1>设备规划</h1>
  </div>
</template>

<script setup lang="ts">
import { ref, onBeforeMount, onMounted } from "vue"
const data1 = ref({})
console.log("data1 :>> ", data1)
onBeforeMount(() => {
  console.log("2.组件挂载页面之前执行")
})
onMounted(() => {
  console.log("3.-组件挂载到页面之后执行")
})
</script>

<style lang="scss" scoped></style>
