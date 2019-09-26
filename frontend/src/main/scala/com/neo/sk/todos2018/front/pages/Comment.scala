package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.{getCommentButton, getDeleteButton, getLikeButton, getMyList, taskList}
import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, container, input, td, th}
import com.neo.sk.todos2018.front.utils.{Http, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{CommentInfo, GetListRsp, GoToCommentReq, TaskRecord}
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml.Var
import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.styles.ListStyles._
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, GoToCommentReq, TaskRecord}
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml._
import org.scalajs.dom
import org.scalajs.dom.html.Input

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
    case Nil => <div style ="margin: 30px; font-size: 17px;">出错了！</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>内容</th>
          <th class={th.htmlClass}>创建时间</th>
          <th class={th.htmlClass}>点赞数</th>
          <th class={th.htmlClass}>操作</th>
        </tr>
        {list.map {l =>
        <tr>
          <td class={td.htmlClass}>{l.content}</td>
          <td class={td.htmlClass}>{TimeTool.dateFormatDefault(l.time)}</td>
          <td class={td.htmlClass}>0</td>
          <td class={td.htmlClass}>{getDeleteButton(l.id)}</td>
          <td class={td.htmlClass}>{getCommentButton(l.id)}</td>
          <td class={td.htmlClass}>{getLikeButton(l.id)}</td>
        </tr>
      }
        }

      </table>

    </div>
  }

  val commentRx = commentList.map{
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无评论</div>
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
    <div class={container.htmlClass}>
      <div>
        <div style="margin:30px;font-size:25px;">当前微博</div>
        {taskListRx}
      </div>

      <div>
        <div style="margin:30px;font-size:25px;">添加评论</div>
      <input id ="commentInput" class={input.htmlClass}/>
        <button class={addButton.htmlClass} onclick={()=>addComment}>+提交</button>
      </div>

      <div style="margin:30px;font-size:25px;">评论列表</div>
      {commentRx}

    </div>
  }



}
