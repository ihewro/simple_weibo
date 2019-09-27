package com.neo.sk.todos2018.front.pages

import scala.xml.Node

/**
 * 修改当前登录用户的个人信息
 */
object EditProfile {


  def app:Node = {
    <div id="page-question" class="mdui-container">

      <div class="mdui-card mdui-center question mdui-p-t-3 mdui-p-b-3">
        <div class="mdui-textfield">
          <i class="mdui-icon material-icons">lock</i>
          <label class="mdui-textfield-label">密码</label>
          <input class="mdui-textfield-input" type="text"/>
          <div class="mdui-textfield-helper">请仔细记住您的密码</div>
        </div>

        <div class="mdui-textfield">
          <label class="mdui-textfield-label">选择头像</label>

          <form>
            <label class="mdui-radio">
              <input type="radio" name="group1"/>
              <i class="mdui-radio-icon"></i>
              默认不选中
            </label>

            <label class="mdui-radio">
              <input type="radio" name="group1"/>
              <i class="mdui-radio-icon"></i>
              默认选中
            </label>
          </form>
        </div>
        <div class="actions mdui-m-t-2"><button class="mdui-btn mdui-btn-raised mdui-text-color-blue">提交修改</button></div>
      </div>
    </div>
  }
}
