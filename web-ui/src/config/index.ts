import { ISimpleTenant } from "@/types/base"

// 设置 默认的租户
export const defaultTenant: ISimpleTenant = {
  tenantId: "master",
  logo: "http://images.xaaef.com/molly_master_logo.png",
  name: "默认租户",
  linkman: "master"
}

/** 项目配置 */
interface IProjectSettings {
  /**
   * 项目名称
   */
  title: string
}

/** 默认项目配置 */
export const defaultSettings: IProjectSettings = {
  title: "Molly租户管理系统"
}
