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
      <el-container v-loading="loading">
        <el-header>
          <el-row :gutter="20">
            <el-col :span="16">
              <el-input v-model="params.keywords" placeholder="根据租户名称搜索" clearable :prefix-icon="Search" />
            </el-col>
            <el-col :span="8">
              <el-button type="primary" @click="searchTenantList">搜索</el-button>
            </el-col>
          </el-row>
        </el-header>
        <el-main>
          <el-table :data="simpleTenants" style="width: 100%">
            <el-table-column prop="tenantId" label="租户ID" width="120" />
            <el-table-column prop="logo" label="Logo" width="100">
              <template #default="scope">
                <el-avatar :size="50" :icon="UserFilled" :src="scope.row.logo" />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="租户名称">
              <template #default="scope">
                <strong style="color: #409eff" v-if="!currentTenantId(scope.row.tenantId)">
                  {{ scope.row.name }}
                </strong>
                <span v-else> {{ scope.row.name }} </span>
              </template>
            </el-table-column>
            <el-table-column prop="linkman" label="联系人" />
            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button
                  v-if="currentTenantId(scope.row.tenantId)"
                  text
                  type="primary"
                  @click="handleSwitchClick(scope.row)"
                  >切换</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
      <template #footer>
        <el-pagination
          v-model:current-page="params.pageIndex"
          :page-size="params.pageSize"
          :background="true"
          layout="sizes, total, prev, pager, next, jumper"
          :total="params.pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from "vue"
import { Search, UserFilled } from "@element-plus/icons-vue"
import { ISearchQuery, ISimpleTenant } from "@/types/base"
import { simpleQueryApi } from "@/api/tenant"
import { useAppStore } from "@/store/modules/app"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { DeviceEnum } from "@/constants/app-key"

const tenantStore = useTenantStoreHook()

const appStore = useAppStore()

const simpleTenants = ref<ISimpleTenant[]>()

/** 加载 */
const loading = ref(false)

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

/** 控制搜索对话框宽度 */
const modalWidth = computed(() => (appStore.device === DeviceEnum.Mobile ? "80vw" : "40vw"))

const dialogVisible = ref(false)

const value = computed(() => tenantStore.getCurrentTenant().name)

// 获取 input 焦点
const inputFocus = () => {
  dialogVisible.value = true
  searchTenantList()
}

// 切换租户
const handleSwitchClick = (t: ISimpleTenant) => {
  const tenant: ISimpleTenant = {
    tenantId: t.tenantId,
    logo: t.logo,
    name: t.name,
    linkman: t.linkman
  }
  tenantStore.setCurrentTenant(tenant)
  dialogVisible.value = false
}

const handleClose = (done: () => void) => {
  dialogVisible.value = false
  done()
}

const handleSizeChange = (val: number) => {
  params.pageSize = val
  searchTenantList()
}

const handleCurrentChange = (val: number) => {
  params.pageIndex = val
  searchTenantList()
}

const currentTenantId = computed(() => {
  return (tenantId: string) => {
    return tenantStore.getCurrentTenantId() !== tenantId
  }
})

const searchTenantList = () => {
  loading.value = true
  const p: ISearchQuery = {
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords
  }
  simpleQueryApi(p)
    .then((resp) => {
      simpleTenants.value = resp.data.list
      params.pageTotal = resp.data.total
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
    .finally(() => {
      loading.value = false
    })
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
.el-header {
  --el-header-padding: 0px;
}
.el-main {
  --el-main-padding: 0px;
}
</style>
