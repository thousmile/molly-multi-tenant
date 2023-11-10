import store from "@/store"
import { defineStore } from "pinia"
import { computed, ref } from "vue"
import { Client } from "@stomp/stompjs"
// @ts-ignore
import SockJS from "sockjs-client/dist/sockjs.min.js"
import { getToken } from "@/utils/cache/local-storage"
import { useTenantStoreHook } from "./tenant"
import { getEnvBaseURL } from "@/utils"
import { IPushMessage } from "@/types/base"

export const useNoticeStore = defineStore("notice", () => {
  // 广播消息
  const broadcast = ref<IPushMessage[]>([])

  // 推送消息
  const pushNotices = ref<IPushMessage[]>([])

  // 创建租户结果 消息
  const createTenantNotice = ref<IPushMessage>()

  const stompClientActive = ref<boolean>(false)

  // websocket 客户端
  const stompClient = ref<Client>()

  // 启动 WebSocket
  const startWebSocket = () => {
    return new Promise<void>((resolve, reject) => {
      const tenantStore = useTenantStoreHook()
      const stompClientAgent = new Client({
        connectHeaders: {
          Authorization: getToken()!,
          "x-tenant-id": tenantStore.getCurrentTenantId()
        },
        debug: function () {},
        reconnectDelay: 5000, // 重连时间
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000
      })

      const wsUrl = `${getEnvBaseURL()}/stomp/push`

      // 用 SockJS 代替 brokenURL
      stompClientAgent.webSocketFactory = function () {
        return new SockJS(wsUrl)
      }

      // 连接
      stompClientAgent.onConnect = (frame) => {
        broadcast.value = []
        pushNotices.value = []

        console.log("连接 WebSocket 成功 :>> ", frame.headers["user-name"])

        // 订阅广播主题
        stompClientAgent.subscribe("/topic/broadcast/notice", (resp) => {
          const result = JSON.parse(resp.body) as IPushMessage
          broadcast.value?.push(result)
        })

        // 订阅推送主题
        stompClientAgent.subscribe("/user/queue/single/push", (resp) => {
          const result = JSON.parse(resp.body) as IPushMessage
          pushNotices.value?.push(result)
        })

        // 订阅 租户创建结果的 主题
        stompClientAgent.subscribe("/user/queue/single/create/tenant", (resp) => {
          const result = JSON.parse(resp.body) as IPushMessage
          createTenantNotice.value = result
          pushNotices.value?.push(result)
        })
      }

      // 错误
      stompClientAgent.onStompError = function (frame) {
        console.log("Broker reported error: " + frame.headers["message"])
        console.log("Additional details: " + frame.body)
        reject(frame.body)
      }

      // 启动
      stompClientAgent.activate()

      stompClient.value = stompClientAgent

      stompClientActive.value = stompClientAgent.active

      resolve()
    })
  }

  const stopWebSocket = () => {
    // 停止
    if (stompClient.value) {
      stompClient.value!.deactivate()
    }
  }

  const getStompClientActive = (): boolean => {
    return stompClientActive.value
  }

  // 获取广播消息
  const getBroadcast = computed(() => {
    return broadcast.value
  })

  // 获取推送消息
  const getPushNotices = computed(() => {
    return pushNotices.value
  })

  return {
    broadcast,
    pushNotices,
    createTenantNotice,
    getBroadcast,
    getPushNotices,
    startWebSocket,
    stopWebSocket,
    getStompClientActive
  }
})

/** 在 setup 外使用 */
export function useNoticeStoreHook() {
  return useNoticeStore(store)
}
