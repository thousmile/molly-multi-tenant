<template>
  <el-cascader v-model="result" :options="props.options" :props="cascaderProps" filterable clearable />
</template>

<script setup lang="ts" name="CascaderDept">
import { computed } from "vue"
import { IPmsDept } from "@/types/pms"
const props = defineProps({
  modelValue: {
    type: Number,
    required: true
  },
  options: {
    type: Array<IPmsDept>,
    required: true
  }
})

const cascaderProps = { checkStrictly: true, value: "deptId", label: "deptName" }

const emit = defineEmits<{
  (e: "update:modelValue", value: Number): void
}>()

const result = computed({
  get(): Number {
    return props.modelValue
  },
  set(val: Number[]) {
    if (val && val.length > 0) {
      const v1 = val[val.length - 1]
      emit("update:modelValue", v1)
    } else {
      emit("update:modelValue", 0)
    }
  }
})
</script>

<style scoped></style>
