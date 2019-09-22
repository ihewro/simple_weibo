package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.UserLoginAndRegisterReq
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Node

/**
  * User: XuSiRan
  * Date: 2019/3/26
  * Time: 17:40
  */
object Login{

  val url = "#/" + "Login"

  private def userLoginAndRegister(typeStr: String): Unit ={
    val userName = dom.document.getElementById("userName").asInstanceOf[Input].value
    val password = dom.document.getElementById("userPassword").asInstanceOf[Input].value
    var url = ""
    if (typeStr == "login"){
      url = Routes.Login.userLogin
    }else{
      url = Routes.Login.userRegister
    }
    Http.postJsonAndParse[SuccessRsp](url, UserLoginAndRegisterReq(userName, password).asJson.noSpaces).map{
      case Right(rsp) =>
        println(rsp)
        if(rsp.errCode == 0){
          if (typeStr == "login"){
            JsFunc.alert("登陆成功")
          }else{
            JsFunc.alert("注册成功")
          }
          dom.window.location.hash = "/List"
        }
        else{
          if (typeStr == "login"){
            JsFunc.alert(s"登陆失败：${rsp.msg}")
          }else{
            JsFunc.alert(s"注册失败：${rsp.msg}")
          }
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
    }
  }

  def app: Node =
    <div>
      <div class = "LoginForm">
        <h2>欢迎登陆/注册</h2>
        <div class = "inputContent">
          <span>用户名</span>
          <input id = "userName"></input>
        </div>
        <div class = "inputContent">
          <span>密码</span>
          <input id = "userPassword" type = "password"></input>
        </div>
        <button onclick = {()=> userLoginAndRegister("login")}>登陆</button>
        <button onclick = {()=> userLoginAndRegister("register")}>注册</button>

      </div>
    </div>
}
