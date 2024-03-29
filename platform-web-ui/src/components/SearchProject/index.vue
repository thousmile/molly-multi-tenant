<template>
  <el-container v-loading="loading">
    <el-header>
      <el-row :gutter="20">
        <el-col :span="16">
          <el-input v-model="params.keywords" placeholder="根据项目名称搜索" clearable :prefix-icon="Search" />
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="searchProjectList">搜索</el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table :data="simpleProjects" style="width: 100%">
        <el-table-column prop="projectId" label="项目ID" />
        <el-table-column prop="projectName" label="项目名称">
          <template #default="scope">
            <strong style="color: #409eff" v-if="!currentProjectId(scope.row.projectId)">
              {{ scope.row.projectName }}
            </strong>
            <span v-else> {{ scope.row.projectName }} </span>
          </template>
        </el-table-column>
        <el-table-column prop="linkman" label="联系人" />
        <el-table-column prop="areaCode" label="行政区域">
          <template #default="scope">
            {{ showChinaArea(scope.row.areaCode) }}
          </template>
        </el-table-column>
        <el-table-column prop="address" label="联系地址" />
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button
              v-if="currentProjectId(scope.row.projectId)"
              text
              type="primary"
              @click="handleSwitchClick(scope.row)"
              >切换</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer>
      <el-pagination
        v-model:current-page="params.pageIndex"
        :page-size="params.pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :background="true"
        layout="sizes, total, prev, pager, next, jumper"
        :total="params.pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from "vue"
import { Search } from "@element-plus/icons-vue"
import { ISearchQuery, ISimpleProject } from "@/types/base"
import { simpleQueryProjectApi } from "@/api/project"
import { useProjectStoreHook } from "@/store/modules/project"
import { showChinaArea } from "@/hooks/useIndex"

const projectStore = useProjectStoreHook()

const simpleProjects = ref<ISimpleProject[]>()

/** 加载 */
const loading = ref(false)

const params = reactive({
  pageTotal: 0,
  pageIndex: 1,
  pageSize: 5,
  keywords: ""
})

const emit = defineEmits<{
  (e: "handleSwitch", id: string): void
}>()

// 切换项目
const handleSwitchClick = (t: ISimpleProject) => {
  const project: ISimpleProject = {
    projectId: t.projectId,
    projectName: t.projectName,
    areaCode: t.areaCode,
    address: t.address,
    linkman: t.linkman
  }
  projectStore.setCurrentProject(project)
  emit("handleSwitch", t.projectName)
}

const handleSizeChange = (val: number) => {
  params.pageSize = val
  searchProjectList()
}

const handleCurrentChange = (val: number) => {
  params.pageIndex = val
  searchProjectList()
}

const currentProjectId = computed(() => {
  return (projectId: number) => {
    return projectStore.getCurrentProjectId() !== projectId
  }
})

const searchProjectList = () => {
  loading.value = true
  const p: ISearchQuery = {
    pageIndex: params.pageIndex,
    pageSize: params.pageSize,
    keywords: params.keywords
  }
  simpleQueryProjectApi(p)
    .then((resp) => {
      simpleProjects.value = resp.data.list
      params.pageTotal = resp.data.total
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
    .finally(() => {
      loading.value = false
    })
}

const searchList = () => searchProjectList()

// 输出组件的方法，让外部组件可以调用
defineExpose({
  searchList
})
</script>

<style lang="scss" scoped>
.el-header {
  --el-header-padding: 0px;
}

.el-main {
  --el-main-padding: 20px 0px;
}

.el-footer {
  --el-main-padding: 0px;
}
</style>
