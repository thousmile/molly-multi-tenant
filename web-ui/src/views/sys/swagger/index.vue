<template>
  <iframe id="swagger" :src="docAction" scrolling="no" frameborder="0" />
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue"
import { getEnvBaseURL } from "@/utils"

const docAction = ref(`${getEnvBaseURL()}/doc.html`)

const changeSwagger = () => {
  const swagger = document.getElementById("swagger")
  const deviceWidth = document.body.clientWidth
  const deviceHeight = document.body.clientHeight
  if (swagger) {
    if (swagger!.style) {
      swagger!.style.width = Number(deviceWidth) - 240 + "px" // 数字是页面布局宽度差值
      swagger!.style.height = Number(deviceHeight) - 64 + "px" // 数字是页面布局高度差
    }
  }
}

onMounted(() => {
  changeSwagger()
  window.onresize = () => {
    changeSwagger()
  }
})
</script>

<style lang="scss" scoped></style>
