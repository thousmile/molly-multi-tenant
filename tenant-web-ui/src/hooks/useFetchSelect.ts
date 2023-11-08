import { ref, onMounted } from "vue"

type OptionValue = string | number

/** Select 需要的数据格式 */
interface SelectOption {
  value: OptionValue
  label: string
  disabled?: boolean
}

/** 入参格式，暂时只需要传递 api 函数即可 */
interface FetchSelectProps {
  api: () => Promise<IJsonResult<SelectOption[]>>
}

export function useFetchSelect(props: FetchSelectProps) {
  const { api } = props

  const loading = ref<boolean>(false)

  const options = ref<SelectOption[]>([])

  const value = ref<OptionValue>("")

  /** 调用接口获取数据 */
  const loadData = () => {
    loading.value = true
    options.value = []
    api()
      .then((resp) => {
        const { status, data } = resp
        if (status === 200) {
          options.value = data
        }
      })
      .finally(() => {
        loading.value = false
      })
  }

  onMounted(() => {
    loadData()
  })

  return {
    loading,
    options,
    value
  }
}
