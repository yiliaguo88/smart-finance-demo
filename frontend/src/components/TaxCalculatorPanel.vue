<template>
  <div class="panel">
    <h3>增值税计算 — TaxCalculator</h3>
    <div class="formRow">
      <div class="formGroup">
        <label>含税金额（分）</label>
        <input type="number" v-model.number="taxForm.amount" placeholder="e.g. 11300" />
      </div>
      <div class="formGroup">
        <label>税率（%）</label>
        <input type="number" v-model.number="taxForm.taxRate" placeholder="13" />
      </div>
    </div>
    <button class="btnSubmit taxBtn" @click="calcTax">计算税额</button>
    <div v-if="taxResult" class="taxResultBox">
      <div class="result rej">
        税额 = <b>{{ taxResult.taxAmount }}</b> 分
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const taxForm = reactive({ amount: 11300, taxRate: 13 })
const taxResult = ref(null)

async function calcTax() {
  const res = await fetch(`/api/tax/calculate?amount=${taxForm.amount}&taxRate=${taxForm.taxRate}`)
  taxResult.value = await res.json()
}
</script>
