<template>
  <div class="app-container center">
    <el-empty :description="`管理员账户: ${userStore.username}`">
      <el-button type="primary" @click="goToXxlJob">前往调度中心</el-button>
      <el-button type="primary" @click="logout">退出登录</el-button>
    </el-empty>
  </div>
</template>

<script lang="ts" setup>
import { useUserStore } from "@/store/modules/user"
import router from "@/router"

const userStore = useUserStore()

const logout = () => {
  userStore.logout()
  router.push("/login")
}

const goToXxlJob = () => {
  window.open(import.meta.env.VITE_XXL_JOB_ADMIN_URL)
}
</script>

<style lang="scss" scoped>
.center {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
