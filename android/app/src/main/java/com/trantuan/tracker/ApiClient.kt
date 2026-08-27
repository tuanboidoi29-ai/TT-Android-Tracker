package com.trantuan.tracker
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
object ApiClient {
 private const val URL="https://YOUR-SERVER.example/api/location"
 private val client=OkHttpClient(); private val type="application/json; charset=utf-8".toMediaType()
 fun send(id:String,lat:Double,lon:Double,acc:Float,ts:Long){if(URL.contains("YOUR-SERVER"))return;val j=JSONObject().apply{put("device_id",id);put("latitude",lat);put("longitude",lon);put("accuracy",acc);put("timestamp",ts)};val r=Request.Builder().url(URL).post(j.toString().toRequestBody(type)).build();runCatching{client.newCall(r).execute().close()}}
}
