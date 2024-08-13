import {ISearchQuery, IUpdateMenus} from "@/types/base"
import {IPmsRole} from "@/types/pms"
import {httpDelete, httpGet, httpPost, httpPut} from "@/utils/service"

/** 根据Id查询 */
export const getRoleApi = (id: number) => {
  return httpGet<number, IJsonResult<IPmsRole>>(`/pms/role/${id}`)
}

/** 分页查询所有 */
export const queryRoleApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IPmsRole>>("/pms/role/query", data)
}

/** 查询所有 */
export const listRoleApi = () => {
  return httpGet<any, IJsonResult<IPmsRole[]>>("/pms/role/list")
}

/** 新增 */
export const saveRoleApi = (data: IPmsRole) => {
  return httpPost<IPmsRole, IJsonResult<IPmsRole>>("/pms/role", data)
}

/** 修改 */
export const updateRoleApi = (data: IPmsRole) => {
  return httpPut<any, IJsonResult<boolean>>("/pms/role", data)
}

/** 删除 */
export const deleteRoleApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/pms/role/${id}`)
}

/** 拥有的权限 */
export const getRoleMenusApi = (data: number) => {
  return httpGet<ISearchQuery, IJsonResult<IUpdateMenus>>(`/pms/role/menus/${data}`)
}

/** 修改拥有的权限 */
export const updateRoleMenusApi = (data: any) => {
  return httpPost<any, IJsonResult<Boolean>>("/pms/role/menus", data)
}

/** 拥有的部门 */
export const getRoleDeptsApi = (data: number) => {
  return httpGet<ISearchQuery, IJsonResult<IUpdateMenus>>(`/pms/role/depts/${data}`)
}
