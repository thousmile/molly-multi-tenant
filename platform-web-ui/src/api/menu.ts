import {ISearchQuery} from "@/types/base"
import {ISysMenu} from "@/types/pms"
import {httpDelete, httpGet, httpPost, httpPut} from "@/utils/service"

/** 根据Id查询 */
export const getMenuApi = (id: number) => {
  return httpGet<number, IJsonResult<ISysMenu>>(`/sys/menu/${id}`)
}

/** 分页查询所有 */
export const queryMenuApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISysMenu[]>>("/sys/menu/query", data)
}

/** 查询所有 */
export const listMenuApi = () => {
  return httpGet<any, IJsonResult<ISysMenu[]>>("/sys/menu/list")
}

/** 查询树节点 */
export const treeMenuApi = () => {
  return httpGet<any, IJsonResult<ISysMenu[]>>("/sys/menu/tree")
}

/** 新增 */
export const saveMenuApi = (data: ISysMenu) => {
  return httpPost<ISysMenu, IJsonResult<ISysMenu>>("/sys/menu", data)
}

/** 修改 */
export const updateMenuApi = (data: ISysMenu) => {
  return httpPut<any, IJsonResult<boolean>>("/sys/menu", data)
}

/** 删除 */
export const deleteMenuApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/sys/menu/${id}`)
}
