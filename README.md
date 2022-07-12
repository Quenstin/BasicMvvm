# BasicMvvm基于jetpack mvvm retiofit 协程 封装的开发框架

一：需要使用MVVM模式进行数据处理需要实现BaseMVVMActivity<viewModel,viewBinding>
第一步：
引入：project gradle
  ![image](https://user-images.githubusercontent.com/26788041/116022935-c4ee3380-a67d-11eb-889c-329679223092.png)
  
第二步：
  app gradle:
  implementation 'com.github.Quenstin:BasicMvvm:latest'
  ![image](https://user-images.githubusercontent.com/26788041/116022980-d9323080-a67d-11eb-916e-ba224f948980.png)

第三步：
  配置项目中的请求地址baseUrl
![image](https://user-images.githubusercontent.com/26788041/116180263-2c25e980-a74b-11eb-997f-233c477a6094.png)

第四步：
  实现BaseRepository来自定义自己的ApiRepository，后续的Repository都实现ApiRepository
  ![image](https://user-images.githubusercontent.com/26788041/116182891-96d92400-a74f-11eb-9b27-fe1c3e353f46.png)

第五步：
  实现ApiRepository，定义和业务相关的Repository如下：
  ![image](https://user-images.githubusercontent.com/26788041/128305971-dc695043-010d-43d3-8dca-0a87bf9849ae.png)
第六步：实现自己的viewModel
  ![image](https://user-images.githubusercontent.com/26788041/128306101-d897ddd3-e883-4774-947c-04afb4575f3e.png)
第七步：在activity中获取viewModel展示数据：
![image](https://user-images.githubusercontent.com/26788041/128306225-300f42c5-d623-4718-aeec-bbddeb4cd589.png)

二：不需要MVVM处理数据可以实现BaseActivity<viewBinding> 
  
  
PS：
  后续增加：
    1:下载上传
    2:权限申请
    3:暂时没想出来


