package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
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

/**
 * 用户主页
 */
case class User(userId: Int) {


  val fans_num = Var(0)//粉丝数目
  val focus_num = Var(0)//关注者数目
  val isFocus = Var(false)
  val loginName = Var("")
  val userName = Var("")

  val userRecordList = Var(List.empty[TaskRecord])
  val concernUserList = Var(List.empty[UserInfo])
  val fanUserList = Var(List.empty[UserInfo])
  val userRecordListRx: Rx[Elem] = returnRecordRX(userRecordList)
  val currentUser = Var(UserInfo(1,"加载中",AvatarInfo(1,"")))

  val currentUserRx = currentUser.map{
    case user =>
      <div>
        <div class="avatar-box">
          <img src={user.avatar.url} class="avatar" />
        </div>
        <div class="username">
          {user.userName}
        </div>
      </div>
  }

  val concernUserListRx = concernUserList.map{
      case Nil => <div></div>
      case list =>
        <div class="mdui-dialog mc-users-dialog" id="concernUsers">
          <div class="mdui-dialog-title">
            <button class="mdui-btn mdui-btn-icon mdui-ripple" mdui-dialog-close="true"><i class="mdui-icon material-icons">close</i></button>关注了 {list.length}人
          </div>
          <div class="mdui-dialog-content" style="height: 652px;">
            <ul class="mdui-list">
          {list.map{
            l=>
              <li class="mdui-list-item mdui-ripple">
                <div class="mdui-list-item-avatar">
                  <img src={l.avatar.url} />
                </div>
                <div class="mdui-list-item-content">
                  {l.userName}
                </div>
              </li>
          }}
          </ul>
            </div>
          </div>
  }
  def getConcernList(): Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetUserListRsp](Routes.User.getConcernList,data).map{
      case Right(rsp) =>
      if (rsp.errCode == 0){
        concernUserList := rsp.list.get
        fans_num := rsp.list.get.length
      }else{
        JsFunc.alert(rsp.msg)
        dom.window.location.hash = s"#/Login"
        println(rsp.msg)
      }
      case Left(error) =>
    }
  }


  def fansList: Unit ={

  }


  def getCurrentUser: Unit ={
    //不仅要获取该用户的信息，还需要获取当前登录用户和该用户的关系：是否是一个用户，或者是否已经关注
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetOtherUserRsp](Routes.User.getCurrentUser,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          currentUser := rsp.userInfo
          isFocus := rsp.isFocus
          userName := rsp.userInfo.userName
          loginName := rsp.loginName
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
    }

  }

  def getRecordListByUser: Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetListRsp](Routes.List.getRecordById,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          userRecordList := rsp.list.get
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }

      case Left(error) =>
        println(s"get task list error,$error")
    }
  }

  def addOrCancelFocus: Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.User.addOrCancelFocus,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          JsFunc.alert(rsp.msg)
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }

  }

  val focusStatusRx = isFocus.map{
    r =>
      {
        userName.map(u=>
        loginName.map{
          l=>
            if (l == u){
              if (r){
                <div onclick={() => addOrCancelFocus} class="mdui-btn mdui-btn-raised right-btn">取消关注</div>
              }else{
                <div onclick={() => addOrCancelFocus} class="mdui-btn mdui-btn-raised right-btn">关注</div>
              }
            }else{
              <div></div>
            }
        }
        )
      }
  }
  def app:Node = {
    getCurrentUser
    getRecordListByUser
    getConcernList()
    <div id="page-questions" class="mdui-container main">
      <div id="page-user" class="mdui-container">
        <div class="mdui-card cover" style="background-image: url(&quot;https://www.mdui.org/static/common/image/cover/default_l.webp&quot;);">
          <div class="gradient mdui-card-media-covered mdui-card-media-covered-gradient"></div>
          <div class="info">
            {currentUserRx}
            <div class="meta">
              <a mdui-dialog="{target: '#concernUsers'}" class="following">关注了{focus_num} 人</a>
              <span class="mdui-m-x-1">|</span>
              <a mdui-dialog="{target: '#fansUsers'}" class="followers">{fans_num} 位关注者</a>
            </div>
            {focusStatusRx}
          </div>
        </div>
        {userRecordListRx}
        {concernUserListRx}
      </div>
    </div>
  }
}
