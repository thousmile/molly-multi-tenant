<template>
  <div>
    <el-form ref="searchFormRef" :inline="true">
      <el-form-item>
        <el-button type="primary" link :icon="Sort" @click="switchExpandAndCollapse">{{
          expandAndCollapse ? "折叠" : "展开"
          }}</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" link :icon="checkAll ? 'CloseBold' : 'Select'" @click="switchCheckAll">{{
          checkAll ? "取消" : "全选"
          }}</el-button>
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="checkStrictly" label="父子联动" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="filterMenuText" clearable placeholder="根据名称筛选" />
      </el-form-item>
    </el-form>
    <br>
    <div :style="{ height: '300px', overflowY: 'scroll' }">
      <el-tree ref="treeRef" :data="props.data" :check-strictly="!checkStrictly" :default-expand-all="expandAndCollapse"
        :filter-node-method="filterNode" show-checkbox node-key="id" highlight-current
        :props="{ children: 'children', label: 'name', }" />
    </div>
  </div>
</template>

<script setup lang='ts'>
import { ITreeNode } from '@/types/base';
import { Sort } from "@element-plus/icons-vue"
import { ElTree } from 'element-plus';
import { ref, computed, watch, onMounted } from 'vue'

const props = defineProps({
  data: {
    type: Array<ITreeNode>,
    required: true
  }
})

const filterMenuText = ref("")

// 展开
const expandAndCollapse = ref(true)

// 全选
const checkAll = ref(true)

// 父子联动
const checkStrictly = ref(false)

const treeRef = ref<InstanceType<typeof ElTree>>()

// 获取 全部菜单ID
const getMenuIdAll = () => {
  const result: number[] = []
  const deep = (arr1: ITreeNode[], arr2: number[]) => {
    arr1.forEach((item: ITreeNode) => {
      arr2.push(item.id)
      if (item.children && item.children.length > 0) {
        deep(item.children, arr2)
      }
    })
  }
  deep(props.data, result)
  return result
}

// 展开和折叠
const switchExpandAndCollapse = () => {
  expandAndCollapse.value = !expandAndCollapse.value
  props.data.forEach((v) => {
    treeRef.value!.store.nodesMap[v.id].expanded = expandAndCollapse.value
  })
}

// 全选和取消全选
const switchCheckAll = () => {
  checkAll.value = !checkAll.value
  if (checkAll.value) {
    const result: number[] = getMenuIdAll()
    treeRef.value!.setCheckedKeys(result, false)
  } else {
    treeRef.value!.setCheckedKeys([], false)
  }
}

watch(filterMenuText, (val) => {
  treeRef.value!.filter(val)
})

const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.name.includes(value)
}

// 设置 tree 菜单数据
const setCheckedKeys = (value: number[]) => {
  checkAll.value = value.length > 0
  treeRef.value!.setCheckedKeys(value, false)
  expandAndCollapse.value = true
}

// 获取 tree 菜单数据
const getCheckedKeys = (): number[] => {
  return treeRef.value!.getCheckedKeys(false) as number[]
}

defineExpose({
  setCheckedKeys,
  getCheckedKeys
});

</script>

<style lang='scss' scoped></style>
