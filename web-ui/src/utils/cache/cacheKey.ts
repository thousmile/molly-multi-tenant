const SYSTEM_NAME = "molly"

/** 缓存数据时用到的 Key */
class CacheKey {
  static ACCESS_TOKEN = `${SYSTEM_NAME}-token-key`
  static TENANT_ID = `${SYSTEM_NAME}-tenant-id-key`
  static SIDEBAR_STATUS = `${SYSTEM_NAME}-sidebar-status-key`
  static ACTIVE_THEME_NAME = `${SYSTEM_NAME}-active-theme-name-key`
  static CONTROL_SIZE = `${SYSTEM_NAME}-control-size`
}

export default CacheKey
