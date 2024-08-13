<template>
  <div class="app-container" v-loading="loading">
    <el-card shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="params">
        <el-form-item>
          <el-input v-model="params.keywords" clearable placeholder="根据 模板名称 搜索" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" :icon="Plus" @click="handleAdd()">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <div class="toolbar-wrapper">
        <el-table :data="tableData">
          <el-table-column prop="id" label="模板ID" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="description" label="描述">
            <template #default="scope">
              <el-popover placement="top-start" :width="300" trigger="hover" v-if="scope.row.description"
                :content="scope.row.description">
                <template #reference>
                  <el-link> {{ showStringOverflow(scope.row.description) }}</el-link>
                </template>
              </el-popover>
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
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-link :icon="EditPen" type="info" @click="handleEditMenu(scope.row)">权限</el-link>
              &nbsp;
              <el-link :icon="Edit" type="warning" @click="handleEdit(scope.row)">编辑</el-link>
              &nbsp;
              <el-link v-admin :icon="Delete" type="danger" @click="handleDelete(scope.row)">删除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pager-wrapper">
        <el-pagination v-model:current-page="params.pageIndex" :page-size="params.pageSize" :background="true"
          layout="sizes, total, prev, pager, next, jumper" :total="params.pageTotal" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 新增和修改的弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%" :close-on-click-modal="false">
      <el-form ref="entityFormRef" :model="entityForm" :rules="entityFormRules" label-position="right"
        label-width="80px">
        <el-form-item prop="name" label="名称">
          <el-input v-model.trim="entityForm.name" placeholder="名称" type="text" tabindex="1" />
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="entityForm.description" placeholder="描述" :rows="5" :maxlength="100" type="textarea"
            tabindex="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" v-preventReClick @click="handleSaveAndFlush">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改权限 -->
    <el-dialog v-model="menusDialogVisible" :title="menusDialogTitle" width="50%" :close-on-click-modal="false">
      <custom-tree v-if="menusData" :data="menusData.all" ref="menusRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="menusDialogVisible = false">取消</el-button>
          <el-button type="primary" v-preventReClick @click="handleUpdateMenus">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue"
import { Plus, Edit, Delete, Search, EditPen } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules, ElTree } from "element-plus"
import {
  queryTemplateApi,
  saveTemplateApi,
  updateTemplateApi,
  deleteTemplateApi,
  getTemplateMenusApi,
  updateTemplateMenusApi
} from "@/api/template"
import { cloneDeep } from "lodash-es"
import { ISysTemplate } from "@/types/sys"
import { IUpdateMenus } from "@/types/base"
import { showStringOverflow } from "@/hooks/useIndex"
import CustomTree from "@/components/CustomTree/index.vue"

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<ISysTemplate[]>()

const menusDialogVisible = ref(false)

const menusDialogTitle = ref("")

const menusRef = ref<any>()
const menusData = ref<IUpdateMenus>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: "",
  includeCauu: true
})

/// 表单数据
const entityForm = ref<ISysTemplate>({
  id: 0,
  name: "",
  description: ""
})

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  description: [{ required: true, message: "请输入描述", trigger: "blur" }]
}

// 获取数据
const getTableData = () => {
  loading.value = true
  queryTemplateApi(params)
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
    id: 0,
    name: "",
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

// 编辑权限
const handleEditMenu = (data: ISysTemplate) => {
  loading.value = true
  entityForm.value = cloneDeep(data)
  menusDialogTitle.value = "修改"
  getTemplateMenusApi(data.id)
    .then((resp) => {
      menusData.value = resp.data
      menusDialogVisible.value = true
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      // 调用子组件的方法，重新渲染 tree
      menusRef.value!.setCheckedKeys(menusData.value!.have)
      loading.value = false
    })
}

// 修改权限
const handleUpdateMenus = () => {
  const menuIds: number[] = menusRef.value!.getCheckedKeys()
  if (menuIds && menuIds.length < 1) {
    ElMessage({
      message: "至少选择一个权限",
      type: "warning"
    })
    return
  }
  ElMessageBox.confirm(`确要修改 ${entityForm.value.name} 的权限吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      const params = {
        id: entityForm.value.id,
        menus: menuIds
      }
      updateTemplateMenusApi(params)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `修改 ${entityForm.value.name} 的权限成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          menusDialogVisible.value = false
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 编辑
const handleEdit = (data: ISysTemplate) => {
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
const handleDelete = (data: ISysTemplate) => {
  ElMessageBox.confirm(`确要删除 ${data.name} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteTemplateApi(data.id)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.name} 成功！`,
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

// 新增和修改
const handleSaveAndFlush = () => {
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      if (saveFlag.value) {
        saveTemplateApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增模板成功！",
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
          })
      } else {
        updateTemplateApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改模板成功！",
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
