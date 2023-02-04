import store from "@/store"
import { defineStore } from "pinia"
import { ref } from "vue"
import { ISimpleTenant } from "@/types/base"
import { setCurrentTenant as setTenant, getCurrentTenant as getTenant } from "@/utils/cache/localStorage"

export const useTenantStore = defineStore("tenant", () => {
  // 当前租户
  const currentTenant = ref<ISimpleTenant>(getTenant())

  // 获取当前租户
  const getCurrentTenantId = (): string => {
    return getCurrentTenant().tenantId
  }

  // 获取当前租户
  const getCurrentTenant = (): ISimpleTenant => {
    return currentTenant.value
  }

  // 设置当前租户信息
  const setCurrentTenant = (tenant: ISimpleTenant): void => {
    currentTenant.value = tenant
    setTenant(tenant)
  }

  return {
    getCurrentTenant,
    setCurrentTenant,
    getCurrentTenantId
  }
})

/** 在 setup 外使用 */
export function useTenantStoreHook() {
  return useTenantStore(store)
}
