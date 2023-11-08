import { computed } from "vue"
import { useTenantStoreHook } from "@/store/modules/tenant"
import { chinaAreaDeepQuery, expiredDateAgo, timeAgo } from "@/utils"

const tenantStore = useTenantStoreHook()

// 以前的时间
const showTimeAgo = computed(() => {
  return (value: string) => timeAgo(value)
})

// 未来的时间
const showExpiredDateAgo = computed(() => {
  return (value: string) => expiredDateAgo(value)
})

// 截取字符串
const showStringOverflow = computed(() => {
  return (value: string, len = 20) => {
    return value.length <= len ? value : value.substring(0, len)
  }
})

// 显示中国行政区域名称
const showChinaArea = computed(() => {
  return (value: number) => {
    const area = chinaAreaDeepQuery(value)
    if (area) {
      return area.mergerName.replaceAll("-", " / ")
    }
    return ""
  }
})

// 判断当前租户，是否默认租户
const isDefaultTenantId = computed(() => {
  return (tenantId: string) => {
    return tenantStore.getDefaultTenantId() === tenantId
  }
})

export { showTimeAgo, showExpiredDateAgo, showStringOverflow, showChinaArea, isDefaultTenantId }
