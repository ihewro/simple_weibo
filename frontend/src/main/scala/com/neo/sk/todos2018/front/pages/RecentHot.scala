package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.pages.TaskList.returnRecordRX
import com.neo.sk.todos2018.front.utils.{Http, JsFunc}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{GetListRsp, TaskRecord}
import io.circe.generic.auto._
import mhtml.{Rx, Var}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{Elem, Node}

/**
 * 近期热门微博
 */
object RecentHot {

  val recentTaskList = Var(List.empty[TaskRecord])
  val recentTaskListRX = returnRecordRX(recentTaskList)

  def getRecentHot(): Unit ={
    Http.getAndParse[GetListRsp](Routes.List.getRecentHotList).map{
      case Right(rsp: GetListRsp) =>
        if (rsp.errCode == 0){
          recentTaskList := rsp.list.get
        }else{
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get recentHot  error,$error")
    }
  }

  val recentTaskListRx: Rx[Elem] = returnRecordRX(recentTaskList)

  def app:Node = {
    getRecentHot()
    <div id="page-questions" class="mdui-container main">
      {recentTaskListRX}
    </div>
  }
}
