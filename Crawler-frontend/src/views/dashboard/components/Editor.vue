<template>
  <div class="app-container center">
    <el-empty :description="`普通账户: ${userStore.username}`">
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

const goToXxlJob = () => {}
</script>

<style lang="scss" scoped>
.center {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
