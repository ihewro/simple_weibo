# 简易微博
使用mdui css框架，模仿的是mdui的问答界面，是学习Scala的一个小demo

![](https://s2.ax1x.com/2019/09/29/u88ez9.png)


## 工程运行

运行本工程：

* 打开IDEA 下的terminal；
* 输入sbt回车；（如果报错，请确保你的电脑有正确安装sbt，第一次运行需要更新sbt和下载相关依赖，耐心等待）
   * X:\xxx\xxxx\xxx> sbt
* 此时你已经进入sbt交互式命令行状态：
     * sbt:todos2018> project backend
     * sbt:todos2018> reStart
* 工程默认运行在30330端口，浏览器输入http://localhost:30330/todos2018；
* 在浏览器上使用；
* 端口、数据库等数据写在配置文件里，配置文件目录：backend/src/main/resources/application.conf；
* 数据库路径为相对路径，实际操作最好改为绝对路径，即DATA/H2/todos2018文件在自己电脑的绝对路径
* 操作数据库现在使用的是Slick，Slick的代码结构在backend\src\main\scala\com\neo\sk\todos2018\models\SlickTables.scala。
  这是backend\src\main\scala\com\neo\sk\todos2018\utils\MySlickCodeGenerator.scala生成的。
  MySlickCodeGenerator可以右键直接运行

## 参考链接
* [scala-js](http://www.scala-js.org/doc/tutorial/basic/)
* [akka-http](https://doc.akka.io/docs/akka-http/current/introduction.html)
* [monadic-html](https://github.com/OlivierBlanvillain/monadic-html/blob/master/README.md)


