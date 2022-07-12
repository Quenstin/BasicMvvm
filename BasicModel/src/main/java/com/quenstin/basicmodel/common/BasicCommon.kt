package com.hdyj.basicmodel.common

/**
 * Created by hdyjzgq
 * data on 2021/10/9
 * function is ：
 */
object BasicCommon {

     var TOKEN = ""

     /**
      * token
      */
     const val COMMON_TOKEN = "token"

     const val BASE_URL = "https://oa.fc62.com/api/hezi/"



     /**
      * 是否激活
      */
     const val ACTIVE = "active"

     /**
     * 护眼模式  是否开启
      */
     const val  isProtection= "isProtection"

     /**
      * 护眼模式   设置时长  0:每隔5分钟   1:每隔30分钟   2:每隔1小时  3:每隔2小时  4:每隔3小时  5:每隔4小时
      */
     const val SettingTimeType = "SettingTimeType"

     /**
      * 护眼模式, 统计运行时长
      */
     const val appUseTime = "APP_USE_TIME"
}