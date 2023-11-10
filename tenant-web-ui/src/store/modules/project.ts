import store from "@/store"
import { defineStore } from "pinia"
import { ref } from "vue"
import { ISimpleProject } from "@/types/base"
import { defaultProject } from "@/utils"
import { setCurrentProject as setProject, getCurrentProject as getProject } from "@/utils/cache/local-storage"

export const useProjectStore = defineStore("project", () => {
  // 当前项目
  const currentProject = ref<ISimpleProject>(getProject())

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
  const setCurrentProject = (project: ISimpleProject): void => {
    currentProject.value = project
    setProject(project)
  }

  // 重置当前操作的项目
  const resetCurrentProject = (): void => {
    setCurrentProject(defaultProject)
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
