package com.neo.sk.todos2018.front

import cats.Show
import com.neo.sk.todos2018.front.pages.TaskList.taskList
import com.neo.sk.todos2018.front.pages.{Comment, EditProfile, Login, MyFocus, RecentHot, TaskList, User}
import mhtml.mount
import org.scalajs.dom
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, PageSwitcher}
import mhtml._
import org.scalajs.dom
import io.circe.syntax._
import io.circe.generic.auto._
import com.neo.sk.todos2018.front.styles.ListStyles
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import org.querki.jquery.JQueryEventObject
import org.querki.jquery._
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
      case "User"::name:: Nil => User(name).app
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
          JsFunc.alert("退出成功，感谢您在本站点花费的时间")
          taskList := Nil
          dom.window.location.hash = "/Login"
        }
        else{
          JsFunc.alert(s"退出失败：${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"parse error,$error")
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
