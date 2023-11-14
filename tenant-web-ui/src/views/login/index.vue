<script lang="ts" setup>
import { reactive, ref } from "vue"
import { useRouter, useRoute } from "vue-router"
import { useUserStore } from "@/store/modules/user"
import { type FormInstance, type FormRules } from "element-plus"
import { User, Lock, Key, Picture, Loading } from "@element-plus/icons-vue"
import ThemeSwitch from "@/components/ThemeSwitch/index.vue"
import { v4 as uuidv4 } from "uuid"
import { getEnvBaseURL } from "@/utils"
import {
  getUserAndPassword,
  setUserAndPassword,
  removeUserAndPassword,
  setLastTenantId,
  getLastTenantId
} from "@/utils/cache/local-storage"
import { type ILoginData } from "@/types/pms"
import { merge, clone } from "lodash-es"
import { useProjectStoreHook } from "@/store/modules/project"
import { getSimpleTenantApi } from "@/api/tenant"
import { ISimpleTenant } from "@/types/base"
import logoPng from "@/assets/layouts/logo.png?url"

const router = useRouter()

/** 登录表单元素的引用 */
const loginFormRef = ref<FormInstance | null>(null)

/** 登录按钮 Loading */
const loading = ref(false)

/** 标题 */
const appTitle = import.meta.env.VITE_APP_TITLE

/** 验证码图片 URL */
const codeUrl = ref("")

const target: ILoginData = {
  tenantId: "",
  username: "",
  password: "",
  codeKey: uuidv4().replace(/-/g, ""),
  codeText: "",
  rememberMe: false
}
const source = getUserAndPassword()
if (source) {
  source.codeKey = target.codeKey
  source.codeText = ""
  merge(target, source)
}
/** 登录表单数据 */
const loginFormData: ILoginData = reactive(target)

/** 登录表单校验规则 */
const loginFormRules: FormRules = {
  tenantId: [
    { required: true, message: "请输入租户ID", trigger: "blur" },
    { min: 4, max: 12, message: "长度在 4 到 12 个字符", trigger: "blur" }
  ],
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 5, max: 32, message: "长度在 5 到 32 个字符", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, max: 32, message: "长度在 5 到 32 个字符", trigger: "blur" }
  ],
  codeText: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 4, max: 4, message: "长度在 4 个字符", trigger: "blur" }
  ]
}

