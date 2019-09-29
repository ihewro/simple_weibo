package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{FocusUserInfo, GetListRsp, GetRecommendUserListRsp, GoToCommentReq}
import mhtml.Var
import org.scalajs.dom
import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.EditProfile.{avatarList, currentUse, loginName}
import com.neo.sk.todos2018.front.pages.TaskList.{returnRecordRX, taskMyListRx}
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AvatarInfo, GetListRsp, GetOtherUserRsp, GetUserListRsp, GoToCommentReq, TaskRecord, UserInfo}
import mhtml.{Rx, Var}
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{Elem, Node}
import scala.xml.Node

object recommendFocus {

  val userList = Var(List.empty[FocusUserInfo])

  def getRecommendUserList = {
    Http.getAndParse[GetRecommendUserListRsp](Routes.User.getRecommendUserList).map {
      case Right(rsp) =>
        if (rsp.errCode == 0){
          userList := rsp.list.get
        }else{
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")
    }
  }

  def addOrCancelFocus(userId: Int): Unit = {
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.User.addOrCancelFocus, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          JsFunc.showMessage(rsp.msg)
          //刷新数据
          getRecommendUserList
        } else {
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }
  }

  def goUser(userId:Int): Unit = {
    dom.window.location.hash = s"#/User/" + userId

  }
    val userListRx = userList.map {
      list =>
        <div class="mdui-row-xs-2 mdui-row-sm-3 mdui-row-md-4">

          {list.map {
          l =>
            <div class="mdui-col">
              <div class="mdui-card item">
                <div class="mdui-ripple info" onclick={()=>goUser(l.userInfo.id)}>
                  <div class="avatar" style={"background-image: url(" + l.userInfo.avatar.url + ");"}>
                  </div> <div class="username">
                  {l.userInfo.userName}
                </div> <div class="headline">
                </div>
                </div> <div class="actions">
                {if (l.isFocus) {
                  <button class="mdui-btn mdui-text-color-blue mdui-btn-block" onclick={() => addOrCancelFocus(l.userInfo.id)}>取消关注</button>
                } else {
                  <button class="mdui-btn mdui-text-color-blue mdui-btn-block" onclick={() => addOrCancelFocus(l.userInfo.id)}>关注</button>
                }}
              </div>
              </div>
            </div>
        }}
        </div>
    }

    def app: Node = {
      getRecommendUserList
      <div id="page-users" class="mdui-container">
      <div id="recommended">
        <div class="subheading">人员推荐</div>{userListRx}
      </div>
      </div>
    }

}
