<template>
  <div class="panel">
    <h3>提交凭证 <span class="submitCounter">已提交 {{ submitCount }} 笔</span></h3>
    <div class="formGroup">
      <label>公司</label>
      <select v-model="form.companyId">
        <option value="COMP-A">COMP-A</option>
        <option value="COMP-B">COMP-B</option>
      </select>
    </div>
    <div class="formRow">
      <div class="formGroup">
        <label>期间 (YYYY-MM)</label>
        <input type="text" v-model="form.period" placeholder="2024-01" />
      </div>
      <div class="formGroup">
        <label>凭证类型</label>
        <select v-model="form.voucherType">
          <option value="EXPENSE">EXPENSE</option>
          <option value="INCOME">INCOME</option>
          <option value="TRANSFER">TRANSFER</option>
        </select>
      </div>
    </div>
    <div class="formRow">
      <div class="formGroup">
        <label>总金额</label>
        <input type="number" v-model.number="form.totalAmount" placeholder="e.g. 5000.00" step="0.01" />
      </div>
      <div class="formGroup">
        <label>创建人</label>
        <input type="text" v-model="form.createdBy" />
      </div>
    </div>
    <button class="btnSubmit" @click="submitVoucher" :disabled="isSubmitting">
      {{ isSubmitting ? '提交中...' : '提交凭证' }}
    </button>
    <div class="error">{{ formError }}</div>
    <div v-if="lastResult" :class="['result', lastResult.status === 'ok' ? 'ok' : 'rej']">
      <span v-if="lastResult.status === 'ok'">
        ✓ {{ lastResult.voucherNo }} 已保存 #{{ lastResult.savedId }}
      </span>
      <span v-else>✗ {{ lastResult.reason || lastResult.message }}</span>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  company: { type: String, required: true },
  submitCount: { type: Number, default: 0 }
})

const emit = defineEmits(['submitted'])

const form = reactive({
  companyId: props.company, period: '2024-01', voucherType: 'EXPENSE',
  totalAmount: '', createdBy: 'alice'
})

watch(() => props.company, (val) => { form.companyId = val })

const formError    = ref('')
const lastResult    = ref(null)
const isSubmitting  = ref(false)

function validateForm() {
  if (!form.companyId) { formError.value = '请选择公司'; return false }
  if (!form.period?.match(/^\d{4}-\d{2}$/)) { formError.value = '期间格式须为 YYYY-MM'; return false }
  if (!form.totalAmount || form.totalAmount <= 0) { formError.value = '金额须大于 0'; return false }
  formError.value = ''; return true
}

async function submitVoucher() {
  if (!validateForm()) return
  if (isSubmitting.value) return

  isSubmitting.value = true
  try {
    const payload = {
      companyId: form.companyId, period: form.period,
      voucherType: form.voucherType,
      totalAmount: form.totalAmount, createdBy: form.createdBy,
      entries: [
        { accountCode: '6601', accountName: '费用', debitAmount: form.totalAmount, creditAmount: 0 },
        { accountCode: '1002', accountName: '银行存款', debitAmount: 0, creditAmount: form.totalAmount }
      ]
    }
    const res = await fetch('/api/vouchers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'X-Company-Id': form.companyId },
      body: JSON.stringify(payload)
    })

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}: ${res.statusText}`)
    }

    const data = await res.json()
    lastResult.value = data
    emit('submitted', data)
  } catch (err) {
    lastResult.value = { status: 'error', message: err.message }
  } finally {
    isSubmitting.value = false
  }
}
</script>
