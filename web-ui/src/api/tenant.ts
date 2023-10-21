import { IJsonResult, IPageResult, ISearchQuery, ISimpleTenant } from "@/types/base"
import { ISysTenant, ICreateTenant, ICreateTenantAdmin } from "@/types/sys"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"

// 简单查询
export const simpleQueryApi = (params: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISimpleTenant>>("/sys/tenant/simple/query", params)
}

/** 根据Id查询 */
export const getTenantApi = (id: number) => {
  return httpGet<number, IJsonResult<ISysTenant>>(`/sys/tenant/${id}`)
}

/** 分页查询所有 */
export const queryTenantApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISysTenant>>("/sys/tenant/query", data)
}

/** 查询所有 */
export const listTenantApi = () => {
  return httpGet<any, IJsonResult<ISysTenant[]>>("/sys/tenant/list")
}

/** 新增 */
export const saveTenantApi = (data: ICreateTenant) => {
  return httpPost<ICreateTenant, IJsonResult<ICreateTenantAdmin>>("/sys/tenant", data)
}

/** 修改 */
export const updateTenantApi = (data: ISysTenant) => {
  return httpPut<any, IJsonResult<boolean>>("/sys/tenant", data)
}

/** 删除 */
export const deleteTenantApi = (id: string) => {
  return httpDelete<number, IJsonResult<boolean>>(`/sys/tenant/${id}`)
}
