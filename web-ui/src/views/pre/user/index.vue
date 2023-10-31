<template>
  <el-container class="app-container" v-loading="loading" v-has="['pre_user:view']">
    <el-header>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input v-model="params.keywords" clearable placeholder="根据关键字搜索" />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" :icon="Search" @click="searchTableData">搜索</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="success" :icon="Plus" v-has="['pre_user:create']" @click="handleAdd()">新增</el-button>
        </el-col>
        <el-col :span="10">
          <div class="grid-content ep-bg-purple" />
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="avatar" label="头像">
          <template #default="scope">
            <el-avatar :size="40" :icon="UserFilled" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="mobile" label="手机号" />
        <el-table-column prop="gender" label="性别">
          <template #default="scope">
            {{ dictStore.getUserSex(scope.row.gender) }}
          </template>
        </el-table-column>
        <el-table-column prop="roles" label="角色" width="150">
          <template #default="scope">
            <el-tooltip
              v-for="(item, index) in scope.row.roles"
              :key="index"
              effect="dark"
              :content="item.description"
              placement="top-start"
            >
              <el-tag>{{ showStringOverflow(item.roleName, 10) }}</el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="roles" label="部门" width="120">
          <template #default="scope">
            <el-popover placement="top-start" :width="230">
              <template #reference>
                <el-tag>{{ showStringOverflow(scope.row.dept.deptName, 10) }}</el-tag>
              </template>
              <div><strong>部门领导:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.leader }}</div>
              <div><strong>领导手机号:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.leaderMobile }}</div>
              <div><strong>部门描述:</strong> &nbsp;&nbsp;&nbsp;{{ scope.row.dept.description }}</div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="adminFlag" label="管理员">
          <template #default="scope">
            <el-tag v-if="scope.row.adminFlag" type="success">是</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            {{ dictStore.getNormalDisable(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column prop="expired" label="过期时间">
          <template #default="scope">
            <el-tooltip v-if="scope.row.expired" :content="scope.row.expired" placement="top">
              <el-link> {{ showExpiredDateAgo(scope.row.expired) }}</el-link>
            </el-tooltip>
            <span v-else>永久</span>
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
            <el-link :icon="Edit" type="warning" v-has="['pre_user:update']" @click="handleEdit(scope.row)"
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
                  <div v-has="['pre_user:reset:password']">
                    <el-dropdown-item :icon="Link" command="ResetPassword">重置密码</el-dropdown-item>
                  </div>
                  <div v-if="isDefaultTenantId()" v-has="['pre_user:link:tenant']">
                    <el-dropdown-item :icon="RefreshLeft" command="LinkTenant">关联租户</el-dropdown-item>
                  </div>
                  <div v-if="scope.row.adminFlag !== 1" v-has="['pre_user:delete']">
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
              <el-form-item prop="avatar" label="头像">
                <user-avatar :key="entityForm.avatar" :src="entityForm.avatar" @change="getAvatar" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="username" label="用户名">
                <el-input
                  v-model.trim="entityForm.username"
                  placeholder="用户名"
                  type="text"
                  tabindex="1"
                  :disabled="!saveFlag"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="password" label="密码" v-if="saveFlag">
                <el-input
                  v-model.trim="entityForm.password"
                  placeholder="密码"
                  type="password"
                  tabindex="2"
                  show-password
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="mobile" label="手机号">
                <el-input
                  v-model.trim="entityForm.mobile"
                  placeholder="手机号"
                  type="text"
                  maxlength="11"
                  show-word-limit
                  tabindex="3"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="email" label="邮箱">
                <el-input v-model.trim="entityForm.email" placeholder="邮箱" type="text" tabindex="4" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="nickname" label="昵称">
                <el-input v-model.trim="entityForm.nickname" placeholder="昵称" type="text" tabindex="5" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="gender" label="性别">
                <select-dict-data v-model:value="entityForm.gender" dictTypeKey="sys_user_sex" />
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
              <el-form-item prop="roles" label="角色">
                <el-select v-model="entityRoles" filterable clearable multiple placeholder="选择角色">
                  <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
                    <span style="float: left">{{ item.roleId }}</span>
                    <span style="float: right; font-size: 13px">{{ showStringOverflow(item.roleName) }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="expired" label="过期">
                <el-date-picker
                  v-model="entityForm.expired"
                  type="datetime"
                  placeholder="请选择过期时间，不填即是永久"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  :shortcuts="futureShortcuts"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" v-preventReClick @click="handleSaveAndFlush">确定</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 新增和修改的弹窗 -->
      <el-dialog
        v-model="linkTenantForm.visible"
        :title="linkTenantForm.title"
        width="30%"
        :close-on-click-modal="false"
      >
        <el-select-v2
          v-model="linkTenantForm.have"
          :options="linkTenantForm.all"
          placeholder="请选择租户"
          style="width: 100%"
          multiple
          clearable
        >
          <template #default="{ item }">
            <span style="margin-right: 8px">{{ item.value }}</span>
            <span style="color: var(--el-text-color-secondary); font-size: 13px">
              {{ item.label }}
            </span>
          </template>
        </el-select-v2>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="linkTenantForm.visible = false">取消</el-button>
            <el-button type="primary" v-preventReClick @click="handleSaveLinkTenant">确定</el-button>
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
import {
  deleteUserApi,
  listSysUserTenantIdApi,
  queryUserApi,
  resetPasswordApi,
  saveUserApi,
  updateSysUserTenantIdApi,
  updateUserApi
} from "@/api/user"
import { treeDeptApi } from "@/api/dept"
import { listRoleApi } from "@/api/role"
import { ISearchQuery, ISimpleTenant } from "@/types/base"
import { IPmsUser, IPmsDept, IPmsRole } from "@/types/pms"
import UserAvatar from "@/components/UserAvatar/index.vue"
import { isEmail, isPassword, isPhone } from "@/utils/validate"
import { Plus, Edit, Delete, UserFilled, Search, RefreshLeft, Link } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus"
import { useDictStoreHook } from "@/store/modules/dict"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { futureShortcuts } from "@/utils"
import { showExpiredDateAgo, showStringOverflow } from "@/hooks/useIndex"
import { cloneDeep } from "lodash-es"

// 判读 当前选中的租户是否 默认租户
const { isDefaultTenantId } = useTenantStoreHook()

console.log("isDefaultTenantId() :>>", isDefaultTenantId())

const dictStore = useDictStoreHook()

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<IPmsUser[]>()

const deptTree = ref<IPmsDept[]>()

const roleList = ref<IPmsRole[]>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: ""
})

/// 表单数据
const entityForm = ref<IPmsUser>({
  userId: 0,
  avatar: "",
  username: "",
  mobile: "",
  email: "",
  nickname: "",
  password: "",
  gender: 0,
  adminFlag: 0,
  status: 1,
  deptId: 0,
  expired: "",
  dept: null,
  roles: []
})

const emailValidator = (rule: any, value: any, callback: any) => {
  if (!isEmail(value)) {
    callback(new Error("邮箱格式不正确!"))
  } else {
    callback()
  }
}

const phoneValidator = (rule: any, value: any, callback: any) => {
  if (!isPhone(value)) {
    callback(new Error("手机号码格式不正确!"))
  } else {
    callback()
  }
}

const passwordValidator = (rule: any, value: any, callback: any) => {
  if (!isPassword(value)) {
    callback(new Error("包含1个字母并且长度大于5位"))
  } else {
    callback()
  }
}

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  avatar: [{ required: true, message: "头像必须上传", trigger: "blur" }],
  username: [
    { required: true, message: "请输入登录用户名", trigger: "blur" },
    { min: 5, max: 32, message: "长度大于 5 个字符", trigger: "blur" }
  ],
  mobile: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { validator: phoneValidator, trigger: "blur" }
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { validator: emailValidator, trigger: "blur" }
  ],
  nickname: [{ required: true, message: "请输入用户昵称", trigger: "blur" }],
  password: [
    { required: true, message: "请输入登录密码", trigger: "blur" },
    { validator: passwordValidator, trigger: "blur" }
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "blur" }],
  deptId: [{ required: true, message: "请选择所在部门", trigger: "blur" }],
  roles: [{ required: true, message: "请选择角色", trigger: "blur" }]
}

