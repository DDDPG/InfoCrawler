import { request } from "@/utils/service"
import type * as ResultInfoTable from "./types/result_info_table"
import type * as CrawlerManagementTable from "./types/management_table"
import type * as UserTable from "./types/user_table"
import { GetUserTableResponseData } from "./types/user_table"

/** 爬虫信息表接口 */
/** 增 */
export function createResultInfoTableDataApi(data: ResultInfoTable.CreateResultInfoTableRequestData) {
  return request({
    url: "resultInfoTable",
    method: "post",
    data
  })
}

/** 删 */
export function deleteResultInfoTableDataApi(id: number) {
  return request({
    url: `resultInfoTable/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateResultInfoTableDataApi(data: ResultInfoTable.UpdateResultInfoTableRequestData) {
  return request({
    url: "resultInfoTable",
    method: "put",
    data
  })
}

/** 查 */
export function getResultInfoTableDataApi(params: ResultInfoTable.GetResultInfoTableRequestData) {
  return request<ResultInfoTable.GetResultInfoTableResponseData>({
    url: "resultInfoTable",
    method: "get",
    params
  })
}

/** 爬虫管理表接口 */
/** 增 */
export function createCrawlerManagementTableDataApi(
  data: CrawlerManagementTable.CreateCrawlerManagementTableRequestData
) {
  return request({
    url: "crawlerManagementTable",
    method: "post",
    data
  })
}

/** 删 */
export function deleteCrawlerManagementTableDataApi(id: number) {
  return request({
    url: `crawlerManagementTable/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateCrawlerManagementTableDataApi(
  data: CrawlerManagementTable.UpdateCrawlerManagementTableRequestData
) {
  return request({
    url: "crawlerManagementTable",
    method: "put",
    data
  })
}

/** 查 */
export function getCrawlerManagementTableDataApi(params: CrawlerManagementTable.GetCrawlerManagementTableRequestData) {
  return request<CrawlerManagementTable.GetCrawlerManagementTableResponseData>({
    url: "crawlerManagementTable",
    method: "get",
    params
  })
}

/** 用户信息表接口 */

/** 增 */
export function createUserTableDataApi(data: UserTable.CreateUserTableRequestData) {
  return request({
    url: "userTable",
    method: "post",
    data
  })
}

/** 删 */
export function deleteUserTableDataApi(id: number) {
  return request({
    url: `userTable/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateUserTableDataApi(data: UserTable.UpdateUserTableRequestData) {
  return request({
    url: "userTable",
    method: "put",
    data
  })
}

/** 查 */
export function getUserTableDataApi(params: UserTable.GetUserTableRequestData) {
  return request<UserTable.GetUserTableResponseData>({
    url: "userTable",
    method: "get",
    params
  })
}
