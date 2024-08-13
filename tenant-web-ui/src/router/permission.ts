import router from "@/router"
import {useUserStoreHook} from "@/store/modules/user"
import {useDictStoreHook} from "@/store/modules/dict"
import {usePermissionStoreHook} from "@/store/modules/permission"
import {useNoticeStoreHook} from "@/store/modules/notice"

import {ElMessage} from "element-plus"
import {useTitle} from "@/hooks/useTitle"
import {getToken} from "@/utils/cache/local-storage"
import {fixBlankPage} from "@/utils/fix-blank-page"
import {setRouteChange} from "@/hooks/useRouteListener"
import {isWhiteList, loginUrl} from "@/config/white-list"
import NProgress from "nprogress"
import "nprogress/nprogress.css"
import {useTenantStoreHook} from "@/store/modules/tenant"

const { setTitle } = useTitle()
NProgress.configure({ showSpinner: false })

const userStore = useUserStoreHook()
const dictStore = useDictStoreHook()
const noticeStore = useNoticeStoreHook()
const tenantStore = useTenantStoreHook()
const permissionStore = usePermissionStoreHook()

router.beforeEach(async (to, _from, next) => {
  fixBlankPage()
  NProgress.start()

  const token = getToken()
  // 判断该用户是否已经登录
  if (!token) {
    // 如果在免登录的白名单中，则直接进入
    if (isWhiteList(to)) {
      next()
    } else {
      // 其他没有访问权限的页面将被重定向到登录页面
      NProgress.done()
      next(loginUrl)
    }
    return
  }

  // 如果已经登录，并准备进入 Login 页面，则重定向到主页
  if (to.path === loginUrl) {
    NProgress.done()
    return next({ path: "/" })
  }

  // 如果用户已经获得其权限
  if (userStore.menus.length !== 0) {
    return next()
  } else {
    // 否则要重新获取 用户权限菜单
    try {
      // 初始化数据
      await initBasicData()
      // 检查用户是否已获得其权限菜单
      if (userStore.menus.length === 0) {
        await userStore.getUserPerms()
        // 根据 用户权限菜单 生成可访问的 Routes（可访问路由 = 常驻路由 + 有访问权限的动态路由）
        permissionStore.setRoutes(userStore.menus)
        // 将'有访问权限的动态路由' 添加到 Router 中
        permissionStore.dynamicRoutes.forEach((route) => router.addRoute(route))
        // 确保添加路由已完成
        // 设置 replace: true, 因此导航将不会留下历史记录
        next({ ...to, replace: true })
      } else {
        next()
      }
    } catch (err: any) {
      // 过程中发生任何错误，都直接重置 Token，并重定向到登录页面
      userStore.fedLogout()
      ElMessage.error(err.message || "路由守卫过程发生错误")
      NProgress.done()
      next(loginUrl)
    }
  }
})

router.afterEach((to) => {
  setRouteChange(to)
  setTitle(to.meta.title)
  NProgress.done()
})

// 初始化数据,如: 用户信息，数据字典，全局配置
async function initBasicData() {
  // 获取用户信息
  if (userStore.userInfo === undefined || !userStore.userInfo) {
    await userStore.getUserInfo()
    await tenantStore.getLoginUserTenant()
  }

  // 获取字典数据
  if (dictStore.dictMapKeys === undefined || !dictStore.dictMapKeys) {
    await dictStore.getDictMapKeys()
  }

  // 启动 WebSocket
  if (!noticeStore.getStompClientActive()) {
    noticeStore.startWebSocket()
  }
}
