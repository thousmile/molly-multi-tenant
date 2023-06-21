<script lang="ts" setup>
import { storeToRefs } from "pinia"
import { useAppStore } from "@/store/modules/app"
import { useSettingsStore } from "@/store/modules/settings"
import { useUserStore } from "@/store/modules/user"
import { UserFilled } from "@element-plus/icons-vue"
import Breadcrumb from "../Breadcrumb/index.vue"
import Hamburger from "../Hamburger/index.vue"
import SearchTenant from "../SearchTenant/index.vue"
import SelectControlSize from "../SelectControlSize/index.vue"
import SearchRoute from "../SearchRoute/index.vue"
import ThemeSwitch from "@/components/ThemeSwitch/index.vue"
import Screenfull from "@/components/Screenfull/index.vue"
import Notify from "@/components/Notify/index.vue"
import { ElMessageBox } from "element-plus"
import { jumpToLogin } from "@/router"
import { computed } from "vue"

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const userInfo = userStore.userInfo

const { sidebar } = storeToRefs(appStore)
const { showNotify, showThemeSwitch, showScreenfull, showSearchTenant, showSearchRoute, showControlSize } =
  storeToRefs(settingsStore)

/** 切换侧边栏 */
const toggleSidebar = () => {
  appStore.toggleSidebar(false)
}

/** 登出 */
const logout = () => {
  ElMessageBox.confirm("确定要退出系统?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      userStore.userLogout().then(() => jumpToLogin())
    })
    .catch(() => console.log("取消退出登录.. :>> "))
}

const nickname = computed(() => userInfo?.nickname)
</script>

<template>
  <div class="navigation-bar">
    <Hamburger :is-active="sidebar.opened" class="hamburger" @toggle-click="toggleSidebar" />
    <Breadcrumb class="breadcrumb" />
    <div class="right-menu">
      <SearchTenant v-if="showSearchTenant" class="right-menu-item" />
      <SearchRoute v-if="showSearchRoute" class="right-menu-item" />
      <SelectControlSize v-if="showControlSize" class="right-menu-item" />
      <Screenfull v-if="showScreenfull" class="right-menu-item" />
      <ThemeSwitch v-if="showThemeSwitch" class="right-menu-item" />
      <Notify v-if="showNotify" class="right-menu-item" />
      <el-dropdown class="right-menu-item">
        <div class="right-menu-avatar">
          <el-avatar :src="userInfo?.avatar" :icon="UserFilled" :size="36" />
          <span>{{ nickname }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link :to="{ name: 'Personal' }">
              <el-dropdown-item>个人信息</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided @click="logout">
              <span style="display: block">退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.navigation-bar {
  height: var(--v3-navigationbar-height);
  overflow: hidden;
  background: #fff;

  .hamburger {
    display: flex;
    align-items: center;
    height: 100%;
    float: left;
    padding: 0 15px;
    cursor: pointer;
  }

  .breadcrumb {
    float: left;

    // 参考 Bootstrap 的响应式设计将宽度设置为 576
    @media screen and (max-width: 576px) {
      display: none;
    }
  }

  .right-menu {
    float: right;
    margin-right: 10px;
    height: 100%;
    display: flex;
    align-items: center;
    color: #606266;

    .right-menu-item {
      padding: 0 10px;
      cursor: pointer;

      .right-menu-avatar {
        display: flex;
        align-items: center;

        .el-avatar {
          margin-right: 10px;
        }

        span {
          font-size: 16px;
        }
      }
    }
  }
}
</style>
