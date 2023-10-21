import { IJsonResult, IPageResult, ISearchQuery } from "@/types/base"
import { ISysConfig } from "@/types/sys"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"

/** 根据Id查询 */
export const getConfigApi = (id: number) => {
  return httpGet<number, IJsonResult<ISysConfig>>(`/sys/config/${id}`)
}

/** 分页查询所有 */
export const queryConfigApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISysConfig>>("/sys/config/query", data)
}

/** 查询所有 */
export const listConfigApi = () => {
  return httpGet<any, IJsonResult<ISysConfig[]>>("/sys/config/list")
}

/** 新增 */
export const saveConfigApi = (data: ISysConfig) => {
  return httpPost<ISysConfig, IJsonResult<ISysConfig>>("/sys/config", data)
}

/** 修改 */
export const updateConfigApi = (data: ISysConfig) => {
  return httpPut<any, IJsonResult<boolean>>("/sys/config", data)
}

/** 删除 */
export const deleteConfigApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/sys/config/${id}`)
}
