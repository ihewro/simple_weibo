package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.taskMyListRx

import scala.xml.Node

/**
 * 当前登录用户的关注用户内容
 */
object MyFocus {

  def app:Node = {
    <div id="page-questions" class="mdui-container main">
      {taskMyListRx}
    </div>
  }
}
