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
        <el-table-column prop="dictCode" label="ID" />
        <el-table-column prop="dictLabel" label="名称" />
        <el-table-column prop="dictValue" label="键值" />
        <el-table-column prop="isDefault" label="默认">
          <template #default="scope">
            {{ dictStore.getYesNo(scope.row.isDefault) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            <operateUser :dateTime="scope.row.createTime" :entity="scope.row.createUserEntity" />
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="修改时间">
          <template #default="scope">
            <operateUser :dateTime="scope.row.lastUpdateTime" :entity="scope.row.lastUpdateUserEntity" />
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
              <el-form-item prop="dictLabel" label="名称">
                <el-input v-model.trim="entityForm.dictLabel" placeholder="名称" type="text" tabindex="1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="isDefault" label="默认">
                <select-dict-data v-model:value="entityForm.isDefault" dictTypeKey="sys_yes_no" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="dictValue" label="键值">
            <el-input
              v-model="entityForm.dictValue"
              placeholder="键值"
              show-word-limit
              type="textarea"
              :rows="5"
              tabindex="2"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" v-preventReClick @click="handleSaveAndFlush">确定</el-button>
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
import { useRoute } from "vue-router"
import { Plus, Edit, Delete, Search } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus"
import { queryDictDataApi, saveDictDataApi, updateDictDataApi, deleteDictDataApi } from "@/api/dict"
import { cloneDeep } from "lodash-es"
import { IDictData } from "@/types/dict"
import { useDictStoreHook } from "@/store/modules/dict"
import { ISearchQuery } from "@/types/base"

const dictStore = useDictStoreHook()

const route = useRoute()

const typeKey = route.query.typeKey as string

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<IDictData[]>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: "",
  dictTypeKey: typeKey,
  includeCauu: true
})

/// 表单数据
const entityForm = ref<IDictData>({
  dictCode: 0,
  dictSort: 0,
  dictLabel: "",
  dictValue: "",
  typeKey: typeKey,
  isDefault: 0
})

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  dictLabel: [{ required: true, message: "请输入名称", trigger: "blur" }],
  dictValue: [{ required: true, message: "请输入键值", trigger: "blur" }],
  isDefault: [{ required: true, message: "请输入默认", trigger: "blur" }]
}

// 获取数据
const getTableData = () => {
  loading.value = true
  queryDictDataApi(params)
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
    dictCode: 0,
    dictSort: 0,
    dictLabel: "",
    dictValue: "",
    typeKey: typeKey,
    isDefault: 0
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
const handleEdit = (data: IDictData) => {
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
const handleDelete = (data: IDictData) => {
  ElMessageBox.confirm(`确要删除 ${data.dictLabel} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteDictDataApi(data.dictCode)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.dictLabel} 成功！`,
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
        saveDictDataApi(entityForm.value)
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
        updateDictDataApi(entityForm.value)
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
