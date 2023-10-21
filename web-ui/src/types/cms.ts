import { IPmsDept } from "./pms"

/** 项目 */
export interface ICmsProject {
  /* 项目ID */
  projectId: number
  /*项目名称*/
  projectName: string
  /*联系人名称*/
  linkman: string
  /*联系电话*/
  contactNumber: string
  /*行政地址*/
  areaCode: number
  /*所在地，如：左右云创谷1栋A座*/
  address: string
  /*排序*/
  sort: number
  /*项目密码，做一些危险操作时，使用*/
  password: string
  /*状态 【0.禁用 1.正常 2.锁定 】*/
  status: number
  /*所属部门*/
  deptId: number
  /*部门*/
  dept: IPmsDept | null
}
