package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.{addRecord, getMyList, logout, taskListRx}
import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, input, logoutButton}

object Comment {

  val url = "#/" + "Comment"

  var commentid = 1

  def getCurrentComment: Unit ={
    //从session中读取中commentid，并查询内容

  }

  def getChildCommentList: Unit ={
    //
  }

  def app: xml.Node = {
    <div>
      <div>
        <label>当前评论</label>

      </div>

      <div>
        <label>添加评论</label>
        <input id ="commentInput" class={input.htmlClass}/>
        <button class={addButton.htmlClass} onclick={()=>addRecord}>+提交</button>
      </div>

      <div style="margin:30px;font-size:25px;">评论列表</div>

    </div>
  }

}
