import { type Directive } from "vue"
import { useUserStoreHook } from "@/store/modules/user"

/** 权限指令，和权限判断函数 checkPermission 功能类似 */
export const permission: Directive = {
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
