package com.neo.sk.todos2018.ptcl

/**
  * User: sky
  * Date: 2018/4/8
  * Time: 17:00
 * protocol 包里面是后端内部传递消息的结构体
  */
object UserProtocol {
  case class UserBaseInfo(
                         userid: Int,
                         userName:String
                         )
}
