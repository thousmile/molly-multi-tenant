<script lang="ts" setup>
import { IPushMessage } from "@/types/base"

interface Props {
  list: IPushMessage[]
}

const props = defineProps<Props>()
</script>

<template>
  <el-empty v-if="props.list.length === 0" />
  <el-card v-else v-for="(item, index) in props.list" :key="index" shadow="never" class="card-container">
    <template #header>
      <div class="card-header">
        <div>
          <span class="card-title">{{ item.title }}</span>
          <div class="card-time">{{ item.createTime }}</div>
        </div>
      </div>
    </template>
    <div class="card-body">
      {{ item.message ?? "No Data" }}
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.card-container {
  margin-bottom: 10px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      font-weight: bold;
      margin-right: 10px;
    }

    .card-time {
      font-size: 12px;
      color: grey;
    }

    .card-avatar {
      display: flex;
      align-items: center;
    }
  }

  .card-body {
    font-size: 12px;
  }
}
</style>
