<template>
  <div class="app-container">
    <el-row :gutter="10" class="chart">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <v-chart autoresize :option="option1" />
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <v-chart autoresize :option="option2" />
      </el-col>
    </el-row>
    <v-chart autoresize class="chart2" :option="option3" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue"
import { use } from "echarts/core"
import VChart from "vue-echarts"
import { CanvasRenderer } from "echarts/renderers"
import { PieChart, LineChart, BarChart } from "echarts/charts"
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent, ToolboxComponent } from "echarts/components"

use([
  CanvasRenderer,
  PieChart,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  ToolboxComponent
])

const toolbox1 = {
  show: true,
  feature: {
    mark: {
      show: true
    },
    dataView: {
      show: true,
      readOnly: false,
      title: "数据视图"
    },
    restore: {
      show: true,
      title: "还原"
    },
    saveAsImage: {
      show: true,
      title: "保存"
    }
  }
}

const toolbox2 = {
  show: true,
  feature: {
    mark: {
      show: true
    },
    dataView: {
      show: true,
      readOnly: false,
      title: "数据视图"
    },
    magicType: {
      show: true,
      type: ["line", "bar"],
      title: {
        line: "切换为折线图",
        bar: "切换为柱状图"
      }
    },
    restore: {
      show: true,
      title: "还原"
    },
    saveAsImage: {
      show: true,
      title: "保存"
    }
  }
}

const grid1 = {
  left: "3%",
  right: "3%",
  bottom: "3%",
  containLabel: true
}

const option1 = ref({
  title: {
    text: "用户来源",
    left: "center"
  },
  tooltip: {
    trigger: "item",
    formatter: "{b} : {c} ({d}%)"
  },
  grid: grid1,
  legend: {
    orient: "vertical",
    left: "left",
    data: ["微信", "腾讯QQ", "抖音", "微博", "知乎", "支付宝", "淘宝", "拼多多"]
  },
  toolbox: toolbox1,
  series: [
    {
      name: "Traffic Sources",
      type: "pie",
      radius: "55%",
      center: ["50%", "60%"],
      data: [
        { value: 1350, name: "微信" },
        { value: 2101, name: "腾讯QQ" },
        { value: 334, name: "抖音" },
        { value: 165, name: "微博" },
        { value: 190, name: "知乎" },
        { value: 1248, name: "支付宝" },
        { value: 548, name: "淘宝" },
        { value: 348, name: "拼多多" }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)"
        }
      }
    }
  ]
})

const option2 = ref({
  title: {
    text: "访问流量",
    left: "center"
  },
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "shadow"
    }
  },
  grid: grid1,
  toolbox: toolbox2,
  xAxis: [
    {
      type: "category",
      data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
      axisTick: {
        alignWithLabel: true
      }
    }
  ],
  yAxis: [
    {
      type: "value"
    }
  ],
  series: [
    {
      name: "API流量",
      type: "bar",
      barWidth: "60%",
      data: [19849, 45645, 22656, 111334, 64948, 53642, 36665]
    }
  ]
})

const option3 = ref({
  title: {
    text: "电商销售额"
  },
  tooltip: {
    trigger: "axis",
    formatter: (params: any) => {
      let relVal = params[0].name
      params.forEach((element: any) => {
        relVal += `<br/> ${element.marker} ${element.seriesName} : ${element.value} 万`
      })
      return relVal
    }
  },
  legend: {
    data: ["拼多多", "淘宝", "京东", "唯品会", "抖音直播"]
  },
  grid: grid1,
  toolbox: toolbox2,
  xAxis: {
    type: "category",
    boundaryGap: false,
    data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"]
  },
  yAxis: {
    type: "value"
  },
  series: [
    {
      name: "拼多多",
      type: "line",
      stack: "Total",
      data: [120, 132, 101, 134, 90, 230, 210]
    },
    {
      name: "淘宝",
      type: "line",
      stack: "Total",
      data: [220, 182, 191, 234, 290, 330, 310]
    },
    {
      name: "京东",
      type: "line",
      stack: "Total",
      data: [150, 232, 201, 154, 190, 330, 410]
    },
    {
      name: "唯品会",
      type: "line",
      stack: "Total",
      data: [320, 332, 301, 334, 390, 330, 320]
    },
    {
      name: "抖音直播",
      type: "line",
      stack: "Total",
      data: [820, 932, 901, 934, 1290, 1330, 1320]
    }
  ]
})
</script>

<style lang="scss" scoped>
.chart {
  height: 350px;
}

.chart2 {
  height: 500px;
  margin-top: 50px;
}
</style>
