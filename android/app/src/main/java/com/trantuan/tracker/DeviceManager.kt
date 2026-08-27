package com.trantuan.tracker
import android.content.Context
import java.util.UUID
object DeviceManager { fun id(c:Context):String { val p=c.getSharedPreferences("device",0); var x=p.getString("id",null); if(x==null){x="ANDROID-"+UUID.randomUUID().toString().substring(0,8).uppercase();p.edit().putString("id",x).apply()};return x } }
