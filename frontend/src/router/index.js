import { createRouter, createWebHashHistory } from 'vue-router'
import VoucherView from '../views/VoucherView.vue'
import FinanceToolsView from '../views/FinanceToolsView.vue'

const routes = [
  { path: '/', redirect: '/vouchers' },
  { path: '/vouchers', name: 'vouchers', component: VoucherView },
  { path: '/finance-tools', name: 'financeTools', component: FinanceToolsView }
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
