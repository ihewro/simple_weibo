package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.taskMyListRx

import scala.xml.Node

/**
 * 近期热门微博
 */
object RecentHot {
  def app:Node = {
    <div>
      {taskMyListRx}
    </div>
  }
}
