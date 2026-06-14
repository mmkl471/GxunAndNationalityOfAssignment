<template>
  <div class="countdown-timer">
    <div class="timer-icon">
      <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="#ff4757" stroke-width="2">
        <circle cx="12" cy="12" r="10"/>
        <polyline points="12 6 12 12 16 14"/>
      </svg>
    </div>
    <span class="timer-label">距结束</span>
    <div class="time-blocks">
      <span class="time-block">
        <span class="time-num">{{ hours }}</span>
      </span>
      <span class="time-sep">:</span>
      <span class="time-block">
        <span class="time-num">{{ minutes }}</span>
      </span>
      <span class="time-sep">:</span>
      <span class="time-block">
        <span class="time-num">{{ seconds }}</span>
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  endTime: {
    type: String,
    required: true
  }
})

const remainingTime = ref(0)
let timer = null

const hours = computed(() => {
  return String(Math.floor(remainingTime.value / 3600)).padStart(2, '0')
})

const minutes = computed(() => {
  return String(Math.floor((remainingTime.value % 3600) / 60)).padStart(2, '0')
})

const seconds = computed(() => {
  return String(remainingTime.value % 60).padStart(2, '0')
})

function updateTimer() {
  const end = new Date(props.endTime).getTime()
  const now = Date.now()
  const diff = Math.max(0, Math.floor((end - now) / 1000))
  remainingTime.value = diff

  if (diff <= 0) {
    clearInterval(timer)
  }
}

onMounted(() => {
  updateTimer()
  timer = setInterval(updateTimer, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.countdown-timer {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: linear-gradient(135deg, #fff5f5, #ffe8e8);
  border-radius: 8px;
  border: 1px solid rgba(255,71,87,0.15);
}

.timer-icon {
  display: flex;
  align-items: center;
}

.timer-label {
  font-size: 13px;
  color: #ff4757;
  font-weight: 600;
}

.time-blocks {
  display: flex;
  align-items: center;
  gap: 3px;
}

.time-block {
  background: #2f3542;
  border-radius: 4px;
  padding: 2px 6px;
  min-width: 28px;
  text-align: center;
}

.time-num {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 1px;
}

.time-sep {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  font-weight: 800;
  color: #ff4757;
  animation: blink 1s step-end infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}
</style>
