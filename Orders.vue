<template>
  <div class="orders-page">
    <!-- 面包屑 -->
    <div class="breadcrumb">
      <router-link to="/">首页</router-link>
      <span class="sep">/</span>
      <span class="current">我的订单</span>
    </div>

    <!-- 订单状态筛选 -->
    <div class="order-tabs">
      <button
        class="order-tab"
        :class="{ active: activeTab === 'all' }"
        @click="activeTab = 'all'"
      >全部订单</button>
      <button
        class="order-tab"
        :class="{ active: activeTab === 'pending' }"
        @click="activeTab = 'pending'"
      >待付款</button>
      <button
        class="order-tab"
        :class="{ active: activeTab === 'paid' }"
        @click="activeTab = 'paid'"
      >待发货</button>
      <button
        class="order-tab"
        :class="{ active: activeTab === 'shipped' }"
        @click="activeTab = 'shipped'"
      >待收货</button>
      <button
        class="order-tab"
        :class="{ active: activeTab === 'completed' }"
        @click="activeTab = 'completed'"
      >已完成</button>
    </div>

    <!-- 空状态 -->
    <div v-if="filteredOrders.length === 0" class="empty-orders">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" width="64" height="64" fill="none" stroke="#dfe4ea" stroke-width="1.5">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
          <polyline points="14 2 14 8 20 8"/>
          <line x1="16" y1="13" x2="8" y2="13"/>
          <line x1="16" y1="17" x2="8" y2="17"/>
        </svg>
      </div>
      <h3>暂无订单</h3>
      <p>快去挑选心仪的商品吧</p>
      <router-link to="/products" class="go-shop-btn">去购物</router-link>
    </div>

    <!-- 订单列表 -->
    <div v-else class="orders-list">
      <div v-for="order in filteredOrders" :key="order.id" class="order-card">
        <!-- 订单头部 -->
        <div class="order-header">
          <div class="order-header-left">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <span class="order-time">{{ order.createTime }}</span>
          </div>
          <span class="order-status" :class="order.status">{{ getStatusText(order.status) }}</span>
        </div>

        <!-- 订单商品 -->
        <div class="order-items">
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <div class="item-img-wrap">
              <img :src="item.image" :alt="item.name" />
            </div>
            <div class="item-info">
              <h4 class="item-name">{{ item.name }}</h4>
              <p class="item-spec">默认规格</p>
            </div>
            <div class="item-price-info">
              <span class="item-price">¥{{ item.price }}</span>
              <span class="item-qty">x{{ item.quantity }}</span>
            </div>
          </div>
        </div>

        <!-- 订单底部 -->
        <div class="order-footer">
          <div class="order-footer-left">
            <span class="total-label">订单总计：</span>
            <span class="order-total">¥{{ order.totalAmount }}</span>
          </div>
          <div class="order-actions">
            <button v-if="order.status === 'pending'" class="action-btn pay-btn">立即付款</button>
            <button v-if="order.status === 'shipped'" class="action-btn confirm-btn">确认收货</button>
            <button class="action-btn detail-btn">查看详情</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getOrders } from '../api/order'

const orders = ref([])
const activeTab = ref('all')

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(o => o.status === activeTab.value)
})

const statusMap = {
  pending: '待付款',
  paid: '待发货',
  shipped: '待收货',
  completed: '已完成',
  cancelled: '已取消'
}

function getStatusText(status) {
  return statusMap[status] || status
}

onMounted(async () => {
  try {
    const res = await getOrders()
    orders.value = res.data || []
  } catch (error) {
    console.error('获取订单失败', error)
  }
})
</script>

<style scoped>
/* ========== 面包屑 ========== */
.breadcrumb {
  font-size: 13px;
  color: #a4b0be;
  margin-bottom: 16px;
}

.breadcrumb a {
  color: #a4b0be;
  transition: color 0.2s;
}

.breadcrumb a:hover {
  color: #ff4757;
}

.breadcrumb .sep {
  margin: 0 8px;
}

.breadcrumb .current {
  color: #2f3542;
}

/* ========== 订单标签 ========== */
.order-tabs {
  display: flex;
  gap: 4px;
  background: #fff;
  border-radius: 12px;
  padding: 6px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

.order-tab {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #a4b0be;
  background: transparent;
  transition: all 0.2s;
}

.order-tab:hover {
  color: #2f3542;
  background: #f8f9fa;
}

.order-tab.active {
  background: #ff4757;
  color: #fff;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(255,71,87,0.3);
}

/* ========== 空状态 ========== */
.empty-orders {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

.empty-icon {
  margin-bottom: 16px;
}

.empty-orders h3 {
  font-size: 20px;
  color: #2f3542;
  margin-bottom: 8px;
}

.empty-orders p {
  color: #a4b0be;
  margin-bottom: 24px;
}

.go-shop-btn {
  display: inline-block;
  padding: 12px 40px;
  background: linear-gradient(135deg, #ff4757, #ff6b81);
  color: #fff;
  border-radius: 24px;
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s;
}

.go-shop-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(255,71,87,0.3);
}

/* ========== 订单列表 ========== */
.order-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: box-shadow 0.3s;
}

.order-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}

/* 订单头部 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f1f2f6;
}

.order-header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.order-no {
  font-size: 13px;
  color: #2f3542;
  font-weight: 500;
}

.order-time {
  font-size: 12px;
  color: #a4b0be;
}

.order-status {
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.order-status.pending {
  background: #fdf6ec;
  color: #e6a23c;
}

.order-status.paid {
  background: #f0f9ff;
  color: #409eff;
}

.order-status.shipped {
  background: #f0f9ff;
  color: #1e90ff;
}

.order-status.completed {
  background: #f0f9eb;
  color: #67c23a;
}

.order-status.cancelled {
  background: #f5f5f5;
  color: #a4b0be;
}

/* 订单商品 */
.order-items {
  padding: 16px 20px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-bottom: 1px solid #f8f9fa;
}

.order-item:last-child {
  border-bottom: none;
}

.item-img-wrap {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.item-img-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-name {
  font-size: 14px;
  color: #2f3542;
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-spec {
  font-size: 12px;
  color: #a4b0be;
}

.item-price-info {
  text-align: right;
  flex-shrink: 0;
}

.item-price {
  display: block;
  font-size: 14px;
  color: #2f3542;
  font-weight: 500;
  margin-bottom: 2px;
}

.item-qty {
  font-size: 12px;
  color: #a4b0be;
}

/* 订单底部 */
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-top: 1px solid #f1f2f6;
}

.order-footer-left {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.total-label {
  font-size: 14px;
  color: #a4b0be;
}

.order-total {
  font-size: 20px;
  font-weight: 800;
  color: #ff4757;
}

.order-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 8px 20px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  transition: all 0.2s;
}

.pay-btn {
  background: #ff4757;
  color: #fff;
}

.pay-btn:hover {
  background: #ff6b81;
}

.confirm-btn {
  background: #fff;
  color: #ff4757;
  border: 1px solid #ff4757;
}

.confirm-btn:hover {
  background: #fff5f5;
}

.detail-btn {
  background: #f8f9fa;
  color: #2f3542;
}

.detail-btn:hover {
  background: #f1f2f6;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .order-tabs {
    overflow-x: auto;
  }
  .order-tab {
    white-space: nowrap;
    min-width: 80px;
  }
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  .order-footer {
    flex-direction: column;
    gap: 12px;
    align-items: flex-end;
  }
}
</style>
