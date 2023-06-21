/** 判断是否为数组 */
export const testArray = (arg: unknown) => {
  return Array.isArray ? Array.isArray(arg) : Object.prototype.toString.call(arg) === "[object Array]"
}

/** 判断是否为字符串 */
export const testString = (str: unknown) => {
  return typeof str === "string" || str instanceof String
}

/** 判断是否为外链 */
export const testExternal = (path: string) => {
  const reg = /^(https?:|mailto:|tel:)/
  return reg.test(path)
}

/** 判断是否为网址（带协议） */
export const testUrl = (url: string) => {
  const reg = /^(((ht|f)tps?):\/\/)?([^!@#$%^&*?.\s-]([^!@#$%^&*?.\s]{0,63}[^!@#$%^&*?.\s])?\.)+[a-z]{2,6}\/?/
  return reg.test(url)
}

/** 判断是否为网址或 IP（带端口） */
export const testUrlPort = (url: string) => {
  const reg = /^((ht|f)tps?:\/\/)?[\w-]+(\.[\w-]+)+:\d{1,5}\/?$/
  return reg.test(url)
}

/** 判断是否为域名（不带协议） */
export const testDomain = (domain: string) => {
  const reg = /^([0-9a-zA-Z-]{1,}\.)+([a-zA-Z]{2,})$/
  return reg.test(domain)
}

/** 判断版本号格式是否为 X.Y.Z */
export const testVersion = (version: string) => {
  const reg = /^\d+(?:\.\d+){2}$/
  return reg.test(version)
}

/** 判断时间格式是否为 24 小时制（HH:mm:ss） */
export const test24H = (time: string) => {
  const reg = /^(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d$/
  return reg.test(time)
}

/** 判断是否为手机号（1 开头） */
export const testPhoneNumber = (str: string) => {
  const reg = /^(?:(?:\+|00)86)?1\d{10}$/
  return reg.test(str)
}

/** 判断是否为第二代身份证（18 位） */
export const testChineseIdCard = (str: string) => {
  const reg = /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
  return reg.test(str)
}

/** 判断是否为 Email（支持中文邮箱） */
export const testEmail = (email: string) => {
  const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
  return reg.test(email)
}

/** 判断是否为 MAC 地址 */
export const testMAC = (mac: string) => {
  const reg =
    /^(([a-f0-9][0,2,4,6,8,a,c,e]:([a-f0-9]{2}:){4})|([a-f0-9][0,2,4,6,8,a,c,e]-([a-f0-9]{2}-){4}))[a-f0-9]{2}$/i
  return reg.test(mac)
}

/** 判断是否为 IPv4 地址 */
export const testIPv4 = (ip: string) => {
  const reg =
    /^((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])(?::(?:[0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5]))?$/
  return reg.test(ip)
}

/** 判断是否为车牌（兼容新能源车牌） */
export const testLicensePlate = (str: string) => {
  const reg =
    /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$/
  return reg.test(str)
}

/** 图片后缀名 */
export function testImage(value: string) {
  const reg = /\w(\.gif|\.jpeg|\.png|\.jpg|\.bmp)/
  return reg.test(value)
}

/** 压缩包格式 */
export function testCompress(value: string) {
  const reg = /^[^\\/:\\*\\?"<>\\|]+\.(zip|gz|rar|7z|tar|xz|bz2|tar.gz|tar.xz|tar.bz2|tar.7z)$/
  return reg.test(value)
}

/** 用户密码 */
export function testPassword(value: string) {
  const reg = /^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{5,24}$/
  return reg.test(value)
}

/**  用户名称 */
export function testUsername(value: string) {
  const reg = /^[a-zA-Z0-9_-]{5,24}$/
  return reg.test(value)
}

/** 校验 手机号码*/
export function testPhone(value: string) {
  const reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/
  return reg.test(value)
}

/** 电话号码 */
export function testTelphone(value: string) {
  const reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$|^0\d{2,3}-?\d{7,8}$/
  return reg.test(value)
}
