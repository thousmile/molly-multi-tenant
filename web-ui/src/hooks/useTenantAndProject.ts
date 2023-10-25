import { computed } from "vue"
import { useProjectStoreHook } from "@/store/modules/project"
import { useTenantStoreHook } from "@/store/modules/tenant"

const projectStore = useProjectStoreHook()
const tenantStore = useTenantStoreHook()

// 租户ID 和 项目 组成的 唯一ID
const currentOnlyId = computed(() => {
  return tenantStore.getCurrentTenantId() + projectStore.getCurrentProjectId()
})

export function useTenantAndProject() {
  return {
    currentOnlyId
  }
}
