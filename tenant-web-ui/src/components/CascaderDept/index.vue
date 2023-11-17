<template>
  <el-cascader v-model="result" :options="cascaderOptions" :props="{ checkStrictly: true }" filterable clearable />
</template>

<script setup lang="ts" name="CascaderDept">
import { computed } from "vue"
import { IPmsDept } from "@/types/pms"
import type { CascaderValue } from "element-plus"
import { flatTreeToCascaderOption } from "@/utils"

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

const cascaderOptions = flatTreeToCascaderOption(props.options, { value: "deptId", label: "deptName" })

const emit = defineEmits<{
  (e: "update:modelValue", value: Number): void
}>()

const result = computed({
  get(): CascaderValue {
    return props.modelValue
  },
  set(data: CascaderValue) {
    const val = data as number[]
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
