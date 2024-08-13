import {IBaseEntity, ISearchQuery} from "./base"

// 字典搜索分页查询
export interface IDictSearchQuery extends ISearchQuery {
  // 字典类型
  dictTypeKey: string
}

/** 字典类型表 */
export interface IDictType extends IBaseEntity {
  /** 字典类型 ID */
  typeId: number
  /** 字典类型名 */
  typeName: string
  /** 字典类型关键字 */
  typeKey: string
  /** 字典描述 */
  description: string
}

/** 字典数据表 */
export interface IDictData extends IBaseEntity {
  /** 字典类型 ID */
  dictCode: number
  /** 字典排序 */
  dictSort: number
  /** 字典标签 */
  dictLabel: string
  /** 字典键值 */
  dictValue: string
  /** 字典类型关键字 */
  typeKey: string
  /** 是否默认（1.是 0.否） */
  isDefault: number
}

/** 字典数据表 */
export interface IMapDictData extends IBaseEntity {
  /** 字典排序 */
  dictSort: number
  /** 字典标签 */
  dictLabel: string
  /** 字典键值 */
  dictValue: Object
  /** 是否默认 */
  isDefault: boolean
}
