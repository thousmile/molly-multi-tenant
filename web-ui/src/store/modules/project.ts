import store from "@/store"
import { defineStore } from "pinia"
import { ref } from "vue"
import { ISimpleProject } from "@/types/base"
import { defaultProject } from "@/utils"

export const useProjectStore = defineStore("project", () => {
  // 当前项目
  const currentProject = ref<ISimpleProject>(defaultProject)

  // 获取当前项目
  const getCurrentProjectId = (): number => {
    return getCurrentProject().projectId
  }

  // 获取默认项目
  const getDefaultProjectId = (): number => {
    return defaultProject.projectId
  }

  // 获取当前项目
  const getCurrentProject = (): ISimpleProject => {
    return currentProject.value
  }

  // 设置当前项目信息
  const setCurrentProject = (tenant: ISimpleProject): void => {
    currentProject.value = tenant
  }

  // 重置当前操作的项目
  const resetCurrentProject = (): void => {
    currentProject.value = defaultProject
  }

  // 获取当前项目
  const isDefaultProjectId = (): boolean => {
    return getCurrentProjectId() === getDefaultProjectId()
  }

  return {
    getCurrentProjectId,
    getDefaultProjectId,
    getCurrentProject,
    setCurrentProject,
    resetCurrentProject,
    isDefaultProjectId
  }
})

/** 在 setup 外使用 */
export function useProjectStoreHook() {
  return useProjectStore(store)
}
