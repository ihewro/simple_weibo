package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.pages.TaskList.taskMyListRx
import com.neo.sk.todos2018.front.styles.ListStyles.{addButton, container}
import mhtml.Var
import sourcecode.Impls.Chunk.Val

import scala.xml.Node

/**
 * 用户主页
 */
case class User(name: String) {


  val fans_num = Var(Int)//粉丝数目
  val focus_num = Var(Int)//关注者数目


  def concenteList: Unit ={

  }

  def fansList: Unit ={

  }


  def getCurrentUser: Unit ={
    //不仅要获取该用户的信息，还需要获取当前登录用户和该用户的关系：是否是一个用户，或者是否已经关注

  }

  def getRecordListByUser: Unit ={

  }

  def addFocus: Unit ={

  }

  def cancelFocus: Unit ={

  }
  def app:Node = {
    <div id="page-questions" class="mdui-container main">
      <div id="page-user" class="mdui-container">
        <div class="mdui-card cover" style="background-image: url(&quot;https://www.mdui.org/static/common/image/cover/default_l.webp&quot;);">
          <div class="mc-cover-upload">
            <button class="mdui-btn mdui-btn-icon mdui-ripple" type="button"><i class="mdui-icon material-icons">photo_camera</i></button>
            <input type="file" title=" " accept="image/jpeg,image/png" />
          </div>
          <div class="gradient mdui-card-media-covered mdui-card-media-covered-gradient"></div>
          <div class="info">
            <div class="avatar-box">
              <img src="https://www.mdui.org/upload/avatar/ff/12/a2bc5079432aac214e646108dd12d8e4_l.png" class="avatar" />
            </div>
            <div class="username">
              ihewro
            </div>
            <div class="meta">
              <a class="following">关注了 0 人</a>
              <span class="mdui-m-x-1">|</span>
              <a class="followers">0 位关注者</a>
            </div>
            <div class="mdui-btn mdui-btn-raised right-btn">关注</div>
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
        {taskMyListRx}
      </div>
    </div>
  }
}
