<template>
  <el-container class="app-container" v-loading="loading" v-has="['cms_project:view']">
    <el-header>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="params.keywords" clearable placeholder="根据项目名称搜索" />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="success" :icon="Plus" v-has="['cms_project:create']" @click="handleAdd()">新增</el-button>
        </el-col>
        <el-col :span="10"><div class="grid-content ep-bg-purple" /></el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="projectId" label="项目ID" />
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="linkman" label="联系人" />
        <el-table-column prop="contactNumber" label="联系电话" />
        <el-table-column prop="areaCode" label="行政区域">
          <template #default="scope">
            {{ showChinaArea(scope.row.areaCode) }}
          </template>
        </el-table-column>
        <el-table-column prop="address" label="联系地址" />
        <el-table-column prop="dept" label="部门" width="120">
          <template #default="scope">
            <el-popover v-if="scope.row.dept" placement="top-start" :width="230">
              <template #reference>
                <el-tag>{{ scope.row.dept.deptName }}</el-tag>
              </template>
              <div><strong>部门领导:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.leader }}</div>
              <div><strong>领导手机号:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.leaderMobile }}</div>
              <div><strong>部门描述:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.description }}</div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            {{ dictStore.getNormalDisable(scope.row.status) }}
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="130">
          <template #default="scope">
            <el-link :icon="Edit" type="warning" v-has="['cms_project:update']" @click="handleEdit(scope.row)"
              >编辑</el-link
            >
            &nbsp;
            <el-dropdown @command="(cmd: string) => handleCommand(cmd, scope.row)">
              <span class="el-dropdown-link">
                更多
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <div v-has="['cms_project:reset:password']">
                    <el-dropdown-item :icon="Link" command="ResetPassword">重置密码</el-dropdown-item>
                  </div>
                  <div v-if="scope.row.projectId !== 10001" v-has="['cms_project:delete']">
                    <el-dropdown-item :icon="Delete" command="Delete">删除</el-dropdown-item>
                  </div>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
              <el-form-item prop="projectName" label="项目名称">
                <el-input v-model.trim="entityForm.projectName" placeholder="项目名称" type="text" tabindex="1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="saveFlag" prop="password" label="密码">
                <el-input
                  v-model.trim="entityForm.password"
                  placeholder="项目密码"
                  type="password"
                  tabindex="2"
                  show-password
                />
              </el-form-item>
              <el-form-item v-else prop="projectId" label="项目ID">
                <el-input
                  v-model.trim="entityForm.projectId"
                  placeholder="项目ID"
                  type="text"
                  tabindex="1"
                  :disabled="!saveFlag"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="联系人" label="联系人">
                <el-input v-model.trim="entityForm.linkman" placeholder="联系人" type="text" tabindex="4" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="contactNumber" label="联系电话">
                <el-input
                  v-model.trim="entityForm.contactNumber"
                  placeholder="联系电话"
                  type="text"
                  show-word-limit
                  tabindex="3"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="status" label="状态">
                <select-dict-data v-model:value="entityForm.status" dictTypeKey="sys_normal_disable" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="deptId" label="部门">
                <el-cascader
                  v-model="entityForm.deptId"
                  :options="deptTree"
                  :props="{ checkStrictly: true, value: 'deptId', label: 'deptName' }"
                  @change="deptCascaderChange"
                  filterable
                  tabindex="6"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="areaCode" label="行政地址">
                <search-china-area v-model="entityForm.areaCode" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="address" label="联系地址">
                <el-input
                  v-model="entityForm.address"
                  :rows="3"
                  clearable
                  placeholder="联系地址"
                  type="textarea"
                  tabindex="6"
                />
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
import { deleteProjectApi, queryProjectApi, saveProjectApi, updateProjectApi, resetPasswordApi } from "@/api/project"
import { treeDeptApi } from "@/api/dept"
import { ISearchQuery } from "@/types/base"
import { IPmsDept } from "@/types/pms"
import { ICmsProject } from "@/types/cms"
import { isTelphone } from "@/utils/validate"
import { Plus, Edit, Delete, Search, Link } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus"
import SearchChinaArea from "@/components/SearchChinaArea/index.vue"
import { useDictStoreHook } from "@/store/modules/dict"
import { cloneDeep } from "lodash-es"
import { chinaAreaDeepQuery, timeAgo } from "@/utils"

