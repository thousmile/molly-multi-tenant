<template>
  <el-container class="app-container" v-loading="loading" v-has="['pre_dept:view']">
    <el-header height="30px">
      <div class="flex">
        <el-button type="primary" :icon="Plus" v-has="['pre_dept:create']" @click="handleAdd(null)">新增</el-button>
        <el-button type="success" :icon="Sort" @click="switchExpandAndCollapse">{{
          expandAndCollapse ? "折叠" : "展开"
        }}</el-button>
      </div>
    </el-header>
    <el-main>
      <el-table ref="tableRef" :data="tableData" row-key="id" :default-expand-all="expandAndCollapse">
        <el-table-column prop="deptId" label="部门ID" />
        <el-table-column prop="deptName" label="部门名称" />
        <el-table-column prop="leader" label="领导名称" />
        <el-table-column prop="leaderMobile" label="领导联系方式" />
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
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.createTime" :content="scope.row.createTime" placement="top">
              <el-link> {{ showTimeAgo(scope.row.createTime) }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="修改时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.lastUpdateTime" :content="scope.row.lastUpdateTime" placement="top">
              <el-link> {{ showTimeAgo(scope.row.lastUpdateTime) }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-link :icon="Plus" type="primary" v-has="['pre_dept:create']" @click="handleAdd(scope.row)"
              >新增</el-link
            >
            &nbsp;
            <el-link :icon="Edit" type="warning" v-has="['pre_dept:update']" @click="handleEdit(scope.row)"
              >编辑</el-link
            >
            &nbsp;
            <el-link
              :icon="Delete"
              v-if="!scope.row.children"
              type="danger"
              v-has="['pre_dept:delete']"
              @click="handleDelete(scope.row)"
              >删除</el-link
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增和修改的弹窗 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%" :close-on-click-modal="false">
        <el-form
          ref="entityFormRef"
          :model="entityForm"
          :rules="entityFormRules"
          label-position="right"
          label-width="80px"
          @keyup.enter="handleSaveAndFlush"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="deptName" label="部门名称">
                <el-input v-model.trim="entityForm.deptName" placeholder="名称" type="text" tabindex="1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="parentId" label="上级权限">
                <el-cascader
                  v-model="entityForm.parentId"
                  :options="parentMenus"
                  :props="{ checkStrictly: true }"
                  @change="cascaderChange"
                  filterable
                  tabindex="2"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="leader" label="领导名称">
                <el-input v-model.trim="entityForm.leader" placeholder="领导名称" type="text" tabindex="3" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="leaderMobile" label="联系方式">
                <el-input
                  v-model.trim="entityForm.leaderMobile"
                  placeholder="领导联系方式"
                  type="text"
                  maxlength="11"
                  show-word-limit
                  tabindex="4"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="sort" label="部门排序">
                <el-input-number v-model="entityForm.sort" :min="1" :max="9999999" tabindex="5" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="description" label="描述">
                <el-input v-model.trim="entityForm.description" placeholder="描述" type="text" tabindex="6" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSaveAndFlush">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { treeDeptApi, saveDeptApi, updateDeptApi, deleteDeptApi } from "@/api/dept"
import { IPmsDept } from "@/types/pms"
import { ref, onMounted, computed } from "vue"
import { Plus, Edit, Delete, Sort } from "@element-plus/icons-vue"

import { ElMessage, ElMessageBox, FormInstance, FormRules, TableInstance } from "element-plus"
import { testPhone } from "@/utils/regular"
import { cloneDeep } from "lodash-es"
import { timeAgo } from "@/utils"

// 部门
export interface ISimplePmsDept {
  /**
   * 部门 ID
   */
  value: number
  /**
   * 部门 名称
   */
  label: string
  /**
   * 子部门
   */
  children?: ISimplePmsDept[]
}

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableRef = ref<TableInstance | null>(null)

const tableData = ref<IPmsDept[]>()

/// 表单数据
const entityForm = ref<IPmsDept>({
  deptId: 0,
  parentId: 0,
  deptName: "",
  leader: "",
  leaderMobile: "",
  sort: 0,
  description: "",
  ancestors: "",
  children: []
})

const phoneValidator = (rule: any, value: any, callback: any) => {
  if (!testPhone(value)) {
    callback(new Error("手机号码格式不正确!"))
  } else {
    callback()
  }
}

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  parentId: [{ required: true, message: "请选择上级部门", trigger: "blur" }],
  deptName: [{ required: true, message: "请输入名称", trigger: "blur" }],
  leader: [{ required: true, message: "请输入部门领导", trigger: "blur" }],
  leaderMobile: [
    { required: true, message: "请输入领导联系方式", trigger: "blur" },
    { validator: phoneValidator, trigger: "blur" }
  ],
  sort: [{ required: true, message: "请输入排序", trigger: "blur" }],
  description: [{ required: true, message: "请输入描述", trigger: "blur" }]
}

// 上级权限
const parentMenus = ref<ISimplePmsDept[]>()

/** 将权限树形结构扁平化为一维数组，用于权限查询 */
const flatTree = (arr: IPmsDept[]) => {
  const result: ISimplePmsDept[] = []
  const deep = (arr1: IPmsDept[], arr2: ISimplePmsDept[]) => {
    arr1.forEach((item: IPmsDept) => {
      const temp: ISimplePmsDept = {
        value: item.deptId,
        label: item.deptName
      }
      arr2.push(temp)
      if (item.children && item.children.length > 0) {
        temp.children = []
        deep(item.children, temp.children)
      }
    })
  }
  deep(arr, result)
  return result
}

const showTimeAgo = computed(() => {
  return (value: string) => timeAgo(value)
})

const showStringOverflow = computed(() => {
  return (value: string) => (value.length <= 20 ? value : value.substring(0, 20))
})

const expandAndCollapse = ref(true)

// 展开和折叠
const switchExpandAndCollapse = () => {
  expandAndCollapse.value = !expandAndCollapse.value
  const deep = (arr1: IPmsDept[]) => {
    arr1.forEach((item: IPmsDept) => {
      if (item.children) {
        deep(item.children)
      }
      tableRef.value!.toggleRowExpansion(item, expandAndCollapse.value)
    })
  }
  deep(tableData.value!)
}

// 获取数据
const getTableData = () => {
  loading.value = true
  treeDeptApi()
    .then((resp) => {
      tableData.value = resp.data
      parentMenus.value = flatTree(resp.data)
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
    deptId: 0,
    parentId: 0,
    deptName: "",
    leader: "",
    leaderMobile: "",
    sort: 0,
    description: "",
    ancestors: "",
    children: []
  }
}

const cascaderChange = (data: number[]) => {
  entityForm.value.parentId = data[data.length - 1]
}

// 添加
const handleAdd = (data: IPmsDept | null) => {
  resetEntity()
  if (data) {
    // 上级权限
    entityForm.value.parentId = data.deptId
    // 排序
    entityForm.value.sort = data.children ? data.children.length + 1 : 0
  } else {
    // 上级权限
    entityForm.value.parentId = 0
    entityForm.value.sort = 1
  }
  dialogTitle.value = "新增"
  saveFlag.value = true
  dialogVisible.value = true
}

// 编辑
const handleEdit = (data: IPmsDept) => {
  resetEntity()
  entityForm.value = cloneDeep(data)
  dialogTitle.value = "修改"
  saveFlag.value = false
  dialogVisible.value = true
}

// 删除
const handleDelete = (data: IPmsDept) => {
  ElMessageBox.confirm(`确要删除 ${data.deptName} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteDeptApi(data.deptId)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.deptName} 成功！`,
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
        saveDeptApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增部门成功！",
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
        updateDeptApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改部门成功！",
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
