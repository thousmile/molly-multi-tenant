<template>
  <div class="app-container" v-loading="loading">
    <el-row>
      <el-col :span="12" class="card-box">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>CPU</span>
            </div>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">属性</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">值</div></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">核心数</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">{{ server.cpu.cpuNum }}</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">用户使用率</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">{{ server.cpu.used }}%</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">系统使用率</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">{{ server.cpu.sys }}%</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">当前空闲率</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">{{ server.cpu.free }}%</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>内存</span>
            </div>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf"><div class="cell">属性</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">内存</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">JVM</div></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">总内存</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">{{ server.mem.total }}G</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.total }}M</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">已用内存</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">{{ server.mem.used }}G</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.used }}M</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">剩余内存</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">{{ server.mem.free }}G</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.free }}M</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">使用率</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem" :class="{ 'text-danger': server.mem.usage > 80 }">
                      {{ server.mem.usage }}%
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm" :class="{ 'text-danger': server.jvm.usage > 80 }">
                      {{ server.jvm.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>服务器信息</span>
            </div>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">服务器名称</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">{{ server.sys.computerName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf"><div class="cell">操作系统</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">{{ server.sys.osName }}</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">服务器IP</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">{{ server.sys.computerIp }}</div>
                  </td>
                  <td class="el-table__cell is-leaf"><div class="cell">系统架构</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">{{ server.sys.osArch }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Java虚拟机信息</span>
            </div>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%; table-layout: fixed">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">Java名称</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.name }}</div>
                  </td>
                  <td class="el-table__cell is-leaf"><div class="cell">Java版本</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.version }}</div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">启动时间</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.startTime }}</div>
                  </td>
                  <td class="el-table__cell is-leaf"><div class="cell">运行时长</div></td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.runTime }}</div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf"><div class="cell">安装路径</div></td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.home }}</div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf"><div class="cell">项目路径</div></td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">{{ server.sys.userDir }}</div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf"><div class="cell">运行参数</div></td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">{{ server.jvm.inputArgs }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>磁盘状态</span>
            </div>
          </template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell el-table__cell is-leaf"><div class="cell">盘符路径</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">文件系统</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">盘符类型</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">总大小</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">可用大小</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">已用大小</div></th>
                  <th class="el-table__cell is-leaf"><div class="cell">已用百分比</div></th>
                </tr>
              </thead>
              <tbody v-if="server.sysFiles">
                <tr v-for="(sysFile, index) in server.sysFiles" :key="index">
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.dirName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.sysTypeName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.typeName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.total }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.free }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.used }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" :class="{ 'text-danger': sysFile.usage > 80 }">{{ sysFile.usage }}%</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { getServerInfoApi } from "@/api/server"
import { IServerInfo } from "@/types/sys"
import { ref, onMounted } from "vue"
const server = ref<IServerInfo>({
  cpu: {
    /**
     * 核心数
     */
    cpuNum: 0,
    /**
     * CPU总的使用率
     */
    total: 0,
    /**
     * CPU系统使用率
     */
    sys: 0,
    /**
     * CPU用户使用率
     */
    used: 0,
    /**
     * CPU当前等待率
     */
    wait: 0,
    /**
     * CPU当前空闲率
     */
    free: 0
  },
  mem: {
    /**
     * 内存总量
     */
    total: 0,
    /**
     * 已用内存
     */
    used: 0,
    /**
     * 剩余内存
     */
    free: 0,
    /**
     * 使用率
     */
    usage: 0
  },
  jvm: {
    /**
     * 当前JVM占用的内存总数(M)
     */
    total: 0,
    /**
     * JVM最大可用内存总数(M)
     */
    max: 0,
    /**
     * JVM空闲内存(M)
     */
    free: 0,
    /**
     * JVM使用的内存(M)
     */
    used: 0,
    /**
     * JVM使用的内存(M)
     */
    usage: 0,
    /**
     * JDK版本
     */
    version: "",
    /**
     * JDK路径
     */
    home: "",
    /**
     * Java名称
     */
    name: "",
    /**
     * 启动时间
     */
    startTime: "",
    /**
     * 运行时长
     */
    runTime: "",
    /**
     * 运行参数
     */
    inputArgs: ""
  },
  sys: {
    /**
     * 服务器名称
     */
    computerName: "",
    /**
     * 服务器Ip
     */
    computerIp: "",
    /**
     * 项目路径
     */
    userDir: "",
    /**
     * 操作系统,
     */
    osName: "",
    /**
     * 系统架构
     */
    osArch: ""
  },
  sysFiles: []
})

/** 加载 */
const loading = ref(false)

const getServerInfo = () => {
  loading.value = true
  getServerInfoApi()
    .then((resp) => {
      server.value = resp.data
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      loading.value = false
    })
}

onMounted(() => {
  getServerInfo()
})
</script>

<style lang="scss" scoped>
.card-box {
  padding-right: 5px;
  padding-left: 5px;
  margin-bottom: 10px;
}
</style>
