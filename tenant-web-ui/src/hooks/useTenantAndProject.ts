import {computed} from "vue"
import {useProjectStoreHook} from "@/store/modules/project"

const projectStore = useProjectStoreHook()

export function useTenantAndProject() {
  // 租户ID 和 项目 组成的 唯一ID
  const currentOnlyId = computed(() => {
    return projectStore.getCurrentProjectId()
  })

  return {
    currentOnlyId
  }
}
