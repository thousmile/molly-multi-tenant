import { Directive, type App } from "vue"
import { useUserStoreHook } from "@/store/modules/user"

/** 挂载自定义指令 */
export function loadDirectives(app: App) {
  app.directive("has", permission)
}

/** 权限指令 */
const permission: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const buttons = useUserStoreHook().buttons
    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = buttons.some((button) => {
        return value.includes(button.perms)
      })
      if (!hasPermission) {
        el.style.display = "none"
      }
    } else {
      throw new Error(`need permissions! Like v-has="['update:data','add:data']"`)
    }
  }
}
