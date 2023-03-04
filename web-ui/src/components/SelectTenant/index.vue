<template>
  <div>
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
        <el-table
          :data="simpleTenants"
          ref="multipleTableRef"
          @selection-change="handleSelectionChange"
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="tenantId" label="租户ID" width="120" />
          <el-table-column prop="logo" label="Logo" width="100">
            <template #default="scope">
              <el-avatar :size="50" :icon="UserFilled" :src="scope.row.logo" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="租户名称" />
          <el-table-column prop="linkman" label="联系人" />
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
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue"
import { Search, UserFilled } from "@element-plus/icons-vue"
import { ISearchQuery, ISimpleTenant } from "@/types/base"
import { simpleQueryApi } from "@/api/tenant"
import { ElTable } from "element-plus"

const props = defineProps({
  value: {
    type: Array<string>,
    required: true
  }
})

// 基于类型
const emit = defineEmits<{
  (e: "update:value", value: string[]): void
}>()

const simpleTenants = ref<ISimpleTenant[]>()

/** 加载 */
const loading = ref(false)

const searchTenantList = () => {
  loading.value = true
  const p: ISearchQuery = {
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords
  }
  listSysUserTenantIdApi(p)
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
</script>

<style scoped></style>
