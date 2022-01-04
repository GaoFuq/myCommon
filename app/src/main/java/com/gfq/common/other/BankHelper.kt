package com.gfq.common

import com.gfq.common.view.getSplitInputFilter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.StringBuilder

/**
 * 2021/12/149:19
 * @auth gaofuq
 * @description 根据银行卡号 获取银行卡信息
 *
 * @see【https://blog.csdn.net/qq_35491254/article/details/80252607】
 * @see【https://www.jianshu.com/p/c6b67443d2b4】
 *
 * 1.通过阿里的支付宝接口进行校验，能够准确识别是否存在，归属行，卡号类型是储蓄卡（DC）还是信用卡（CC）。
 * https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=6210676802084484923&cardBinCheck=true
 * 需要传入的3个参数
 * 通道：_input_charset
 * 卡号：cardNo
 * 卡本：cardBinCheck
 *
 * 返回：{"cardType":"DC","bank":"BJRCB","key":"6210676802084484923","messages":[],"validated":true,"stat":"ok"}
 *   validated：是否正确有效
 *   bank：银行简称
 *   cardType：卡号类型是储蓄卡（DC）还是信用卡（CC）
 *
 * 2.获取银行卡的logo图
 * https://apimg.alipay.com/combo.png?d=cashier&t=ABC（银行简称-大写）
 *
 */

data class BankInfoResp(
    val key: String?,
    val bank: String?,
    val messages: List<Message?>?,
    val stat: String?,
    val validated: Boolean?,
    val cardType: String?
)

data class Message(
    val errorCodes: String?,
    val name: String?
)

/**
 * map<String,String>
 * key：银行英文简称
 * value：银行中文全称
 *
 * 如：key ICBC , value 中国工商银行
 */
val bankNameMap by lazy { bankAbbrNames.toBean<Map<String, String>>()!! }

/**
 * @param bankAbbrName 银行英文简称
 * @return 银行logo图片地址
 * 使用：
 *  Glide.with(this).load(bankLogoUrl("ABC")).into(binding.ivBankLogo)
 */
fun bankLogoUrl(bankAbbrName: String) =
    "https://apimg.alipay.com/combo.png?d=cashier&t=$bankAbbrName"

/**
 * @param bankNo 银行卡号
 * @return [BankInfoResp] 银行卡信息
 * {
"cardType": "DC",
"bank": "BJRCB",
"key": "6210676802084484923",
"messages": [],
"validated": true,
"stat": "ok"
}
 */
suspend fun getBankInfo(bankNo: String) = bankService.getBankName(bankNo)

/**
 *  用于 EditText 输入银行卡号时，自动分割
 *  注意：EditText 在xml中要设置 "inputType" = "phone"
 */
fun getBankSpaceInputFilter() = getSplitInputFilter(4," ",false)

/**
 * 用于直接设置一个完整的银行卡号
 * @param bankNo 银行卡号
 * return "1111 2222 3333 4444 0000"
 */
fun getBankSpaceText(bankNo: String?): String {
    if (bankNo.isNullOrEmpty()) return ""

    var result = ""

    val temp = if (bankNo.contains(" ")) {
        bankNo.replace(" ", "")
    } else {
        bankNo
    }

    val x = temp.length % 4
    val z = temp.length / 4


    if (z > 0) {
        val sb = StringBuilder()
        repeat(z) {
            sb.append(temp.subSequence(it * 4, (it + 1) * 4)).append(" ")
        }
        if (x > 0) {
            sb.append(temp.subSequence(z * 4, z * 4 + x)).append(" ")
        }
        result = sb.toString().trimEnd()
    }
    return result
}


