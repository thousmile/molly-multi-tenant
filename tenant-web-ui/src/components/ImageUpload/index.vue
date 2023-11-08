<template>
  <div class="avatar-wrapper">
    <el-upload
      class="avatar-uploader"
      :action="getAction"
      :headers="myHeaders"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      accept=".jpg,.jpeg,.png,.gif"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
  </div>
</template>

<script setup lang="ts" name="UserAvatar">
import { ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { Plus } from "@element-plus/icons-vue"
import type { UploadProps } from "element-plus"
import { deleteFile } from "@/api/upload"
import { getEnvBaseURLPrefix } from "@/utils"
import { getToken } from "@/utils/cache/cookies"
import { useUserStoreHook } from "@/store/modules/user"

const props = defineProps({
  src: {
    type: String,
    default: ""
  }
})

// 基于类型
const emit = defineEmits<{
  (e: "change", id: string): void
}>()

const imageUrl = ref<string>("")

onMounted(() => {
  imageUrl.value = props.src!
})

// 删除旧的图片
const deleteOldFile = (url: string) => {
  deleteFile(url).then((result) => console.log("deleteOldFile :", result.data))
}

// 获取上传地址
const getAction = `${getEnvBaseURLPrefix()}/upload/image?width=100&height=100`

const tenantId = useUserStoreHook().userInfo?.tenantId

// 请求头
const myHeaders = {
  Authorization: getToken(),
  "x-tenant-id": tenantId
}

const handleAvatarSuccess: UploadProps["onSuccess"] = (response, _) => {
  if (response.status === 200) {
    const resp = response.data
    // 删除旧的头像
    if (imageUrl.value) {
      deleteOldFile(imageUrl.value)
    }
    // 将新的头像赋值
    imageUrl.value = resp.url
    emit("change", imageUrl.value)
  } else {
    ElMessage.error(response.message)
  }
}

const regular = new RegExp("(image/jpg|image/jpeg|image/png|image/gif|)")

const beforeAvatarUpload: UploadProps["beforeUpload"] = (rawFile) => {
  if (!regular.test(rawFile.type)) {
    ElMessage.error("头像只能是[ jpeg , png , jpg , gif ]的图片格式")
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("头像图片大小不能超过2MB")
    return false
  }
  return true
}
</script>

<style lang="scss" scoped>
$size: 100px;
.avatar-uploader .el-upload {
  border: 1px solid #78f00e;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-wrapper {
  border: 2px solid #8c939d;
  border-radius: 3px;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: $size;
  height: $size;
  line-height: $size;
  text-align: center;
}

.avatar {
  width: $size;
  height: $size;
  display: block;
  border-radius: 5px;
}
</style>
