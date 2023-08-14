export interface CreateResultInfoTableRequestData {
  /** 标题 **/
  title: string
  /** 详情页链接 **/
  url: string
  /** 地区 **/
  area: string
  /** 来源网站链接 **/
  sourceUrl?: string
  /** 发布日期 **/
  date: string
  /** 来源网站 **/
  sourceWebsite: string
}

export interface UpdateResultInfoTableRequestData {
  id: number
  /** 标题 **/
  title?: string
  /** 详情页链接 **/
  url?: string
  /** 地区 **/
  area?: string
  /** 来源网站链接 **/
  sourceUrl?: string
  /** 来源网站 **/
  sourceWebsite: string
  /** 发布日期 **/
  date: string
}

export interface GetResultInfoTableData {
  /** id **/
  id: number
  /** 标题 **/
  title: string
  /** 详情页链接 **/
  url: string
  /** 地区 **/
  area: string
  /** 来源网站链接 **/
  sourceUrl: string
  /** 发布日期 **/
  date: string
  /** 来源网站 **/
  sourceWebsite: string
}

export interface GetResultInfoTableRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：标题 */
  title?: string
  /** 查询参数：来源网站 */
  sourceWebsite?: string
  /** 查询参数：地区 */
  area?: string
  /** 查询参数：日期 */
  startDate?: string
  endDate?: string
}

export type GetResultInfoTableResponseData = ApiResponseData<{
  list: GetResultInfoTableData[]
  total: number
}>
