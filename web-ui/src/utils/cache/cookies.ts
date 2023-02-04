/** 统一处理 Cookie */

import CacheKey from "./cacheKey"
import Cookies from "js-cookie"

export const getAccessToken = () => {
  return Cookies.get(CacheKey.ACCESS_TOKEN)
}

export const setAccessToken = (token: string) => {
  Cookies.set(CacheKey.ACCESS_TOKEN, token)
}

export const removeAccessToken = () => {
  Cookies.remove(CacheKey.ACCESS_TOKEN)
}
