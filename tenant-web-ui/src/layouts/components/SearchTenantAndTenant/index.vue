<template>
  <div>
    <el-input v-model="value" size="small" :readonly="true" :prefix-icon="Search" @click="inputFocus" />
    <el-dialog
      v-model="dialogVisible"
      :before-close="handleClose"
      :width="modalWidth"
      top="5vh"
      class="search-modal__private"
      append-to-body
    >
      <SearchProject @handle-switch="childHandleSwitch" ref="childProject" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue"
import { Search } from "@element-plus/icons-vue"
import { useAppStore } from "@/store/modules/app"
import { useProjectStoreHook } from "@/store/modules/project"
import { DeviceEnum } from "@/constants/app-key"
import SearchProject from "@/components/SearchProject/index.vue"

const projectStore = useProjectStoreHook()

const appStore = useAppStore()

/** 控制搜索对话框宽度 */
const modalWidth = computed(() => (appStore.device === DeviceEnum.Mobile ? "80vw" : "40vw"))

const dialogVisible = ref(false)

// 项目切换组件
const childProject = ref()

const value = computed(() => projectStore.getCurrentProject().projectName)

// 获取 input 焦点
const inputFocus = () => {
  dialogVisible.value = true
}

const handleClose = (done: () => void) => {
  dialogVisible.value = false
  done()
}

const childHandleSwitch = (str: string) => {
  console.log("str :>> ", str)
  dialogVisible.value = false
}
</script>

<style lang="scss" scoped>
.search-modal__private {
  .svg-icon {
    font-size: 18px;
  }

  .el-dialog__header {
    display: none;
  }

  .el-dialog__footer {
    border-top: 1px solid var(--el-border-color);
    padding: var(--el-dialog-padding-primary);
  }
}
</style>