const getTableData = () => {
  loading.value = true
  const p: ISearchQuery = {
    includeCauu: true,
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords
  }
  queryUserApi(p)
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
    userId: 0,
    avatar: "",
    username: "",
    mobile: "",
    email: "",
    nickname: "",
    password: "",
    gender: 0,
    adminFlag: 0,
    status: 1,
    deptId: 0,
    expired: "",
    dept: null,
    roles: []
  }
}

const getAvatar = (data: string) => {
  entityForm.value.avatar = data
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

const entityRoles = computed({
  get() {
    if (entityForm.value.roles) {
      return entityForm.value.roles.map((role) => role.roleId)
    } else {
      return []
    }
  },
  set(data: number[]) {
    entityForm.value.roles = data.map((id) => {
      const role: IPmsRole = {
        roleId: id,
        roleName: "",
        sort: 0,
        description: ""
      }
      return role
    })
  }
})

const handleCommand = (command: string, data: IPmsUser) => {
  switch (command) {
    case "ResetPassword":
      handleResetPassword(data)
      break
    case "LinkTenant":
      handleLinkTenant(data)
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
const handleResetPassword = (data: IPmsUser) => {
  ElMessageBox.prompt(`确定要重置 ${data.nickname} 的密码吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{5,24}$/,
    inputErrorMessage: "密码包含字母和数字，长度5~24位"
  })
    .then(({ value }) => {
      const params = {
        userId: data.userId,
        newPwd: value
      }
      resetPasswordApi(params)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `重置 ${data.nickname} 的密码成功！`,
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
const handleEdit = (data: IPmsUser) => {
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

// 获取角色数据
const getListRoleData = () => {
  loading.value = true
  listRoleApi()
    .then((resp) => {
      roleList.value = resp.data
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
const handleDelete = (data: IPmsUser) => {
  ElMessageBox.confirm(`确要删除 ${data.nickname} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteUserApi(data.userId)
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

// 新增和修改
const handleSaveAndFlush = () => {
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      if (saveFlag.value) {
        saveUserApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增用户成功！",
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
        updateUserApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改用户成功！",
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

interface LinkTenant {
  visible: boolean
  title: string
  userId: number
  all: ISimpleTenant[]
  have: string[]
}

const linkTenantForm = reactive<LinkTenant>({
  visible: false,
  title: "",
  userId: 0,
  all: [],
  have: []
})

const resetLinkTenant = () => {
  linkTenantForm.userId = 0
  linkTenantForm.title = ""
  linkTenantForm.visible = false
  linkTenantForm.all = []
  linkTenantForm.have = []
}

// 系统用户关联租户
const handleLinkTenant = (data: IPmsUser) => {
  listSysUserTenantIdApi(data.userId)
    .then((resp) => {
      if (resp.data) {
        linkTenantForm.all = resp.data.all
        linkTenantForm.have = resp.data.have
      }
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      linkTenantForm.userId = data.userId
      linkTenantForm.title = `${data.nickname} 关联租户`
      linkTenantForm.visible = true
    })
}

const handleSaveLinkTenant = () => {
  const tenantIds = linkTenantForm.have.map((id) => id)
  const params = {
    userId: linkTenantForm.userId,
    tenantIds: tenantIds
  }
  ElMessageBox.confirm(`确定要关联租户吗?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      updateSysUserTenantIdApi(params)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: "用户关联租户成功！",
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => resetLinkTenant())
    })
    .catch(() => resetLinkTenant())
}

onMounted(() => {
  getTableData()
  getDeptTreeData()
  getListRoleData()
})
</script>

<style lang="scss" scoped>
.el-tag {
  margin: 2px;
}
</style>
