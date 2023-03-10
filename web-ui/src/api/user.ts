import { IJsonResult, IPageResult, ISearchQuery } from "@/types/base"
import { IPmsUser } from "@/types/pms"
import { httpPost, httpPut, httpGet, httpDelete } from "@/utils/http"

/** 修改密码 */
export const updatePasswordApi = (data: any) => {
  return httpPost<any, IJsonResult<boolean>>("/pms/user/update/password", data)
}

/** 重置密码 */
export const resetPasswordApi = (data: any) => {
  return httpPost<any, IJsonResult<boolean>>("/pms/user/reset/password", data)
}

/** 查询用户 */
export const queryUserApi = (params: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IPmsUser>>("/pms/user/query", params)
}

/** 查询所有 */
export const listUserApi = () => {
  return httpGet<any, IJsonResult<IPmsUser[]>>("/pms/user/list")
}

/** 新增 */
export const saveUserApi = (data: IPmsUser) => {
  return httpPost<IPmsUser, IJsonResult<IPmsUser>>("/pms/user", data)
}

/** 修改用户信息 */
export const updateUserApi = (data: any) => {
  return httpPut<any, IJsonResult<boolean>>("/pms/user", data)
}

/** 删除 */
export const deleteUserApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/pms/user/${id}`)
}
