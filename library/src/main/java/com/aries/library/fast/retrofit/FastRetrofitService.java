package com.aries.library.fast.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @Author: AriesHoo on 2018/7/23 14:23
 * @E-Mail: AriesHoo@126.com
 * Function: 快速实现文件下载/上传
 * Description:
 */
interface FastRetrofitService {

    /**
     * 大文件官方建议用 @Streaming 来进行注解,不然会出现IO异常,小文件可以忽略
     *
     * @param fileUrl 地址
     * @param header  可增加header信息
     * @return ResponseBody
     */
//    @Headers({"User-Agent: Mozilla/5.0 (Android/4.0.3; HUAWEI U9200; IMEI/111111111111111; IMSI/111111111111)"})
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl, @HeaderMap Map<String, Object> header);

    /**
     * 文件及其它参数
     *
     * @param uploadUrl 接口全路径
     * @param body
     * @param header
     * @return
     */
    @POST
    Observable<ResponseBody> uploadFile(@Url String uploadUrl, @Body RequestBody body, @HeaderMap Map<String, Object> header);

}
