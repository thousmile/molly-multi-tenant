<template>
  <el-container class="app-container" v-loading="loading" v-has="['pre_role:view']">
    <el-header>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="params.keywords" clearable placeholder="根据关键字搜索" />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" :icon="Delete" @click="handleBatchDelete">批量删除</el-button>
        </el-col>
        <el-col :span="10"><div class="grid-content ep-bg-purple" /></el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="grantType" label="登录类型" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="username" label="用户名">
          <template #default="scope">
            <el-tooltip effect="dark" :content="`用户ID: ${scope.row.userId}`" placement="top">
              <el-link> {{ scope.row.username }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="头像">
          <template #default="scope">
            <el-avatar :size="40" :icon="UserFilled" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="requestUrl" label="请求地址" />
        <el-table-column prop="requestIp" label="请求IP" />
        <el-table-column prop="address" label="请求地址" />
        <el-table-column prop="osName" label="操作系统" />
        <el-table-column prop="browser" label="浏览器" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.createTime" :content="scope.row.createTime" placement="top">
              <el-link> {{ showTimeAgo(scope.row.createTime) }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70">
          <template #default="scope">
            <el-link :icon="Delete" type="danger" v-has="['pre_role:delete']" @click="handleDelete(scope.row)"
              >删除</el-link
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
import { ref, reactive, onMounted, computed } from "vue"
import { Delete, Search, UserFilled } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { queryLoginLogApi, deleteLoginLogApi } from "@/api/loginLog"
import { ILoginLog } from "@/types/lms"
import { timeAgo } from "@/utils"

/** 加载 */
const loading = ref(false)

const tableData = ref<ILoginLog[]>()

const multipleSelection = ref<ILoginLog[]>([])

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

// 获取数据
const getTableData = () => {
  loading.value = true
  queryLoginLogApi(params)
    .then((resp) => {
      tableData.value = resp.data.list
      params.pageTotal = resp.data.total
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      loading.value = false
    })
}

const showTimeAgo = computed(() => {
  return (value: string) => timeAgo(value)
})

const searchTableData = () => {
  params.pageIndex = 1
  getTableData()
}

const handleSizeChange = (val: number) => {
  params.pageSize = val
  getTableData()
}

const handleCurrentChange = (val: number) => {
  params.pageIndex = val
  getTableData()
}

const handleSelectionChange = (data: ILoginLog[]) => {
  multipleSelection.value = data
}

// 批量删除
const handleBatchDelete = () => {
  if (multipleSelection.value.length < 1) {
    ElMessage({
      message: "最少选择一个",
      type: "warning"
    })
    return
  }
  const items = multipleSelection.value.map((r) => r.id)
  ElMessageBox.confirm(`确要批量删除吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteLoginLogApi(items)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `批量删除成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          getTableData()
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 删除
const handleDelete = (data: ILoginLog) => {
  ElMessageBox.confirm(`确要删除 ${data.nickname} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteLoginLogApi([data.id])
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.nickname} 成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          getTableData()
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

onMounted(() => {
  getTableData()
})
</script>

<style lang="scss" scoped></style>