const dictStore = useDictStoreHook()

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<ICmsProject[]>()

const deptTree = ref<IPmsDept[]>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

/// 表单数据
const entityForm = ref<ICmsProject>({
  projectId: 0,
  projectName: "",
  linkman: "",
  contactNumber: "",
  areaCode: 0,
  address: "",
  sort: 0,
  status: 1,
  deptId: 0,
  password: "",
  dept: null
})

const telphoneValidator = (rule: any, value: any, callback: any) => {
  if (!isTelphone(value)) {
    callback(new Error("手机或者电话号码格式不正确!"))
  } else {
    callback()
  }
}
const passwordValidator = (rule: any, value: any, callback: any) => {
  if (!/^\w{5,24}$/.test(value)) {
    callback(new Error("密码数字和字母长度5~24位"))
  } else {
    callback()
  }
}

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  projectName: [{ required: true, message: "请输入项目名称", trigger: "blur" }],
  linkman: [{ required: true, message: "请输入联系人", trigger: "blur" }],
  contactNumber: [
    { required: true, message: "请输入联系方式", trigger: "blur" },
    { validator: telphoneValidator, trigger: "blur" }
  ],
  areaCode: [{ required: true, message: "请选择行政区域", trigger: "blur" }],
  address: [{ required: true, message: "请输入联系地址", trigger: "blur" }],
  sort: [{ required: true, message: "请输入排序", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "blur" }],
  password: [
    { required: true, message: "请输入项目密码", trigger: "blur" },
    { validator: passwordValidator, trigger: "blur" }
  ],
  deptId: [{ required: true, message: "请选择所在部门", trigger: "blur" }]
}

const getTableData = () => {
  loading.value = true
  const p: ISearchQuery = {
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords
  }
  queryProjectApi(p)
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
    projectId: 0,
    projectName: "",
    linkman: "",
    contactNumber: "",
    areaCode: 0,
    address: "",
    sort: 0,
    status: 1,
    deptId: 0,
    password: "123456",
    dept: null
  }
}

// 过期时间
const showTimeAgo = computed(() => {
  return (value: string) => timeAgo(value)
})

const showChinaArea = computed(() => {
  return (value: number) => {
    const area = chinaAreaDeepQuery(value)
    if (area) {
      return area.mergerName.replaceAll("-", " / ")
    }
    return ""
  }
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

const handleCommand = (command: string, data: ICmsProject) => {
  switch (command) {
    case "ResetPassword":
      handleResetPassword(data)
      break
    case "Delete":
      handleDelete(data)
      break
    default:
      console.log("command :>> ", command)
      break
  }
}

// 新增
const handleAdd = () => {
  resetEntity()
  dialogTitle.value = "新增"
  saveFlag.value = true
  dialogVisible.value = true
}

// 重置密码
const handleResetPassword = (data: ICmsProject) => {
  ElMessageBox.prompt(`确定要重置 ${data.projectName} 的密码吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /^\w{5,24}$/,
    inputErrorMessage: "密码数字和字母长度5~24位"
  })
    .then(({ value }) => {
      const params = {
        projectId: data.projectId,
        newPwd: value
      }
      resetPasswordApi(params)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `重置 ${data.projectName} 的密码成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
    })
    .catch(() => console.log("catch..."))
}

// 编辑
const handleEdit = (data: ICmsProject) => {
  resetEntity()
  entityForm.value = cloneDeep(data)
  dialogTitle.value = "修改"
  saveFlag.value = false
  dialogVisible.value = true
}

// 获取部门数据
const getDeptTreeData = () => {
  loading.value = true
  treeDeptApi()
    .then((resp) => {
      deptTree.value = resp.data
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      loading.value = false
    })
}

// 选择部门
const deptCascaderChange = (data: number[]) => {
  entityForm.value.deptId = data[data.length - 1]
}

// 删除
const handleDelete = (data: ICmsProject) => {
  ElMessageBox.confirm(`确要删除 ${data.projectName} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteProjectApi(data.projectId)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.projectName} 成功！`,
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
        saveProjectApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增项目成功！",
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
        updateProjectApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改项目成功！",
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
  getDeptTreeData()
})
</script>

<style lang="scss" scoped>
.el-tag {
  margin: 2px;
}
</style>
