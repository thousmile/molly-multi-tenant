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
        <el-col :span="10">
          <div class="grid-content ep-bg-purple" />
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="id" label="模板ID" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="description" label="描述">
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

      <!-- 新增和修改的弹窗 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%" :close-on-click-modal="false">
        <el-form
          ref="entityFormRef"
          :model="entityForm"
          :rules="entityFormRules"
          label-position="right"
          label-width="80px"
        >
          <el-form-item prop="name" label="名称">
            <el-input v-model.trim="entityForm.name" placeholder="名称" type="text" tabindex="1" />
          </el-form-item>
          <el-form-item prop="description" label="描述">
            <el-input
              v-model="entityForm.description"
              placeholder="描述"
              :rows="5"
              :maxlength="100"
              type="textarea"
              tabindex="3"
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

      <!-- 修改权限 -->
      <el-dialog v-model="menusDialogVisible" :title="menusDialogTitle" width="30%" :close-on-click-modal="false">
        <el-row :gutter="20">
          <el-col :span="14">
            <el-input v-model="filterMenuText" clearable placeholder="根据菜单名称筛选" />
          </el-col>
          <el-col :span="5">
            <el-button type="success" :icon="Sort" @click="switchExpandAndCollapse">{{
              expandAndCollapse ? "折叠" : "展开"
            }}</el-button>
          </el-col>
          <el-col :span="5">
            <el-button type="success" :icon="checkAll ? 'CloseBold' : 'Select'" @click="switchCheckAll">{{
              checkAll ? "取消" : "全选"
            }}</el-button>
          </el-col>
        </el-row>
        <br />
        <div :style="{ height: '400px', overflowY: 'scroll' }">
          <el-tree
            ref="treeRef"
            :data="menusData?.all"
            :check-strictly="true"
            :default-expand-all="expandAndCollapse"
            :filter-node-method="filterNode"
            show-checkbox
            node-key="id"
            highlight-current
            :props="{ children: 'children', label: 'name' }"
          />
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="menusDialogVisible = false">取消</el-button>
            <el-button type="primary" v-preventReClick @click="handleUpdateMenus">确定</el-button>
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
import { ref, reactive, onMounted, watch } from "vue"
import { Plus, Edit, Delete, Search, EditPen, Sort } from "@element-plus/icons-vue"
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
import { ISearchQuery, ISimpleMenu, IUpdateMenus } from "@/types/base"
import { showStringOverflow } from "@/hooks/useIndex"

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<ISysTemplate[]>()

const menusDialogVisible = ref(false)

const menusDialogTitle = ref("")

const filterMenuText = ref("")

const menusData = ref<IUpdateMenus>()

const treeRef = ref<InstanceType<typeof ElTree>>()

const expandAndCollapse = ref(true)

const checkAll = ref(true)

// 获取 全部菜单ID
const getMenuIdAll = () => {
  const result: number[] = []
  const deep = (arr1: ISimpleMenu[], arr2: number[]) => {
    arr1.forEach((item: ISimpleMenu) => {
      arr2.push(item.id)
      if (item.children && item.children.length > 0) {
        deep(item.children, arr2)
      }
    })
  }
  deep(menusData.value!.all, result)
  return result
}

// 展开和折叠
const switchExpandAndCollapse = () => {
  expandAndCollapse.value = !expandAndCollapse.value
  menusData.value!.all.forEach((data) => {
    treeRef.value!.store.nodesMap[data.id].expanded = expandAndCollapse.value
  })
}

// 全选和取消全选
const switchCheckAll = () => {
  checkAll.value = !checkAll.value
  if (checkAll.value) {
    const result: number[] = getMenuIdAll()
    treeRef.value!.setCheckedKeys(result, false)
  } else {
    treeRef.value!.setCheckedKeys([], false)
  }
}

watch(filterMenuText, (val) => {
  treeRef.value!.filter(val)
})

const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.name.includes(value)
}

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
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
  const p: ISearchQuery = {
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords,
    includeCauu: true
  }
  queryTemplateApi(p)
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
      const have: number[] = menusData.value!.have
      if (have.length > 0) {
        checkAll.value = true
      } else {
        checkAll.value = false
      }
      treeRef.value!.setCheckedKeys(have, false)
      expandAndCollapse.value = true
      loading.value = false
    })
}

// 修改权限
const handleUpdateMenus = () => {
  const menuIds: number[] = treeRef.value!.getCheckedKeys(false) as number[]
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
