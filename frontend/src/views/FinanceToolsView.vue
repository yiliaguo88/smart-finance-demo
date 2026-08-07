<template>
  <div class="container">
    <TaxCalculatorPanel />
    <LedgerBatchPanel />
    <MonthlyReportPanel />
  </div>
</template>

<script setup>
import TaxCalculatorPanel from '../components/TaxCalculatorPanel.vue'
import LedgerBatchPanel from '../components/LedgerBatchPanel.vue'
import MonthlyReportPanel from '../components/MonthlyReportPanel.vue'
</script>
