<script lang="ts" setup>
import { testExternal } from "@/utils/validate"

interface Props {
  to: string
}

const props = defineProps<Props>()
</script>

<template>
  <a v-if="testExternal(props.to)" :href="props.to" target="_blank" rel="noopener">
    <slot />
  </a>
  <router-link v-else :to="props.to">
    <slot />
  </router-link>
</template>
