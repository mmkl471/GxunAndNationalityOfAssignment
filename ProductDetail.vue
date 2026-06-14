<template>
  <div class="product-detail">
    <div v-if="product" class="detail-container">
      <!-- 面包屑 -->
      <div class="breadcrumb">
        <router-link to="/">首页</router-link>
        <span class="sep">/</span>
        <router-link to="/products">全部商品</router-link>
        <span class="sep">/</span>
        <span class="current">{{ product.name }}</span>
      </div>

      <!-- 商品主区域 -->
      <div class="product-main">
        <!-- 左侧图片 -->
        <div class="product-gallery">
          <div class="main-image">
            <img :src="product.image" :alt="product.name" />
            <div class="img-badge" v-if="product.promotionPrice">
              <span>{{ discount }}% OFF</span>
            </div>
          </div>
          <div class="thumbnail-list">
            <div class="thumbnail active" v-for="i in 4" :key="i">
              <img :src="product.image" :alt="product.name" />
            </div>
          </div>
        </div>

        <!-- 右侧信息 -->
        <div class="product-info">
          <h1 class="product-title">{{ product.name }}</h1>
          <p class="product-subtitle">{{ product.description }}</p>

          <!-- 促销标签 -->
          <PromotionTag
            v-if="product.promotion"
            :promotion="product.promotion"
          />

          <!-- 价格区域 -->
          <div class="price-box">
            <div class="price-main">
              <span class="price-label">促销价</span>
              <span class="price-value">
                <span class="yen">¥</span>{{ product.promotionPrice || product.price }}
              </span>
              <span class="price-original" v-if="product.promotionPrice">¥{{ product.price }}</span>
              <span class="price-save" v-if="product.promotionPrice">
                立省 ¥{{ (product.price - product.promotionPrice).toFixed(2) }}
              </span>
            </div>
            <div class="price-promotions" v-if="product.promotionPrice">
              <span class="promo-tag">限时特惠</span>
              <span class="promo-tag">满减优惠</span>
              <span class="promo-tag">包邮</span>
            </div>
          </div>

          <!-- 倒计时 -->
          <CountdownTimer
            v-if="product.promotion?.endTime"
            :end-time="product.promotion.endTime"
          />

          <!-- 配送信息 -->
          <div class="delivery-info">
            <div class="info-row">
              <span class="info-label">配送</span>
              <span class="info-value">全国包邮 · 预计3-5天送达</span>
            </div>
            <div class="info-row">
              <span class="info-label">服务</span>
              <div class="service-tags">
                <span class="service-tag">7天无理由</span>
                <span class="service-tag">假一赔十</span>
                <span class="service-tag">运费险</span>
              </div>
            </div>
          </div>

          <!-- 数量选择 -->
          <div class="quantity-section">
            <span class="qty-label">数量</span>
            <div class="quantity-selector">
              <button class="qty-btn" @click="quantity > 1 && quantity--">-</button>
              <input v-model.number="quantity" type="number" min="1" class="qty-input" />
              <button class="qty-btn" @click="quantity++">+</button>
            </div>
            <span class="stock-info">库存充足</span>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <button class="btn-add-cart" @click="addToCart">
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
              </svg>
              加入购物车
            </button>
            <button class="btn-buy-now" @click="buyNow">立即购买</button>
          </div>
        </div>
      </div>

      <!-- 商品详情 -->
      <div class="product-tabs">
        <div class="tabs-header">
          <button class="tab" :class="{ active: activeTab === 'detail' }" @click="activeTab = 'detail'">商品详情</button>
          <button class="tab" :class="{ active: activeTab === 'spec' }" @click="activeTab = 'spec'">规格参数</button>
          <button class="tab" :class="{ active: activeTab === 'reviews' }" @click="activeTab = 'reviews'">用户评价</button>
        </div>
        <div class="tab-content">
          <div v-if="activeTab === 'detail'" class="detail-content">
            <div class="detail-img">
              <img :src="product.image" :alt="product.name" />
            </div>
            <div class="detail-text">
              <h3>商品特点</h3>
              <ul>
                <li>精选优质材料，品质有保障</li>
                <li>人性化设计，使用便捷</li>
                <li>经过严格质检，安全可靠</li>
                <li>全国联保，售后无忧</li>
              </ul>
            </div>
          </div>
          <div v-if="activeTab === 'spec'" class="spec-content">
            <div class="spec-table">
              <div class="spec-row" v-for="item in specs" :key="item.label">
                <span class="spec-label">{{ item.label }}</span>
                <span class="spec-value">{{ item.value }}</span>
              </div>
            </div>
          </div>
          <div v-if="activeTab === 'reviews'" class="reviews-content">
            <div class="review-summary">
              <div class="review-score">4.8</div>
              <div class="review-stars">&#9733;&#9733;&#9733;&#9733;&#9733;</div>
              <span class="review-count">1280条评价</span>
            </div>
            <div class="review-list">
              <div class="review-item" v-for="i in 3" :key="i">
                <div class="reviewer">
                  <div class="avatar">用</div>
                  <span class="reviewer-name">用户{{ 1000 + i }}</span>
                  <span class="review-stars-sm">&#9733;&#9733;&#9733;&#9733;&#9733;</span>
                </div>
                <p class="review-text">商品质量非常好，物流也很快，包装精美，非常满意这次购物体验！</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import PromotionTag from '../components/PromotionTag.vue'
