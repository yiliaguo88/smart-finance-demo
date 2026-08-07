<template>
  <div class="panel">
    <h3>月末批量入账 — LedgerBalanceService</h3>
    <div class="scenarioBox">
      <span class="scenarioLabel">场景</span>
      月末关账日，财务部 50 名会计同时提交费用报销凭证，均记「银行存款（1002）」借方，每笔 ¥100，合计应到账 ¥5,000。
    </div>
    <button class="btnSubmit ledgerBtn" @click="triggerBugD" :disabled="loading">
      {{ loading ? '入账处理中...' : '执行批量入账' }}
    </button>
    <div v-if="result" class="ledgerResultBox">
      <div class="ledgerCard">
        <div class="ledgerCardTitle">科目 1002 · 银行存款 — 对账结果</div>
        <div class="ledgerRow">
          <span>应到账合计</span>
          <b class="amtNeutral">¥5,000.00</b>
        </div>
        <div class="ledgerRow">
          <span>账面实际余额</span>
          <b :class="result.bugTriggered ? 'amtErr' : 'amtOk'">{{ formatAmt(result.actual) }}</b>
        </div>
        <div v-if="result.bugTriggered" class="ledgerAlert">
          账务差异 {{ formatAmt(result.lost) }}，部分凭证未能入账
        </div>
        <div v-else class="ledgerPass">账务核对一致</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const result  = ref(null)
const loading = ref(false)

async function triggerBugD() {
  loading.value = true
  result.value  = null
  try {
    const res = await fetch('/api/report/ledger/trigger-bug-d', { method: 'POST' })
    result.value = await res.json()
  } finally {
    loading.value = false
  }
}

function formatAmt(v) {
  if (v == null) return '—'
  return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
}
</script>
