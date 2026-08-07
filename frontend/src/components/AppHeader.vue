<template>
  <header>
    <h1>Finance Voucher System</h1>
    <small>多租户财务凭证系统 — Java 后端面试 Vibe Code Demo</small>
    <div class="headerRight">
      <select :value="company" @change="$emit('update:company', $event.target.value)">
        <option value="COMP-A">公司 A</option>
        <option value="COMP-B">公司 B</option>
      </select>
    </div>
  </header>
</template>

<script setup>

defineProps({
  company: { type: String, required: true }
})
defineEmits(['update:company'])
</script>
