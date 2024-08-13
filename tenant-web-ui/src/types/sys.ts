import {IBaseEntity} from "./base"

/** 租户模板 */
export interface ISysTemplate extends IBaseEntity {
  /**
   * 唯一id
   */
  id: number
  /**
   * 名称
   */
  name: string
  /**
   * 描述
   */
  description: string
  /**
   * 菜单ID
   */
  menuIds?: number[]
}

/** 租户 */
export interface ISysTenant extends IBaseEntity {
  /**
   * 租户id
   */
  tenantId: string
  /**
   * 租户logo
   */
  logo: string
  /**
   * 租户名称
   */
  name: string
  /**
   * 租户邮箱
   */
  email: string
  /**
   * 联系人名称
   */
  linkman: string
  /**
   * 联系电话
   */
  contactNumber: string
  /**
   * 行政地址
   */
  areaCode: number
  /**
   * 联系地址
   */
  address: string
  /**
   * 状态 【0.禁用 1.正常】
   */
  status: number
  /**
   * 过期时间  默认是 100 年
   */
  expired: string
  /**
   * 租户 模板
   */
  templates: ISysTemplate[]
}

/** 创建租户 */
export interface ICreateTenantAdmin {
  /**
   * 管理员昵称
   */
  adminNickname: string
  /**
   * 管理员用户名
   */
  adminUsername: string
  /**
   * 管理员手机号
   */
  adminMobile: string
  /**
   * 管理员邮箱
   */
  adminEmail: string
  /**
   * 管理员密码
   */
  adminPwd: string
}

/** 创建租户 */
export interface ICreateTenant extends ICreateTenantAdmin {
  /**
   * 租户id
   */
  tenantId: string
  /**
   * 租户logo
   */
  logo: string
  /**
   * 租户名称
   */
  name: string
  /**
   * 租户邮箱
   */
  email: string
  /**
   * 联系人名称
   */
  linkman: string
  /**
   * 联系电话
   */
  contactNumber: string
  /**
   * 行政地址
   */
  areaCode: number
  /**
   * 联系地址
   */
  address: string
  /**
   * 租户 模板
   */
  templates: ISysTemplate[]
  /**
   * 过期时间  默认是 100 年
   */
  expired: string
}

/** 系统配置 */
export interface ISysConfig extends IBaseEntity {
  /**
   * 唯一id
   */
  configId: number
  /**
   * 参数名称
   */
  configName: string
  /**
   * 参数键名
   */
  configKey: string
  /**
   * 参数键值
   */
  configValue: string
  /**
   * 系统内置（1.是 0.否）
   */
  configType: number
}

/** 服务器信息 */
export interface IServerInfo {
  /**
   * CPU相关信息
   */
  cpu: Cpu
  /**
   * 內存相关信息
   */
  mem: Mem
  /**
   * JVM相关信息
   */
  jvm: Jvm
  /**
   * 服务器相关信息
   */
  sys: Sys
  /**
   * 磁盘相关信息
   */
  sysFiles: SysFile[]
}

/** 服务器CPU信息 */
export interface Cpu {
  /**
   * 核心数
   */
  cpuNum: number
  /**
   * CPU总的使用率
   */
  total: number
  /**
   * CPU系统使用率
   */
  sys: number
  /**
   * CPU用户使用率
   */
  used: number
  /**
   * CPU当前等待率
   */
  wait: number
  /**
   * CPU当前空闲率
   */
  free: number
}

/** 服务器Jvm信息 */
export interface Jvm {
  /**
   * 当前JVM占用的内存总数(M)
   */
  total: number
  /**
   * JVM最大可用内存总数(M)
   */
  max: number
  /**
   * JVM空闲内存(M)
   */
  free: number
  /**
   * JVM使用的内存(M)
   */
  used: number
  /**
   * JVM使用的内存(M)
   */
  usage: number
  /**
   * JDK版本
   */
  version: string
  /**
   * JDK路径
   */
  home: string
  /**
   * Java名称
   */
  name: string
  /**
   * 启动时间
   */
  startTime: string
  /**
   * 运行时长
   */
  runTime: string
  /**
   * 运行参数
   */
  inputArgs: string
}

/** 服务器内存信息 */
export interface Mem {
  /**
   * 内存总量
   */
  total: number
  /**
   * 已用内存
   */
  used: number
  /**
   * 剩余内存
   */
  free: number
  /**
   * 使用率
   */
  usage: number
}

/** 服务器系统信息 */
export interface Sys {
  /**
   * 服务器名称
   */
  computerName: string
  /**
   * 服务器Ip
   */
  computerIp: string
  /**
   * 项目路径
   */
  userDir: string
  /**
   * 操作系统
   */
  osName: string
  /**
   * 系统架构
   */
  osArch: string
}

/** 服务器文件系统信息 */
export interface SysFile {
  /**
   * 盘符路径
   */
  dirName: string
  /**
   * 盘符类型
   */
  sysTypeName: string
  /**
   * 文件类型
   */
  typeName: string
  /**
   * 总大小
   */
  total: string
  /**
   * 剩余大小
   */
  free: string
  /**
   * 已经使用量
   */
  used: string
  /**
   * 资源的使用率
   */
  usage: number
}
