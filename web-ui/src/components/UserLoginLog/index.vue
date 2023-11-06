<template>
  <el-form v-loading="loading" :model="entityForm" label-position="right" label-width="80px">
    <el-form-item label="登录地址">
      {{ entityForm.address }}
    </el-form-item>
    <el-form-item label="登录设备">
      {{ entityForm.osName }}
      {{ entityForm.browser }}
    </el-form-item>
    <el-form-item label="请求IP">
      {{ entityForm.requestIp }}
    </el-form-item>
    <el-form-item label="登录时间">
      {{ entityForm.createTime }}
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts" name="UserLoginLog">
import { onMounted, watch, ref } from "vue"
import { getLoginLogApi } from "@/api/loginLog"
import { ILoginLog } from "@/types/lms"

const props = defineProps({
  value: {
    type: String,
    required: true
  }
})

const loading = ref(false)

/// 表单数据
const entityForm = ref<ILoginLog>({
  /* ID */
  id: "",
  /*授权类型*/
  grantType: "",
  /*用户ID*/
  userId: 0,
  /*用户昵称*/
  nickname: "",
  /*用户名*/
  username: "",
  /*头像*/
  avatar: "",
  /*请求地址*/
  requestUrl: "",
  /*请求IP*/
  requestIp: "",
  /*请求地址*/
  address: "",
  /*操作系统*/
  osName: "",
  /*浏览器*/
  browser: "",
  /*创建时间*/
  createTime: ""
})

const getLoginLogData = (loginId: String) => {
  loading.value = true
  getLoginLogApi(loginId)
    .then((resp) => {
      entityForm.value = resp.data
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
    .finally(() => {
      loading.value = false
    })
}

watch(props, (newValue, _) => {
  console.log("新的值: ", newValue.value)
  getLoginLogData(newValue.value)
})

onMounted(() => {
  getLoginLogData(props.value)
})
</script>

<style lang="scss" scoped></style>
