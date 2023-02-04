import { IJsonResult } from "@/types/base"
import { httpDelete } from "@/utils/http"

// 删除 oss 上的图片
export const deleteFile = (params: string) => {
  return httpDelete<any, IJsonResult<boolean>>("/upload", { fileUrl: params })
}
