<script lang="ts" setup>
import { computed, reactive, ref, watch } from "vue"
import {
  updateResultInfoTableDataApi,
  getResultInfoTableDataApi,
  createResultInfoTableDataApi,
  deleteResultInfoTableDataApi
} from "@/api/table"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight, Edit } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { GetResultInfoTableData } from "@/api/table/types/result_info_table"

defineOptions({
  // 命名当前组件
  name: "infotable"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = reactive({
  title: "",
  url: "",
  sourceWebsite: "",
  sourceUrl: "",
  area: "",
  date: ""
})
const formRules: FormRules = reactive({
  title: [{ required: true, message: "请输入标题", trigger: "blur" }],
  url: [{ required: true, message: "请输入链接", trigger: "blur" }],
  sourceWebsite: [{ required: false, message: "请输入来源网站/可留空", trigger: "blur" }],
  sourceUrl: [{ required: false, message: "请输入来源链接/可留空", trigger: "blur" }],
  area: [{ required: true, message: "请输入地区", trigger: "blur" }],
  date: [{ required: true, message: "请选择日期", trigger: "blur" }]
})
const handleCreate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (valid) {
      if (currentUpdateId.value === undefined) {
        createResultInfoTableDataApi(formData)
          .then(() => {
            ElMessage.success("新增成功")
            getTableData()
          })
          .finally(() => {
            dialogVisible.value = false
          })
      } else {
        updateResultInfoTableDataApi({
          id: currentUpdateId.value,
          title: formData.title,
          url: formData.url,
          sourceWebsite: formData.sourceWebsite,
          sourceUrl: formData.sourceUrl,
          area: formData.area,
          date: formData.date
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
  formData.title = ""
  formData.url = ""
  formData.sourceWebsite = ""
  formData.area = ""
  formData.date = ""
}
//#endregion

//#region 删
const handleDelete = (row: GetResultInfoTableData) => {
  ElMessageBox.confirm(`正在删除数据：${row.title}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteResultInfoTableDataApi(row.id).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const currentUpdateId = ref<undefined | number>(undefined)
const handleUpdate = (row: GetResultInfoTableData) => {
  currentUpdateId.value = row.id
  formData.title = row.title
  formData.url = row.url
  formData.sourceWebsite = row.sourceWebsite
  formData.sourceUrl = row.sourceUrl
  formData.area = row.area
  formData.date = row.date
  dialogVisible.value = true
}
//#endregion

//#region 查
const tableData = ref<GetResultInfoTableData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const dateRange = ref("")
// 使用计算属性动态获取 startDate 和 endDate 的值
const startDate = computed(() => {
  return dateRange.value ? dateRange.value[0] : ""
})
const endDate = computed(() => {
  return dateRange.value ? dateRange.value[1] : ""
})
const searchData = reactive({
  title: "",
  sourceWebsite: "",
  area: "",
  startDate: startDate,
  endDate: endDate
})

const getTableData = () => {
  loading.value = true
  getResultInfoTableDataApi({
    currentPage: paginationData.currentPage,
    size: paginationData.pageSize,
    title: searchData.title,
    sourceWebsite: searchData.sourceWebsite,
    area: searchData.area,
    startDate: searchData.startDate,
    endDate: searchData.endDate
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
        <el-form-item prop="title" label="标题">
          <el-input v-model="searchData.title" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="sourceWebsite" label="来源网站">
          <el-input v-model="searchData.sourceWebsite" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="area" label="地区">
          <el-input v-model="searchData.area" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            value-format="YYYY-MM-DD"
            v-model="dateRange"
            type="daterange"
            start-placeholder="起始时间"
            end-placeholder="截止时间"
          />
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
          <el-table-column type="index" label="序号" align="center" width="60" />

          <el-table-column prop="title" label="标题" align="center">
            <template #default="scope">
              <el-link :href="scope.row.url" target="_blank">
                {{ scope.row.title }}
              </el-link>
            </template>
          </el-table-column>

          <el-table-column prop="sourceWebsite" label="来源网站" align="center" sortable="true">
            <template #default="scope">
              <el-link :href="scope.row.sourceUrl" target="_blank">
                {{ scope.row.sourceWebsite }}
              </el-link>
            </template>
          </el-table-column>

          <el-table-column prop="area" label="地区" align="center" width="80" />

          <el-table-column prop="date" label="日期" align="center" sortable="true" />

          <el-table-column fixed="right" label="操作" width="250" align="center">
            <template #default="scope">
              <el-button type="primary" :icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="danger" :icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" title="编辑数据" @close="resetForm" width="30%">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="title" label="标题">
          <el-input v-model="formData.title" placeholder="请输入标题" required clearable />
        </el-form-item>
        <el-form-item prop="url" label="链接">
          <el-input v-model="formData.url" placeholder="请输入链接" clearable />
        </el-form-item>
        <el-form-item prop="sourceWebsite" label="来源网站">
          <el-input v-model="formData.sourceWebsite" placeholder="请输入来源网站" clearable />
        </el-form-item>
        <el-form-item prop="sourceUrl" label="来源链接">
          <el-input v-model="formData.sourceUrl" placeholder="请输入来源链接" clearable />
        </el-form-item>
        <el-form-item prop="area" label="地区">
          <el-input v-model="formData.area" placeholder="请输入地区" clearable />
        </el-form-item>
        <el-form-item prop="date" label="日期">
          <el-date-picker v-model="formData.date" type="date" placeholder="请选择日期" style="width: 100%" />
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
