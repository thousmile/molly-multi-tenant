<template>
  <el-steps :active="active" finish-status="success">
    <el-step title="租户信息" />
    <el-step title="管理员信息" />
    <el-step title="结果" />
  </el-steps>
  <br />
  <br />
  <el-form ref="entityFormRef" :model="entityForm" :rules="entityFormRules" label-position="right" label-width="100px">
    <template v-if="active === 0">
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
              type="text"
              tabindex="1"
              onkeyup="value=value.replace(/[^\w]/g,'')"
              maxlength="12"
              show-word-limit
            >
              <template #append>
                <el-button :icon="Refresh" @click="refreshTenantId" />
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" />
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="name" label="名称">
            <el-input v-model.trim="entityForm.name" clearable placeholder="租户名称" type="text" tabindex="2" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="email" label="邮箱">
            <el-input v-model.trim="entityForm.email" clearable placeholder="租户邮箱" type="text" tabindex="3" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="linkman" label="联系人">
            <el-input v-model="entityForm.linkman" clearable placeholder="租户联系人" type="text" tabindex="4" />
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
            <el-input v-model="entityForm.address" clearable placeholder="租户联系地址" type="text" tabindex="6" />
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
            />
          </el-form-item>
        </el-col>
      </el-row>
    </template>

    <template v-else-if="active === 1">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="adminUsername" label="用户名">
            <el-input v-model.trim="entityForm.adminUsername" placeholder="管理员用户名" type="text" tabindex="1">
              <template #append>
                <el-button :icon="Refresh" @click="refreshUsername" />
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="adminPwd" label="密码">
            <el-input v-model.trim="entityForm.adminPwd" placeholder="管理员密码" type="text" tabindex="2" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="adminEmail" label="邮箱">
            <el-input v-model.trim="entityForm.adminEmail" placeholder="管理员邮箱" type="text" tabindex="3" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="adminMobile" label="手机号">
            <el-input v-model.trim="entityForm.adminMobile" placeholder="管理员手机号" type="text" tabindex="4" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="adminNickname" label="昵称">
            <el-input v-model.trim="entityForm.adminNickname" placeholder="管理员昵称" type="text" tabindex="5" />
          </el-form-item>
        </el-col>
        <el-col :span="12" />
      </el-row>
    </template>
    <template v-else>
      <el-form-item prop="adminNickname" label="管理员昵称">
        {{ adminResult.adminNickname }}
      </el-form-item>
      <el-form-item prop="adminUsername" label="登录用户名">
        {{ adminResult.adminUsername }}
      </el-form-item>
      <el-form-item prop="adminPwd" label="登录密码">
        {{ adminResult.adminPwd }}
      </el-form-item>
      <el-form-item prop="adminEmail" label="管理员邮箱">
        {{ adminResult.adminEmail }}
      </el-form-item>
      <el-form-item prop="adminMobile" label="管理员手机号">
        {{ adminResult.adminMobile }}
      </el-form-item>
    </template>
  </el-form>
  <br />
  <el-button-group :style="{ float: 'right' }">
    <template v-if="active === 0">
      <el-button type="primary" @click="next">
        下一步<el-icon class="el-icon--right"><ArrowRight /></el-icon>
      </el-button>
    </template>
    <template v-if="active === 1">
      <el-button type="info" :icon="ArrowLeft" @click="previous" v-loading="loading">上一步</el-button>
      <el-button type="primary" @click="next" v-loading="loading">
        确定 <el-icon class="el-icon--right"><Check /></el-icon>
      </el-button>
    </template>
    <template v-if="active === 2">
      <el-button type="primary" @click="close">
        关闭<el-icon class="el-icon--right"><Close /></el-icon>
      </el-button>
    </template>
  </el-button-group>
  <br />
  <br />
</template>

<script setup lang="ts">
import { ElMessage, FormInstance, FormRules } from "element-plus"
import { ref, onMounted, computed, toRefs } from "vue"
import { ISysTemplate, ICreateTenant, ICreateTenantAdmin } from "@/types/sys"
import { futureShortcuts } from "@/utils"
import { saveTenantApi } from "@/api/tenant"
import { Refresh, ArrowLeft, ArrowRight, Check, Close } from "@element-plus/icons-vue"
import ImageUpload from "@/components/ImageUpload/index.vue"
import SearchChinaArea from "@/components/SearchChinaArea/index.vue"
import { v4 as uuidv4 } from "uuid"
import { testEmail, testTelphone } from "@/utils/validate"

