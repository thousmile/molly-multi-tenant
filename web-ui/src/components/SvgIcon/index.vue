<script lang="ts" setup>
import { computed } from "vue"

interface Props {
  name: string
  prefix?: string
  size?: number
}

const props = withDefaults(defineProps<Props>(), {
  prefix: "icon",
  size: 20
})

const symbolId = computed(() => `#${props.prefix}-${props.name}`)
</script>

<template>
  <svg class="svg-icon" aria-hidden="true" :font-size="size">
    <use :href="symbolId" />
  </svg>
</template>

<style lang="scss" scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  overflow: hidden;
  outline: none;
}
</style>
