<template>
  <div class="login-page">
    <div class="login-container">
      <!-- 左侧装饰 -->
      <div class="login-banner">
        <div class="banner-content">
          <div class="banner-logo">
            <svg viewBox="0 0 40 40" width="48" height="48">
              <circle cx="20" cy="20" r="18" fill="rgba(255,255,255,0.2)"/>
              <text x="20" y="26" text-anchor="middle" fill="white" font-size="16" font-weight="bold">优</text>
            </svg>
          </div>
          <h2>欢迎回到优选商城</h2>
          <p>登录后享受更多专属优惠和服务</p>
          <div class="banner-features">
            <div class="banner-feature">
              <span>✓</span> 专属会员折扣
            </div>
            <div class="banner-feature">
              <span>✓</span> 订单实时追踪
            </div>
            <div class="banner-feature">
              <span>✓</span> 积分兑换好礼
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧表单 -->
      <div class="login-form-wrap">
        <div class="login-header">
          <h1>用户登录</h1>
          <p>还没有账号？<router-link to="/login" class="register-link">立即注册</router-link></p>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label class="form-label">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              用户名
            </label>
            <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              class="form-input"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              密码
            </label>
            <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              class="form-input"
              required
            />
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="rememberMe" />
              <span>记住我</span>
            </label>
            <a href="#" class="forgot-password">忘记密码？</a>
          </div>

          <button type="submit" class="login-btn">
            登录
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5">
              <line x1="5" y1="12" x2="19" y2="12"/>
              <polyline points="12 5 19 12 12 19"/>
            </svg>
          </button>
        </form>

        <div class="social-login">
          <div class="divider">
            <span>其他登录方式</span>
          </div>
          <div class="social-icons">
            <button class="social-btn wechat">
              <span>微信</span>
            </button>
            <button class="social-btn qq">
              <span>QQ</span>
            </button>
            <button class="social-btn phone">
              <span>手机</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})
const rememberMe = ref(false)

// 页面加载时，如果有记住的用户名则自动填充
onMounted(() => {
  const saved = localStorage.getItem('rememberedUsername')
  if (saved) {
    form.value.username = saved
    rememberMe.value = true
  }
})

async function handleLogin() {
  try {
    const res = await login(form.value)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', res.data.username)
    
    // 记住我：保存用户名到localStorage
    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', form.value.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }
    
    alert('登录成功！')
    // 强制刷新页面以更新导航栏登录状态
    window.location.href = '/'
  } catch (error) {
    alert('登录失败：' + error.message)
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 200px);
  padding: 40px 0;
}

.login-container {
  display: grid;
  grid-template-columns: 400px 420px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0,0,0,0.12);
  max-width: 820px;
  width: 100%;
}

/* 左侧装饰 */
.login-banner {
  background: linear-gradient(135deg, #ff4757 0%, #ff6348 50%, #ffa502 100%);
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-banner::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
}

.banner-content {
  position: relative;
  z-index: 1;
}

.banner-logo {
  margin-bottom: 24px;
}

.login-banner h2 {
  font-size: 26px;
  font-weight: 800;
  color: #fff;
  margin-bottom: 12px;
  line-height: 1.3;
}

.login-banner p {
  font-size: 15px;
  color: rgba(255,255,255,0.85);
  margin-bottom: 32px;
}

.banner-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.banner-feature {
  display: flex;
  align-items: center;
  gap: 10px;
  color: rgba(255,255,255,0.9);
  font-size: 14px;
}

.banner-feature span {
  width: 22px;
  height: 22px;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  flex-shrink: 0;
}

/* 右侧表单 */
.login-form-wrap {
  background: #fff;
  padding: 48px 40px;
}

.login-header {
  margin-bottom: 32px;
}

.login-header h1 {
  font-size: 24px;
  font-weight: 800;
  color: #2f3542;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 14px;
  color: #a4b0be;
}

.register-link {
  color: #ff4757;
  font-weight: 600;
  transition: color 0.2s;
}

.register-link:hover {
  color: #ff6b81;
}

/* 表单 */
.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #2f3542;
  margin-bottom: 8px;
}

.form-input {
  width: 100%;
  height: 48px;
  padding: 0 16px;
  border: 2px solid #f1f2f6;
  border-radius: 12px;
  font-size: 14px;
  color: #2f3542;
  transition: all 0.3s;
  background: #fafafa;
}

.form-input::placeholder {
  color: #a4b0be;
}

.form-input:focus {
  border-color: #ff4757;
  background: #fff;
  box-shadow: 0 0 0 4px rgba(255,71,87,0.1);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #a4b0be;
  cursor: pointer;
}

.remember-me input {
  accent-color: #ff4757;
}

.forgot-password {
  font-size: 13px;
  color: #ff4757;
  transition: color 0.2s;
}

.forgot-password:hover {
  color: #ff6b81;
}

.login-btn {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #ff4757, #ff6b81);
  color: #fff;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
  box-shadow: 0 4px 16px rgba(255,71,87,0.3);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(255,71,87,0.4);
}

/* 社交登录 */
.social-login {
  margin-top: 32px;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #f1f2f6;
}

.divider span {
  padding: 0 16px;
  font-size: 12px;
  color: #a4b0be;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.social-btn {
  width: 72px;
  height: 40px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.social-btn.wechat {
  background: #f0f9eb;
  color: #67c23a;
}

.social-btn.wechat:hover {
  background: #67c23a;
  color: #fff;
}

.social-btn.qq {
  background: #f0f9ff;
  color: #409eff;
}

.social-btn.qq:hover {
  background: #409eff;
  color: #fff;
}

.social-btn.phone {
  background: #fff5f5;
  color: #ff4757;
}

.social-btn.phone:hover {
  background: #ff4757;
  color: #fff;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
    max-width: 420px;
  }
  .login-banner {
    padding: 32px 24px;
  }
  .login-form-wrap {
    padding: 32px 24px;
  }
}
</style>
