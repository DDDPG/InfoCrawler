<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { GetCrawlerManagementTableData } from "@/api/table/types/management_table"
import {
  updateCrawlerManagementTableDataApi,
  getCrawlerManagementTableDataApi,
  createCrawlerManagementTableDataApi,
  deleteCrawlerManagementTableDataApi
} from "@/api/table"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"

defineOptions({
  // 命名当前组件
  name: "managementtable"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = reactive({
  ifCrawl: true,
  websiteName: "",
  homePage: "",
  contact: "",
  emails: "",
  crawlerKeywords: ""
})

const formRules = reactive({
  websiteName: [
    {
      required: true,
      message: "请输入网站名称",
      trigger: "blur"
    }
  ],

  homePage: [
    {
      required: true,
      message: "请输入主页地址",
      trigger: "blur"
    }
  ],

  contact: [
    {
      required: false,
      message: "请输入联系人/可留空",
      trigger: "blur"
    }
  ],

  emails: [
    {
      required: false,
      message: "请输入邮箱地址/留空不发送邮件",
      trigger: "blur"
    }
  ],

  crawlerKeywords: [
    {
      required: true,
      message: "请输入爬取关键字",
      trigger: "blur"
    }
  ]
})
/** 增 */
const handleCreate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (valid) {
      if (currentUpdateId.value === undefined) {
        createCrawlerManagementTableDataApi(formData)
          .then(() => {
            ElMessage.success("新增成功")
            getTableData()
          })
          .finally(() => {
            dialogVisible.value = false
          })
      } else {
        updateCrawlerManagementTableDataApi({
          id: currentUpdateId.value,
          ifCrawl: formData.ifCrawl,
          websiteName: formData.websiteName,
          homePage: formData.homePage,
          contact: formData.contact,
          emails: formData.emails,
          crawlerKeywords: formData.crawlerKeywords
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
      console.error("表单校验不通过", fields)
    }
  })
}
const resetForm = () => {
  currentUpdateId.value = undefined
  formData.ifCrawl = false
  formData.websiteName = ""
  formData.homePage = ""
  formData.contact = ""
  formData.emails = ""
  formData.crawlerKeywords = ""
}
//#endregion

//#region 删
const handleDelete = (row: GetCrawlerManagementTableData) => {
  ElMessageBox.confirm(`正在删除数据：${row.websiteName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteCrawlerManagementTableDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const currentUpdateId = ref<undefined | number>(undefined)
const handleUpdate = (row: GetCrawlerManagementTableData) => {
  currentUpdateId.value = row.id
  formData.ifCrawl = row.ifCrawl
  formData.websiteName = row.websiteName
  formData.homePage = row.homePage
  formData.contact = row.contact
  formData.emails = row.emails
  formData.crawlerKeywords = row.crawlerKeywords
  dialogVisible.value = true
}
//#endregion

//#region 查
const tableData = ref<GetCrawlerManagementTableData[]>([])
const searchFormRef = ref<FormInstance | null>(null)

const searchData = reactive({
  ifCrawl: true,
  websiteName: "",
  homePage: "",
  contact: "",
  emails: "",
  crawlerKeywords: ""
})

const getTableData = () => {
  loading.value = true
  getCrawlerManagementTableDataApi({
    currentPage: paginationData.currentPage,
    size: paginationData.pageSize,
    ifCrawl: searchData.ifCrawl,
    websiteName: searchData.websiteName,
    homePage: searchData.homePage,
    contact: searchData.contact,
    emails: searchData.emails,
    crawlerKeywords: searchData.crawlerKeywords
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
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <!--  查询模块  -->
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item label="是否爬取">
          <el-radio-group v-model="searchData.ifCrawl" style="align-items: start">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">不是</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="网站名称">
          <el-input v-model="searchData.websiteName" placeholder="请输入网站名称" />
        </el-form-item>

        <el-form-item label="首页地址">
          <el-input v-model="searchData.homePage" placeholder="请输入首页地址" style="margin-left: 14px" />
        </el-form-item>

        <el-form-item label="联系方式">
          <el-input v-model="searchData.contact" placeholder="请输入联系方式" style="margin-right: 36px" />
        </el-form-item>

        <el-form-item label="联系邮箱">
          <el-input v-model="searchData.emails" placeholder="请输入联系邮箱" />
        </el-form-item>

        <el-form-item label="爬取关键词">
          <el-input v-model="searchData.crawlerKeywords" placeholder="请输入爬取关键词" />
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
        </div>
        <div>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div>
      </div>

      <!--表格-->
      <div class="table-wrapper">
        <el-table :data="tableData">
          <el-table-column type="index" label="序号" width="60" align="center" />

          <el-table-column prop="ifCrawl" label="是否爬取" width="50" align="center">
            <template v-slot="scope">
              {{ scope.row.ifCrawl ? "是" : "否" }}
            </template>
          </el-table-column>

          <el-table-column prop="websiteName" label="网站名称" align="center">
            <template #default="scope">
              <el-link :href="scope.row.homePage" target="_blank">
                {{ scope.row.websiteName }}
              </el-link>
            </template>
          </el-table-column>

          <el-table-column prop="contact" label="联系人" align="center" />

          <el-table-column prop="emails" label="发信邮箱" align="center" />

          <el-table-column prop="crawlerKeywords" label="爬取关键字" align="center" />

          <el-table-column fixed="right" label="操作" width="200" align="center">
            <template v-slot="scope">
              <el-button type="primary" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

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
    <el-dialog v-model="dialogVisible" title="编辑网站" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="ifCrawl" label="是否爬取">
          <el-switch v-model="formData.ifCrawl" />
        </el-form-item>

        <el-form-item prop="websiteName" label="网站名称">
          <el-input v-model="formData.websiteName" placeholder="请输入网站名称" required clearable />
        </el-form-item>

        <el-form-item prop="homePage" label="主页">
          <el-input v-model="formData.homePage" placeholder="请输入主页" clearable />
        </el-form-item>

        <el-form-item prop="contact" label="联系人">
          <el-input v-model="formData.contact" placeholder="请输入联系人" clearable />
        </el-form-item>

        <el-form-item prop="emails" label="邮箱">
          <el-input v-model="formData.emails" placeholder="请输入邮箱(留空则不发送邮件)" clearable />
        </el-form-item>

        <el-form-item prop="crawlerKeywords" label="爬取关键字">
          <el-input v-model="formData.crawlerKeywords" placeholder="请输入爬取关键字" required clearable />
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
