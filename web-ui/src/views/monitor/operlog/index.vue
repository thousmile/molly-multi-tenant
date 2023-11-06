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
        <el-col :span="10">
          <div class="grid-content ep-bg-purple" />
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="serviceName" label="服务名称" />
        <el-table-column prop="requestMethod" label="请求类型" />
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
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="timeCost" label="耗时(毫秒)" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.createTime" :content="scope.row.createTime" placement="top">
              <el-link> {{ showTimeAgo(scope.row.createTime) }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-link :icon="View" type="primary" v-has="['pre_role:delete']" @click="handleInfo(scope.row)"
              >详情</el-link
            >
            &nbsp;
            <el-link :icon="Delete" type="danger" v-has="['pre_role:delete']" @click="handleDelete(scope.row)"
              >删除</el-link
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增和修改的弹窗 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
        <el-form
          ref="entityFormRef"
          :model="entityForm"
          label-position="right"
          label-width="50px"
          @keyup.enter="dialogVisible = false"
        >
          <el-form-item label="描述">
            {{ entityForm.description }}
          </el-form-item>
          <el-form-item label="地址">
            {{ entityForm.requestUrl }}
          </el-form-item>
          <el-form-item label="类型">
            {{ entityForm.requestMethod }}
          </el-form-item>
          <el-form-item label="参数">
            <el-input v-model="entityForm.methodArgs" rows="5" type="textarea" />
          </el-form-item>
          <el-form-item label="结果">
            <el-input v-model="entityForm.responseResult" rows="10" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button type="primary" @click="dialogVisible = false">确定</el-button>
          </span>
        </template>
      </el-dialog>
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
import { ref, reactive, onMounted } from "vue"
import { Delete, Search, UserFilled, View } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { queryOperLogApi, deleteOperLogApi } from "@/api/loginLog"
import { IOperLog } from "@/types/lms"
import { showTimeAgo } from "@/hooks/useIndex"

/** 加载 */
const loading = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<IOperLog[]>()

/// 表单数据
const entityForm = ref<IOperLog>({
  /**
   * ID
   */
  id: "",
  /**
   * 标题
   */
  title: "",
  /**
   * 描述
   */
  description: "",
  /**
   * 服务名称
   */
  serviceName: "",
  /**
   * 用户ID
   */
  userId: 0,
  /**
   * 用户昵称
   */
  nickname: "",
  /**
   * 用户名
   */
  username: "",
  /**
   * 头像
   */
  avatar: "",
  /**
   * 方法
   */
  method: "",
  /**
   * 方法参数
   */
  methodArgs: "",
  /**
   * 请求类型
   */
  requestMethod: "",
  /**
   * 请求地址
   */
  requestUrl: "",
  /**
   * 请求IP
   */
  requestIp: "",
  /**
   * 请求地址
   */
  address: "",
  /**
   * 请求响应
   */
  responseResult: "",
  /**
   * 状态
   */
  status: 0,
  /**
   * 错误日志
   */
  errorLog: "",
  /**
   * 耗时(毫秒)
   */
  timeCost: 0,
  /**
   * 创建时间
   */
  createTime: ""
})

const multipleSelection = ref<IOperLog[]>([])

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: "",
  includeCauu: true
})

// 获取数据
const getTableData = () => {
  loading.value = true
  queryOperLogApi(params)
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

const handleSelectionChange = (data: IOperLog[]) => {
  multipleSelection.value = data
}

// 详情
const handleInfo = (data: IOperLog) => {
  data.methodArgs = JSON.stringify(JSON.parse(data.methodArgs), null, "\t")
  data.responseResult = JSON.stringify(JSON.parse(data.responseResult), null, "\t")
  entityForm.value = data
  dialogVisible.value = true
  dialogTitle.value = data.title
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
      deleteOperLogApi(items)
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
const handleDelete = (data: IOperLog) => {
  ElMessageBox.confirm(`确要删除 ${data.title} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteOperLogApi([data.id])
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.title} 成功！`,
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
