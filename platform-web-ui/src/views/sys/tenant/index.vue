<template>
  <div class="app-container" v-loading="loading">
    <el-card shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="params">
        <el-form-item>
          <el-input v-model="params.keywords" clearable placeholder="根据 租户名称 搜索" />
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
          <el-table-column prop="tenantId" label="租户ID" />
          <el-table-column prop="logo" label="Logo">
            <template #default="scope">
              <el-avatar :size="40" :icon="UserFilled" :src="scope.row.logo" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="linkman" label="联系人" />
          <el-table-column prop="contactNumber" label="联系方式" />
          <el-table-column prop="areaCode" label="行政区域">
            <template #default="scope">
              {{ showChinaArea(scope.row.areaCode) }}
            </template>
          </el-table-column>
          <el-table-column prop="address" label="联系地址" />
          <el-table-column prop="templates" label="权限模板">
            <template #default="scope">
              <el-tooltip
                v-for="(item, index) in scope.row.templates"
                :key="index"
                effect="dark"
                :content="item.description"
                placement="top-start"
              >
                <el-tag>{{ item.name }}</el-tag>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <template v-if="scope.row.status === 9">
                <el-text class="mx-1" type="warning">初始化中...</el-text>
                <el-icon class="is-loading">
                  <Loading />
                </el-icon>
              </template>
              <span v-else>
                {{ dictStore.getNormalDisable(scope.row.status) }}
              </span>
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
              <el-link :icon="Edit" type="warning" @click="handleEdit(scope.row)">编辑</el-link>
              &nbsp;
              <!-- v-admin 只有管理员才可以 删除租户、重置数据 -->
              <el-dropdown
                @command="(cmd: string) => handleCommand(cmd, scope.row)"
                v-if="!isDefaultTenantId(scope.row.tenantId)"
              >
                <span class="el-dropdown-link">
                  更多
                  <el-icon class="el-icon--right">
                    <arrow-down />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :icon="Link" command="Jump">跳转客户系统</el-dropdown-item>
                    <el-dropdown-item command="QRcode">
                      <template #default><SvgIcon name="qrcode" />&nbsp;生成二维码</template>
                    </el-dropdown-item>
                    <div v-admin>
                      <el-dropdown-item :icon="Refresh" command="ResetData">重置数据</el-dropdown-item>
                    </div>
                    <div v-admin>
                      <el-dropdown-item :icon="Delete" command="Delete">删除</el-dropdown-item>
                    </div>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pager-wrapper">
        <el-pagination
          v-model:current-page="params.pageIndex"
          :page-size="params.pageSize"
          :background="true"
          layout="sizes, total, prev, pager, next, jumper"
          :total="params.pageTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 生成二维码弹窗 -->
    <el-dialog v-model="qrCodeDialog.visible" :title="qrCodeDialog.title" width="500px">
      <div style="text-align: center">
        <el-link type="primary" :href="qrCodeDialog.link" target="_blank">
          {{ qrCodeDialog.link }}
        </el-link>
        <br />
        <br />
        <QRCodeVue3
          :key="qrCodeDialog.tenantId"
          :width="300"
          :value="qrCodeDialog.link"
          :imageOptions="{ hideBackgroundDots: true, imageSize: 0.4, margin: 0 }"
          :image="qrCodeDialog.logo"
        />
      </div>
    </el-dialog>

    <!-- 新增和修改的弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%" :close-on-click-modal="false">
      <template v-if="saveFlag">
        <create-tenant :template-list="templateList!" @close="closeCreateTenant" />
      </template>

      <template v-if="!saveFlag">
        <el-form
          ref="entityFormRef"
          :model="entityForm"
          :rules="entityFormRules"
          label-position="right"
          label-width="80px"
        >
          <el-form-item prop="logo" label="Logo">
            <image-upload :key="entityForm.logo" :src="entityForm.logo" @change="getLogoSrc" />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="tenantId" label="租户ID">
                <el-input
                  v-model.trim="entityForm.tenantId"
                  clearable
                  placeholder="租户ID"
                  disabled
                  type="text"
                  tabindex="1"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="name" label="名称">
                <el-input v-model.trim="entityForm.name" clearable placeholder="租户名称" type="text" tabindex="2" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="email" label="邮箱">
                <el-input v-model.trim="entityForm.email" clearable placeholder="租户邮箱" type="text" tabindex="3" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="status" label="状态">
                <select-dict-data v-model:value="entityForm.status" dictTypeKey="sys_normal_disable" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="linkman" label="联系人">
                <el-input
                  v-model.trim="entityForm.linkman"
                  clearable
                  placeholder="租户联系人"
                  type="text"
                  tabindex="4"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="contactNumber" label="联系方式">
                <el-input
                  v-model.trim="entityForm.contactNumber"
                  clearable
                  placeholder="租户联系方式"
                  type="text"
                  tabindex="5"
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
                  v-model.trim="entityForm.address"
                  clearable
                  placeholder="租户联系地址"
                  type="text"
                  tabindex="6"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item prop="templates" label="租户模板">
                <el-select v-model="entityTemplates" filterable clearable multiple placeholder="选择租户模板">
                  <el-option v-for="item in templateList" :key="item.id" :label="item.name" :value="item.id">
                    <span style="float: left">{{ item.name }}</span>
                    <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">{{
                      showStringOverflow(item.description)
                    }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="expired" label="过期时间">
                <el-date-picker
                  v-model="entityForm.expired"
                  clearable
                  type="datetime"
                  placeholder="请选择过期时间"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  :shortcuts="futureShortcuts"
                  tabindex="7"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>

      <template v-if="!saveFlag" #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" v-preventReClick @click="handleUpdate">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from "vue"
