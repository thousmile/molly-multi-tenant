<template>
  <el-popover placement="bottom-start" :width="540" v-model:visible="showChooseIcon" trigger="click">
    <template #reference>
      <el-input v-model="result" placeholder="点击选择图标" v-click-outside="hideSelectIcon" readonly>
        <template #prefix>
          <svg-icon v-if="result" :name="result" :size="18" />
          <el-icon v-else><search /></el-icon>
        </template>
      </el-input>
    </template>
    <div class="icon-body">
      <el-input
        v-model="iconName"
        style="position: relative"
        clearable
        placeholder="请输入图标名称"
        @clear="filterIcons"
        @input="filterIcons"
        :prefix-icon="Search"
      />
      <br />
      <br />
      <div class="icon-list">
        <div v-for="(item, index) in iconList" :key="index" @click="handleSelect(item)">
          <svg-icon :name="item" :size="18" /> &nbsp;&nbsp;
          <span>{{ item }}</span>
        </div>
      </div>
    </div>
  </el-popover>
</template>

<script setup lang="ts">
import { computed, ref } from "vue"
import { Search } from "@element-plus/icons-vue"
import { ClickOutside as vClickOutside } from "element-plus"

const icons: string[] = []
const modules = import.meta.glob("../../icons/svg/*.svg")
for (const path in modules) {
  const p = path.split("icons/svg/")[1].split(".svg")[0]
  icons.push(p)
}

const props = defineProps({
  value: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: "请选择图标"
  }
})

const emit = defineEmits<{
  (e: "update:value", value: String): void
}>()

const result = computed({
  get(): String {
    return props.value
  },
  set(v: String) {
    emit("update:value", v)
  }
})

const iconName = ref("")

const iconList = ref(icons)

const showChooseIcon = ref(false)

function filterIcons() {
  iconList.value = icons
  if (iconName.value) {
    iconList.value = icons.filter((item) => item.indexOf(iconName.value) !== -1)
  }
}

/** 选择图标 */
function handleSelect(name: string) {
  showChooseIcon.value = false
  result.value = name
}

/** 图标外层点击隐藏下拉列表 */
function hideSelectIcon(event: any) {
  const elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget
  const className = elem.className
  if (className !== "el-input__inner") {
    showChooseIcon.value = false
  }
}
</script>

<style lang="scss" scoped>
.icon-body {
  width: 100%;
  padding: 10px;
  .icon-list {
    height: 200px;
    overflow-y: scroll;

    div {
      height: 30px;
      line-height: 30px;
      margin-bottom: -5px;
      cursor: pointer;
      width: 33%;
      float: left;
    }

    span svg {
      display: inline-block;
      vertical-align: -0.15em;
      fill: currentColor;
      overflow: hidden;
    }
  }
}
</style>
