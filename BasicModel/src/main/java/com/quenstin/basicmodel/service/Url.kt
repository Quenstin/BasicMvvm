package com.hdyj.basicmodel.service

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：网络接口
 */

/**
 * app更新
 * https://2020.fc62.com/hezi.json
 */
const val mUpdateUrl = "client/app_version"

/**
 * app数据更新
 * /api/hezi/client/version
 */
const val mAppDataUrl = "client/version"

/**
 * 获取token
 * /api/hezi/client/testlogin
 */
const val mTokenUrl = "client/testlogin?code=1"

/**
 * 登录接口获取token请求词条列表
 */
const val mMainListDataUrl = "citiao/index"

/**
 * 栏目数据接口
 */
const val mMainDataUrl = "channel/index"

/**
 * 音频数据接口
 */
const val mAudioUrl = "channel/audio"


/**
 * 视频数据接口
 */
const val mVideoUrl = "video/index"

/**
 * 试卷视频列表
 */
const val mPaperVideo = "video/paper_video"

/**
 * 试卷分类列表
 */
const val paperVideoCategory = "video/paper_video_category"

/**
 * 试卷视频内容列表
 */
const val paperVideoContent = "video/paper_video_content"


/**
 * 栏目内容
 */
const val mContentData = "channel/content"

/**
 * 题库真题列表
 */
const val mTkRealTestList = "channel/topiccontent"

/**
 * 题库真题详情
 */
const val mTkRealTestPageInfo = "channel/topichalt"

/**
 * 真题年份地区
 */
const val mTkRealTestYearRegion = "channel/topicarea"

/**
 * 题库列表
 */
const val mTestList = "tiku/index"
const val mTestViews = "https://oa.fc62.com/index/tiku/views"


/**
 * 激活码激活
 */
const val edition = "client/edition"

/**
 * 获取验证码
 */
const val sms = "client/sms"

/**
 * 激活
 */
const val register = "client/register"


/**
 * 答疑  接口域名
 */
const val ANSWER_BASE_URL = "https://2020.fc62.com/"


/**
 * 全部帖子
 */
const val plateAll = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_all"

/**
 * 收藏帖子,取消帖子
 */
const val collectPlate = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_like"

/**
 * 查看帖子
 */
const val selectPlate = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_view"

/**
 * 板块分类
 */
const val plateClass = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_type"


/**
 * 板块分类详情
 */
const val plateClassSelect = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_subject"

/**
 * 我的答疑
 */
const val myAnswer = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_thread"

/**
 * 我的收藏
 */
const val answerCollect = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_mylike"

/**
 * 搜索帖子
 */
const val searchPlate = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_search"

/**
 * 答疑提问
 */
const val choseSubject = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_problem_form"

/**
 * 答疑统计接口
 */
const val plateCount = ANSWER_BASE_URL + "bbs/app.php?m=hezi&c=pltae&action=plate_count"





