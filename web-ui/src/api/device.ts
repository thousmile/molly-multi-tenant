import { ISearchQuery } from "@/types/base"
import { ICmsDevice } from "@/types/cms"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"

/** 根据Id查询 */
export const getDeviceApi = (id: number) => {
  return httpGet<number, IJsonResult<ICmsDevice>>(`/cms/device/${id}`)
}

/** 分页查询所有 */
export const queryDeviceApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ICmsDevice>>("/cms/device/query", data)
}

/** 查询所有 */
export const listDeviceApi = () => {
  return httpGet<any, IJsonResult<ICmsDevice[]>>("/cms/device/list")
}

/** 新增 */
export const saveDeviceApi = (data: ICmsDevice) => {
  return httpPost<ICmsDevice, IJsonResult<ICmsDevice>>("/cms/device", data)
}

/** 修改 */
export const updateDeviceApi = (data: ICmsDevice) => {
  return httpPut<any, IJsonResult<boolean>>("/cms/device", data)
}

/** 删除 */
export const deleteDeviceApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/cms/device/${id}`)
}
