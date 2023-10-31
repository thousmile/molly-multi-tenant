import { type App } from "vue"
import { type Directive } from "vue"
import { useUserStoreHook } from "@/store/modules/user"

/** 挂载自定义指令 */
export function loadDirectives(app: App) {
  // example: v-has='pre_dept:view'
  app.directive("has", permission)
  // example: v-admin
  app.directive("admin", adminFlag)
  // example: v-preventReClick
  app.directive("preventReClick", preventReClick)
}

/** 权限指令，和权限判断函数 checkPermission 功能类似 */
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

/** 权限指令，判断当前登录的用户是否管理员 */
const adminFlag: Directive = {
  mounted(el) {
    const hasPermission = useUserStoreHook().userInfo?.adminFlag === 1
    if (!hasPermission) {
      el.style.display = "none"
    }
  }
}

/** 防止重复请求 指令 */
const preventReClick: Directive = {
  mounted(el, binding) {
    const { value } = binding
    el.addEventListener("click", () => {
      el.disabled = true
      el.loading = true
      setTimeout(() => {
        el.disabled = false
        el.loading = false
      }, value || 3000)
    })
  }
}
