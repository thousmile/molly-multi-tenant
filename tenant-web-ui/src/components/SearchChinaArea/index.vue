<template>
  <el-cascader v-model="value" filterable :options="options" :props="cascaderProps" separator="-" clearable />
</template>

<script setup lang="ts">
import { computed } from "vue"

import options from "@/assets/ChinaArea.json"

const props = defineProps({
  value: {
    type: Number,
    default: 0
  }
})

// 基于类型
const emit = defineEmits<{
  (e: "change", val: number): void
}>()

const cascaderProps = {
  children: "children",
  label: "name",
  value: "areaCode",
  emitPath: false
}

const value = computed({
  get() {
    return props.value
  },
  set(value: number) {
    emit("change", value)
  }
})
</script>

<style scoped></style>
