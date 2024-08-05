<template>
  <div class="app-container" v-loading="loading" v-has="['pre_menu:view']">
    <el-card shadow="never" class="search-wrapper">
      <div class="flex">
        <el-button type="primary" :icon="Plus" v-has="['pre_menu:create']" @click="handleAdd(null)">新增</el-button>
        <el-button type="success" :icon="Sort" @click="switchExpandAndCollapse">{{
          expandAndCollapse ? "折叠" : "展开"
        }}</el-button>
      </div>
    </el-card>

    <el-card shadow="never">
      <div class="toolbar-wrapper">
        <el-table ref="tableRef" :data="tableData" row-key="id" :default-expand-all="expandAndCollapse">
          <el-table-column prop="menuId" label="权限ID" />
          <el-table-column prop="menuName" label="权限名称" />
          <el-table-column prop="perms" label="权限标识" />
          <el-table-column prop="component" label="权限组件" />
          <el-table-column prop="path" label="路由地址">
            <template #default="scope">
              <el-popover :content="scope.row.path" placement="top-start" :width="200" trigger="hover">
                <template #reference>
                  <el-text class="w-150px mb-2" truncated>
                    {{ scope.row.path }}
                  </el-text>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="icon" label="权限图标">
            <template #default="scope">
              <svg-icon v-if="scope.row.icon" :name="scope.row.icon" :size="28" />
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column prop="target" label="所属用户" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.target === 0" effect="dark">
                {{ dictStore.getMenuTarget(scope.row.target) }}
              </el-tag>
              <el-tag v-else-if="scope.row.target === 1" effect="light" type="info">
                {{ dictStore.getMenuTarget(scope.row.target) }}
              </el-tag>
              <el-tag v-else effect="plain" type="danger">
                {{ dictStore.getMenuTarget(scope.row.target) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="menuType" label="权限类型">
            <template #default="scope">
              <el-tag v-if="scope.row.menuType === 0" effect="plain" type="info">
                {{ dictStore.getMenuType(scope.row.menuType) }}
              </el-tag>
              <el-tag v-else effect="light" type="danger">
                {{ dictStore.getMenuType(scope.row.menuType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visible" label="状态">
            <template #default="scope">
              {{ dictStore.getShowHide(scope.row.visible) }}
            </template>
          </el-table-column>
          <el-table-column prop="keepAlive" label="保存状态">
            <template #default="scope">
              {{ dictStore.getYesNo(scope.row.keepAlive) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间">
            <template #default="scope">
              <operateUser :dateTime="scope.row.createTime" :entity="scope.row.createUserEntity" />
            </template>
          </el-table-column>
          <el-table-column prop="lastUpdateTime" label="修改时间">
            <template #default="scope">
              <operateUser :dateTime="scope.row.lastUpdateTime" :entity="scope.row.lastUpdateUserEntity" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-link
                :icon="Plus"
                v-if="scope.row.menuType === 0"
                v-has="['pre_menu:create']"
                type="primary"
                @click="handleAdd(scope.row)"
                >新增</el-link
              >
              &nbsp;
              <el-link :icon="Edit" type="warning" v-has="['pre_menu:update']" @click="handleEdit(scope.row)"
                >编辑</el-link
              >
              &nbsp;
              <el-link
                :icon="Delete"
                v-if="!scope.row.children"
                type="danger"
                v-has="['pre_menu:delete']"
                @click="handleDelete(scope.row)"
                >删除</el-link
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 新增和修改的弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%" :close-on-click-modal="false">
      <el-form
        ref="entityFormRef"
        :model="entityForm"
        :rules="entityFormRules"
        label-position="right"
        label-width="80px"
        @keyup.enter="handleSaveAndFlush"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="menuName" label="权限名称">
              <el-input v-model.trim="entityForm.menuName" placeholder="名称" type="text" tabindex="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="perms" label="权限标识">
              <el-input v-model.trim="entityForm.perms" placeholder="标识" type="text" tabindex="2" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="sort" label="权限排序">
              <el-input-number v-model="entityForm.sort" :min="1" :max="9999999" tabindex="6" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="menuType" label="权限类型">
              <select-dict-data v-model:value="entityForm.menuType" dictTypeKey="sys_menu_type" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="entityForm.menuType == 0">
          <el-col :span="12">
            <el-form-item prop="component" label="权限组件">
              <el-input v-model.trim="entityForm.component" placeholder="组件" type="text" tabindex="3" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="icon" label="图标">
              <svg-icon-select v-model:value="entityForm.icon" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="entityForm.menuType == 0">
          <el-col :span="12">
            <el-form-item prop="visible" label="显示隐藏">
              <select-dict-data v-model:value="entityForm.visible" dictTypeKey="sys_show_hide" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="path" label="地址" v-if="entityForm.menuType == 0">
              <el-input v-model.trim="entityForm.path" placeholder="路由" type="text" tabindex="5" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="target" label="权限所属">
              <select-dict-data v-model:value="entityForm.target" dictTypeKey="sys_menu_target" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="parentId" label="上级权限">
              <el-cascader
                v-model="entityForm.parentId"
                :options="parentNodes"
                :props="{ checkStrictly: true }"
                @change="cascaderChange"
                filterable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="keepAlive" label="保持状态">
              <select-dict-data v-model:value="entityForm.keepAlive" dictTypeKey="sys_yes_no" />
            </el-form-item>
          </el-col>
          <el-col :span="12"> &nbsp; </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" v-preventReClick @click="handleSaveAndFlush">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { treeMenuApi, saveMenuApi, updateMenuApi, deleteMenuApi } from "@/api/menu"
import { useDictStoreHook } from "@/store/modules/dict"
import { cloneDeep } from "lodash-es"
import { ISysMenu } from "@/types/pms"
import { ref, onMounted } from "vue"
import { Plus, Edit, Delete, Sort } from "@element-plus/icons-vue"
import SvgIconSelect from "@/components/SvgIconSelect/index.vue"
import {
  ElMessage,
  ElMessageBox,
  FormInstance,
  FormRules,
  TableInstance,
  type CascaderOption,
  type CascaderValue
} from "element-plus"
import { flatTreeToCascaderOption } from "@/utils"

const dictStore = useDictStoreHook()

/** 加载 */
const loading = ref(false)

// true : 新增，false : 修改
const saveFlag = ref(false)

const dialogVisible = ref(false)

const dialogTitle = ref("")

const tableRef = ref<TableInstance | null>(null)

const tableData = ref<ISysMenu[]>([])

/// 表单数据
const entityForm = ref<ISysMenu>({
  menuId: 0,
  parentId: 0,
  menuName: "",
  perms: "",
  component: "",
  icon: "",
  sort: 0,
  path: "",
  target: 2,
  menuType: 0,
  visible: 0,
  keepAlive: 0,
  children: []
})

const entityFormRef = ref<FormInstance | null>(null)

/// 表单校验规则
const entityFormRules: FormRules = {
  parentId: [{ required: true, message: "请选择上级权限", trigger: "blur" }],
  menuName: [{ required: true, message: "请输入名称", trigger: "blur" }],
  perms: [
    { required: true, message: "请输入权限标识", trigger: "blur" },
    { min: 5, max: 64, message: "长度最少 5 个字符", trigger: "blur" }
  ],
  component: [{ required: true, message: "请输入组件名称", trigger: "blur" }],
  icon: [{ required: true, message: "请输入图标名称", trigger: "blur" }],
  sort: [{ required: true, message: "请输入权限排序", trigger: "blur" }],
  path: [{ required: true, message: "请输入权限路由", trigger: "blur" }],
  target: [{ required: true, message: "请输入权限所属", trigger: "blur" }],
  menuType: [{ required: true, message: "请输入权限类型", trigger: "blur" }],
  visible: [{ required: true, message: "请输入显示或者隐藏", trigger: "blur" }],
  keepAlive: [{ required: true, message: "请选择保持状态", trigger: "blur" }]
}

// 上级权限
const parentNodes = ref<CascaderOption[]>()

const cascaderChange = (data: CascaderValue) => {
  const arr1 = data as number[]
  entityForm.value.parentId = arr1[arr1.length - 1]
}

const expandAndCollapse = ref(false)

// 展开和折叠
const switchExpandAndCollapse = () => {
  expandAndCollapse.value = !expandAndCollapse.value
  const deep = (arr1: ISysMenu[]) => {
    arr1.forEach((item: ISysMenu) => {
      if (item.children) {
        deep(item.children)
      }
      tableRef.value!.toggleRowExpansion(item, expandAndCollapse.value)
    })
  }
  deep(tableData.value!)
}

// 获取数据
const getTableData = () => {
  loading.value = true
  treeMenuApi()
    .then((resp) => {
      tableData.value = resp.data
      parentNodes.value = flatTreeToCascaderOption(resp.data, { value: "id", label: "menuName" })
      const temp: CascaderOption = {
        value: 0,
        label: "顶级权限"
      }
      parentNodes.value.unshift(temp)
    })
    .catch((err) => {
      console.log("err :>> ", err)
    })
    .finally(() => {
      loading.value = false
    })
}

// 重置 Entity 属性
const resetEntity = () => {
  entityForm.value = {
    menuId: 0,
    parentId: 0,
    menuName: "",
    perms: "",
    component: "",
    icon: "",
    sort: 0,
    path: "",
    target: 2,
    menuType: 0,
    visible: 0,
    keepAlive: 0,
    children: []
  }
}

// 添加
const handleAdd = (data: ISysMenu | null) => {
  resetEntity()
  if (data) {
    // 上级权限
    entityForm.value.parentId = data.menuId
    // 排序
    if (data.children) {
      entityForm.value.sort = (data.children.length + 1) * 10
    } else {
      entityForm.value.sort = 0
    }
  } else {
    // 上级权限
    entityForm.value.parentId = 0
    entityForm.value.sort = (tableData.value.length + 1) * 10
  }
  // 显示和隐藏
  entityForm.value.visible = 1
  dialogTitle.value = "新增"
  saveFlag.value = true
  dialogVisible.value = true
}

// 编辑
const handleEdit = (data: ISysMenu) => {
  resetEntity()
  entityForm.value = cloneDeep(data)
  dialogTitle.value = "修改"
  saveFlag.value = false
  dialogVisible.value = true
}

// 删除
const handleDelete = (data: ISysMenu) => {
  ElMessageBox.confirm(`确要删除 ${data.menuName} 吗?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteMenuApi(data.menuId)
        .then((resp) => {
          if (resp.data) {
            ElMessage({
              message: `删除 ${data.menuName} 成功！`,
              type: "success"
            })
          }
        })
        .catch((err) => {
          console.log("err :>> ", err)
        })
        .finally(() => {
          getTableData()
        })
    })
    .catch((error) => {
      console.log("error :>> ", error)
    })
}

// 新增和修改
const handleSaveAndFlush = () => {
  entityFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      if (saveFlag.value) {
        saveMenuApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "新增权限成功！",
                type: "success"
              })
            }
          })
          .catch((err) => {
            console.log("err :>> ", err)
          })
          .finally(() => {
            loading.value = false
            dialogVisible.value = false
            getTableData()
          })
      } else {
        updateMenuApi(entityForm.value)
          .then((resp) => {
            if (resp.data) {
              ElMessage({
                message: "修改权限成功！",
                type: "success"
              })
            }
          })
          .catch((err) => {
            console.log("err :>> ", err)
          })
          .finally(() => {
            loading.value = false
            dialogVisible.value = false
            getTableData()
          })
      }
    } else {
      loading.value = false
      return false
    }
  })
}

onMounted(() => {
  getTableData()
})
</script>

<style lang="scss" scoped></style>
