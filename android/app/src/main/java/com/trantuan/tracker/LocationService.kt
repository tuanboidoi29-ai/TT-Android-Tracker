package com.trantuan.tracker
import android.app.*
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
class LocationService:Service(){private lateinit var fused:FusedLocationProviderClient;private lateinit var cb:LocationCallback
 override fun onCreate(){super.onCreate();val ch="tt_location";getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel(ch,"Chia sẻ vị trí",NotificationManager.IMPORTANCE_LOW));startForeground(1,NotificationCompat.Builder(this,ch).setContentTitle("TT Tracker đang chia sẻ vị trí").setContentText("Vị trí đang được đồng bộ về máy chủ.").setSmallIcon(android.R.drawable.ic_menu_mylocation).setOngoing(true).build());fused=LocationServices.getFusedLocationProviderClient(this);val req=LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,15000).setMinUpdateIntervalMillis(5000).build();cb=object:LocationCallback(){override fun onLocationResult(r:LocationResult){val id=DeviceManager.id(this@LocationService);r.locations.forEach{l->Thread{ApiClient.send(id,l.latitude,l.longitude,l.accuracy,System.currentTimeMillis())}.start()}}};try{fused.requestLocationUpdates(req,cb,Looper.getMainLooper())}catch(_:SecurityException){stopSelf()}}
 override fun onDestroy(){if(::fused.isInitialized&&::cb.isInitialized)fused.removeLocationUpdates(cb);super.onDestroy()}
 override fun onBind(i:Intent?):IBinder?=null
}
