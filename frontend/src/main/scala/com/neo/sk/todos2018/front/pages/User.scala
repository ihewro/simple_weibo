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


  def app:Node = {
    <div id="page-user" class="mdui-container">
      <div class="mdui-card cover" style="background-image: url(&quot;https://www.mdui.org/static/common/image/cover/default_l.webp&quot;);">
        <div class="mc-cover-upload">
          <button class="mdui-btn mdui-btn-icon mdui-ripple upload-btn" type="button" title="点击上传封面"><i class="mdui-icon material-icons">photo_camera</i></button>
          <input type="file" title=" " accept="image/jpeg,image/png" />
        </div>
        <div class="gradient mdui-card-media-covered mdui-card-media-covered-gradient"></div>
        <div class="info">
          <div class="avatar-box">
            <div class="mc-avatar-upload">
              <button class="mdui-btn mdui-btn-icon mdui-ripple upload-btn" type="button" title="点击上传头像"><i class="mdui-icon material-icons">photo_camera</i></button>
              <input type="file" title=" " accept="image/jpeg,image/png" />
            </div>
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
          <div class="menu">
            <button class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white mdui-text-color-white" mdui-menu="{target: '#user-menu-menu', position: 'top', align: 'right'}"><i class="mdui-icon material-icons">more_vert</i></button>
            <ul class="mdui-menu" id="user-menu-menu" style="transform-origin: 100% 100%; position: fixed;">
              <li class="mdui-menu-item"><a class="mdui-ripple">删除头像</a></li>
              <li class="mdui-menu-item"><a class="mdui-ripple">删除封面</a></li>
            </ul>
          </div>
          <button class="right-btn mdui-btn mdui-btn-raised">修改个人简介</button>
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

  }
}
