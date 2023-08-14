<script lang="ts" setup>
import { useRouter } from "vue-router"
import { usePagination } from "@/hooks/usePagination"
import { reactive, ref, watch } from "vue"
import { ElMessage, ElMessageBox, FormInstance } from "element-plus"
import { GetUserTableData } from "@/api/table/types/user_table"
import {
  getUserTableDataApi,
  createUserTableDataApi,
  updateUserTableDataApi,
  deleteUserTableDataApi
} from "@/api/table"
import { CirclePlus, Delete, Download, Refresh, RefreshRight, Search, Edit } from "@element-plus/icons-vue"

const router = useRouter()

defineOptions({
  // 命名当前组件
  name: "usersManage"
})
const handleRolesChange = () => {
  router.push({ path: "/403" })
}

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const userForm = reactive({
  username: "",
  password: "",
  email: "",
  authorities: [] as string[]
})

const formRules = reactive({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码(牢记)", trigger: "blur" }],
  email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
  authorities: [{ required: true, message: "请选择角色", trigger: "blur" }]
})

const handleCreate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (valid) {
      if (currentUpdateId.value === undefined) {
        createUserTableDataApi({
          username: userForm.username,
          password: userForm.password,
          email: userForm.email,
          authorities: userForm.authorities.join(",")
        })
          .then(() => {
            ElMessage.success("新增成功")
            getTableData()
          })
          .finally(() => {
            dialogVisible.value = false
          })
      } else {
        updateUserTableDataApi({
          id: currentUpdateId.value,
          username: userForm.username,
          password: userForm.password,
          email: userForm.email,
          authorities: userForm.authorities.join(",")
        })
          .then(() => {
            ElMessage.success("修改成功")
            getTableData()
          })
          .finally(() => {
            dialogVisible.value = false
          })
      }
    } else {
      ElMessage.error("表单验证失败")
    }
  })
}

const resetForm = () => {
  userForm.username = ""
  userForm.password = ""
  userForm.email = ""
  userForm.authorities = []
}

//#region 删
const handleDelete = (row: GetUserTableData) => {
  ElMessageBox.confirm(`正在删除数据：${row.username}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteUserTableDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}

//#region 改
const currentUpdateId = ref<number | undefined>(undefined)

const handleUpdate = (row: GetUserTableData) => {
  currentUpdateId.value = row.id
  userForm.username = row.username
  userForm.password = row.password
  userForm.email = row.email
  userForm.authorities = row.authorities.split(",")
  dialogVisible.value = true
}

//#region 查
const tableData = ref<GetUserTableData[]>([])
const searchFormRef = ref<FormInstance | null>(null)

const searchData = reactive({
  username: "",
  email: "",
  authorities: ""
})

const getTableData = () => {
  loading.value = true
  getUserTableDataApi({
    currentPage: paginationData.currentPage,
    size: paginationData.pageSize,
    username: searchData.username,
    email: searchData.email,
    authorities: searchData.authorities
  })
    .then((res) => {
      paginationData.total = res.data.total
      tableData.value = res.data.list
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}

const handleSearch = () => {
  paginationData.currentPage === 1 ? getTableData() : (paginationData.currentPage = 1)
}

const resetSearch = () => {
  searchFormRef.value?.resetFields()
  handleSearch()
}

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <!--  查询模块  -->
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="searchData.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="searchData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="权限" prop="authorities">
          <el-radio-group v-model="searchData.authorities">
            <el-radio-button label="admin" />
            <el-radio-button label="editor" />
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!--展示模块-->
    <el-card v-loading="loading" shadow="never">
      <!--工具栏-->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增数据</el-button>
          <el-tag type="danger" size="large" effect="plain" style="margin-left: 12px">
            密码由SpringSecurity中BCryptPasswordEncoder算法加密, 新增数据后请牢记密码明文
          </el-tag>
        </div>
        <div>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div>
      </div>

      <!--表格-->
      <el-table :data="tableData" border stripe>
        <el-table-column type="index" width="50" align="center" />
        <el-table-column prop="username" label="用户名" align="center" />
        <el-table-column prop="password" label="密码" align="center" />
        <el-table-column prop="email" label="邮箱" align="center" />
        <el-table-column prop="authorities" label="权限" align="center">
          <template #default="scope">
            {{ scope.row.authorities.includes("admin") ? "管理员" : "普通用户" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button type="danger" :icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-wrapper">
        <el-pagination
          background
          :layout="paginationData.layout"
          :page-sizes="paginationData.pageSizes"
          :total="paginationData.total"
          :page-size="paginationData.pageSize"
          :currentPage="paginationData.currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/修改数据表单 -->
    <el-dialog v-model="dialogVisible" title="编辑用户" :before-close="() => (dialogVisible = false)">
      <el-form :model="userForm" :rules="formRules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userForm.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色" prop="authorities">
          <el-checkbox-group v-model="userForm.authorities">
            <el-checkbox label="admin" name="type" />
            <el-checkbox label="editor" name="type" />
          </el-checkbox-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.search-wrapper {
  margin-bottom: 20px;

  :deep(.el-card__body) {
    padding-bottom: 2px;
  }
}

.toolbar-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-wrapper {
  margin-bottom: 20px;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
}
</style>
