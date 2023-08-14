export interface GetUserTableData {
  /** id **/
  id: number
  /** 用户名 **/
  username: string
  /** 密码 **/
  password: string
  /** 邮箱 **/
  email: string
  /** 权限 **/
  authorities: string
}

export interface GetUserTableRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  username?: string
  /** 查询参数：邮箱 */
  email?: string
  /** 查询参数：权限 */
  authorities?: string
}

export interface CreateUserTableRequestData {
  /** 用户名 **/
  username: string
  /** 密码 **/
  password: string
  /** 邮箱 **/
  email: string
  /** 权限 **/
  authorities: string
}

export interface UpdateUserTableRequestData {
  /** id **/
  id: number
  /** 用户名 **/
  username: string
  /** 密码 **/
  password: string
  /** 邮箱 **/
  email: string
  /** 权限 **/
  authorities: string
}

export type GetUserTableResponseData = ApiResponseData<{
  list: GetUserTableData[]
  total: number
}>
