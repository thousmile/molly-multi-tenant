import router from "@/router"
import { useUserStoreHook } from "@/store/modules/user"
import { useDictStoreHook } from "@/store/modules/dict"
import { usePermissionStoreHook } from "@/store/modules/permission"
import { loginUrl, whiteList } from "@/config/white-list"
import NProgress from "nprogress"
import "nprogress/nprogress.css"
import getPageTitle from "@/utils"
import { useNoticeStoreHook } from "@/store/modules/notice"

NProgress.configure({ showSpinner: false })

router.beforeEach((to, _from, next) => {
  NProgress.start()
  // 设置页面标题
  document.title = getPageTitle(to.meta.title!)
  const userStore = useUserStoreHook()
  // 判断该用户是否登录
  if (userStore.accessToken) {
    if (to.path === loginUrl) {
      // 如果已经登录，并准备进入 Login 页面，则重定向到主页
      next({ path: "/" })
    } else {
      // 初始化数据
      initBasicData().then(async () => {
        // 检查用户是否已获得其权限菜单
        if (userStore.menus.length === 0) {
          await userStore.getUserPerms()

          // 将用户的菜单权限，构建成 vue-router 的动态路由
          const dynamicRoutes = await usePermissionStoreHook().toVueRoutes(userStore.menus)

          // 将'有访问权限的动态路由' 添加到 Router 中
          dynamicRoutes.forEach((route) => router.addRoute(route))

          // hack方法 确保addRoute已完成 设置 replace: true, 因此导航将不会留下历史记录
          next({ path: to.path, query: to.query, replace: true })
        } else {
          next()
        }
      })
    }
  } else {
    // 如果没有 Token
    if (whiteList.indexOf(to.path) !== -1) {
      // 如果在免登录的白名单中，则直接进入
      next()
    } else {
      // 其他没有访问权限的页面将被重定向到登录页面
      const query = to.query
      query.redirect = to.path
      next({ path: loginUrl, query: query })
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

// 初始化数据,如: 用户信息，数据字典，全局配置
async function initBasicData() {
  const userStore = useUserStoreHook()
  // 获取用户信息
  if (userStore.userInfo === undefined || !userStore.userInfo) {
    await userStore.getUserInfo()
  }
  const dictStore = useDictStoreHook()

  // 获取字典数据
  if (dictStore.dictMapKeys === undefined || !dictStore.dictMapKeys) {
    await dictStore.getDictMapKeys()
  }

  // 启动 WebSocket
  const noticeStore = useNoticeStoreHook()
  if (!noticeStore.getStompClientActive()) {
    noticeStore.startWebSocket()
  }
}
