# HttpServer

##  通过Netty实现一套简易的HTTP服务器

### [参考项目](https://gitee.com/IdeaHome_admin/ITree/tree/master)

模块 | netty-all | cglib | fastjson | lombok | slf4j-api

模块|netty-all|cglib|fastjson|lombok 
---|---|---|---|---
最新版本|[![Download](https://img.shields.io/badge/Download-4.1.36-brightgreen.svg)](https://mvnrepository.com/artifact/io.netty/netty-all/4.1.36.Final)|[![Download](https://img.shields.io/badge/Download-3.2.12-brightgreen.svg)](https://mvnrepository.com/artifact/cglib/cglib/3.2.12)|[![Download](https://img.shields.io/badge/Download-1.2.58-brightgreen.svg)](https://mvnrepository.com/artifact/com.alibaba/fastjson/1.2.58)|[![Download](https://img.shields.io/badge/Download-1.18.8-brightgreen.svg)](https://mvnrepository.com/artifact/org.projectlombok/lombok/1.18.8)

### 功能介绍：
*  **支持对静态文件css，js，html，图片资源的访问；**
*  **支持用户自定义配置文件httpserver-config.properties；**
*  **利用注解@Controller实现控制器，并模拟视图解析器； **
*  **利用注解@Filter实现过滤器，并可以设置优先级。 **

### 主要流程：
*  **注册所有的Controller控制器与Filter过滤器，配置Netty服务器（HttpserverApplication.start()函数）；**
*  **Netty服务器接收请求（NettyHttpServerHandler类）；**
*  **获取请求信息，包括地址与各类参数（CommonUtil类）；**
*  **根据请求地址寻找对应的控制器（ControllerReactor类）；**
*  **判断是否需要读取静态资源（StaticFileHandler类）；**
*  **利用Cglib生成代理对象，在代理前后执行过滤器（ControllerCglib类）；**
*  **将返回结果（JSON/视图）生成标准响应（ResponseCoreHandle类）；**
*  **Netty服务器返回响应，流程结束。**


