<script lang="ts" setup>
import { reactive, ref } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/store/modules/user"
import { type FormInstance, type FormRules } from "element-plus"
import { User, Lock, Key, Picture, Loading } from "@element-plus/icons-vue"
import ThemeSwitch from "@/components/ThemeSwitch/index.vue"
import { v4 as uuidv4 } from "uuid"
import { getEnvBaseURL } from "@/utils"
import { getUserAndPassword, setUserAndPassword, removeUserAndPassword } from "@/utils/cache/local-storage"
import { type ILoginData } from "@/types/pms"
import { merge, clone } from "lodash-es"

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
  username: "admin",
  password: "123456",
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
          // 登录成功后，是否记住密码 ?
          if (loginFormData.rememberMe) {
            const saveUserInfo = clone(loginFormData)
            setUserAndPassword(saveUserInfo)
          } else {
            removeUserAndPassword()
          }
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

/** 初始化验证码 */
createCode()
</script>

<template>
  <div class="login-container">
    <ThemeSwitch class="theme-switch" />
    <div class="login-card">
      <div class="title">
        <strong>{{ appTitle }}</strong>
      </div>
      <div class="content">
        <el-form ref="loginFormRef" :model="loginFormData" :rules="loginFormRules" @keyup.enter="handleLogin">
          <el-form-item prop="username">
            <el-input
              v-model.trim="loginFormData.username"
              placeholder="用户名"
              type="text"
              tabindex="1"
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model.trim="loginFormData.password"
              placeholder="密码"
              type="password"
              tabindex="2"
              :prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-form-item prop="codeText">
            <el-input
              v-model.trim="loginFormData.codeText"
              placeholder="验证码"
              type="text"
              tabindex="3"
              :prefix-icon="Key"
              maxlength="7"
              size="large"
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
  background: url("@/assets/login-bg3.svg") no-repeat;

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

    .title {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 60px;

      img {
        height: 100%;
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
