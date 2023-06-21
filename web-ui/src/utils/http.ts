import axios, { AxiosResponse, type AxiosInstance, type AxiosRequestConfig } from "axios"
import { useUserStoreHook } from "@/store/modules/user"
import { ElMessage, ElMessageBox } from "element-plus"
import { get, merge } from "lodash-es"
import { getAccessToken } from "./cache/local-storage"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { getEnvBaseURLPrefix } from "."
import { ISimpleTenant } from "@/types/base"
import { defaultTenant } from "@/config"

/** 创建请求实例 */
function createService() {
  // 创建一个 axios 实例命名为 service
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
      // apiData 是 api 返回的数据
      const apiData = response.data
      // 二进制数据则直接返回
      const responseType = response.request?.responseType
      if (responseType === "blob" || responseType === "arraybuffer") return apiData
      // 这个 code 是和后端约定的业务 code
      const code = apiData.status
      // 如果没有 code, 代表这不是项目后端开发的 api
      if (code === undefined) {
        ElMessage.error("非本系统的接口")
        return Promise.reject(new Error("非本系统的接口"))
      }
      switch (code) {
        case 200:
          // code === 200 代表没有错误
          return apiData
        case 400004:
        case 400010:
        case 400011:
        case 400012:
          logout(apiData.message)
          return Promise.reject(new Error(apiData.message))
        case 400444:
          // 如果租户ID 不存在，就重置为默认租户
          ElMessage.error(apiData.message)
          // 表示租户不存在，已经被删除了
          useTenantStoreHook().resetCurrentTenant()
          return resetTenant(service, response)
        case 400446:
          // 此用户不包含此租户ID
          ElMessage.error(`您没有操作 ${useTenantStoreHook().getCurrentTenant().name} 租户的权限`)
          useTenantStoreHook().setCurrentTenant(apiData.data as ISimpleTenant)
          return resetTenant(service, response)
        default:
          // 不是正确的 Code
          ElMessage.error(apiData.message || "Error")
          return Promise.reject(new Error(apiData.message || "Error"))
      }
    },
    (error) => {
      // status 是 HTTP 状态码
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

/** 创建请求方法 */
function createRequestFunction(service: AxiosInstance) {
  return function <T>(config: AxiosRequestConfig): Promise<T> {
    let tenant: ISimpleTenant = useTenantStoreHook().getCurrentTenant()
    if (config.url === "/auth/login") {
      tenant = defaultTenant
    }
    const defaultConfig = {
      headers: {
        // 携带 Token
        "x-tenant-id": tenant.tenantId,
        Authorization: getAccessToken(),
        "Content-Type": "application/json"
      },
      timeout: 5000,
      baseURL: getEnvBaseURLPrefix()
    }
    // 将默认配置 defaultConfig 和传入的自定义配置 config 进行合并成为 mergeConfig
    const mergeConfig = merge(defaultConfig, config)
    return service(mergeConfig)
  }
}

// 重置租户
function resetTenant(service: AxiosInstance, response: AxiosResponse) {
  const config = response.config
  const tenant = useTenantStoreHook().getCurrentTenant()
  config.headers["x-tenant-id"] = tenant.tenantId
  // 获取当前失败的请求，重新发起请求
  return service(config)
}

// 退出登录
function logout(message: string) {
  ElMessageBox.confirm(message || "登录已经过期，需要重新登录", "登录过期", {
    confirmButtonText: "重新登录",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      useUserStoreHook().fedLogout()
      // 强制刷新浏览器也行，只是交互体验不是很好
      location.reload()
    })
    .catch(() => console.log("取消退出..."))
}

/** 用于网络请求的实例 */
const axiosService = createService()

/** 用于网络请求的方法 */
const axiosRequest = createRequestFunction(axiosService)

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
