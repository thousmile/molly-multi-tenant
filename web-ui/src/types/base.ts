// json 返回值
export interface IJsonResult<T> {
  status: number

  message: string

  data: T
}

// 分页
export interface IPagination<T> {
  total: number

  list: T[]
}

// 分页 返回值
export interface IPageResult<T> {
  status: number

  message: string

  data: IPagination<T>
}

// 搜索分页查询
export interface ISearchQuery {
  // 当前是第几页
  pageIndex: number

  // 每页多少条
  pageSize: number

  // 关键字
  keywords: string

  // 开始日期 YYYY-MM-DD
  startDate?: string

  // 结束日期 YYYY-MM-DD
  endDate?: string
}

// 简单租户
export interface ISimpleTenant {
  /** 租户ID */
  tenantId: string
  /** Logo */
  logo: string
  /** 租户名称 */
  name: string
  /** 联系人名称 */
  linkman: string
}

// 基础类型
export interface IBaseEntity {
  /** 创建时间 */
  createTime?: string
  /** 创建人 id */
  createUser?: number
  /** 最后一次修改时间 */
  lastUpdateTime?: string
  /** 最后一次修改人 id */
  lastUpdateUser?: number
}

// 简单菜单
export interface ISimpleMenu {
  /** 菜单ID */
  id: number
  /** 菜单名称 */
  name: string
  /** 上级 */
  parentId: number
  /** 排序 */
  weight: number
  /** 子菜单 */
  children: ISimpleMenu[]
}

// 修改菜单
export interface IUpdateMenus {
  /** 拥有的菜单ID */
  have: number[]
  /** 全部的菜单 */
  all: ISimpleMenu[]
}
