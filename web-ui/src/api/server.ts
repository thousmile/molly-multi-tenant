import { IJsonResult } from "@/types/base"
import { IServerInfo } from "@/types/sys"
import { httpGet } from "@/utils/http"

/** 查询服务器信息 */
export const getServerInfoApi = () => {
  return httpGet<any, IJsonResult<IServerInfo>>("/server/info")
}
