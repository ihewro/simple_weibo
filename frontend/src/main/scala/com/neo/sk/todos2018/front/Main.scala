package com.neo.sk.todos2018.front

import com.neo.sk.todos2018.front.pages.TaskList.taskList
import com.neo.sk.todos2018.front.pages._
import com.neo.sk.todos2018.front.styles.ListStyles
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, PageSwitcher}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import io.circe.generic.auto._
import mhtml.{mount, _}
import org.querki.jquery.{JQueryEventObject, _}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by haoshuhan on 2018/6/4.
  * changed by Xu Si-ran on 2019/3/21.
  */
object Main extends PageSwitcher {
  val currentPage = currentHashVar.map { ls =>
    println(s"currentPage change to ${ls.mkString(",")}")
    println(ls)
    ls match {
      case "List" :: Nil => TaskList.app
      case "Login" :: Nil => Login.app
      case "Detail"::id :: Nil => Comment(id.toInt).app
      case "User"::name:: Nil => User(name.toInt).app
      case "MyFocus"::Nil => MyFocus.app
      case "RecentHot"::Nil => RecentHot.app
      case "EditProfile"::Nil => EditProfile.app
      case _ => Login.app
    }

  }

  def show(): Cancelable = {
    switchPageByHash()
    javascript()
    val page =
      <div>
        {currentPage}
      </div>
    mount(dom.document.getElementById("project-container"), page)
  }

  def logout(): Unit = {
    Http.getAndParse[SuccessRsp](Routes.Login.userLogout).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          JsFunc.showMessage("退出成功，感谢您在本站花费的时间")
          taskList := Nil
          dom.window.location.hash = "/Login"
        }
        else{
          JsFunc.showMessage(s"退出失败：${rsp.msg}")
          dom.window.location.hash = "/Login"
        }
      case Left(error) =>
        JsFunc.showMessage(s"parse error,$error")
    }
  }


  def javascript(): Unit ={
    $("#exitApp").on("click", (eventObject: JQueryEventObject, data: Any) => {
      //Match data on what is required
      //for this example it will be a string
      data match {
        case msg: String => println(msg)
        case _ => logout()
      }
    });
  }


  def main(args: Array[String]): Unit ={
    import scalacss.ProdDefaults._
    ListStyles.addToDocument()
    show()
  }
}
