import {type App} from "vue"

// 字典数据选择器
import SelectDictData from "@/components/SelectDictData/index.vue"
import OperateUser from "@/components/OperateUser/index.vue"

export function loadGlobalComponent(app: App) {
  app.component("SelectDictData", SelectDictData)
  app.component("OperateUser", OperateUser)
}
