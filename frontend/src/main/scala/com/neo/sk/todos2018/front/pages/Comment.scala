package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.{getDeleteButton, getLikeButton}
import com.neo.sk.todos2018.front.styles.ListStyles.{td, th}
import com.neo.sk.todos2018.front.utils.{Http, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{CommentInfo, GetListRsp, GoToCommentReq, TaskRecord}
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml.Var
import org.querki.jquery._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

case class Comment(commentid:Int) {

  val url = "#/" + "Comment"


  val commentList = Var(List.empty[CommentInfo])

  val currentTask = Var(List.empty[TaskRecord])

  def getCurrentRecord: Unit ={
    //从session中读取中commentid，并查询内容
    println("评论id"+commentid)
    val data = GoToCommentReq(commentid).asJson.noSpaces
    Http.postJsonAndParse[GetListRsp](Routes.List.getRecordById,data).map{
      case Right(rep) =>
        currentTask := rep.list.get
      case Left(error) =>
        println(s"get  error,$error")
    }


  }

  def getChildCommentList: Unit ={
    //


  }

  def addLike: Unit ={

  }

  def deleteComment: Unit ={

  }

  def addComment: Unit ={

  }


  val taskListRx = currentTask.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">加载中……</div>
    case list =>
      <div>
        {list.map {l =>
          <div class="mdui-card mdui-center question mdui-p-t-2">
            <div>
              <div class="mc-user-line">
                <a class="avatar" href="/users/10413" style="background-image: url(&quot;https://www.mdui.org/upload/avatar/ff/12/0874da5deda2ae122d9c46d5a8e4bdb0_m.png&quot;);"></a>
                <div class="info">
                  <div class="username">
                    <a href={"/todos2018#/User/" + l.id}>ihewro</a></div>
                  <div class="headline"></div>
                </div>
                <div class="more">
                  <span class="time" title="2019-09-26 17:11:16">今天 17:11</span></div>
              </div>
              <div class="mdui-typo content">
                {l.content}
              </div>
              <div class="actions">
                <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent">点赞</button>
                <button class="mdui-btn mdui-btn-raised mdui-color-theme-accent">评论</button>
              </div>
            </div>
            <div class="mc-loading mdui-spinner mdui-center mdui-m-y-3 mdui-hidden">
              <div class="mdui-spinner-layer ">
                <div class="mdui-spinner-circle-clipper mdui-spinner-left">
                  <div class="mdui-spinner-circle"></div>
                </div>
                <div class="mdui-spinner-gap-patch">
                  <div class="mdui-spinner-circle"></div>
                </div>
                <div class="mdui-spinner-circle-clipper mdui-spinner-right">
                  <div class="mdui-spinner-circle"></div>
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
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>发布者</th>
          <th class={th.htmlClass}>内容</th>
          <th class={th.htmlClass}>创建时间</th>
          <th class={th.htmlClass}>点赞数</th>
          <th class={th.htmlClass}>操作</th>
        </tr>
        {list.map { l =>
        <tr>
          <td class={td.htmlClass}>
            匿名
          </td>
          <td class={td.htmlClass}>
            {l.content}
          </td>
          <td class={td.htmlClass}>
            {TimeTool.dateFormatDefault(l.time)}
          </td>
          <td class={td.htmlClass}>0</td>
          <td class={td.htmlClass}>
            {getDeleteButton(l.id)}
          </td>
          <td class={td.htmlClass}>
            {getLikeButton(l.id)}
          </td>
        </tr>
      }
        }
        </table>
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
