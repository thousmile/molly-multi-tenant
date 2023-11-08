import { ISearchQuery } from "@/types/base"
import { IPmsDept } from "@/types/pms"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"

/** 根据Id查询 */
export const getDeptApi = (id: number) => {
  return httpGet<number, IJsonResult<IPmsDept>>(`/pms/dept/${id}`)
}

/** 分页查询所有 */
export const queryDeptApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IPmsDept[]>>("/pms/dept/query", data)
}

/** 查询所有 */
export const listDeptApi = () => {
  return httpGet<any, IJsonResult<IPmsDept[]>>("/pms/dept/list")
}

/** 查询树节点 */
export const treeDeptApi = () => {
  return httpGet<any, IJsonResult<IPmsDept[]>>("/pms/dept/tree")
}

/** 新增 */
export const saveDeptApi = (data: IPmsDept) => {
  return httpPost<IPmsDept, IJsonResult<IPmsDept>>("/pms/dept", data)
}

/** 修改 */
export const updateDeptApi = (data: IPmsDept) => {
  return httpPut<any, IJsonResult<boolean>>("/pms/dept", data)
}

/** 删除 */
export const deleteDeptApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/pms/dept/${id}`)
}