import { Plus, Edit, Delete, Search, UserFilled, Loading, Link, Refresh } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus"
import { queryTenantApi, updateTenantApi, deleteTenantApi, resetDataTenantApi } from "@/api/tenant"
import { listTemplateApi } from "@/api/template"
import { ISysTenant, ISysTemplate } from "@/types/sys"
import ImageUpload from "@/components/ImageUpload/index.vue"
import SearchChinaArea from "@/components/SearchChinaArea/index.vue"
import createTenant from "./create.vue"
import { useDictStoreHook } from "@/store/modules/dict"
import { cloneDeep } from "lodash-es"
import { showExpiredDateAgo, showStringOverflow, showChinaArea, isDefaultTenantId } from "@/hooks/useIndex"
import { isEmail, isTelphone } from "@/utils/validate"
import { futureShortcuts } from "@/utils"
import { useNoticeStoreHook } from "@/store/modules/notice"
import QRCodeVue3 from "qrcode-vue3"
import { getFileBlob } from "@/utils/service"

const dictStore = useDictStoreHook()

const noticeStore = useNoticeStoreHook()

// 监听
watch(
  () => noticeStore.createTenantNotice,
  (newValue, _) => {
    ElMessageBox.confirm(newValue!.content, "是否重新加载数据?", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(() => getTableData())
      .catch(() => console.log())
  }
)

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableData = ref<ISysTenant[]>()

const templateList = ref<ISysTemplate[]>()

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 10,
  keywords: "",
  includeCauu: true
})

/// 表单数据
const entityForm = ref<ISysTenant>({
  tenantId: "",
  logo: "",
  name: "",
  email: "",
  linkman: "",
  contactNumber: "",
  areaCode: 0,
  address: "",
  status: 0,
  expired: "",
  templates: []
})

const entityFormRef = ref<FormInstance | null>(null)

const emailValidator = (rule: any, value: any, callback: any) => {
  if (!isEmail(value)) {
    callback(new Error("邮箱格式不正确!"))
  } else {
    callback()
  }
}

const telphoneValidator = (rule: any, value: any, callback: any) => {
  if (!isTelphone(value)) {
    callback(new Error("手机或者电话号码格式不正确!"))
  } else {
    callback()
  }
}

/// 表单校验规则
const entityFormRules: FormRules = {
  tenantId: [{ required: true, message: "请输入租户ID", trigger: "blur" }],
  logo: [{ required: true, message: "请上传租户Logo", trigger: "blur" }],
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { validator: emailValidator, trigger: "blur" }
  ],
  linkman: [{ required: true, message: "请输入联系人", trigger: "blur" }],
  contactNumber: [
    { required: true, message: "请输入联系方式", trigger: "blur" },
    { validator: telphoneValidator, trigger: "blur" }
  ],
  areaCode: [{ required: true, message: "请选择行政区域", trigger: "blur" }],
  address: [{ required: true, message: "请输入联系地址", trigger: "blur" }],
  status: [{ required: true, message: "请输入状态", trigger: "blur" }],
  templates: [{ required: true, message: "请选择租户模板", trigger: "blur" }]
}

