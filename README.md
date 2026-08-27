# TT Android Tracker

MVP theo dõi vị trí có sự chấp thuận trên thiết bị Android.

## Kiến trúc
Android Client → HTTPS API → SQLite → Admin Dashboard

## Nguyên tắc
- Vị trí chỉ được gửi sau khi người dùng cấp quyền Android.
- Android hiển thị Foreground Service notification khi đang chia sẻ vị trí.
- Không root, không né quyền, không ẩn camera hoặc ghi màn hình.

## Chạy server
```bash
cd server
npm install
npm start
```

Mặc định API chạy tại `http://localhost:3000`.

## Android
Mở thư mục `android` bằng Android Studio, cấu hình `SERVER_URL` trong `ApiClient.kt`, rồi Build APK.