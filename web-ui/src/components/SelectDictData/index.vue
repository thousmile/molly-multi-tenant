<template>
  <el-select v-model="result" :clearable="clearable" :disabled="disabled" :placeholder="placeholder">
    <el-option v-for="(item, index) in items" :key="index" :label="item.dictLabel" :value="item.dictValue">
      <span style="float: left">{{ item.dictLabel }}</span>
      <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">{{ item.dictValue }}</span>
    </el-option>
  </el-select>
</template>

<script setup lang="ts" name="SelectDictData">
import { useDictStoreHook } from "@/store/modules/dict"
import { computed } from "vue"
const dictStore = useDictStoreHook()
const props = defineProps({
  value: {
    type: [String, Number],
    required: true
  },
  dictTypeKey: {
    type: String,
    required: true
  },
  disabled: {
    type: Boolean,
    default: false
  },
  clearable: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: "请选择"
  }
})

// 基于类型
const emit = defineEmits<{
  (e: "update:value", value: String | Number): void
}>()

const items = dictStore.getDictMapList(props.dictTypeKey)

const result = computed({
  get(): String | Number {
    return props.value
  },
  set(v: String | Number) {
    emit("update:value", v)
  }
})
</script>

<style scoped></style>