/** 登录逻辑 */
const handleLogin = () => {
  loginFormRef.value?.validate((valid: boolean, fields) => {
    if (valid) {
      loading.value = true
      useUserStore()
        .userLogin(loginFormData)
        .then(() => {
          // 获取 上一次登录的租户Id。如果和本次不同，清空项目缓存
          if (getLastTenantId() !== loginFormData.tenantId) {
            // 将项目，重置为 默认项目
            useProjectStoreHook().resetCurrentProject()
          }
          // 登录成功后，是否记住密码 ?
          if (loginFormData.rememberMe) {
            const saveUserInfo = clone(loginFormData)
            setUserAndPassword(saveUserInfo)
          } else {
            removeUserAndPassword()
          }
          setLastTenantId(loginFormData.tenantId)
          router.push({ path: "/" })
        })
        .catch(() => {
          createCode()
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      console.error("表单校验不通过", fields)
    }
  })
}

/** 创建验证码 */
const createCode = () => {
  // 先清空验证码的输入
  loginFormData.codeText = ""
  // 获取验证码
  codeUrl.value = `${getEnvBaseURL()}/auth/captcha/codes?codeKey=${loginFormData.codeKey}&r=${Math.random()}`
}

/** 判断租户ID 是否存在 */
const getSimpleTenant = async (tenantId: string) => {
  const { data } = await getSimpleTenantApi(tenantId)
  return data
}

/** 初始化验证码 */
createCode()

const route = useRoute()
const tenantId = ref()

if (route.query.tenantId) {
  // 从 URL 参数中获取 租户ID http://molly.xaaef.com/tenant/#/login?tenantId=google
  tenantId.value = route.query.tenantId as string
} else {
  // 如果 URL 参数中没有，那么就从 域名前缀截取
  // 如 http://google.molly.xaaef.com/tenant/#/login 截取 google
  const domain = location.hostname
  const idx1 = domain.indexOf(".")
  tenantId.value = domain.substring(0, idx1)
}

const simpleTenant = ref<ISimpleTenant>()

if (tenantId.value) {
  console.log("tenantId :>>", tenantId.value)
  getSimpleTenant(tenantId.value).then((data) => {
    if (data) {
      target.tenantId = tenantId.value
      simpleTenant.value = data
    } else {
      tenantId.value = null
    }
  })
}
</script>

<template>
  <div class="login-container">
    <ThemeSwitch class="theme-switch" />
    <div class="login-card">
      <div class="head">
        <div class="img">
          <el-avatar v-if="simpleTenant" :size="80" :src="simpleTenant.logo" />
          <el-avatar v-else :size="80" :src="logoPng" />
        </div>
        <div class="title">
          <template v-if="simpleTenant">
            {{ appTitle.replaceAll("Molly", `${simpleTenant.name} `) }}
          </template>
          <template v-else>
            {{ appTitle }}
          </template>
        </div>
      </div>
      <div class="content">
        <el-form ref="loginFormRef" :model="loginFormData" :rules="loginFormRules" @keyup.enter="handleLogin">
          <el-form-item prop="tenantId" v-if="!tenantId">
            <el-input
              v-model.trim="loginFormData.tenantId"
              placeholder="租户Id"
              type="text"
              tabindex="1"
              size="large"
              clearable
            >
              <template #prefix>
                <SvgIcon name="peoples" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="username">
            <el-input
              v-model.trim="loginFormData.username"
              placeholder="用户名"
              type="text"
              tabindex="2"
              :prefix-icon="User"
              size="large"
              clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model.trim="loginFormData.password"
              placeholder="密码"
              type="password"
              tabindex="3"
              :prefix-icon="Lock"
              size="large"
              clearable
              show-password
            />
          </el-form-item>
          <el-form-item prop="codeText">
            <el-input
              v-model.trim="loginFormData.codeText"
              placeholder="验证码"
              type="text"
              tabindex="4"
              :prefix-icon="Key"
              maxlength="4"
              size="large"
              clearable
            >
              <template #append>
                <el-image :src="codeUrl" @click="createCode" class="code-url" draggable="false">
                  <template #placeholder>
                    <el-icon>
                      <Picture />
                    </el-icon>
                  </template>
                  <template #error>
                    <el-icon>
                      <Loading />
                    </el-icon>
                  </template>
                </el-image>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="rememberMe">
            <el-checkbox v-model="loginFormData.rememberMe" label="记住密码" size="large" />
          </el-form-item>
          <el-button :loading="loading" type="primary" size="large" @click.prevent="handleLogin">登 录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 100%;
  background: url("@/assets/login-bg1.svg") no-repeat;

  .theme-switch {
    position: fixed;
    top: 5%;
    right: 5%;
    cursor: pointer;
  }

  .login-card {
    width: 430px;
    border-radius: 5px;
    box-shadow: 0 0 10px #dcdfe6;
    background-color: #fff;
    overflow: hidden;

    .head {
      text-align: center;

      .img {
        margin: 10px 0px;
      }

      .title {
        font-weight: 600;
        font-size: larger;
        margin-bottom: 10px;
      }
    }

    .content {
      padding: 20px 30px 30px 30px;

      :deep(.el-input-group__append) {
        padding: 0;
        overflow: hidden;

        .el-image {
          width: 100px;
          height: 40px;
          border-left: 0px;
          user-select: none;
          cursor: pointer;
          text-align: center;
        }
      }

      .el-button {
        width: 100%;
        margin-top: 10px;
      }
    }
  }

  .code-url {
    border: #dcdfe6 2px solid;
  }
}
</style>
