<template>
  <div class="app-container1">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="basic-info">
            <div class="text-center">
              <user-avatar :src="userInfoForm.avatar" @change="getAvatar" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <el-icon><Connection /></el-icon>
                用户ID
                <div class="pull-right">{{ userInfoForm.userId }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Filter /></el-icon>
                昵称
                <div class="pull-right">{{ userInfoForm.nickname }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><User /></el-icon>
                用户名
                <div class="pull-right">{{ userInfoForm.username }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Calendar /></el-icon>
                邮箱
                <div class="pull-right">{{ userInfoForm.email }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Phone /></el-icon>
                电话
                <div class="pull-right">{{ userInfoForm.mobile }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Brush /></el-icon>
                角色
                <div class="pull-right">
                  <el-tag v-for="(role, index) in userInfoForm.roles" :key="index" type="success"
                    >{{ role.description }}
                  </el-tag>
                </div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>基本资料</span>
            </div>
          </template>
          <div class="basic-info">
            <el-tabs v-model="activeName" class="demo-tabs">
              <el-tab-pane label="基本资料" name="first">
                <el-form
                  ref="userInfoFormRef"
                  :model="userInfoForm"
                  :rules="userInfoFormRules"
                  label-width="80px"
                  v-loading="loading"
                  class="user-form"
                >
                  <el-form-item label="昵称" prop="nickname">
                    <el-input v-model.trim="userInfoForm.nickname" placeholder="昵称" tabindex="1" />
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model.trim="userInfoForm.email" placeholder="邮箱" tabindex="2" />
                  </el-form-item>
                  <el-form-item label="手机号" prop="mobile">
                    <el-input
                      v-model.trim="userInfoForm.mobile"
                      placeholder="手机号"
                      type="text"
                      maxlength="11"
                      show-word-limit
                      tabindex="3"
                    />
                  </el-form-item>
                  <el-form-item label="性别" prop="gender">
                    <select-dict-data v-model:value="userInfoForm.gender" dictTypeKey="sys_user_sex" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onUpdateUserInfo">立即修改</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="second">
                <el-form
                  ref="updatePwdFormRef"
                  :model="updatePwdForm"
                  :rules="updatePwdFormRules"
                  label-width="80px"
                  v-loading="loading"
                  class="user-form"
                >
                  <el-form-item label="旧密码" prop="oldPwd">
                    <el-input v-model="updatePwdForm.oldPwd" show-password />
                  </el-form-item>
                  <el-form-item label="新密码" prop="newPwd">
                    <el-input v-model="updatePwdForm.newPwd" show-password />
                  </el-form-item>
                  <el-form-item label="确认密码" prop="confirmPwd">
                    <el-input v-model="updatePwdForm.confirmPwd" show-password />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onUpdatePassword">立即修改</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup name="Personal">
import { useUserStore } from "@/store/modules/user"
import UserAvatar from "@/components/UserAvatar/index.vue"
import { ref, reactive } from "vue"
import { type FormInstance, FormRules, ElMessageBox } from "element-plus"
import { updatePasswordApi, updateUserInfoApi } from "@/api/user"
import { testEmail, testPassword, testPhone } from "@/utils/validate"
import { useRouter } from "vue-router"
import { ILoginUserInfo } from "@/types/pms"
const router = useRouter()

const activeName = ref("first")

const loading = ref(false)

const userStore = useUserStore()

const userInfoFormRef = ref<FormInstance | null>(null)

const userInfoForm = reactive<ILoginUserInfo>(userStore.userInfo!)

const emailValidator = (rule: any, value: any, callback: any) => {
  if (!testEmail(value)) {
    callback(new Error("邮箱格式不正确!"))
  } else {
    callback()
  }
}

const phoneValidator = (rule: any, value: any, callback: any) => {
  if (!testPhone(value)) {
    callback(new Error("手机号码格式不正确!"))
  } else {
    callback()
  }
}

/** 登录表单校验规则 */
const userInfoFormRules: FormRules = {
  nickname: [
    { required: true, message: "请输入用户昵称", trigger: "blur" },
    { min: 5, max: 32, message: "长度大于 5 个字符", trigger: "blur" }
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { validator: emailValidator, trigger: "blur" }
  ],
  mobile: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { validator: phoneValidator, trigger: "blur" }
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "blur" }]
}

const getAvatar = (data: string) => {
  userInfoForm.avatar = data
}

const onUpdateUserInfo = () => {
  userInfoFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      ElMessageBox.confirm("确定要修改信息吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          loading.value = true
          const params = {
            userId: userInfoForm.userId,
            username: userInfoForm.username,
            mobile: userInfoForm.mobile,
            avatar: userInfoForm.avatar,
            nickname: userInfoForm.nickname,
            gender: userInfoForm.gender,
            email: userInfoForm.email
          }
          updateUserInfoApi(params)
            .then(() => userStore.getUserInfo())
            .catch((err) => {
              console.log("err", err)
            })
            .finally(() => {
              loading.value = false
            })
        })
        .catch(() => {
          loading.value = false
        })
    } else {
      loading.value = false
      return false
    }
  })
}

//-------------- 修改密码 ---------------

const updatePwdFormRef = ref<FormInstance | null>(null)

const updatePwdForm = reactive({
  oldPwd: "",
  newPwd: "",
  confirmPwd: ""
})

const newPwdValidator = (rule: any, value: string, callback: any) => {
  if (!testPassword(value)) {
    callback(new Error("密码必须包含字母和数字!"))
  } else {
    callback()
  }
}

const confirmPwdValidator = (rule: any, value: string, callback: any) => {
  if (!testPassword(value)) {
    callback(new Error("密码必须包含字母和数字!"))
  } else {
    if (value === updatePwdForm.newPwd) {
      callback()
    } else {
      callback(new Error("两次密码不相同!"))
    }
  }
}

const updatePwdFormRules: FormRules = {
  oldPwd: [
    { required: true, message: "请输入旧的密码", trigger: "blur" },
    { min: 5, max: 32, message: "长度在 5 个字符", trigger: "blur" }
  ],
  newPwd: [
    { required: true, message: "请输入新的密码", trigger: "blur" },
    { validator: newPwdValidator, trigger: "blur" }
  ],
  confirmPwd: [
    { required: true, message: "请输入确认密码", trigger: "blur" },
    { validator: confirmPwdValidator, trigger: "blur" }
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "blur" }]
}

const onUpdatePassword = () => {
  updatePwdFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      ElMessageBox.confirm("确定要修改密码吗? 修改密码会导致重新登录！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const params = {
            userId: userInfoForm.userId,
            oldPwd: updatePwdForm.oldPwd,
            newPwd: updatePwdForm.newPwd,
            confirmPwd: updatePwdForm.confirmPwd
          }
          updatePasswordApi(params)
            .then(async () => {
              await userStore.userLogout()
              router.push("/login")
            })
            .catch((err) => {
              console.log("err", err)
            })
            .finally(() => {
              loading.value = false
            })
        })
        .catch(() => {
          loading.value = false
        })
    } else {
      loading.value = false
      return false
    }
  })
}
</script>

<style lang="scss" scoped>
.list-group {
  padding-left: 0;
  list-style: none;
}

.list-group-striped {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;

  .svg-icon {
    margin-right: 10px;
  }

  i {
    margin-right: 10px;
  }
}

.pull-right {
  float: right !important;
}

.text-center {
  text-align: center;
}

.basic-info {
  height: 500px;
}

.user-form {
  .el-input {
    width: 300px;
  }

  .el-select {
    width: 300px;
  }
}
</style>
