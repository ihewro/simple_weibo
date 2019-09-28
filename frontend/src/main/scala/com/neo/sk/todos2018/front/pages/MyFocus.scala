package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList._
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{GetListRsp, TaskRecord}
import io.circe.generic.auto._
import mhtml.Var
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Node

/**
 * 当前登录用户的关注用户内容
 */
object MyFocus {

  val focusTaskList = Var(List.empty[TaskRecord])

  def getFocusRecordList: Unit ={
    Http.getAndParse[GetListRsp](Routes.List.getFocusRecordList).map{
      case Right(rsp) =>
        if (rsp.errCode == 0 ){
          focusTaskList := rsp.list.get
        }else{
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }
  }

  val focusTaskListRx = returnRecordRX(focusTaskList)

  def app:Node = {
    getFocusRecordList

    <div id="page-questions" class="mdui-container main">
      {focusTaskListRx}
    </div>
  }
}
