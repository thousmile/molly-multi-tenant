<template>
  <el-container class="app-container" v-loading="loading">
    <el-header>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="params.keywords" clearable placeholder="根据关键字搜索" />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="success" :icon="Plus" @click="handleAdd()">新增</el-button>
        </el-col>
        <el-col :span="10"><div class="grid-content ep-bg-purple" /></el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="typeId" label="字典ID" />
        <el-table-column prop="typeName" label="字典名称" />
        <el-table-column prop="typeKey" label="字典关键字">
          <template #default="scope">
            <router-link :to="{ path: '/sys/dict/data', query: { typeKey: scope.row.typeKey } }">
              <el-link type="primary">{{ scope.row.typeKey }}</el-link>
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="字典描述">
          <template #default="scope">
            <el-popover
              placement="top-start"
              :width="300"
              trigger="hover"
              v-if="scope.row.description"
              :content="scope.row.description"
            >
              <template #reference>
                <el-link> {{ showStringOverflow(scope.row.description) }}</el-link>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="修改时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.lastUpdateTime" :content="scope.row.lastUpdateTime" placement="top">
              <el-link> {{ showTimeAgo(scope.row.lastUpdateTime) }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130">
          <template #default="scope">
            <div v-admin>
              <el-link :icon="Edit" type="warning" @click="handleEdit(scope.row)">编辑</el-link>
              &nbsp;
              <el-link v-if="scope.row.configType !== 1" :icon="Delete" type="danger" @click="handleDelete(scope.row)"
                >删除</el-link
              >
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增和修改的弹窗 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%" :close-on-click-modal="false">
        <el-form
          ref="entityFormRef"
          :model="entityForm"
          :rules="entityFormRules"
          label-position="right"
          label-width="80px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="typeName" label="字典名称">
                <el-input v-model.trim="entityForm.typeName" placeholder="字典名称" type="text" tabindex="1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="typeKey" label="字典KEY">
                <el-input v-model.trim="entityForm.typeKey" placeholder="字典KEY" type="text" tabindex="1" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="description" label="字典描述">
            <el-input
              v-model="entityForm.description"
              placeholder="字典描述"
              show-word-limit
              type="textarea"
              :rows="5"
              tabindex="3"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSaveAndFlush">确定</el-button>
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
import { Plus, Edit, Delete, Search } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus"
import { queryDictTypeApi, saveDictTypeApi, updateDictTypeApi, deleteDictTypeApi } from "@/api/dict"
import { cloneDeep } from "lodash-es"
import { IDictType } from "@/types/dict"
import { showStringOverflow, showTimeAgo } from "@/hooks/useIndex"

import { useDictStoreHook } from "@/store/modules/dict"
const dictStore = useDictStoreHook()

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<IDictType[]>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

/// 表单数据
const entityForm = ref<IDictType>({
  typeId: 0,
  typeName: "",
  typeKey: "",
  description: ""
})

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  typeName: [{ required: true, message: "请输入字典名称", trigger: "blur" }],
  typeKey: [{ required: true, message: "请输入字典关键字", trigger: "blur" }],
  description: [{ required: true, message: "请输入字典描述", trigger: "blur" }]
}

// 获取数据
const getTableData = () => {
  loading.value = true
  queryDictTypeApi(params)
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

// 重置 Entity 属性
const resetEntity = () => {
  entityForm.value = {
    typeId: 0,
    typeName: "",
    typeKey: "",
    description: ""
  }
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

// 编辑
const handleEdit = (data: IDictType) => {
  resetEntity()
  entityForm.value = cloneDeep(data)
  dialogTitle.value = "修改"
  saveFlag.value = false
  dialogVisible.value = true
}

// 新增
const handleAdd = () => {
  resetEntity()
  dialogTitle.value = "新增"
  saveFlag.value = true
  dialogVisible.value = true
}

// 删除
const handleDelete = (data: IDictType) => {
  ElMessageBox.confirm(`确要删除 ${data.typeName} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteDictTypeApi(data.typeId)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.typeName} 成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          getTableData()
          dictStore.getDictMapKeys()
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 新增和修改
const handleSaveAndFlush = () => {
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      if (saveFlag.value) {
        saveDictTypeApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增字典成功！",
                type: "success"
              })
            }
          })
          .catch((err) => {
            console.log("err :>> ", err)
          })
          .finally(() => {
            loading.value = false
            dialogVisible.value = false
            getTableData()
            dictStore.getDictMapKeys()
          })
      } else {
        updateDictTypeApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改字典成功！",
                type: "success"
              })
            }
          })
          .catch((err) => {
            console.log("err :>> ", err)
          })
          .finally(() => {
            loading.value = false
            dialogVisible.value = false
            getTableData()
            dictStore.getDictMapKeys()
          })
      }
    } else {
      loading.value = false
      return false
    }
  })
}

onMounted(() => {
  getTableData()
})
</script>

<style lang="scss" scoped></style>
