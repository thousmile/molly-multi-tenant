// 搜索分页查询
export interface ISearchQuery {
  // 包含操作用户信息
  includeCauu?: boolean
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

// 简单项目
export interface ISimpleProject {
  /** 项目ID */
  projectId: number
  /** 项目名称 */
  projectName: string
  /** 联系人名称 */
  linkman: string
  /** 行政地址 */
  areaCode: number
  /** 所在地址 */
  address: string
}

// 基础类型
export interface IBaseEntity {
  /** 创建时间 */
  createTime?: string
  /** 创建人 id */
  createUser?: number
  /** 创建人信息 */
  createUserEntity?: IOperateUserEntity
  /** 最后一次修改时间 */
  lastUpdateTime?: string
  /** 最后一次修改人 id */
  lastUpdateUser?: number
  /** 最后一次修改人信息 */
  lastUpdateUserEntity?: IOperateUserEntity
}

// 操作用户信息
export interface IOperateUserEntity {
  /** 操作的用户ID */
  userId: number
  /** 操作的用户头像 */
  avatar: string
  /** 操作的用户昵称 */
  nickname: string
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

// 推送消息
export interface IPushMessage {
  /** ID */
  id: number
  /** 标题 */
  title: string
  /** 消息 */
  message: string
  /** 创建时间 */
  createTime: string
}
