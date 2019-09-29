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
import com.neo.sk.todos2018.front.pages.recommendFocus.goUser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{Elem, Node}

/**
 * 用户主页
 */
case class User(userId: Int) {


  val fans_num = Var(0)//粉丝数目
  val focus_num = Var(0)//关注者数目
  var isFocus = false
  var loginName = ""
  var userName = ""

  val userRecordList = Var(List.empty[TaskRecord])
  val concernUserList = Var(List.empty[UserInfo])
  val fanUserList = Var(List.empty[UserInfo])
  val userRecordListRx: Rx[Elem] = returnRecordRX(userRecordList)
  val currentUser = Var(UserInfo(1,"加载中",AvatarInfo(1,"http://localhost:30330/todos2018/static/avatar/5.jpg")))

  val currentUserRx = currentUser.map{
    case user =>
      <div class="mdui-card cover" style="background-image: url(http://localhost:30330/todos2018/static/avatar/default_bg.jpg);">
        <div class="gradient mdui-card-media-covered mdui-card-media-covered-gradient"></div>
        <div class="info">
      <div>
        <div class="avatar-box">
          <img src={user.avatar.url} class="avatar" />
        </div>
        <div class="username">
          {user.userName}
        </div>
      </div>
          <div class="meta">
            <a mdui-dialog="{target: '#concernUsers',history:false}" class="following">关注了{focus_num} 人</a>
            <span class="mdui-m-x-1">|</span>
            <a mdui-dialog="{target: '#fansUsers'}" class="followers">{fans_num} 位粉丝</a>
          </div>
          {
          println("当前登录"+ userName + "登录用户" + loginName)
          if (userName != loginName){
            if (isFocus){
              <div onclick={() => addOrCancelFocus} class="mdui-btn mdui-btn-raised right-btn">取消关注</div>
            }else{
              <div onclick={() => addOrCancelFocus} class="mdui-btn mdui-btn-raised right-btn">关注</div>
            }
          }else{
            <div></div>
          }
          }
        </div>
      </div>
  }

  val concernUserListRx = concernUserList.map{
      case Nil => <div class="mdui-dialog mc-users-dialog" id="concernUsers">
        <div class="mdui-dialog-title">
          <button class="mdui-btn mdui-btn-icon mdui-ripple close" mdui-dialog-close="true">
            <i class="mdui-icon material-icons">close</i></button>关注了 0 人
        </div>
        <div class="mdui-dialog-content">
          <ul class="mdui-list"></ul>
          <div class="mc-empty"><div class="title">尚未关注任何用户</div>
            <div class="description">关注用户后，相应用户就会显示在此处。</div></div>
        </div>
      </div>
      case list =>
        <div class="mdui-dialog mc-users-dialog" id="concernUsers">
          <div class="mdui-dialog-title">
            <button class="mdui-btn mdui-btn-icon mdui-ripple" mdui-dialog-close="true"><i class="mdui-icon material-icons">close</i></button>关注了 {list.length}人
          </div>
          <div class="mdui-dialog-content">
            <ul class="mdui-list">
          {list.map{
            l=>
              <li class="mdui-list-item mdui-ripple" onclick={()=>goUser(l.id)}>
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



  val fanUserListRx = fanUserList.map{
    case Nil => <div class="mdui-dialog mc-users-dialog" id="fansUsers">
      <div class="mdui-dialog-title">
        <button class="mdui-btn mdui-btn-icon mdui-ripple close" mdui-dialog-close="true">
          <i class="mdui-icon material-icons">close</i></button> 0 个粉丝
      </div>
      <div class="mdui-dialog-content">
        <ul class="mdui-list"></ul>
        <div class="mc-empty"><div class="title">没有任何粉丝</div>
          <div class="description">如果有人喜欢Ta，相应用户就会显示在此处。</div></div>
      </div>
    </div>
    case list =>
      <div class="mdui-dialog mc-users-dialog" id="fansUsers">
        <div class="mdui-dialog-title">
          <button class="mdui-btn mdui-btn-icon mdui-ripple" mdui-dialog-close="true"><i class="mdui-icon material-icons">close</i></button> {list.length} 个粉丝
        </div>
        <div class="mdui-dialog-content">
          <ul class="mdui-list">
            {list.map{
            l=>
              <li class="mdui-list-item mdui-ripple" onclick={()=>goUser(l.id)}>
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

  def getConcernList: Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetUserListRsp](Routes.User.getConcernList,data).map{
      case Right(rsp) =>
      if (rsp.errCode == 0){
        concernUserList := rsp.list.get
        focus_num := rsp.list.get.length
      }else{
        JsFunc.showMessage(rsp.msg)
        dom.window.location.hash = s"#/Login"
        println(rsp.msg)
      }
      case Left(error) =>
    }
  }


  def getFansList: Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetUserListRsp](Routes.User.getFansList,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          fanUserList := rsp.list.get
          fans_num := rsp.list.get.length
        }else{
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
    }
  }


  def getCurrentUser: Unit ={
    //不仅要获取该用户的信息，还需要获取当前登录用户和该用户的关系：是否是一个用户，或者是否已经关注
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetOtherUserRsp](Routes.User.getCurrentUser,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          isFocus = rsp.isFocus
          userName = rsp.userInfo.userName
          loginName = rsp.loginName
          currentUser := rsp.userInfo//必须放在最后，不然前面变量更新无法及时的更新！！
        }else{
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
    }

  }

  def getRecordListByUser: Unit ={
    val data = GoToCommentReq(userId).asJson.noSpaces
    Http.postJsonAndParse[GetListRsp](Routes.List.getRecordListByUser,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          userRecordList := rsp.list.get
        }else{
          JsFunc.showMessage(rsp.msg)
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
          JsFunc.showMessage(rsp.msg)
          //刷新数据
          getCurrentUser
          getConcernList
          getFansList
        }else{
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }

  }

  def app:Node = {
    getCurrentUser
    getRecordListByUser
    getConcernList
    getFansList
    <div id="page-questions" class="mdui-container main">
      <div id="page-user" class="mdui-container">
        {currentUserRx}
        {userRecordListRx}
        {concernUserListRx}
        {fanUserListRx}
      </div>
    </div>
  }
}
