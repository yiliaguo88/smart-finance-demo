<template>
  <div class="container">
    <div class="panel">
      <h3>凭证列表 — VoucherServiceImpl</h3>
      <VoucherTable :vouchers="vouchers" :total-amount="totalAmountFromServer" />
    </div>

    <VoucherForm :company="selectedCompany" :submit-count="submitCount" @submitted="onSubmitted" />

    <div class="panel">
      <h3>多租户上下文 — TenantContextHolder</h3>
      <div class="tenantNote">
        当前公司选择（页面右上角切换）会决定请求携带的租户标识，凭证列表按租户隔离展示。切换公司后重新拉取列表验证隔离是否生效。
      </div>
    </div>
  </div>
</template>

<script setup>
import { inject, ref, watch, onMounted } from 'vue'
import VoucherTable from '../components/VoucherTable.vue'
import VoucherForm from '../components/VoucherForm.vue'

const selectedCompany = inject('selectedCompany')

const vouchers = ref([])
const totalAmountFromServer = ref(0)
const submitCount = ref(0)

async function fetchVouchers() {
  const res  = await fetch(`/api/vouchers/mine?period=2024-01`, {
    headers: { 'X-Company-Id': selectedCompany.value }
  })
  const data = await res.json()
  vouchers.value = data.vouchers || []
  totalAmountFromServer.value = data.totalAmount || 0
}

function onSubmitted(result) {
  if (result.status === 'ok') {
    submitCount.value++
    fetchVouchers()
  }
}

watch(selectedCompany, fetchVouchers)

onMounted(fetchVouchers)
</script>
