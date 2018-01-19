基于 Spring MVC 和 Spring schedule 的 Yandex 壁纸接口  

### 使用
- 每日壁纸  
  ````
  curl -v https://pub.shadowland.cn/wallpaper/yandex/
  ````
  ````
  HTTP/1.1 302 Found
  Location: https://avatars.mds.yandex.net/get-imageoftheday/142379/6322d63994e04710a0ffdc4eddae9610/orig
  ````
- 详细数据  
  ````
  curl https://pub.shadowland.cn/wallpaper/yandex/2018-01-19
  ````
  ````
  {
     "code": 200,
     "data": {
       "date": "2018-01-19 ",
       "url": "https://avatars.mds.yandex.net/get-imageoftheday/142379/6322d63994e04710a0ffdc4eddae9610/orig",
       "title": "Twilight",
       "description": "Cosy cottages beckon on an austere Norwegian coastline.",
       "authorName": "Sergey",
       "authorLink": "http://www.airpano.com/Photogallery.php?gallery=243",
       "partner": "Airpano",
       "hashDate": "IBgBGQ"
     }
   }
  ```` 