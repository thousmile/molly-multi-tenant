import {ISearchQuery, ISimpleProject} from "@/types/base"
import {ICmsProject} from "@/types/cms"
import {httpDelete, httpGet, httpPost, httpPut} from "@/utils/service"

/** 根据Id查询 */
export const getProjectApi = (id: number) => {
  return httpGet<number, IJsonResult<ICmsProject>>(`/cms/project/${id}`)
}

// 简单查询
export const simpleQueryProjectApi = (params: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISimpleProject>>("/cms/project/simple/query", params)
}

/** 分页查询所有 */
export const queryProjectApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ICmsProject>>("/cms/project/query", data)
}

/** 查询所有 */
export const listProjectApi = () => {
  return httpGet<any, IJsonResult<ICmsProject[]>>("/cms/project/list")
}

/** 新增 */
export const saveProjectApi = (data: ICmsProject) => {
  return httpPost<ICmsProject, IJsonResult<ICmsProject>>("/cms/project", data)
}

/** 修改 */
export const updateProjectApi = (data: ICmsProject) => {
  return httpPut<any, IJsonResult<boolean>>("/cms/project", data)
}

/** 删除 */
export const deleteProjectApi = (id: number, password: string) => {
  const params = {
    projectId: id,
    password: password
  }
  return httpDelete<any, IJsonResult<boolean>>("/cms/project", params)
}

/** 重置密码 */
export const resetPasswordApi = (data: any) => {
  return httpPost<any, IJsonResult<boolean>>("/cms/project/reset/password", data)
}
