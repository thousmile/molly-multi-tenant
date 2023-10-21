import { IJsonResult } from "@/types/base"
import { ILoginData, ILoginUserInfo, IOAuth2Token, IUserPerms } from "@/types/pms"
import { httpGet, httpPost } from "@/utils/service"

/** 登录 */
export const loginApi = (data?: ILoginData) => {
  return httpPost<ILoginData, IJsonResult<IOAuth2Token>>("/auth/login", data)
}

/** 退出登录 */
export const logoutApi = () => {
  return httpPost<string, IJsonResult<string>>("/auth/logout")
}

/** 获取用户详情 */
export const getUserInfoApi = () => {
  return httpGet<string, IJsonResult<ILoginUserInfo>>("/auth/login/user")
}

/** 获取用户权限 */
export const getUserPermsApi = () => {
  return httpGet<string, IJsonResult<IUserPerms>>("/pms/user/rights")
}
