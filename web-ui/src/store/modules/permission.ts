import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { type RouteRecordRaw } from "vue-router"
import { constantRoutes } from "@/router"
import { IPermsMenus } from "@/types/pms"

const Layout = () => import("@/layout/index.vue")

// 遍历后台传来的路由字符串，转换为组件对象
function flatTreeRoutes(arr: IPermsMenus[]): RouteRecordRaw[] {
  const result: RouteRecordRaw[] = []
  const deep = (arr1: IPermsMenus[], arr2: RouteRecordRaw[]) => {
    arr1.forEach((source: IPermsMenus) => {
      const target: any = {
        path: source.path,
        name: source.name,
        meta: {
          title: source.meta.title,
          svgIcon: source.meta.icon,
          hidden: source.meta.hidden
        }
      }
      if (source.component === "Layout") {
        target.component = Layout
      } else {
        target.component = loadView(source.component)
      }
      arr2.push(target)
      if (source.children && source.children.length > 0) {
        target.children = []
        deep(source.children, target.children)
      }
    })
  }
  deep(arr, result)
  return result
}

// 匹配views里面所有的.vue文件
const modules = import.meta.glob("../../views/**/**.vue")

export const loadView = (view: string) => {
  let res
  for (const path in modules) {
    const dir = path.split("views/")[1].split(".vue")[0]
    if (dir === view) {
      res = () => modules[path]()
    }
  }
  return res
}

export const usePermissionStore = defineStore("permission", () => {
  // 全部路由 = 常量路由 + 动态路由
  const routes = ref<RouteRecordRaw[]>([])

  // 后端传入的动态路由
  const dynamicRoutes = ref<RouteRecordRaw[]>([])

  const getRoutes = (): RouteRecordRaw[] => {
    return routes.value
  }

  // 将后端的菜单数据转换为vue-router的路由
  const toVueRoutes = (data: IPermsMenus[]) => {
    return new Promise((resolve) => {
      const asyncRoutes = flatTreeRoutes(data)
      dynamicRoutes.value = asyncRoutes
      routes.value = constantRoutes.concat(asyncRoutes)
      resolve(asyncRoutes)
    })
  }

  return { routes, getRoutes, dynamicRoutes, toVueRoutes }
})

/** 在 setup 外使用 */
export function usePermissionStoreHook() {
  return usePermissionStore(store)
}
