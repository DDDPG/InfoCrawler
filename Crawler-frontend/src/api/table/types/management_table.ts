export interface CreateCrawlerManagementTableRequestData {
  /** 是否爬取 **/
  ifCrawl: boolean
  /** 网站名称 **/
  websiteName: string
  /** 主页 **/
  homePage?: string
  /** 联系人 **/
  contact?: string
  /** 邮箱 **/
  emails?: string
  /** 爬取关键字 **/
  crawlerKeywords: string
}

export interface UpdateCrawlerManagementTableRequestData {
  id: number
  /** 是否爬取 **/
  ifCrawl: boolean
  /** 网站名称 **/
  websiteName: string
  /** 主页 **/
  homePage?: string
  /** 联系人 **/
  contact: string
  /** 邮箱 **/
  emails: string
  /** 爬取关键字 **/
  crawlerKeywords: string
}

export interface GetCrawlerManagementTableData {
  /** id **/
  id: number
  /** 是否爬取 **/
  ifCrawl: boolean
  /** 网站名称 **/
  websiteName: string
  /** 主页 **/
  homePage: string
  /** 联系人 **/
  contact: string
  /** 邮箱 **/
  emails: string
  /** 爬取关键字 **/
  crawlerKeywords: string
}

export interface GetCrawlerManagementTableRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：是否爬取 */
  ifCrawl?: boolean
  /** 查询参数：网站名称 */
  websiteName?: string
  /** 查询参数：主页 */
  homePage?: string
  /** 查询参数：联系人 */
  contact?: string
  /** 查询参数：邮箱 */
  emails?: string
  /** 查询参数：爬取关键字 */
  crawlerKeywords?: string
}

export type GetCrawlerManagementTableResponseData = ApiResponseData<{
  list: GetCrawlerManagementTableData[]
  total: number
}>
