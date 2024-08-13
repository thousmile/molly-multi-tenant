import {ISearchQuery} from "@/types/base"
import {ILoginLog, IOperLog} from "@/types/lms"
import {httpGet, httpPost} from "@/utils/service"

/** 根据Id查询 */
export const getLoginLogApi = (id: String) => {
  return httpGet<number, IJsonResult<ILoginLog>>(`/lms/lgoin/${id}`)
}

/** 分页查询 登录日志 */
export const queryLoginLogApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ILoginLog>>("/lms/lgoin/query", data)
}

/** 删除 登录日志 */
export const deleteLoginLogApi = (ids: string[]) => {
  return httpPost<string[], IJsonResult<boolean>>("/lms/lgoin", ids)
}

/** 分页查询 操作日志 */
export const queryOperLogApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IOperLog>>("/lms/oper/query", data)
}

/** 删除 操作日志 */
export const deleteOperLogApi = (ids: string[]) => {
  return httpPost<string[], IJsonResult<boolean>>("/lms/oper", ids)
}