internal val bankRetrofit by lazy {
    Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .baseUrl("https://ccdcapi.alipay.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}

internal interface BankApi {

    @GET("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true")
    suspend fun getBankName(@Query("cardNo") cardNo: String): BankInfoResp?
}

internal val bankService by lazy { bankRetrofit.create(BankApi::class.java) }


internal const val bankAbbrNames = "{SRCB:深圳农村商业银行," +
        "BGB:广西北部湾银行," +
        "SHRCB:上海农村商业银行," +
        "BJBANK:北京银行," +
        "WHCCB:威海市商业银行," +
        "BOZK:周口银行," +
        "KORLABANK:库尔勒市商业银行," +
        "SPABANK:平安银行," +
        "SDEB:顺德农商银行," +
        "HURCB:湖北省农村信用社," +
        "WRCB:无锡农村商业银行," +
        "BOCY:朝阳银行," +
        "CZBANK:浙商银行," +
        "HDBANK:邯郸银行," +
        "BOC:中国银行," +
        "BOD:东莞银行," +
        "CCB:中国建设银行," +
        "ZYCBANK:遵义市商业银行," +
        "SXCB:绍兴银行," +
        "GZRCU:贵州省农村信用社," +
        "ZJKCCB:张家口市商业银行," +
        "BOJZ:锦州银行," +
        "BOP:平顶山银行," +
        "HKB:汉口银行," +
        "SPDB:上海浦东发展银行," +
        "NXRCU:宁夏黄河农村商业银行," +
        "NYNB:广东南粤银行," +
        "GRCB:广州农商银行," +
        "BOSZ:苏州银行," +
        "HZCB:杭州银行," +
        "HSBK:衡水银行," +
        "HBC:湖北银行," +
        "JXBANK:嘉兴银行," +
        "HRXJB:华融湘江银行," +
        "BODD:丹东银行," +
        "AYCB:安阳银行," +
        "EGBANK:恒丰银行," +
        "CDB:国家开发银行," +
        "TCRCB:江苏太仓农村商业银行," +
        "NJCB:南京银行," +
        "ZZBANK:郑州银行," +
        "DYCB:德阳商业银行," +
        "YBCCB:宜宾市商业银行," +
        "SCRCU:四川省农村信用," +
        "KLB:昆仑银行," +
        "LSBANK:莱商银行," +
        "YDRCB:尧都农商行," +
        "CCQTGB:重庆三峡银行," +
        "FDB:富滇银行," +
        "JSRCU:江苏省农村信用联合社," +
        "JNBANK:济宁银行," +
        "CMB:招商银行," +
        "JINCHB:晋城银行JCBANK," +
        "FXCB:阜新银行," +
        "WHRCB:武汉农村商业银行," +
        "HBYCBANK:湖北银行宜昌分行," +
        "TZCB:台州银行," +
        "TACCB:泰安市商业银行," +
        "XCYH:许昌银行," +
        "CEB:中国光大银行," +
        "NXBANK:宁夏银行," +
        "HSBANK:徽商银行," +
        "JJBANK:九江银行," +
        "NHQS:农信银清算中心," +
        "MTBANK:浙江民泰商业银行," +
        "LANGFB:廊坊银行," +
        "ASCB:鞍山银行," +
        "KSRB:昆山农村商业银行," +
        "YXCCB:玉溪市商业银行," +
        "DLB:大连银行," +
        "DRCBCL:东莞农村商业银行," +
        "GCB:广州银行," +
        "NBBANK:宁波银行," +
        "BOYK:营口银行," +
        "SXRCCU:陕西信合," +
        "GLBANK:桂林银行," +
        "BOQH:青海银行," +
        "CDRCB:成都农商银行," +
        "QDCCB:青岛银行," +
        "HKBEA:东亚银行," +
        "HBHSBANK:湖北银行黄石分行," +
        "WZCB:温州银行," +
        "TRCB:天津农商银行," +
        "QLBANK:齐鲁银行," +
        "GDRCC:广东省农村信用社联合社," +
        "ZJTLCB:浙江泰隆商业银行," +
        "GZB:赣州银行," +
        "GYCB:贵阳市商业银行," +
        "CQBANK:重庆银行," +
        "DAQINGB:龙江银行," +
        "CGNB:南充市商业银行," +
        "SCCB:三门峡银行," +
        "CSRCB:常熟农村商业银行," +
        "SHBANK:上海银行," +
        "JLBANK:吉林银行," +
        "CZRCB:常州农村信用联社," +
        "BANKWF:潍坊银行," +
        "ZRCBANK:张家港农村商业银行," +
        "FJHXBC:福建海峡银行," +
        "ZJNX:浙江省农村信用社联合社," +
        "LZYH:兰州银行," +
        "JSB:晋商银行," +
        "BOHAIB:渤海银行," +
        "CZCB:浙江稠州商业银行," +
        "YQCCB:阳泉银行," +
        "SJBANK:盛京银行," +
        "XABANK:西安银行," +
        "BSB:包商银行," +
        "JSBANK:江苏银行," +
        "FSCB:抚顺银行," +
        "HNRCU:河南省农村信用," +
        "COMM:交通银行," +
        "XTB:邢台银行," +
        "CITIC:中信银行," +
        "HXBANK:华夏银行," +
        "HNRCC:湖南省农村信用社," +
        "DYCCB:东营市商业银行," +
        "ORBANK:鄂尔多斯银行," +
        "BJRCB:北京农村商业银行," +
        "XYBANK:信阳银行," +
        "ZGCCB:自贡市商业银行," +
        "CDCB:成都银行," +
        "HANABANK:韩亚银行," +
        "CMBC:中国民生银行," +
        "LYBANK:洛阳银行," +
        "GDB:广东发展银行," +
        "ZBCB:齐商银行," +
        "CBKF:开封市商业银行," +
        "H3CB:内蒙古银行," +
        "CIB:兴业银行," +
        "CRCBANK:重庆农村商业银行," +
        "SZSBK:石嘴山银行," +
        "DZBANK:德州银行," +
        "SRBANK:上饶银行," +
        "LSCCB:乐山市商业银行," +
        "JXRCU:江西省农村信用," +
        "ICBC:中国工商银行," +
        "JZBANK:晋中市商业银行," +
        "HZCCB:湖州市商业银行," +
        "NHB:南海农村信用联社," +
        "XXBANK:新乡银行," +
        "JRCB:江苏江阴农村商业银行," +
        "YNRCC:云南省农村信用社," +
        "ABC:中国农业银行," +
        "GXRCU:广西省农村信用," +
        "PSBC:中国邮政储蓄银行," +
        "BZMD:驻马店银行," +
        "ARCU:安徽省农村信用社," +
        "GSRCU:甘肃省农村信用," +
        "LYCB:辽阳市商业银行," +
        "JLRCU:吉林农信," +
        "URMQCCB:乌鲁木齐市商业银行," +
        "XLBANK:中山小榄村镇银行," +
        "CSCB:长沙银行," +
        "JHBANK:金华银行," +
        "BHB:河北银行," +
        "NBYZ:鄞州银行," +
        "LSBC:临商银行," +
        "BOCD:承德银行," +
        "SDRCU:山东农信," +
        "NCB:南昌银行," +
        "TCCB:天津银行," +
        "WJRCB:吴江农商银行," +
        "CBBQS:城市商业银行资金清算中心," +
        "HBRCU:河北省农村信用社}"





