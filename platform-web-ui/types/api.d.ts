// json 返回值
interface IJsonResult<T> {
  /* 状态码 200: 成功！ 100: 失败！ 其他: 都是报错！ */
  status: number
  /* 返回消息 */
  message: string
  /* 数据 */
  data: T
}

// 分页
interface IPagination<T> {
  /* 总页数 */
  total: number
  /* 数据列表 */
  list: T[]
}

// 分页 返回值
interface IPageResult<T> {
  /* 状态码 200: 成功！ 100: 失败！ 其他: 都是报错！ */
  status: number
  /* 返回消息 */
  message: string
  /* 分页数据 */
  data: IPagination<T>
}
