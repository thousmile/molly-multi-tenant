const SYSTEM_NAME = "molly-tenant"

/** 缓存数据时用到的 Key */
class CacheKey {
  static readonly CONFIG_LAYOUT = `${SYSTEM_NAME}-config-layout-key`
  static readonly SIDEBAR_STATUS = `${SYSTEM_NAME}-sidebar-status-key`
  static readonly ACTIVE_THEME_NAME = `${SYSTEM_NAME}-active-theme-name-key`
  static readonly VISITED_VIEWS = `${SYSTEM_NAME}-visited-views-key`
  static readonly CACHED_VIEWS = `${SYSTEM_NAME}-cached-views-key`
  static readonly CONTROL_SIZE = `${SYSTEM_NAME}-control-size-key`

  static readonly TOKEN = `${SYSTEM_NAME}-token`
  static readonly USER_AND_PASSWORD = `${SYSTEM_NAME}-uap`
  static readonly PROJECT_ID = `${SYSTEM_NAME}-project-id`
  static readonly LAST_TENANT_ID = `${SYSTEM_NAME}-last-tenant-id`
}

export default CacheKey
