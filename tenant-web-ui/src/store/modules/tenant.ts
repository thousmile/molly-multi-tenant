import store from "@/store"
import {defineStore} from "pinia"
import {ref} from "vue"
import {ISimpleTenant} from "@/types/base"
import {useUserStoreHook} from "./user"
import {getSimpleTenantApi} from "@/api/tenant"

export const useTenantStore = defineStore("tenant", () => {
  // 当前租户
  const tenant = ref<ISimpleTenant>()

  // 获取当前租户
  const getTenantId = (): string => {
    return tenant.value!.tenantId
  }

  // 获取当前租户
  const getTenant = (): ISimpleTenant => {
    return tenant.value!
  }

  /** 获取登录用户所在的租户 */
  const getLoginUserTenant = () => {
    return new Promise((resolve, reject) => {
      const tenantId = useUserStoreHook().userInfo?.tenantId
      if (tenantId) {
        getSimpleTenantApi(tenantId)
          .then((resp) => {
            if (resp.data) {
              tenant.value = resp.data
              resolve(resp)
            } else {
              reject(resp.message)
            }
          })
          .catch((error) => {
            reject(error)
          })
      }
    })
  }

  return {
    tenant,
    getTenantId,
    getTenant,
    getLoginUserTenant
  }
})

/** 在 setup 外使用 */
export function useTenantStoreHook() {
  return useTenantStore(store)
}
