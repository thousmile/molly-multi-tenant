import router from "@/router"
import { useUserStoreHook } from "@/store/modules/user"
import { usePermissionStoreHook } from "@/store/modules/permission"
import { useDictStoreHook } from "@/store/modules/dict"
import { ElMessage } from "element-plus"
import { loginUrl, whiteList } from "@/config/white-list"
import NProgress from "nprogress"
import "nprogress/nprogress.css"
import getPageTitle, { getRelativePath } from "@/utils"
import { useNoticeStoreHook } from "@/store/modules/notice"

NProgress.configure({ showSpinner: false })

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  // 设置页面标题
  document.title = getPageTitle(to.meta.title!)
  const userStore = useUserStoreHook()
  const permissionStore = usePermissionStoreHook()
  const noticeStore = useNoticeStoreHook()
  const dictStore = useDictStoreHook()
  // 判断该用户是否登录
  if (userStore.accessToken) {
    if (to.path === loginUrl) {
      // 如果已经登录，并准备进入 Login 页面，则重定向到主页
      next({ path: "/" })
      NProgress.done()
    } else {
      // 获取 URL 地址
      const relativePath = getRelativePath()
      // 获取用户信息
      if (userStore.userInfo === undefined || !userStore.userInfo) {
        await userStore.getUserInfo()
      }
      // 获取字典数据
      if (dictStore.dictMapKeys === undefined || !dictStore.dictMapKeys) {
        await dictStore.getDictMapKeys()
      }

      // 启动 WebSocket
      if (!noticeStore.getStompClientActive()) {
        noticeStore.startWebSocket()
      }

      // 检查用户是否已获得其权限菜单
      if (userStore.menus.length === 0) {
        try {
          await userStore.getUserPerms()
          // 构建动态路由
          permissionStore.toVueRoutes(userStore.menus)
          // 将'有访问权限的动态路由' 添加到 Router 中
          permissionStore.dynamicRoutes.forEach((route) => router.addRoute(route))
          // 确保添加路由已完成
          if (relativePath.indexOf(loginUrl) !== -1) {
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          } else {
            next({ path: relativePath, replace: true })
          }
        } catch (err: any) {
          // 过程中发生任何错误，都直接重置 Token，并重定向到登录页面
          userStore.resetToken()
          ElMessage.error(err.message || "路由守卫过程发生错误")
          next(loginUrl)
          NProgress.done()
        }
      } else {
        next()
      }
    }
  } else {
    // 如果没有 Token
    if (whiteList.indexOf(to.path) !== -1) {
      // 如果在免登录的白名单中，则直接进入
      next()
    } else {
      // 其他没有访问权限的页面将被重定向到登录页面
      next(loginUrl)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
