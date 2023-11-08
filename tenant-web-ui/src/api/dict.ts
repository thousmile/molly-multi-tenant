import { ISearchQuery } from "@/types/base"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"
import { IMapDictData, IDictType, IDictData, IDictSearchQuery } from "@/types/dict"

/** [Map]查询所有 */
export const mapKeysApi = () => {
  return httpGet<string, IJsonResult<Map<string, IMapDictData[]>>>("/dict/data/mapKeys")
}

/** 根据Id查询 */
export const getDictTypeApi = (id: number) => {
  return httpGet<number, IJsonResult<IDictType>>(`/dict/type/${id}`)
}

/** 分页查询所有 */
export const queryDictTypeApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IDictType>>("/dict/type/query", data)
}

/** 查询所有 */
export const listDictTypeApi = () => {
  return httpGet<any, IJsonResult<IDictType[]>>("/dict/type/list")
}

/** 新增 */
export const saveDictTypeApi = (data: IDictType) => {
  return httpPost<IDictType, IJsonResult<IDictType>>("/dict/type", data)
}

/** 修改 */
export const updateDictTypeApi = (data: IDictType) => {
  return httpPut<any, IJsonResult<boolean>>("/dict/type", data)
}

/** 删除 */
export const deleteDictTypeApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/dict/type/${id}`)
}

/// ----------------------  字典数据 --------------------------------

/** 分页查询所有 */
export const queryDictDataApi = (data: IDictSearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<IDictData>>("/dict/data/query", data)
}

/** 新增 */
export const saveDictDataApi = (data: IDictData) => {
  return httpPost<IDictData, IJsonResult<IDictData>>("/dict/data", data)
}

/** 修改 */
export const updateDictDataApi = (data: IDictData) => {
  return httpPut<any, IJsonResult<boolean>>("/dict/data", data)
}

/** 删除 */
export const deleteDictDataApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/dict/data/${id}`)
}
