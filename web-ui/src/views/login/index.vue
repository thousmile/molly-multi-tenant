<script lang="ts" setup>
import { reactive, ref } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/store/modules/user"
import { User, Lock, Key, Picture, Loading } from "@element-plus/icons-vue"
import ThemeSwitch from "@/components/ThemeSwitch/index.vue"
import { type FormInstance, FormRules } from "element-plus"
import { v4 as uuidv4 } from "uuid"
import { getEnvBaseURL } from "@/utils"
import { ILoginData } from "@/types/pms"

const router = useRouter()
const loginFormRef = ref<FormInstance | null>(null)

/** 登录按钮 Loading */
const loading = ref(false)

/** 验证码图片 URL */
const codeUrl = ref("")

/** 登录表单数据 */
const loginForm: ILoginData = reactive({
  username: "admin",
  password: "a1234567",
  codeKey: uuidv4().replace(/-/g, ""),
  codeText: ""
})

/** 登录表单校验规则 */
const loginFormRules: FormRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 5, max: 32, message: "长度在 4 个字符", trigger: "blur" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, max: 32, message: "长度最少 5 个字符", trigger: "blur" }
  ],
  codeText: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 4, max: 4, message: "长度在 4 个字符", trigger: "blur" }
  ]
}

/** 登录逻辑 */
const handleLogin = () => {
  loginFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      const params: ILoginData = {
        username: loginForm.username,
        password: loginForm.password,
        codeKey: loginForm.codeKey,
        codeText: loginForm.codeText
      }
      useUserStore()
        .userLogin(params)
        .then(() => router.push({ path: "/" }))
        .catch((err) => {
          console.log("err", err)
          createCode()
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      loading.value = false
    }
  })
}

/** 创建验证码 */
const createCode = () => {
  // 先清空验证码的输入
  loginForm.codeText = ""
  // 获取验证码
  codeUrl.value = `${getEnvBaseURL()}/auth/captcha/codes?codeKey=${loginForm.codeKey}&r=${Math.random()}`
}

/** 初始化验证码 */
createCode()
</script>

<template>
  <div class="login-container">
    <ThemeSwitch class="theme-switch" />
    <div class="login-card">
      <div class="title">
        <img src="@/assets/layout/logo.png" />
      </div>
      <div class="content">
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" @keyup.enter="handleLogin">
          <el-form-item prop="username">
            <el-input
              v-model.trim="loginForm.username"
              placeholder="用户名"
              type="text"
              tabindex="1"
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model.trim="loginForm.password"
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
              v-model.trim="loginForm.codeText"
              placeholder="验证码"
              type="text"
              tabindex="3"
              :prefix-icon="Key"
              maxlength="4"
              size="large"
            >
              <template #append>
                <el-image :src="codeUrl" @click="createCode" draggable="false">
                  <template #placeholder>
                    <el-icon><Picture /></el-icon>
                  </template>
                  <template #error>
                    <el-icon><Loading /></el-icon>
                  </template>
                </el-image>
              </template>
            </el-input>
          </el-form-item>
          <el-button :loading="loading" type="primary" size="large" @click.prevent="handleLogin"> 登 录 </el-button>
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
  .theme-switch {
    position: fixed;
    top: 5%;
    right: 5%;
    cursor: pointer;
  }
  .login-card {
    width: 480px;
    border-radius: 5px;
    box-shadow: 0 0 10px #dcdfe6;
    background-color: #fff;
    overflow: hidden;
    .title {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 150px;
      img {
        height: 80px;
      }
      div {
        text-align: center;
      }
    }
    .content {
      padding: 20px 50px 50px 50px;
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
}
</style>
