package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.{addRecord, getCommentButton, getDeleteButton, getLikeButton, getMyList, logout, taskListRx}
import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, container, input, logoutButton, td, th}
import com.neo.sk.todos2018.front.utils.TimeTool
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{CommentInfo, TaskRecord}
import mhtml.Var

object Comment {

  val url = "#/" + "Comment"

  var commentid = 1

  val commentList = Var(List.empty[CommentInfo])

  val currentComment = Var(Some(CommentInfo))

  def getCurrentComment: Unit ={
    //从session中读取中commentid，并查询内容


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
    getCurrentComment
    getChildCommentList
    <div class={container.htmlClass}>
      <div>
        <div style="margin:30px;font-size:25px;">当前微博</div>


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
