/** 登录日志 */
export interface ILoginLog {
  /* ID */
  id: string
  /*授权类型*/
  grantType: string
  /*用户ID*/
  userId: number
  /*用户昵称*/
  nickname: string
  /*用户名*/
  username: string
  /*头像*/
  avatar: string
  /*请求地址*/
  requestUrl: string
  /*请求IP*/
  requestIp: string
  /*请求地址*/
  address: string
  /*操作系统*/
  osName: string
  /*浏览器*/
  browser: string
  /*创建时间*/
  createTime: string
}

/** 操作日志 */
export interface IOperLog {
  /*ID*/
  id: string
  /*标题*/
  title: string
  /*描述*/
  description: string
  /*服务名称*/
  serviceName: string
  /*用户ID*/
  userId: number
  /*用户昵称*/
  nickname: string
  /*用户名*/
  username: string
  /*头像*/
  avatar: string
  /*方法*/
  method: string
  /*方法参数*/
  methodArgs: string
  /*请求类型*/
  requestMethod: string
  /*请求地址*/
  requestUrl: string
  /*请求IP*/
  requestIp: string
  /*请求地址*/
  address: string
  /*请求响应*/
  responseResult: string
  /*状态*/
  status: number
  /*错误日志*/
  errorLog: string
  /*耗时(毫秒)*/
  timeCost: number
  /*创建时间*/
  createTime: string
}
