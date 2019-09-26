package com.neo.sk.todos2018.front

import cats.Show
import com.neo.sk.todos2018.front.pages.{Comment, Login, TaskList, User}
import mhtml.mount
import org.scalajs.dom
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, PageSwitcher}
import mhtml._
import org.scalajs.dom
import io.circe.syntax._
import io.circe.generic.auto._
import com.neo.sk.todos2018.front.styles.ListStyles

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
      case _ => Login.app
    }

  }

  def show(): Cancelable = {
    switchPageByHash()
    val page =
      <div>
        {currentPage}
      </div>
    mount(dom.document.getElementById("page-questions"), page)
  }


  def main(args: Array[String]): Unit ={
    import scalacss.ProdDefaults._
    ListStyles.addToDocument()
    show()
  }
}
