/** 统一处理 localStorage */

import CacheKey from "./cacheKey"
import { type ThemeName } from "@/hooks/useTheme"
import { ISimpleTenant } from "@/types/base"
import { defaultTenant } from "@/config"

export const getSidebarStatus = () => {
  return localStorage.getItem(CacheKey.SIDEBAR_STATUS)
}

export const setSidebarStatus = (sidebarStatus: "opened" | "closed") => {
  localStorage.setItem(CacheKey.SIDEBAR_STATUS, sidebarStatus)
}

export const getActiveThemeName = () => {
  return localStorage.getItem(CacheKey.ACTIVE_THEME_NAME) as ThemeName
}

export const setActiveThemeName = (themeName: ThemeName) => {
  localStorage.setItem(CacheKey.ACTIVE_THEME_NAME, themeName)
}

/// 获取当前操作的租户
export const getCurrentTenant = () => {
  let jsonStr = localStorage.getItem(CacheKey.TENANT_ID)
  if (jsonStr === null || jsonStr === undefined || jsonStr === "") {
    setCurrentTenant(defaultTenant)
    jsonStr = localStorage.getItem(CacheKey.TENANT_ID)
  }
  return JSON.parse(jsonStr!) as ISimpleTenant
}

/// 设置当前操作的租户
export const setCurrentTenant = (tenant: ISimpleTenant) => {
  const jsonStr = JSON.stringify(tenant)
  localStorage.setItem(CacheKey.TENANT_ID, jsonStr)
}

/// 控件尺寸
export const getControlSize = () => {
  return localStorage.getItem(CacheKey.CONTROL_SIZE) as string
}

/// 控件尺寸
export const setControlSize = (size: string) => {
  localStorage.setItem(CacheKey.CONTROL_SIZE, size)
}