// 随机生成租户ID
const randomTenantId = () => {
  return uuidv4().split("-")[4]
}

/** 加载 */
const loading = ref(false)

const active = ref(0)

const props = defineProps({
  templateList: {
    type: Array<ISysTemplate>,
    required: true
  }
})

const { templateList } = toRefs(props)

// 基于类型
const emit = defineEmits<{
  (e: "close"): void
}>()

/// 表单数据
const entityForm = ref<ICreateTenant>({
  tenantId: randomTenantId(),
  logo: "",
  name: "",
  email: "",
  linkman: "",
  contactNumber: "",
  areaCode: 0,
  address: "",
  templates: [],
  expired: "",
  adminNickname: "",
  adminUsername: "",
  adminMobile: "",
  adminEmail: "",
  adminPwd: ""
})

/// 表单数据
const adminResult = ref<ICreateTenantAdmin>({
  adminNickname: "",
  adminUsername: "",
  adminMobile: "",
  adminEmail: "",
  adminPwd: ""
})

const entityFormRef = ref<FormInstance | null>(null)

const tenantIdValidator = (rule: any, value: any, callback: any) => {
  if (!/\w{4,12}$/.test(value)) {
    callback(new Error("只能是字母和数字，长度4~12位!"))
  } else {
    callback()
  }
}

const emailValidator = (rule: any, value: any, callback: any) => {
  if (!testEmail(value)) {
    callback(new Error("邮箱格式不正确!"))
  } else {
    callback()
  }
}

const telphoneValidator = (rule: any, value: any, callback: any) => {
  if (!testTelphone(value)) {
    callback(new Error("手机或者电话号码格式不正确!"))
  } else {
    callback()
  }
}

/// 表单校验规则
const entityFormRules: FormRules = {
  tenantId: [
    { required: true, message: "请输入租户ID", trigger: "blur" },
    { validator: tenantIdValidator, trigger: "blur" }
  ],
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

// 重置 Entity 属性
const resetEntity = () => {
  active.value = 0
  entityForm.value = {
    tenantId: randomTenantId(),
    logo: "",
    name: "",
    email: "",
    linkman: "",
    contactNumber: "",
    areaCode: 0,
    address: "",
    templates: [],
    expired: "",
    adminNickname: "",
    adminUsername: "",
    adminMobile: "",
    adminEmail: "",
    adminPwd: ""
  }
  adminResult.value = {
    adminNickname: "",
    adminUsername: "",
    adminMobile: "",
    adminEmail: "",
    adminPwd: ""
  }
}

const showStringOverflow = computed(() => {
  return (value: string) => (value.length <= 20 ? value : value.substring(0, 20))
})

const getLogoSrc = (data: string) => {
  entityForm.value.logo = data
}

const refreshTenantId = () => {
  entityForm.value.tenantId = randomTenantId()
}

const refreshUsername = () => {
  entityForm.value.adminUsername = randomTenantId()
}

const next = () => {
  if (active.value === 0) {
    entityFormRef.value?.validate((valid: boolean) => {
      if (valid) {
        active.value++
        return
      }
    })
  }
  if (active.value === 1) {
    entityFormRef.value?.validate((valid: boolean) => {
      if (valid) {
        loading.value = true
        saveTenantApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "添加租户成功！",
                type: "success"
              })
              adminResult.value = resp.data
            }
          })
          .catch((err) => {
            console.log("err :>> ", err)
          })
          .finally(() => {
            loading.value = false
            active.value++
          })
      }
    })
  }
}

const previous = () => {
  if (active.value > 0) {
    active.value--
  }
}

const close = () => {
  resetEntity()
  emit("close")
}

onMounted(() => {
  console.log("3.-组件挂载到页面之后执行")
})
</script>

<style lang="scss" scoped></style>
