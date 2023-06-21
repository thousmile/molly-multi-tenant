import { IBaseEntity, ISimpleTenant } from "./base"

/** 用户登录 */
export interface ILoginData {
  /** 用户名 */
  username: string
  /** 密码 */
  password: string
  /** 验证码 */
  codeText: string
  /** 验证码key */
  codeKey: string
}

/** 用户登录的信息 */
export interface ILoginUserInfo {
  tenantId: string
  loginId: string
  grantType: string
  userId: number
  mobile: string
  email: string
  nickname: string
  avatar: string
  username: string
  status: number
  gender: number
  adminFlag: number
  userType: number
  loginTime: string
  roles: IUserRole[]
}

/** 用户登录的角色 */
export interface IUserRole {
  roleId: number
  roleName: string
  description: string
}

/** 认证 token */
export interface IOAuth2Token {
  /** `token` */
  access_token: string
  /** 前缀 `Bearer ` */
  token_type: string
  /** 用于调用刷新`accessToken`的接口时所需的`token` */
  refresh_token: string
  scope: string
  /** `accessToken`的过期时间  秒 */
  expires_in: number
}

// 系统菜单
export interface ISysMenu extends IBaseEntity {
  /**
   * 菜单 ID
   */
  menuId: number
  /**
   * 上级菜单
   */
  parentId: number
  /**
   * 菜单 名称
   */
  menuName: string
  /**
   * 权限标识
   */
  perms: string
  /**
   * 组件
   */
  component: string
  /**
   * 菜单图标
   */
  icon: string
  /**
   * 排序
   */
  sort: number
  /**
   * 路由地址
   */
  path: string
  /**
   * 0.租户用户   1. 系统用户   2.全部
   */
  target: number
  /**
   * 菜单类型（0.菜单 1.按钮）
   */
  menuType: number
  /**
   * 菜单状态（1.显示 0.隐藏）
   */
  visible: number
  /**
   * 保持状态（1.保持 0.不保持）
   */
  keepAlive: number
  /**
   * 子菜单
   */
  children: ISysMenu[]
}

/** 用户权限 */
export interface IUserPerms {
  /** 按钮 */
  buttons: IPermsButton[]
  /** 菜单 */
  menus: IPermsMenus[]
}

/** 按钮权限 */
export interface IPermsButton {
  /** `标识符` */
  perms: string
  /** 标题 */
  title: string
}

/** 按钮权限 */
export interface IPermsMenus {
  id: number
  parentId: number
  weight: number
  name: string
  meta: IPermsMenusMeta
  path: string
  component: string
  children: IPermsMenus[]
}

/** 按钮权限 */
export interface IPermsMenusMeta {
  /* 图标 */
  icon: string
  /* 标题 */
  title: string
  /** 是否隐藏 */
  hidden: boolean
  /** 保持状态 */
  keepAlive: boolean
}

/** 系统用户 */
export interface IPmsUser extends IBaseEntity {
  /**
   * 用户唯一id
   */
  userId: number
  /**
   * 头像
   */
  avatar: string
  /**
   * 用户名
   */
  username: string
  /**
   * 手机号码
   */
  mobile: string
  /**
   * 邮箱
   */
  email: string
  /**
   * 昵称
   */
  nickname: string
  /**
   * 密码
   */
  password: string
  /**
   * 性别[ 0.女  1.男  2.未知]
   */
  gender: number
  /**
   * 是否管理员 0. 普通用户  1. 管理员
   */
  adminFlag: number
  /**
   * 状态 【0.禁用 1.正常】
   */
  status: number
  /**
   * 所属部门
   */
  deptId: number
  /**
   * 过期时间  为空就是永久
   */
  expired: string
  /**
   * 部门
   */
  dept: IPmsDept | null
  /**
   * 角色列表
   */
  roles: IPmsRole[]
}

/** 系统角色 */
export interface IPmsRole extends IBaseEntity {
  /**
   * 用户唯一id
   */
  roleId: number
  /**
   * 角色名称
   */
  roleName: string
  /**
   * 排序
   */
  sort: number
  /**
   * 角色描述
   */
  description: string
}

/** 系统部门 */
export interface IPmsDept extends IBaseEntity {
  /**
   * 部门 ID
   */
  deptId: number
  /**
   * 部门上级
   */
  parentId: number
  /**
   * 部门 名称
   */
  deptName: string
  /**
   * 部门 领导名称
   */
  leader: string
  /**
   * 部门 领导手机号
   */
  leaderMobile: string
  /**
   * 排序
   */
  sort: number
  /**
   * 部门描述
   */
  description: string
  /**
   * 祖级列表
   */
  ancestors: string
  /**
   * 子菜单
   */
  children: IPmsDept[]
}

/** 用户关联的租户 */
export interface IUserListTenant {
  /**
   * 已经拥有的 租户ID
   */
  have: string[]
  /**
   * 全部 租户信息
   */
  all: ISimpleTenant[]
}
