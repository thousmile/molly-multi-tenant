import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { getAccessToken, removeAccessToken, setAccessToken } from "@/utils/cache/cookies"
import { resetRouter } from "@/router"
import { loginApi, getUserInfoApi, logoutApi, getUserPermsApi } from "@/api/login"
import { type ILoginData, ILoginUserInfo, IPermsMenus, IPermsButton } from "@/types/pms"

export const useUserStore = defineStore("user", () => {
  // token信息
  const accessToken = ref<string>(getAccessToken() || "")

  // 用户详情
  const userInfo = ref<ILoginUserInfo>()

  // 用户的权限按钮
  const buttons = ref<IPermsButton[]>([])

  // 用户的权限菜单
  const menus = ref<IPermsMenus[]>([])

  /** 登录 */
  const userLogin = (loginData: ILoginData) => {
    return new Promise((resolve, reject) => {
      loginApi(loginData)
        .then((resp) => {
          const token = resp.data.token_type + resp.data.access_token
          setAccessToken(token)
          accessToken.value = token
          resolve(resp)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 获取用户详情 */
  const getUserInfo = () => {
    return new Promise((resolve, reject) => {
      getUserInfoApi()
        .then((resp) => {
          userInfo.value = resp.data
          resolve(resp)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 获取用户权限菜单 */
  const getUserPerms = () => {
    return new Promise((resolve, reject) => {
      getUserPermsApi()
        .then((resp) => {
          buttons.value = resp.data.buttons
          menus.value = resp.data.menus
          resolve(resp)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 登出 */
  const userLogout = () => {
    return new Promise((resolve, reject) => {
      logoutApi()
        .then((resp) => {
          removeAccessToken()
          userInfo.value = undefined
          accessToken.value = ""
          resetRouter()
          resolve(resp)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 前端登录，不用请求后台，直接删除所有 cookie */
  const fedLogout = () => {
    removeAccessToken()
    userInfo.value = undefined
    accessToken.value = ""
    resetRouter()
    location.reload()
  }

  /** 重置 Token */
  const resetToken = () => {
    removeAccessToken()
  }

  return {
    accessToken,
    userInfo,
    buttons,
    menus,
    userLogin,
    getUserPerms,
    getUserInfo,
    userLogout,
    resetToken,
    fedLogout
  }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
  return useUserStore(store)
}