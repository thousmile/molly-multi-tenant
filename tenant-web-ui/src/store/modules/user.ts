import {ref} from "vue"
import store from "@/store"
import {defineStore} from "pinia"
import {ElMessage} from "element-plus"
import {useNoticeStoreHook} from "./notice"
import {useSettingsStore} from "./settings"
import {useTagsViewStore} from "./tags-view"
import {getToken, removeToken, setToken} from "@/utils/cache/local-storage"
import {getPublicKeyApi, getUserInfoApi, getUserPermsApi, loginApi, logoutApi} from "@/api/login"
import {ILoginData, ILoginUserInfo, type IPermsButton, IPermsMenus} from "@/types/pms"
import {resetRouter} from "@/router"
import JSEncrypt from "jsencrypt"

export const useUserStore = defineStore("user", () => {

  // RSA加密
  const encryptor = ref<JSEncrypt>(new JSEncrypt())

  // token信息
  const accessToken = ref<string>(getToken() || "")

  // 用户详情
  const userInfo = ref<ILoginUserInfo>()

  // 用户的权限按钮
  const buttons = ref<IPermsButton[]>([])

  // 用户的权限菜单
  const menus = ref<IPermsMenus[]>([])

  const noticeStore = useNoticeStoreHook()

  const tagsViewStore = useTagsViewStore()

  const settingsStore = useSettingsStore()

  /** 登录 */
  const userLogin = async (loginData: ILoginData) => {
    return new Promise((resolve, reject) => {
      let encryptLoginData: ILoginData = {
        username: encryptor.value.encrypt(loginData.username).toString(),
        password: encryptor.value.encrypt(loginData.password).toString(),
        codeText: loginData.codeText,
        tenantId: loginData.tenantId,
        codeKey: loginData.codeKey,
        rememberMe: loginData.rememberMe
      }
      loginApi(encryptLoginData)
        .then((resp) => {
          const token = resp.data.token_type + resp.data.access_token
          setToken(token)
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
          if (resp.data) {
            userInfo.value = resp.data
            resolve(resp)
          } else {
            fedLogout()
            reject(resp.message)
          }
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
          if (resp.data) {
            buttons.value = resp.data.buttons
            menus.value = resp.data.menus
            if (resp.data.menus.length === 0) {
              ElMessage({
                message: "您暂无权限，请联系管理员!",
                type: "warning"
              })
              fedLogout()
            }
            resolve(resp)
          } else {
            fedLogout()
            reject(resp.message)
          }
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
          fedLogout()
          resolve(resp)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 前端登录，不用请求后台，直接删除所有 cookie */
  const fedLogout = () => {
    removeToken()
    userInfo.value = undefined
    accessToken.value = ""
    resetRouter()
    // 关闭 WebSocket
    noticeStore.stopWebSocket()
    noticeStore.broadcast = []
    noticeStore.pushNotices = []
    noticeStore.createTenantNotice = undefined

    buttons.value = []
    menus.value = []
    _resetTagsView()
  }

  /** 重置 Visited Views 和 Cached Views */
  const _resetTagsView = () => {
    if (!settingsStore.cacheTagsView) {
      tagsViewStore.delAllVisitedViews()
      tagsViewStore.delAllCachedViews()
    }
  }

  /** 获取公钥 */
  const getPublicKey = () => {
    return new Promise((resolve, reject) => {
      getPublicKeyApi()
        .then((resp) => {
          if (resp.data) {
            encryptor.value.setPublicKey(resp.data)
            resolve(resp)
          } else {
            reject(resp.message)
          }
        })
        .catch((error) => {
          reject(error)
        })
    })
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
    getPublicKey,
    fedLogout
  }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
  return useUserStore(store)
}
