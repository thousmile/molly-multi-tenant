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
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="tabsHandleClick">
        <el-tab-pane label="切换租户" name="tenant">
          <SearchTenant ref="childTenant" />
        </el-tab-pane>
        <el-tab-pane label="切换项目" name="project">
          <SearchProject ref="childProject" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue"
import { Search } from "@element-plus/icons-vue"
import { useAppStore } from "@/store/modules/app"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { useProjectStoreHook } from "@/store/modules/project"
import { DeviceEnum } from "@/constants/app-key"
import type { TabsPaneContext } from "element-plus"
import SearchTenant from "@/components/SearchTenant/index.vue"
import SearchProject from "@/components/SearchProject/index.vue"

const tenantStore = useTenantStoreHook()
const projectStore = useProjectStoreHook()

const appStore = useAppStore()

/** 控制搜索对话框宽度 */
const modalWidth = computed(() => (appStore.device === DeviceEnum.Mobile ? "80vw" : "40vw"))

const dialogVisible = ref(false)

const activeName = ref("tenant")
const childTenant = ref()
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

onMounted(() => {
  childTenant.value.searchTenantList()
})

const tabsHandleClick = (tab: TabsPaneContext) => {
  if (tab.paneName === "tenant") {
    childTenant.value.searchTenantList()
  } else {
    childProject.value.searchProjectList()
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
