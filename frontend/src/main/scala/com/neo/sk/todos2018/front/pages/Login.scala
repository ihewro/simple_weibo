package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.LoginProtocol.UserLoginAndRegisterReq
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.html.Input

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
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = "/List"
        }
        else{
          if (typeStr == "login"){
            JsFunc.showMessage(s"${rsp.msg}")
          }else{
            JsFunc.showMessage(s"${rsp.msg}")
          }
        }
      case Left(error) =>
        JsFunc.showMessage(s"parse error,$error")
    }
  }

  def app: Node =
    <div>

      <div class="mc-login mdui-dialog mdui-dialog-open" style="display: block; top: 150.5px;">
        <div class="mdui-dialog-title mdui-color-theme">
          欢迎登陆/注册
        </div>
        <form>
          <div class="mdui-textfield mdui-textfield-floating-label mdui-textfield-has-bottom mdui-textfield-not-empty">
            <label class="mdui-textfield-label">用户名或邮箱</label>
            <input class="mdui-textfield-input" id="userName" name="name" type="text" required="true" />
            <div class="mdui-textfield-error">
              账号不能为空
            </div>
          </div>
          <div class="mdui-textfield mdui-textfield-floating-label mdui-textfield-has-bottom mdui-textfield-not-empty">
            <label class="mdui-textfield-label">密码</label>
            <input class="mdui-textfield-input" id="userPassword" name="password" type="password" required="true" />
            <div class="mdui-textfield-error">
              密码不能为空
            </div>
          </div>
          <div class="actions mdui-clearfix">
            <button type="button" onclick = {()=> userLoginAndRegister("register")} class="mdui-btn mdui-btn-raised mdui-color-theme-accent mdui-float-left">注册</button>
            <button type="button" onclick = {()=> userLoginAndRegister("login")} class="mdui-btn mdui-btn-raised mdui-color-theme-accent mdui-float-right">登录</button>
          </div>
        </form>
      </div>
    </div>
}
