package com.neo.sk.todos2018.shared.ptcl

/**
  * User: XuSiRan
  * Date: 2019/3/26
  * Time: 19:02
 * shared 文件夹是返回给后端返回给前端的信息结构体
  */
object LoginProtocol {

  case class UserLoginAndRegisterReq(
    userName: String,
    password: String
  )

  case class UserLoginRsp(
    errCode: Int,
    msg: String
  ) extends CommonRsp


}