import CountdownTimer from '../components/CountdownTimer.vue'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const product = ref(null)
const quantity = ref(1)
const activeTab = ref('detail')

const specs = ref([
  { label: '品牌', value: '优选品牌' },
  { label: '型号', value: 'PRO-2026' },
  { label: '颜色', value: '经典黑/珍珠白' },
  { label: '材质', value: '优质合金/环保材料' },
  { label: '尺寸', value: '标准尺寸' },
  { label: '重量', value: '500g' },
  { label: '产地', value: '中国' },
  { label: '保修', value: '全国联保1年' }
])

const discount = computed(() => {
  if (!product.value || !product.value.promotionPrice) return 0
  return Math.round((1 - product.value.promotionPrice / product.value.price) * 100)
})

onMounted(() => {
  product.value = {
    id: route.params.id,
    name: '示例商品 ' + route.params.id,
    description: '这是一个示例商品描述，包含商品的详细信息和特点。品质优良，做工精细。',
    price: 199.00,
    promotionPrice: 149.00,
    image: 'https://picsum.photos/seed/product' + route.params.id + '/600/600',
    promotion: {
      type: 'flash_sale',
      tag: '限时抢购',
      discount: '7.5折',
      endTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString()
    }
  }
})

function addToCart() {
  cartStore.addToCart({ ...product.value, quantity: quantity.value })
}

function buyNow() {
  cartStore.addToCart({ ...product.value, quantity: quantity.value })
  router.push('/cart')
}
</script>

