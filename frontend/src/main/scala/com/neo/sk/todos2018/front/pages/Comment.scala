package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.{addRecord, getList, logout, taskListRx}
import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, input, logoutButton}

object Comment {

  val url = "#/" + "Comment"




  def app: xml.Node = {
    <div>
      <div>
        <label>当前评论</label>
      </div>
      <div style="margin:30px;font-size:25px;">评论列表</div>

    </div>
  }

}
