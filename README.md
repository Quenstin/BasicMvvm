# BasicMvvm基于jetpack mvvm retiofit 协程 封装的开发框架

第一步：
引入：project gradle
  ![image](https://user-images.githubusercontent.com/26788041/116022935-c4ee3380-a67d-11eb-889c-329679223092.png)
  
第二步：
  app gradle:
  implementation 'com.github.Quenstin:BasicMvvm:1.0.1'
  ![image](https://user-images.githubusercontent.com/26788041/116022980-d9323080-a67d-11eb-916e-ba224f948980.png)

第三步：
  配置项目中的请求地址baseUrl
![image](https://user-images.githubusercontent.com/26788041/116180263-2c25e980-a74b-11eb-997f-233c477a6094.png)

第四步：
  实现BaseRepository来自定义自己的ApiRepository，后续的Repository都实现ApiRepository
  ![image](https://user-images.githubusercontent.com/26788041/116182891-96d92400-a74f-11eb-9b27-fe1c3e353f46.png)