<style scoped>
/* ========== 面包屑 ========== */
.breadcrumb {
  font-size: 13px;
  color: #a4b0be;
  margin-bottom: 20px;
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

/* ========== 商品主区域 ========== */
.product-main {
  display: grid;
  grid-template-columns: 480px 1fr;
  gap: 40px;
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  margin-bottom: 24px;
}

/* 图片区域 */
.main-image {
  position: relative;
  width: 100%;
  height: 480px;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.main-image:hover img {
  transform: scale(1.03);
}

.img-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: #ff4757;
  color: #fff;
  padding: 4px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

.thumbnail {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.thumbnail.active {
  border-color: #ff4757;
}

.thumbnail:hover {
  border-color: #ff6b81;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 商品信息 */
.product-title {
  font-size: 22px;
  font-weight: 700;
  color: #2f3542;
  margin-bottom: 8px;
  line-height: 1.4;
}

.product-subtitle {
  font-size: 14px;
  color: #a4b0be;
  margin-bottom: 16px;
}

/* 价格区域 */
.price-box {
  background: linear-gradient(135deg, #fff5f5, #fff0f0);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
}

.price-main {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 10px;
}

.price-label {
  font-size: 13px;
  color: #ff4757;
  font-weight: 600;
}

.price-value {
  font-size: 32px;
  font-weight: 800;
  color: #ff4757;
}

.yen {
  font-size: 18px;
}

.price-original {
  font-size: 16px;
  color: #a4b0be;
  text-decoration: line-through;
}

.price-save {
  font-size: 12px;
  color: #fff;
  background: #ff4757;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: 600;
}

.price-promotions {
  display: flex;
  gap: 8px;
}

.promo-tag {
  padding: 3px 10px;
  background: rgba(255,71,87,0.1);
  color: #ff4757;
  font-size: 12px;
  border-radius: 4px;
  border: 1px solid rgba(255,71,87,0.2);
}

/* 配送信息 */
.delivery-info {
  padding: 16px 0;
  border-top: 1px solid #f1f2f6;
  border-bottom: 1px solid #f1f2f6;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 10px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 13px;
  color: #a4b0be;
  min-width: 40px;
}

.info-value {
  font-size: 13px;
  color: #2f3542;
}

.service-tags {
  display: flex;
  gap: 8px;
}

.service-tag {
  padding: 2px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 12px;
  color: #2f3542;
}

/* 数量选择 */
.quantity-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.qty-label {
  font-size: 14px;
  color: #2f3542;
  font-weight: 600;
}

.quantity-selector {
  display: flex;
  align-items: center;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.qty-btn {
  width: 36px;
  height: 36px;
  background: #f8f9fa;
  color: #2f3542;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.qty-btn:hover {
  background: #ff4757;
  color: #fff;
}

.qty-input {
  width: 56px;
  height: 36px;
  text-align: center;
  border: none;
  border-left: 1px solid #e0e0e0;
  border-right: 1px solid #e0e0e0;
  font-size: 14px;
  color: #2f3542;
}

.qty-input::-webkit-inner-spin-button,
.qty-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
}

.stock-info {
  font-size: 13px;
  color: #2ed573;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
}

.btn-add-cart {
  flex: 1;
  padding: 14px;
  background: linear-gradient(135deg, #ffa502, #ff9f43);
  color: #fff;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
}

.btn-add-cart:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255,165,2,0.3);
}

.btn-buy-now {
  flex: 1;
  padding: 14px;
  background: linear-gradient(135deg, #ff4757, #ff6b81);
  color: #fff;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  transition: all 0.3s;
}

.btn-buy-now:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255,71,87,0.3);
}

/* ========== 商品详情 Tabs ========== */
.product-tabs {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  overflow: hidden;
}

.tabs-header {
  display: flex;
  border-bottom: 2px solid #f1f2f6;
}

.tab {
  padding: 16px 32px;
  font-size: 15px;
  color: #a4b0be;
  font-weight: 600;
  position: relative;
  transition: all 0.2s;
}

.tab::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 3px;
  background: #ff4757;
  border-radius: 2px;
  transition: width 0.3s;
}

.tab:hover {
  color: #2f3542;
}

.tab.active {
  color: #ff4757;
}

.tab.active::after {
  width: 60%;
}

.tab-content {
  padding: 32px;
}

/* 详情内容 */
.detail-img {
  margin-bottom: 24px;
  border-radius: 12px;
  overflow: hidden;
}

.detail-img img {
  width: 100%;
  display: block;
}

.detail-text h3 {
  font-size: 18px;
  color: #2f3542;
  margin-bottom: 16px;
}

.detail-text ul {
  list-style: none;
  padding: 0;
}

.detail-text li {
  padding: 8px 0;
  padding-left: 20px;
  position: relative;
  color: #57606f;
  font-size: 14px;
}

.detail-text li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  background: #ff4757;
  border-radius: 50%;
}

/* 规格参数 */
.spec-table {
  max-width: 600px;
}

.spec-row {
  display: flex;
  padding: 14px 0;
  border-bottom: 1px solid #f1f2f6;
}

.spec-row:nth-child(odd) {
  background: #fafafa;
  margin: 0 -32px;
  padding: 14px 32px;
}

.spec-label {
  width: 120px;
  color: #a4b0be;
  font-size: 14px;
  flex-shrink: 0;
}

.spec-value {
  color: #2f3542;
  font-size: 14px;
}

/* 评价 */
.review-summary {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #fafafa;
  border-radius: 12px;
  margin-bottom: 24px;
}

.review-score {
  font-size: 40px;
  font-weight: 800;
  color: #ff4757;
}

.review-stars {
  color: #ffa502;
  font-size: 18px;
  letter-spacing: 2px;
}

.review-count {
  font-size: 14px;
  color: #a4b0be;
}

.review-item {
  padding: 16px 0;
  border-bottom: 1px solid #f1f2f6;
}

.reviewer {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff4757, #ff6b81);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.reviewer-name {
  font-size: 14px;
  color: #2f3542;
  font-weight: 500;
}

.review-stars-sm {
  color: #ffa502;
  font-size: 12px;
  letter-spacing: -1px;
}

.review-text {
  font-size: 14px;
  color: #57606f;
  line-height: 1.6;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .product-main {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 20px;
  }
  .main-image {
    height: 300px;
  }
  .action-buttons {
    flex-direction: column;
  }
}
</style>
