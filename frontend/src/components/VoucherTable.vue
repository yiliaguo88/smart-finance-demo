<template>
  <table>
    <thead>
      <tr><th>#</th><th>凭证号</th><th>类型</th><th>金额</th><th>期间</th><th>状态</th><th>创建人</th></tr>
    </thead>
    <tbody>
      <tr v-for="v in vouchers" :key="v.id">
        <td>{{ v.id }}</td>
        <td>{{ v.voucherNo }}</td>
        <td>{{ v.voucherType }}</td>
        <td class="priceUp">{{ formatAmt(v.totalAmount) }}</td>
        <td>{{ v.period }}</td>
        <td :class="v.status === 'POSTED' ? 'priceUp' : ''">{{ v.status }}</td>
        <td class="createdByCell">{{ v.createdBy }}</td>
      </tr>
    </tbody>
  </table>
  <div class="spreadBar">
    汇总金额: <span>{{ formatRawAmt(totalAmount) }}</span>
  </div>
  <p v-if="!vouchers.length" class="emptyHint">暂无凭证</p>
</template>

<script setup>

defineProps({
  vouchers: { type: Array, default: () => [] },
  totalAmount: { type: Number, default: 0 }
})

function formatAmt(v) {
  if (v == null) return '—'
  return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
}

function formatRawAmt(v) {
  if (v == null) return '—'
  return Number(v).toPrecision(17).replace(/\.?0+$/, '')
}
</script>