// 获取模板数据
const getListTemplateData = () => {
  loading.value = true
  listTemplateApi()
    .then((resp) => {
      templateList.value = resp.data
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      loading.value = false
    })
}

// 获取数据
const getTableData = () => {
  loading.value = true
  queryTenantApi(params)
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
    tenantId: "",
    logo: "",
    name: "",
    email: "",
    linkman: "",
    contactNumber: "",
    areaCode: 0,
    address: "",
    status: 0,
    expired: "",
    templates: []
  }
}

const getLogoSrc = (data: string) => {
  entityForm.value.logo = data
}

const entityTemplates = computed({
  get() {
    if (entityForm.value.templates) {
      return entityForm.value.templates.map((t) => t.id)
    } else {
      return []
    }
  },
  set(data: number[]) {
    entityForm.value.templates = data.map((id) => {
      return {
        id: id,
        name: "",
        description: ""
      }
    })
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

// 编辑
const handleEdit = (data: ISysTenant) => {
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

const handleCommand = (command: string, data: ISysTenant) => {
  switch (command) {
    case "ResetData":
      handleResetData(data)
      break
    case "Delete":
      handleDelete(data)
      break
    case "Jump":
      window.open(genTenantLink(data.tenantId), "_blank")
      break
    case "QRcode":
      getFileBlob(data.logo)
        .then((blob) => {
          const reader = new FileReader()
          reader.readAsDataURL(blob)
          reader.onload = () => {
            qrCodeDialog.value.title = `扫码进入 ${data.name} 客户管理系统`
            qrCodeDialog.value.link = genTenantLink(data.tenantId)
            qrCodeDialog.value.tenantId = data.tenantId
            const base64 = reader.result as string
            qrCodeDialog.value.logo = base64
            qrCodeDialog.value.visible = true
          }
          reader.onerror = (err) => console.log("err :>> ", err)
        })
        .catch((err) => {
          console.log("QRcode error :>> ", err)
        })
      break
    default:
      console.log("command :>> ", command)
      break
  }
}

const qrCodeDialog = ref({
  visible: false,
  title: "",
  tenantId: "",
  link: "",
  logo: ""
})

// 生成租户二级域名跳转链接
const genTenantLink = (tenantId: string) => {
  return `http://${tenantId}.${import.meta.env.VITE_TENANT_LINK}`
}

// 删除
const handleDelete = (data: ISysTenant) => {
  ElMessageBox.prompt(`请在下方的输入框中填写租户ID`, `确要删除 ${data.name} 吗?`, {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /^\w{4,12}$/,
    inputErrorMessage: "租户ID可以是字母和数字，长度4~12位",
    type: "warning"
  })
    .then((value) => {
      if (value.value === data.tenantId) {
        deleteTenantApi(data.tenantId)
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
      } else {
        ElMessage({
          message: `租户ID输入错误！`,
          type: "error"
        })
      }
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 重置数据
const handleResetData = (data: ISysTenant) => {
  ElMessageBox.prompt(`请在下方的输入框中填写租户ID`, `确要重置 ${data.name} 的数据吗?`, {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /^\w{4,12}$/,
    inputErrorMessage: "租户ID可以是字母和数字，长度4~12位",
    type: "warning"
  })
    .then((value) => {
      if (value.value === data.tenantId) {
        resetDataTenantApi(data.tenantId)
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
      } else {
        ElMessage({
          message: `租户ID输入错误！`,
          type: "error"
        })
      }
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 修改
const handleUpdate = () => {
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      updateTenantApi(entityForm.value)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: "修改租户成功！",
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
      return false
    }
  })
}

// 新增
const closeCreateTenant = () => {
  dialogVisible.value = false
  getTableData()
}

onMounted(() => {
  getTableData()
  getListTemplateData()
})
</script>

<style lang="scss" scoped>
.el-tag {
  margin: 2px;
}
</style>
