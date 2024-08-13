<template>
  <div class="app-container" v-loading="loading" v-has="['pre_role:view']">
    <el-card shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="params">
        <el-form-item>
          <el-input v-model="params.keywords" clearable placeholder="根据 角色名称 搜索" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" :icon="Plus" v-has="['pre_role:create']" @click="handleAdd()">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never">
      <div class="toolbar-wrapper">
        <el-table :data="tableData">
          <el-table-column prop="roleId" label="角色ID" />
          <el-table-column prop="roleName" label="名称" />
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
          <el-table-column prop="sort" label="排序" sortable />
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
              <el-link :icon="EditPen" type="info" v-has="['pre_role:update:permissions']"
                @click="handleEditMenu(scope.row)">权限</el-link>
              &nbsp;
              <el-link :icon="Edit" type="warning" v-has="['pre_role:update']"
                @click="handleEdit(scope.row)">编辑</el-link>
              &nbsp;
              <el-link :icon="Delete" type="danger" v-has="['pre_role:delete']"
                @click="handleDelete(scope.row)">删除</el-link>
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
        label-width="80px" @keyup.enter="handleSaveAndFlush">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="roleName" label="名称">
              <el-input v-model.trim="entityForm.roleName" placeholder="名称" type="text" tabindex="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="sort" label="排序">
              <el-input-number v-model="entityForm.sort" :min="1" :max="9999999" tabindex="2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="description" label="描述">
          <el-input v-model="entityForm.description" placeholder="描述" :rows="3" :maxlength="100" type="textarea"
            tabindex="3" />
        </el-form-item>
        <el-form-item prop="dataScope" label="数据权限">
          <select-dict-data v-model:value="dataScope" dictTypeKey="sys_data_scope" />
        </el-form-item>
        <el-form-item v-if="entityForm.dataScope === 2" prop="deptIds" label="自定部门">
          <custom-tree v-if="deptsData" :data="deptsData?.all" ref="deptsRef" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveAndFlush">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改权限 -->
    <el-dialog v-model="menusDialog.visible" :title="menusDialog.title" width="50%" :close-on-click-modal="false">
      <custom-tree v-if="menusDialog.data" :data="menusDialog.data?.all" ref="menusRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="menusDialog.visible = false">取消</el-button>
          <el-button type="primary" v-preventReClick @click="handleUpdateMenus">确定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue"
import { Plus, Edit, Delete, Search, EditPen } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules, ElTree } from "element-plus"
import {
  queryRoleApi,
  saveRoleApi,
  updateRoleApi,
  deleteRoleApi,
  getRoleMenusApi,
  updateRoleMenusApi,
  getRoleDeptsApi
} from "@/api/role"
import { cloneDeep } from "lodash-es"
import { IPmsRole } from "@/types/pms"
import { showStringOverflow } from "@/hooks/useIndex"
import { IUpdateMenus } from "@/types/base"
import CustomTree from "@/components/CustomTree/index.vue"

interface ITreeDialog {
  /** 菜单ID */
  visible: boolean
  /** 菜单名称 */
  title: string
  /** 上级 */
  data?: IUpdateMenus
}

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<IPmsRole[]>()

const deptsRef = ref<any>()
const deptsData = ref<IUpdateMenus>()

const menusRef = ref<any>()
const menusDialog = reactive<ITreeDialog>({
  visible: false,
  title: "",
  data: undefined
})

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: "",
  includeCauu: true
})

/// 表单数据
const entityForm = ref<IPmsRole>({
  roleId: 0,
  roleName: "",
  sort: 0,
  description: "",
  dataScope: 4,
  deptIds: []
})

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  roleName: [{ required: true, message: "请输入名称", trigger: "blur" }],
  sort: [{ required: true, message: "请输入排序", trigger: "blur" }],
  description: [{ required: true, message: "请输入描述", trigger: "blur" }],
  dataScope: [{ required: true, message: "请选择数据权限", trigger: "blur" }],
}

// 获取数据
const getTableData = () => {
  loading.value = true
  queryRoleApi(params)
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
    roleId: 0,
    roleName: "",
    sort: 0,
    description: "",
    dataScope: 4,
    deptIds: []
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
const handleEditMenu = (data: IPmsRole) => {
  loading.value = true
  entityForm.value = cloneDeep(data)
  menusDialog.title = "修改"
  getRoleMenusApi(data.roleId)
    .then((resp) => {
      menusDialog.data = resp.data
      menusDialog.visible = true
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      // 调用子组件的方法，重新渲染 tree
      menusRef.value!.setCheckedKeys(menusDialog.data!.have)
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
  ElMessageBox.confirm(`确要修改 ${entityForm.value.roleName} 的权限吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      const params = {
        id: entityForm.value.roleId,
        menus: menuIds
      }
      updateRoleMenusApi(params)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `修改 ${entityForm.value.roleName} 的权限成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          menusDialog.visible = false
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}


const dataScope = computed({
  get(): number {
    return entityForm.value.dataScope
  },
  set(v: number) {
    if (v === 2) {
      handleEditDept()
    }
    entityForm.value.dataScope = v
  }
})


// 编辑部门
const handleEditDept = () => {
  loading.value = true
  getRoleDeptsApi(entityForm.value.roleId)
    .then((resp) => {
      deptsData.value = resp.data
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      // 调用子组件的方法，重新渲染 tree
      deptsRef.value!.setCheckedKeys(deptsData.value!.have)
      loading.value = false
    })
}


// 编辑
const handleEdit = (data: IPmsRole) => {
  resetEntity()
  entityForm.value = cloneDeep(data)
  if (data.dataScope === 2) {
    handleEditDept()
  }
  dialogTitle.value = "修改"
  saveFlag.value = false
  dialogVisible.value = true
}

// 新增
const handleAdd = () => {
  resetEntity()
  entityForm.value.sort = params.pageTotal + 1
  dialogTitle.value = "新增"
  saveFlag.value = true
  dialogVisible.value = true
}

// 删除
const handleDelete = (data: IPmsRole) => {
  ElMessageBox.confirm(`确要删除 ${data.roleName} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteRoleApi(data.roleId)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.roleName} 成功！`,
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
  // 自定义权限
  if (entityForm.value.dataScope === 2) {
    const deptIds: number[] = deptsRef.value!.getCheckedKeys()
    if (deptIds && deptIds.length < 1) {
      ElMessage({
        message: "至少选择一个部门",
        type: "warning"
      })
      return
    }
    entityForm.value.deptIds = deptIds
  }
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      if (saveFlag.value) {
        saveRoleApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增角色成功！",
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
        updateRoleApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改角色成功！",
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
