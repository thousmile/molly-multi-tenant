/** 统一处理 localStorage */

import CacheKey from "@/constants/cache-key"
import { type SidebarOpened, type SidebarClosed } from "@/constants/app-key"
import { type ThemeName } from "@/hooks/useTheme"
import { type TagView } from "@/store/modules/tags-view"
import { type LayoutSettings } from "@/config/layouts"
import { ILoginData } from "@/types/pms"
import { encode, decode } from "js-base64"
import { ISimpleProject } from "@/types/base"
import { defaultProject, hashCode } from ".."
import { isJSON } from "../validate"

//#region token
export const getToken = () => {
  return localStorage.getItem(CacheKey.TOKEN)
}

export const setToken = (token: string) => {
  localStorage.setItem(CacheKey.TOKEN, token)
}

export const removeToken = () => {
  localStorage.removeItem(CacheKey.TOKEN)
}
//#endregion

//#region 系统布局配置
export const getConfigLayout = () => {
  const json = localStorage.getItem(CacheKey.CONFIG_LAYOUT)
  return json ? (JSON.parse(json) as LayoutSettings) : null
}

export const setConfigLayout = (settings: LayoutSettings) => {
  localStorage.setItem(CacheKey.CONFIG_LAYOUT, JSON.stringify(settings))
}

export const removeConfigLayout = () => {
  localStorage.removeItem(CacheKey.CONFIG_LAYOUT)
}
//#endregion

//#region 侧边栏状态
export const getSidebarStatus = () => {
  return localStorage.getItem(CacheKey.SIDEBAR_STATUS)
}

export const setSidebarStatus = (sidebarStatus: SidebarOpened | SidebarClosed) => {
  localStorage.setItem(CacheKey.SIDEBAR_STATUS, sidebarStatus)
}
//#endregion

//#region 正在应用的主题名称
export const getActiveThemeName = () => {
  return localStorage.getItem(CacheKey.ACTIVE_THEME_NAME) as ThemeName | null
}

export const setActiveThemeName = (themeName: ThemeName) => {
  localStorage.setItem(CacheKey.ACTIVE_THEME_NAME, themeName)
}
//#endregion

//#region 标签栏

export const getVisitedViews = () => {
  const json = localStorage.getItem(CacheKey.VISITED_VIEWS)
  return JSON.parse(json ?? "[]") as TagView[]
}

export const setVisitedViews = (views: TagView[]) => {
  views.forEach((view) => {
    // 删除不必要的属性，防止 JSON.stringify 处理到循环引用
    delete view.matched
    delete view.redirectedFrom
  })
  localStorage.setItem(CacheKey.VISITED_VIEWS, JSON.stringify(views))
}

export const getCachedViews = () => {
  const json = localStorage.getItem(CacheKey.CACHED_VIEWS)
  return JSON.parse(json ?? "[]") as string[]
}

export const setCachedViews = (views: string[]) => {
  localStorage.setItem(CacheKey.CACHED_VIEWS, JSON.stringify(views))
}

//#region 控件尺寸
/// 控件尺寸
export const getControlSize = () => {
  return localStorage.getItem(CacheKey.CONTROL_SIZE) as string
}

/// 控件尺寸
export const setControlSize = (size: string) => {
  localStorage.setItem(CacheKey.CONTROL_SIZE, size)
}
//#endregion

//#region 获取保存的 用户名和密码
export const getUserAndPassword = () => {
  const base64Str = localStorage.getItem(CacheKey.USER_AND_PASSWORD)
  if (base64Str) {
    // 第一次 base64 解密
    const desVal1 = decode(base64Str)
    if (desVal1) {
      // 根据 userAgent 生成 hashCode
      const hc = hashCode(navigator.userAgent)
      // hashCode 。base64 加密
      const salt = encode(hc.toString())
      // 祛除盐
      const desVal2Str = desVal1.replaceAll(`${salt}=.`, "")
      const desVal3Json = decode(desVal2Str)
      if (isJSON(desVal3Json)) {
        const data = JSON.parse(desVal3Json) as ILoginData
        let saltPassword = decode(data.password)
        saltPassword = saltPassword.replaceAll(`${hc}`, "")
        const password = saltPassword.replaceAll(`${salt}`, "")
        data.password = password
        return data
      }
    }
    removeUserAndPassword()
  }
  return null
}

// 保存 用户名和密码
export function setUserAndPassword(data: ILoginData) {
  // 根据 userAgent 生成 hashCode
  const hc = hashCode(navigator.userAgent)
  // hashCode 。base64 加密
  const salt = encode(hc.toString())
  // 将 用户密码加盐，再加密
  data.password = encode(`${hc}${data.password}${salt}`)
  // 将 用户名和密码转json 。base64 加密
  const encodeVal = encode(JSON.stringify(data))
  // 再 拼接上盐
  const newVal = `${salt}=.${encodeVal}`
  // 再次 base64 加密
  const newVal2 = encode(newVal)
  return localStorage.setItem(CacheKey.USER_AND_PASSWORD, newVal2)
}

// 删除 用户名和密码
export function removeUserAndPassword() {
  return localStorage.removeItem(CacheKey.USER_AND_PASSWORD)
}

//#endregion

//#region 项目选择

// 获取当前操作的项目
export const getCurrentProject = () => {
  let jsonStr = localStorage.getItem(CacheKey.PROJECT_ID)
  if (!jsonStr || !isJSON(jsonStr)) {
    setCurrentProject(defaultProject)
    jsonStr = localStorage.getItem(CacheKey.PROJECT_ID)
  }
  return JSON.parse(jsonStr!) as ISimpleProject
}

// 设置当前操作的项目
export const setCurrentProject = (project: ISimpleProject) => {
  const jsonStr = JSON.stringify(project)
  localStorage.setItem(CacheKey.PROJECT_ID, jsonStr)
}

//#endregion

// 获取 上一次登录的租户Id
export const getLastTenantId = () => {
  return localStorage.getItem(CacheKey.LAST_TENANT_ID)
}

// 设置 上一次登录的租户Id
export const setLastTenantId = (tenantId: string) => {
  localStorage.setItem(CacheKey.LAST_TENANT_ID, tenantId)
}

//#endregion
