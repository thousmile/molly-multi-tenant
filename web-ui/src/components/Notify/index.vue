<script lang="ts" setup>
import { ref, computed } from "vue"
import { ElMessage } from "element-plus"
import { Bell } from "@element-plus/icons-vue"
import { useNoticeStoreHook } from "@/store/modules/notice"

type TabNameType = "广播" | "推送"

const noticeStore = useNoticeStoreHook()

/** 角标当前值 */
const badgeValue = computed(() => {
  let value = 0
  value += noticeStore.broadcast.length
  value += noticeStore.pushNotices.length
  return value
})

/** 角标最大值 */
const badgeMax = 99

/** 面板宽度 */
const popoverWidth = 350

/** 当前 Tab */
const activeName = ref<TabNameType>("广播")

noticeStore.getPushNotices

/** 所有数据 */
const data = ref<any[]>([
  // 通知数据
  {
    name: "广播",
    type: "primary",
    list: noticeStore.getBroadcast
  },
  // 消息数据
  {
    name: "推送",
    type: "danger",
    list: noticeStore.getPushNotices
  }
])

const handleHistory = () => {
  ElMessage.success(`跳转到${activeName.value}历史页面`)
}
</script>

<template>
  <div class="notify">
    <el-popover placement="bottom" :width="popoverWidth" trigger="click">
      <template #reference>
        <el-badge :value="badgeValue" :max="badgeMax" :hidden="badgeValue === 0">
          <el-tooltip effect="dark" content="消息通知" placement="bottom">
            <el-icon :size="20">
              <Bell />
            </el-icon>
          </el-tooltip>
        </el-badge>
      </template>
      <template #default>
        <el-tabs v-model="activeName" class="demo-tabs" stretch>
          <el-tab-pane v-for="(item, index) in data" :name="item.name" :key="index">
            <template #label>
              {{ item.name }}
              <el-badge :value="item.list.length" :max="badgeMax" :type="item.type" />
            </template>
            <el-scrollbar height="400px">
              <el-empty v-if="item.list.length === 0" />
              <ul v-else class="list">
                <li v-for="(item1, index1) in item.list" :key="index1" class="list-item">
                  <strong>{{ item1.id }}</strong>
                  &nbsp;&nbsp;&nbsp;
                  <span>{{ item1.msg }}</span>
                  <div>{{ item1.dataArr }}</div>
                </li>
              </ul>
            </el-scrollbar>
          </el-tab-pane>
        </el-tabs>
        <div class="notify-history">
          <el-button link @click="handleHistory">查看{{ activeName }}历史</el-button>
        </div>
      </template>
    </el-popover>
  </div>
</template>

<style lang="scss" scoped>
.notify {
  margin-right: 10px;
  color: var(--el-text-color-regular);
}
.notify-history {
  text-align: center;
  padding-top: 12px;
  border-top: 1px solid var(--el-border-color);
}

.list {
  padding: 0;
  margin: 0;
  list-style: none;

  .list-item {
    justify-content: center;
    align-items: center;
    height: 50px;
    background: var(--el-color-danger-light-9);
    color: var(--el-color-danger);
    padding: 5px;
    border-radius: 5px;
  }

  .list-item + .list-item {
    margin-top: 10px;
  }
}
</style>
