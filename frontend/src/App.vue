<template>
  <div>
    <AppHeader v-model:company="selectedCompany" />
    <nav class="mainNav">
      <RouterLink to="/vouchers" class="navLink">凭证管理</RouterLink>
      <RouterLink to="/finance-tools" class="navLink">财务工具</RouterLink>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { ref, provide } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import AppHeader from './components/AppHeader.vue'

const selectedCompany = ref('COMP-A')

provide('selectedCompany', selectedCompany)
</script>
