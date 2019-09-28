package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.RecentHot.recentTaskList
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AvatarInfo, GetAvatarListRsp, GetListRsp, PostUserReq, UserInfo}

import scala.xml.Node
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml.Var
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * 修改当前登录用户的个人信息
 */
object EditProfile {

  val avatarList = Var(List.empty[AvatarInfo])
  val currentUse = Var(1)
  var currentChoose = 1

  def saveProfile: Unit ={
    val password = dom.document.getElementById("userPassword").asInstanceOf[Input].value
    Http.postJsonAndParse[SuccessRsp](Routes.User.editProfile,PostUserReq(password,currentChoose).asJson.noSpaces).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          JsFunc.alert("修改成功")
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
    Http.getAndParse[GetAvatarListRsp](Routes.User.getAvatarList).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          avatarList := rsp.list.get
          currentUse := rsp.currentUseAvatarId
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")
    }

  }

  def changeCurrentUse(id:Int): Unit ={
    currentChoose = id
  }

  val avatarListRx = avatarList.map{
    case Nil =>
      <form class="mdui-p-l-5"></form>
    case list =>
      <form class="mdui-p-l-5">
        { list.map{
        l =>
          if (l.id.toString == currentUse.toString()){
            <label class="mdui-radio" onclick={()=>changeCurrentUse(l.id)}>
              <input type="radio" name="group1" checked="true" />
              <i class="mdui-radio-icon"></i>
              <img class="avatar-choice" src={l.url} />
            </label>
          }else{
            <label class="mdui-radio" onclick={()=>changeCurrentUse(l.id)}>
              <input type="radio" name="group1"/>
              <i class="mdui-radio-icon"></i>
              <img class="avatar-choice" src={l.url} />
            </label>
          }
      }}

      </form>
  }

  def app:Node = {

    getAvatarList

    <div id="page-question" class="mdui-container">

      <div class="mdui-card mdui-center question mdui-p-t-3 mdui-p-b-3">
        <div class="mdui-textfield">
          <i class="mdui-icon material-icons">lock</i>
          <label class="mdui-textfield-label">密码</label>
          <input class="mdui-textfield-input" id="userPassword" type="text"/>
        </div>

        <div class="mdui-textfield">
          <label class="mdui-textfield-label mdui-m-b-1">选择头像</label>
            {avatarListRx}
        </div>
        <div class="actions mdui-m-t-2"><button class="mdui-btn mdui-btn-raised mdui-text-color-blue" onclick={()=>saveProfile}>提交修改</button></div>
      </div>
    </div>
  }
}
