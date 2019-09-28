package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.RecentHot.recentTaskList
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AvatarInfo, GetListRsp, PostUserReq, UserInfo}

import scala.xml.Node
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * 修改当前登录用户的个人信息
 */
object EditProfile {

  def saveProfile: Unit ={
    Http.postJsonAndParse[SuccessRsp](Routes.User.editProfile,PostUserReq(UserInfo(1,"",AvatarInfo(1,""))).asJson.noSpaces).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          JsFunc.alert("保存成功")
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")
    }
  }

  def getAvatarList: Unit = {

  }

  def app:Node = {
    <div id="page-question" class="mdui-container">

      <div class="mdui-card mdui-center question mdui-p-t-3 mdui-p-b-3">
        <div class="mdui-textfield">
          <i class="mdui-icon material-icons">lock</i>
          <label class="mdui-textfield-label">密码</label>
          <input class="mdui-textfield-input" id="userPassword" type="text"/>
        </div>

        <div class="mdui-textfield">
          <i class="mdui-icon material-icons">email</i>
          <label class="mdui-textfield-label mdui-m-b-1">选择头像</label>
          <form class="mdui-p-l-5">
            <label class="mdui-radio">
              <input type="radio" name="group1"/>
              <i class="mdui-radio-icon"></i>
              默认不选中
            </label>

            <label class="mdui-radio">
              <input type="radio" name="group1"/>
              <i class="mdui-radio-icon"></i>
              默认选中
            </label>
          </form>
        </div>
        <div class="actions mdui-m-t-2"><button class="mdui-btn mdui-btn-raised mdui-text-color-blue">提交修改</button></div>
      </div>
    </div>
  }
}
