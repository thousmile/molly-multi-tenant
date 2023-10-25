import dayjs from "dayjs"
import { removeConfigLayout } from "@/utils/cache/local-storage"
import chinaAreaJson from "@/assets/ChinaArea.json"
import { ISimpleProject, ISimpleTenant } from "@/types/base"

//#region 格式化日期时间
export const DEFAULT_DATE_TIME_PATTERN = "YYYY-MM-DD HH:mm:ss"

export const DEFAULT_DATE_PATTERN = "YYYY-MM-DD"

export const DEFAULT_TIME_PATTERN = "HH:mm:ss"

/** 格式化日期时间 */
export const formatDateTime = (time: string | number | Date) => {
  return time ? dayjs(new Date(time)).format(DEFAULT_DATE_TIME_PATTERN) : "N/A"
}

/** 格式化日期 */
export const formatDate = (time: string | number | Date) => {
  return time ? dayjs(new Date(time)).format(DEFAULT_DATE_PATTERN) : "N/A"
}

/** 格式化时间 */
export const formatTime = (time: string | number | Date) => {
  return time ? dayjs(new Date(time)).format(DEFAULT_TIME_PATTERN) : "N/A"
}

/** 计算时间差 */
export const diffDateTime = (time1: string | number | Date, time2: string | number | Date) => {
  if (!time1) {
    return "N/A"
  }
  if (!time2) {
    return "N/A"
  }
  const diff = dayjs(new Date(time1)).diff(new Date(time2), "days")
  return diff
}

/**
 * @param {number} time
 */
export function timeAgo(time: string | number | Date) {
  const limit = dayjs(Date.now()).diff(dayjs(new Date(time)), "second")
  let content = ""
  if (limit < 60) {
    content = "刚刚"
  } else if (limit >= 60 && limit < 3600) {
    content = Math.floor(limit / 60) + " 分钟前"
  } else if (limit >= 3600 && limit < 86400) {
    content = Math.floor(limit / 3600) + " 小时前"
  } else if (limit >= 86400 && limit < 2592000) {
    content = Math.floor(limit / 86400) + " 天前"
  } else if (limit >= 2592000 && limit < 31104000) {
    content = Math.floor(limit / 2592000) + " 个月前"
  } else {
    content = dayjs(time).format(DEFAULT_DATE_PATTERN)
  }
  return content
}

export function expiredDateAgo(time: string | number | Date) {
  const data = dayjs(time, DEFAULT_DATE_TIME_PATTERN)
  const now = dayjs()
  if (data.diff(now, "year") > 0) {
    return `${data.diff(now, "year")}年后`
  } else if (data.diff(now, "month") > 0) {
    return `${data.diff(now, "month")}月后`
  } else if (data.diff(now, "day") > 0) {
    return `${data.diff(now, "day")}天后`
  } else if (data.isBefore(now)) {
    return "已过期"
  } else {
    return data.format("MM-DD HH:mm")
  }
}

//#endregion

/** 用 JS 获取全局 css 变量 */
export const getCssVariableValue = (cssVariableName: string) => {
  let cssVariableValue = ""
  try {
    // 没有拿到值时，会返回空串
    cssVariableValue = getComputedStyle(document.documentElement).getPropertyValue(cssVariableName)
  } catch (error) {
    console.error(error)
  }
  return cssVariableValue
}

/** 用 JS 设置全局 CSS 变量 */
export const setCssVariableValue = (cssVariableName: string, cssVariableValue: string) => {
  try {
    document.documentElement.style.setProperty(cssVariableName, cssVariableValue)
  } catch (error) {
    console.error(error)
  }
}

/** 重置项目配置 */
export const resetConfigLayout = () => {
  removeConfigLayout()
  location.reload()
}

/**
 * @param {string} url
 * @returns {Object}
 */
export const getQueryObject = (url: string) => {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf("?") + 1)
  const obj: any = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    const val = String(decodeURIComponent($2))
    obj[name] = val
    return rs
  })
  return obj
}

/** 获取 http 请求头 前缀 */
export const getEnvBaseURLPrefix = () => {
  return import.meta.env.MODE === "development" ? "/api/v1" : getEnvBaseURL()
}

/** 获取 默认api url */
export const getEnvBaseURL = () => {
  return import.meta.env.VITE_BASE_API
}

/** 获取 区域名称 */
export const chinaAreaDeepQuery = (areaCode: number) => {
  let isGet = false
  let node = { areaCode: 0, mergerName: "" }
  const deepSearch = (tree: any, id: any) => {
    for (let i = 0; i < tree.length; i++) {
      const temp = tree[i]
      if (temp.children && temp.children.length > 0) {
        deepSearch(temp.children, id)
      }
      if (id === temp.areaCode || isGet) {
        isGet || (node = { areaCode: temp.areaCode, mergerName: temp.mergerName })
        isGet = true
        break
      }
    }
  }
  deepSearch(chinaAreaJson, areaCode)
  return node
}

// 未来的时间
export const futureShortcuts = [
  {
    text: "一周后",
    value: dayjs().add(1, "week").toDate()
  },
  {
    text: "一个月后",
    value: dayjs().add(1, "month").toDate()
  },
  {
    text: "三个月后",
    value: dayjs().add(3, "month").toDate()
  },
  {
    text: "六个月后",
    value: dayjs().add(6, "month").toDate()
  },
  {
    text: "一年后",
    value: dayjs().add(1, "year").toDate()
  },
  {
    text: "三年后",
    value: dayjs().add(3, "year").toDate()
  }
]

// 过去的时间
export const pastShortcuts = [
  {
    text: "一周前",
    value: dayjs().subtract(1, "week").toDate()
  },
  {
    text: "一个月前",
    value: dayjs().subtract(1, "month").toDate()
  },
  {
    text: "三个月前",
    value: dayjs().subtract(3, "month").toDate()
  },
  {
    text: "六个月前",
    value: dayjs().subtract(6, "month").toDate()
  },
  {
    text: "一年前",
    value: dayjs().subtract(1, "year").toDate()
  },
  {
    text: "三年前",
    value: dayjs().subtract(3, "year").toDate()
  }
]

// 设置 默认的租户
export const defaultTenant: ISimpleTenant = {
  tenantId: "master",
  logo: "http://images.xaaef.com/molly_master_logo.png",
  name: "默认租户",
  linkman: "master"
}

// 设置 默认的项目
export const defaultProject: ISimpleProject = {
  projectId: 10001,
  projectName: "默认项目",
  linkman: "默认项目",
  address: "默认项目"
}
