/** 声明 vite 环境变量的类型（如果未声明则默认是 any） */
declare interface ImportMetaEnv {
  readonly VITE_BASE_API: string
  readonly VITE_ROUTER_HISTORY: "hash" | "html5"
  readonly VITE_PUBLIC_PATH: string
  readonly VITE_XXL_JOB_ADMIN_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
