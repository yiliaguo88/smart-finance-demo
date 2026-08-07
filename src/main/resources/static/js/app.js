
(function () {
  'use strict';

  var state = {
    selectedCompany: 'COMP-A',
    vouchers: [],
    totalAmountFromServer: 0,
    submitCount: 0,
    isSubmitting: false,
    form: {
      companyId: 'COMP-A',
      period: '2024-01',
      voucherType: 'EXPENSE',
      totalAmount: '',
      createdBy: 'alice'
    },
    tax: { amount: 11300, taxRate: 13 },
    bugDLoading: false,
    bugHLoading: false
  };

  var el = {
    submitCount: document.getElementById('submitCount'),
    companySelect: document.getElementById('companySelect'),
    voucherTableBody: document.getElementById('voucherTableBody'),
    totalAmountFromServer: document.getElementById('totalAmountFromServer'),
    emptyHint: document.getElementById('emptyHint'),

    formCompanyId: document.getElementById('formCompanyId'),
    formPeriod: document.getElementById('formPeriod'),
    formVoucherType: document.getElementById('formVoucherType'),
    formTotalAmount: document.getElementById('formTotalAmount'),
    formCreatedBy: document.getElementById('formCreatedBy'),
    btnSubmit: document.getElementById('btnSubmit'),
    formError: document.getElementById('formError'),
    resultBox: document.getElementById('resultBox'),

    taxAmount: document.getElementById('taxAmount'),
    taxRate: document.getElementById('taxRate'),
    btnCalcTax: document.getElementById('btnCalcTax'),
    taxResultBox: document.getElementById('taxResultBox'),

    btnBugD: document.getElementById('btnBugD'),
    bugDResultBox: document.getElementById('bugDResultBox'),

    btnBugH: document.getElementById('btnBugH'),
    bugHResultBox: document.getElementById('bugHResultBox')
  };

  function escapeHtml(v) {
    if (v == null) return '';
    return String(v)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  function formatAmt(v) {
    if (v == null) return '—';
    return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 });
  }

  function formatRawAmt(v) {
    if (v == null) return '—';
    return Number(v).toPrecision(17).replace(/\.?0+$/, '');
  }

  // ---- 初始表单默认值 ----
  el.formPeriod.value = state.form.period;
  el.formCreatedBy.value = state.form.createdBy;
  el.taxAmount.value = state.tax.amount;
  el.taxRate.value = state.tax.taxRate;

  // ---- 渲染 ----
  function renderVouchers() {
    el.voucherTableBody.innerHTML = state.vouchers.map(function (v) {
      var statusClass = v.status === 'POSTED' ? 'priceUp' : '';
      return '' +
        '<tr>' +
        '<td>' + escapeHtml(v.id) + '</td>' +
        '<td>' + escapeHtml(v.voucherNo) + '</td>' +
        '<td>' + escapeHtml(v.voucherType) + '</td>' +
        '<td class="priceUp">' + formatAmt(v.totalAmount) + '</td>' +
        '<td>' + escapeHtml(v.period) + '</td>' +
        '<td class="' + statusClass + '">' + escapeHtml(v.status) + '</td>' +
        '<td style="color:#8b949e">' + escapeHtml(v.createdBy) + '</td>' +
        '</tr>';
    }).join('');
    el.emptyHint.style.display = state.vouchers.length ? 'none' : 'block';
    el.totalAmountFromServer.textContent = formatRawAmt(state.totalAmountFromServer);
  }

  function renderResultBox(result) {
    if (!result) { el.resultBox.innerHTML = ''; return; }
    if (result.status === 'ok') {
      el.resultBox.innerHTML = '<div class="result ok">✓ ' +
        escapeHtml(result.voucherNo) + ' 已保存 #' + escapeHtml(result.savedId) + '</div>';
    } else {
      el.resultBox.innerHTML = '<div class="result rej">✗ ' +
        escapeHtml(result.reason || result.message) + '</div>';
    }
  }

  function renderTaxResult(result) {
    if (!result) { el.taxResultBox.innerHTML = ''; return; }
    el.taxResultBox.innerHTML =
      '<div style="margin-top:8px">' +
      '<div class="result rej">税额 = <b>' + escapeHtml(result.taxAmount) + '</b> 分</div>' +
      '</div>';
  }

  function renderBugDResult(result) {
    if (!result) { el.bugDResultBox.innerHTML = ''; return; }
    el.bugDResultBox.innerHTML =
      '<div style="margin-top:12px">' +
      '<div class="ledgerCard">' +
      '<div class="ledgerCardTitle">科目 1002 · 银行存款 — 对账结果</div>' +
      '<div class="ledgerRow"><span>应到账合计</span><b class="amtNeutral">¥5,000.00</b></div>' +
      '<div class="ledgerRow"><span>账面实际余额</span><b class="' +
        (result.bugTriggered ? 'amtErr' : 'amtOk') + '">' + formatAmt(result.actual) + '</b></div>' +
      (result.bugTriggered
        ? '<div class="ledgerAlert">账务差异 ' + formatAmt(result.lost) + '，部分凭证未能入账</div>'
        : '<div class="ledgerPass">账务核对一致</div>') +
      '</div></div>';
  }

  function renderBugHResult(result) {
    if (!result) { el.bugHResultBox.innerHTML = ''; return; }
    el.bugHResultBox.innerHTML =
      '<div style="margin-top:12px">' +
      '<div class="ledgerCard">' +
      '<div class="ledgerCardTitle">月度凭证汇总报表</div>' +
      '<div class="ledgerRow"><span>实际录入笔数</span><b class="amtNeutral">' + escapeHtml(result.expected) + ' 笔</b></div>' +
      '<div class="ledgerRow"><span>系统统计笔数</span><b class="' +
        (result.actual < result.expected ? 'amtErr' : 'amtOk') + '">' + escapeHtml(result.actual) + ' 笔</b></div>' +
      (result.lost > 0
        ? '<div class="ledgerAlert">统计缺失 ' + escapeHtml(result.lost) + ' 笔，月末报表数据不完整</div>'
        : '<div class="ledgerPass">统计核对一致</div>') +
      '</div></div>';
  }

  // ---- 数据请求 ----
  function fetchVouchers() {
    return fetch('/api/vouchers/mine?period=2024-01', {
      headers: { 'X-Company-Id': state.selectedCompany }
    })
      .then(function (res) { return res.json(); })
      .then(function (data) {
        state.vouchers = data.vouchers || [];
        state.totalAmountFromServer = data.totalAmount || 0;
        renderVouchers();
      });
  }

  function validateForm() {
    if (!state.form.companyId) { el.formError.textContent = '请选择公司'; return false; }
    if (!/^\d{4}-\d{2}$/.test(state.form.period || '')) { el.formError.textContent = '期间格式须为 YYYY-MM'; return false; }
    if (!state.form.totalAmount || state.form.totalAmount <= 0) { el.formError.textContent = '金额须大于 0'; return false; }
    el.formError.textContent = '';
    return true;
  }

  function submitVoucher() {
    if (!validateForm()) return;
    if (state.isSubmitting) return;

    state.isSubmitting = true;
    el.btnSubmit.disabled = true;
    el.btnSubmit.textContent = '提交中...';

    var payload = {
      companyId: state.form.companyId,
      period: state.form.period,
      voucherType: state.form.voucherType,
      totalAmount: state.form.totalAmount,
      createdBy: state.form.createdBy,
      entries: [
        { accountCode: '6601', accountName: '费用', debitAmount: state.form.totalAmount, creditAmount: 0 },
        { accountCode: '1002', accountName: '银行存款', debitAmount: 0, creditAmount: state.form.totalAmount }
      ]
    };

    fetch('/api/vouchers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'X-Company-Id': state.form.companyId },
      body: JSON.stringify(payload)
    })
      .then(function (res) {
        if (!res.ok) throw new Error('HTTP ' + res.status + ': ' + res.statusText);
        return res.json();
      })
      .then(function (data) {
        renderResultBox(data);
        state.submitCount++;
        el.submitCount.textContent = state.submitCount;
        if (data.status === 'ok') return fetchVouchers();
      })
      .catch(function (err) {
        renderResultBox({ status: 'error', message: err.message });
      })
      .finally(function () {
        state.isSubmitting = false;
        el.btnSubmit.disabled = false;
        el.btnSubmit.textContent = '提交凭证';
      });
  }

  function calcTax() {
    return fetch('/api/tax/calculate?amount=' + state.tax.amount + '&taxRate=' + state.tax.taxRate)
      .then(function (res) { return res.json(); })
      .then(function (data) { renderTaxResult(data); });
  }

  function triggerBugD() {
    state.bugDLoading = true;
    el.btnBugD.disabled = true;
    el.btnBugD.textContent = '入账处理中...';
    renderBugDResult(null);

    return fetch('/api/report/ledger/trigger-bug-d', { method: 'POST' })
      .then(function (res) { return res.json(); })
      .then(function (data) { renderBugDResult(data); })
      .finally(function () {
        state.bugDLoading = false;
        el.btnBugD.disabled = false;
        el.btnBugD.textContent = '执行批量入账';
      });
  }

  function triggerBugH() {
    state.bugHLoading = true;
    el.btnBugH.disabled = true;
    el.btnBugH.textContent = '报表生成中...';
    renderBugHResult(null);

    return fetch('/api/report/stats/trigger-bug-h', { method: 'POST' })
      .then(function (res) { return res.json(); })
      .then(function (data) { renderBugHResult(data); })
      .finally(function () {
        state.bugHLoading = false;
        el.btnBugH.disabled = false;
        el.btnBugH.textContent = '生成月度报表';
      });
  }

  // ---- 事件绑定 ----
  el.companySelect.addEventListener('change', function (e) {
    state.selectedCompany = e.target.value;
    state.form.companyId = state.selectedCompany;
    el.formCompanyId.value = state.selectedCompany;
    fetchVouchers();
  });

  el.formCompanyId.addEventListener('change', function (e) { state.form.companyId = e.target.value; });
  el.formPeriod.addEventListener('input', function (e) { state.form.period = e.target.value; });
  el.formVoucherType.addEventListener('change', function (e) { state.form.voucherType = e.target.value; });
  el.formTotalAmount.addEventListener('input', function (e) { state.form.totalAmount = e.target.value === '' ? '' : Number(e.target.value); });
  el.formCreatedBy.addEventListener('input', function (e) { state.form.createdBy = e.target.value; });
  el.btnSubmit.addEventListener('click', submitVoucher);

  el.taxAmount.addEventListener('input', function (e) { state.tax.amount = Number(e.target.value); });
  el.taxRate.addEventListener('input', function (e) { state.tax.taxRate = Number(e.target.value); });
  el.btnCalcTax.addEventListener('click', calcTax);

  el.btnBugD.addEventListener('click', triggerBugD);
  el.btnBugH.addEventListener('click', triggerBugH);

  // ---- 初始加载 ----
  fetchVouchers();
})();
