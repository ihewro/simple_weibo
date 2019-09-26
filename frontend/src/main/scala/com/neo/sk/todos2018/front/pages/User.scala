package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, container}

import scala.xml.Node

/**
 * 用户主页
 */
case class User(name: String) {


  def concenteList: Unit ={

  }

  def fansList: Unit ={

  }


  def app:Node = {
    <div class={container.htmlClass}>
      <div style="margin:30px;">
        <div style="font-size:25px;">xxx 的 微博个人主页</div>
      </div>

      <div style="margin-bottom:30px;margin-top:30px">
        <button class={addButton.htmlClass} onclick={()=>concenteList}>关注列表</button>
        <button class={addButton.htmlClass} onclick={()=>fansList}>粉丝列表</button>
      </div>

    </div>
  }
}
