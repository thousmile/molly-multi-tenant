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
      <el-tabs v-model="activeName" type="card" @tab-click="tabsHandleClick">
        <el-tab-pane label="切换租户" name="tenant">
          <SearchTenant @handle-switch="childHandleSwitch" ref="childTenant" />
        </el-tab-pane>
        <el-tab-pane label="切换项目" name="project">
          <SearchProject @handle-switch="childHandleSwitch" ref="childProject" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue"
import { Search } from "@element-plus/icons-vue"
import type { TabsPaneContext } from "element-plus"
import { useAppStore } from "@/store/modules/app"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { useProjectStoreHook } from "@/store/modules/project"
import { DeviceEnum } from "@/constants/app-key"
import SearchTenant from "@/components/SearchTenant/index.vue"
import SearchProject from "@/components/SearchProject/index.vue"

const tenantStore = useTenantStoreHook()
const projectStore = useProjectStoreHook()

const appStore = useAppStore()

/** 控制搜索对话框宽度 */
const modalWidth = computed(() => (appStore.device === DeviceEnum.Mobile ? "80vw" : "40vw"))

const dialogVisible = ref(false)

const activeName = ref("tenant")

// 租户切换组件
const childTenant = ref()
// 项目切换组件
const childProject = ref()

const value = computed(() => `${tenantStore.getCurrentTenant().name} / ${projectStore.getCurrentProject().projectName}`)

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

const tabsHandleClick = (tab: TabsPaneContext) => {
  if (tab.paneName === "tenant") {
    childTenant.value.searchList()
  } else {
    childProject.value.searchList()
  }
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
