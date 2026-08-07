<template>
  <div class="panel">
    <h3>月度凭证统计 — FinanceStatisticsCollector</h3>
    <div class="scenarioBox">
      <span class="scenarioLabel">场景</span>
      月初开账，统计服务重启后首批凭证集中涌入。模拟处理 30 笔当月凭证，查看月度汇总报表。
    </div>
    <button class="btnSubmit reportBtn" @click="triggerBugH" :disabled="loading">
      {{ loading ? '报表生成中...' : '生成月度报表' }}
    </button>
    <div v-if="result" class="ledgerResultBox">
      <div class="ledgerCard">
        <div class="ledgerCardTitle">月度凭证汇总报表</div>
        <div class="ledgerRow">
          <span>实际录入笔数</span>
          <b class="amtNeutral">{{ result.expected }} 笔</b>
        </div>
        <div class="ledgerRow">
          <span>系统统计笔数</span>
          <b :class="result.actual < result.expected ? 'amtErr' : 'amtOk'">{{ result.actual }} 笔</b>
        </div>
        <div v-if="result.lost > 0" class="ledgerAlert">
          统计缺失 {{ result.lost }} 笔，月末报表数据不完整
        </div>
        <div v-else class="ledgerPass">统计核对一致</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const result  = ref(null)
const loading = ref(false)

async function triggerBugH() {
  loading.value = true
  result.value  = null
  try {
    const res = await fetch('/api/report/stats/trigger-bug-h', { method: 'POST' })
    result.value = await res.json()
  } finally {
    loading.value = false
  }
}
</script>
