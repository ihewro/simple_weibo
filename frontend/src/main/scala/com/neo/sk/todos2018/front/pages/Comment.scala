package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol._
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml.Var
import org.scalajs.dom
import org.scalajs.dom.html.Input

import scala.concurrent.ExecutionContext.Implicits.global

case class Comment(recordId:Int) {

  val url = "#/" + "Comment"

  val loginName = Var("")
  val userName = Var("")
  val commentList = Var(List.empty[CommentInfo])

  val currentTask = Var(List.empty[TaskRecord])
  val likedUserList = Var(List.empty[UserInfo])
  val isLiked = Var(false)

  def getCurrentRecord: Unit ={
    println("评论id"+recordId)
    val data = GoToCommentReq(recordId).asJson.noSpaces
    Http.postJsonAndParse[GetListRsp](Routes.List.getRecordById,data).map{
      case Right(rep) =>
        currentTask := rep.list.get
        userName := rep.list.get.head.userInfo.userName
      case Left(error) =>
        println(s"get  error,$error")
    }
  }

  def getLikedUserListByRecordId: Unit ={
    val data = GoToCommentReq(recordId).asJson.noSpaces
    //获取某篇微博的所有点赞用户，同时也要返回当前登录用户与该微博发布者的关系，是同一个人还是已经点赞过了
    Http.postJsonAndParse[GetLikedUserListRsp](Routes.User.getLikedUserListByRecordId,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          likedUserList := rsp.list.get
          isLiked := rsp.isLike
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")

    }
  }

  def getChildCommentList: Unit ={
    //获取该微博下面的所有评论
    val data = GoToCommentReq(recordId).asJson.noSpaces
    Http.postJsonAndParse[GetCommentListRsq](Routes.List.getCommentListByRecordId,data).map{
      case Right(rsp) =>
        commentList := rsp.list.get
      case Left(error) =>
        println(s"get  error,$error")
    }
  }

  def addWeiboLike: Unit ={
    //给微博添加点赞
    val data = GoToCommentReq(recordId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.User.addLike,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          //点赞成功
          JsFunc.showMessage("点赞成功")
          getLikedUserListByRecordId
        }
    }
  }

  def cancelWebLike: Unit = {
    val data = GoToCommentReq(recordId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.User.cancelLike,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          //点赞成功
          JsFunc.showMessage("取消点赞成功")
          getLikedUserListByRecordId
        }
    }
  }

  def deleteRecord(): Unit = {
    //删除该微博
    val data = GoToCommentReq(recordId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.delRecord, data).map {
      case Right(rsp) =>
        println(rsp)
        JsFunc.alert("删除成功")
        //TODO:跳转到微博列表页面
      case Left(error) =>
        println(s"parse error,$error")
    }

  }


  def addComment: Unit ={
    //向某篇微博发送评论
    val content = dom.document.getElementById("commentContentInput").asInstanceOf[Input].value
    val data = AddCommentReq(content,recordId).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.addComment,data).map{
      case Right(rsp) =>
        if (rsp.errCode == 0){
          JsFunc.alert("添加成功")
          getChildCommentList
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"parse error,$error")
    }
  }

  val likedUserListRx = likedUserList.map{
    case Nil =>
      <div></div>
    case list =>
      <div class="mdui-p-y-3">
        {list.map{
        l =>
          <a class="avatar">
            <img src={l.avatar.url} />
          </a>
        }}
        <span class="mdui-text-color-black-secondary">等人觉得很赞</span>
      </div>

  }

  val optionStatusRx = isLiked.map{
    t=>
    {if(t){
      <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent" onclick={() => cancelWebLike}>取消点赞</button>
    }else{
      <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent" onclick={() => addWeiboLike}>点赞</button>
    }}
  }

  val deleteStatusRx = loginName.map {
    l =>
        {userName.map {
        u =>
            {if (l == u) {
            <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent" onclick={() => deleteRecord()}>删除</button>
          }else{
              <div></div>
            }}
      }}
  }
  val taskListRx = currentTask.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">加载中……</div>
    case list =>
      <div>
        {list.map {l =>
          <div class="mdui-card mdui-center question mdui-p-t-2">
            <div>
              <div class="mc-user-line">
                <a class="avatar" href={"/todos2018#/User/" + l.id} style={"background-image: url("+l.userInfo.avatar.url+");"}></a>
                    <div class="info">
                      <div class="username">
                        <a href={"/todos2018#/User/" + l.id}>{l.userInfo.userName}</a></div>
                <div class="headline"></div>
              </div>
              <div class="more">
                <span class="time" title="2019-09-26 17:11:16">{TimeTool.dateFormatDefault(l.time)}</span></div>
              </div>
              <div class="mdui-typo content">
                {l.content}
              </div>
              <div class="actions">
                {deleteStatusRx}
                {optionStatusRx}
                <button mdui-dialog="{target: '#page-answer-editor'}" class="mdui-btn mdui-btn-raised mdui-color-theme-accent">评论</button>
              </div>

            </div>

            <div class="mdui-dialog mc-editor-dialog" id="page-answer-editor">
              <div class="mc-editor mdui-m-x-5 mdui-m-y-5">
                <textarea id="commentContentInput" class="mdui-p-y-5 mdui-textfield-input mdui-typo" rows="20" placeholder="写评论"></textarea>
                <div class="toolbar">
                  <button class="mdui-btn mdui-btn-icon mdui-ripple close" mdui-dialog-close="true"><i class="mdui-icon material-icons">close</i></button>
                  <div class="mdui-toolbar-spacer"></div>
                  <button type="button" onclick ={()=> addComment} class="submit-text mdui-btn mdui-btn-raised mdui-color-indigo mdui-color-theme">发布</button>
                  <button type="button" onclick ={()=> addComment}  class="submit-icon mdui-btn mdui-btn-icon mdui-text-color-indigo mdui-text-color-theme"><i class="mdui-icon material-icons">send</i></button>
                </div>
              </div>
            </div>
          </div>
        }}
      </div>

  }

  val commentRx = commentList.map{
    case Nil =>
      <div class="mc-empty"><div class="title">没有人撩你</div><div class="description"></div></div>
    case list => <div>
      <div class="mdui-typo-headline answers-count">共 {list.length} 个评论</div>
      <div class="mdui-card mdui-center answers">
        {list.map { l =>
        <div class="item">
          <div class="mc-user-line">
            <a class="avatar" href={"/todos2018#/User/" + l.id}  style={"background-image: url("+l.userInfo.avatar.url+");"}></a>
            <div class="info">
              <div class="username">
                <a href={"/todos2018#/User/" + l.id}>{l.userInfo.userName}</a>
              </div>
              <div class="headline"></div>
            </div>
            <div class="more">
              <span class="time" title="2019-09-27 20:32:24">{TimeTool.dateFormatDefault(l.time)}</span>
            </div>
          </div>
          <div class="content mdui-typo">
            <p>{l.content}</p>
          </div>
        </div>
      }
        }
      </div>
      </div>
  }

  def app: xml.Node = {
    getCurrentRecord
    getChildCommentList
    <div id="page-question" class="mdui-container">
      <div>
        {taskListRx}
      </div>
      {commentRx}

    </div>
  }



}
