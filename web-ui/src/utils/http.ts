import axios, { AxiosInstance, type AxiosRequestConfig } from "axios"
import { useUserStoreHook } from "@/store/modules/user"
import { ElMessage, ElMessageBox } from "element-plus"
import { get } from "lodash-es"
import { getAccessToken } from "./cache/cookies"
import { defaultTenant } from "@/config"
import { getEnvBaseURLPrefix } from "."

import { getCurrentTenant } from "./cache/localStorage"
import { ISimpleTenant } from "@/types/base"

/** 创建请求实例 */
function createService() {
  // 创建一个 Axios 实例
  const service = axios.create()
  // 请求拦截
  service.interceptors.request.use(
    (config) => config,
    // 发送失败
    (error) => Promise.reject(error)
  )
  // 响应拦截（可根据具体业务作出相应的调整）
  service.interceptors.response.use(
    (response) => {
      // apiData 是 API 返回的数据
      const apiData = response.data as any
      // 这个 status 是和后端约定的业务 status
      const code = apiData.status
      // 如果没有 Code, 代表这不是项目后端开发的 API
      if (code === undefined) {
        ElMessage.error("非本系统的接口")
        return Promise.reject(new Error("非本系统的接口"))
      } else {
        switch (code) {
          case 200:
            // code === 200 代表没有错误
            return apiData
          case 400004:
          case 400010:
          case 400011:
          case 400012:
            logout(apiData.message)
            return
          default:
            // 不是正确的 Code
            ElMessage.error(apiData.message || "Error")
            return Promise.reject(new Error("Error"))
        }
      }
    },
    (error) => {
      // Status 是 HTTP 状态码
      const status = get(error, "response.status")
      switch (status) {
        case 400:
          error.message = "请求错误"
          break
        case 401:
          // Token 过期时，直接退出登录并强制刷新页面（会重定向到登录页）
          useUserStoreHook().userLogout()
          location.reload()
          break
        case 403:
          error.message = "拒绝访问"
          break
        case 404:
          error.message = "请求地址出错"
          break
        case 408:
          error.message = "请求超时"
          break
        case 500:
          error.message = "服务器内部错误"
          break
        case 501:
          error.message = "服务未实现"
          break
        case 502:
          error.message = "网关错误"
          break
        case 503:
          error.message = "服务不可用"
          break
        case 504:
          error.message = "网关超时"
          break
        case 505:
          error.message = "HTTP 版本不受支持"
          break
        default:
          break
      }
      ElMessage.error(error.message)
      return Promise.reject(error)
    }
  )
  return service
}

// 退出登录
function logout(message: string) {
  ElMessageBox.confirm(message || "登录已经过期，需要重新登录", "登录过期", {
    confirmButtonText: "重新登录",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => useUserStoreHook().fedLogout())
    .catch(() => console.log("取消退出..."))
}

/** 创建请求方法 */
function createRequestFunction(service: AxiosInstance) {
  return function (config: AxiosRequestConfig) {
    let tenant: ISimpleTenant = getCurrentTenant()
    if (config.url === "/auth/login") {
      tenant = defaultTenant
    }
    const configDefault = {
      headers: {
        // 携带 Token
        Authorization: getAccessToken(),
        "x-tenant-id": tenant.tenantId,
        "Content-Type": get(config, "headers.Content-Type", "application/json")
      },
      timeout: 5000,
      baseURL: getEnvBaseURLPrefix()
    }
    return service(Object.assign(configDefault, config))
  }
}

/** 用于网络请求的实例 */
export const axiosService = createService()

/** 用于网络请求的方法 */
export const axiosRequest = createRequestFunction(axiosService)

/** 通用请求工具函数 */
function httpRequest<T>(config: AxiosRequestConfig): Promise<T> {
  // 单独处理自定义请求/响应回掉
  return new Promise((resolve, reject) =>
    axiosRequest(config)
      .then((response) => {
        resolve(response as T)
      })
      .catch((error) => {
        reject(error)
      })
  )
}

/** 单独抽离的get工具函数 */
function httpGet<T, P>(url: string, params?: T): Promise<P> {
  return httpRequest<P>({ method: "get", url, params })
}

/** 单独抽离的post工具函数 */
function httpPost<T, P>(url: string, data?: T): Promise<P> {
  return httpRequest<P>({ method: "post", url, data })
}

/** 单独抽离的put工具函数 */
function httpPut<T, P>(url: string, data?: T): Promise<P> {
  return httpRequest<P>({ method: "put", url, data })
}

/** 单独抽离的delete工具函数 */
function httpDelete<T, P>(url: string, params?: T): Promise<P> {
  return httpRequest<P>({ method: "delete", url, params })
}

export { httpRequest, httpGet, httpPost, httpPut, httpDelete }
