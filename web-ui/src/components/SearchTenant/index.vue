<template>
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
    <el-footer>
      <el-pagination
        v-model:current-page="params.pageIndex"
        :page-size="params.pageSize"
        :background="true"
        layout="sizes, total, prev, pager, next, jumper"
        :total="params.pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import { computed, reactive, ref, onMounted } from "vue"
import { Search, UserFilled } from "@element-plus/icons-vue"
import { ISearchQuery, ISimpleTenant } from "@/types/base"
import { simpleQueryTenantApi } from "@/api/tenant"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { useProjectStoreHook } from "@/store/modules/project"

const tenantStore = useTenantStoreHook()
const projectStore = useProjectStoreHook()

const simpleTenants = ref<ISimpleTenant[]>()

/** 加载 */
const loading = ref(false)

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

const emit = defineEmits<{
  (e: "handleSwitch", id: string): void
}>()

// 切换租户
const handleSwitchClick = (t: ISimpleTenant) => {
  const tenant: ISimpleTenant = {
    tenantId: t.tenantId,
    logo: t.logo,
    name: t.name,
    linkman: t.linkman
  }
  tenantStore.setCurrentTenant(tenant)
  // 重置当前项目为默认项目
  projectStore.resetCurrentProject()
  emit("handleSwitch", t.name)
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
  simpleQueryTenantApi(p)
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

onMounted(() => {
  searchTenantList()
})

const searchList = () => searchTenantList()

// 输出组件的方法，让外部组件可以调用
defineExpose({
  searchList
})
</script>

<style lang="scss" scoped>
.el-header {
  --el-header-padding: 0px;
}
.el-main {
  --el-main-padding: 20px 0px;
}
.el-footer {
  --el-main-padding: 0px;
}
</style>
