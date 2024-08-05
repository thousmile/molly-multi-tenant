<template>
  <el-popover v-if="dateTime && entity" placement="top-start" :width="160" trigger="hover">
    <template #reference>
      <el-link>{{ showTimeAgo(dateTime) }}</el-link>
    </template>
    <template #default>
      <div v-if="entity" class="demo-rich-conent" style="display: flex; gap: 16px; flex-direction: column">
        <el-avatar :src="entity.avatar" style="margin-bottom: 8px" />
        <div>
          <p style="margin: 0; font-size: 16px; font-weight: 700">
            {{ showStringOverflow(title) }}
          </p>
          <p style="margin: 0; font-size: 14px; color: var(--el-color-info)">
            {{ dateTime }}
          </p>
        </div>
      </div>
      <div v-else>
        {{ dateTime }}
      </div>
    </template>
  </el-popover>
  <div v-else></div>
</template>

<script setup lang="ts" name="OperateUser">
import { ref } from "vue"
import { showTimeAgo, showStringOverflow } from "@/hooks/useIndex"
import { IOperateUserEntity } from "@/types/base"

// 操作用户信息
interface IPropsEntity {
  /** 操作时间 */
  dateTime?: string
  /** 操作的用户信息 */
  entity?: IOperateUserEntity
}

const { dateTime, entity } = defineProps<IPropsEntity>()
const title = ref<string>(entity ? entity.nickname : "")
</script>
