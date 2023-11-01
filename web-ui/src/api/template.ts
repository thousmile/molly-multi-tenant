import { ISearchQuery, IUpdateMenus } from "@/types/base"
import { ISysTemplate } from "@/types/sys"
import { httpDelete, httpGet, httpPost, httpPut } from "@/utils/service"

/** 根据Id查询 */
export const getTemplateApi = (id: number) => {
  return httpGet<number, IJsonResult<ISysTemplate>>(`/sys/template/${id}`)
}

/** 分页查询所有 */
export const queryTemplateApi = (data: ISearchQuery) => {
  return httpGet<ISearchQuery, IPageResult<ISysTemplate>>("/sys/template/query", data)
}

/** 查询所有 */
export const listTemplateApi = () => {
  return httpGet<any, IJsonResult<ISysTemplate[]>>("/sys/template/list")
}

/** 新增 */
export const saveTemplateApi = (data: ISysTemplate) => {
  return httpPost<ISysTemplate, IJsonResult<ISysTemplate>>("/sys/template", data)
}

/** 修改 */
export const updateTemplateApi = (data: ISysTemplate) => {
  return httpPut<any, IJsonResult<boolean>>("/sys/template", data)
}

/** 删除 */
export const deleteTemplateApi = (id: number) => {
  return httpDelete<number, IJsonResult<boolean>>(`/sys/template/${id}`)
}

/** 拥有的权限 */
export const getTemplateMenusApi = (data: number) => {
  return httpGet<ISearchQuery, IJsonResult<IUpdateMenus>>(`/sys/template/menus/${data}`)
}

/** 修改拥有的权限 */
export const updateTemplateMenusApi = (data: any) => {
  return httpPost<any, IJsonResult<Boolean>>("/sys/template/menus", data)
}
