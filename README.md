## AndroidJointLanding
---
### 1. android 微信、QQ、微博联合登陆
+ QQ/微博登录注意权限配置
+ 微信登录注意在默认包名目录新增wxapi目录和WxEnteryActivity类文件（固定格式，如果不对则无法接受到微信反馈的信息）
+  java.lang.UnsatisfiedLinkError: com.android.tools.fd.runtime.IncrementalClas 错误 在libs下新建一个armeabi-v7a，然后将liblocSDK3.so复制一份到该文件夹

### 2. android 微信、QQ、微博获取用户信息 

### 3. android 微信、QQ、微博分享

##使用帮助
---
*微信登陆需要使用正确的APPKEY，并且应用包名必须与用户注册的微信公众号创建应用时所用包名一致，才能够在WXEntryActivity页面实现回调获取到从微信服务器返回的access_token，再通过获取到access_token获取到相应的用户信息。*