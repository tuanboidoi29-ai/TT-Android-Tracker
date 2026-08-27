package com.trantuan.tracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.*

class MainActivity : ComponentActivity() {
 private val req=100
 override fun onCreate(b: Bundle?) { super.onCreate(b); setContentView(R.layout.activity_main)
  val status=findViewById<TextView>(R.id.status); findViewById<TextView>(R.id.deviceId).text="Mã thiết bị: ${DeviceManager.id(this)}"
  findViewById<Button>(R.id.startButton).setOnClickListener { if(!hasPermission()) ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),req) else start() }
  findViewById<Button>(R.id.stopButton).setOnClickListener { stopService(Intent(this,LocationService::class.java)); status.text="Đã dừng chia sẻ vị trí." }
 }
 private fun hasPermission()=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
 private fun start(){ ContextCompat.startForegroundService(this,Intent(this,LocationService::class.java)); findViewById<TextView>(R.id.status).text="Đang chia sẻ vị trí qua dịch vụ nền." }
 override fun onRequestPermissionsResult(r:Int,p:Array<out String>,g:IntArray){super.onRequestPermissionsResult(r,p,g);if(r==req&&g.any{it==PackageManager.PERMISSION_GRANTED})start()}
}
