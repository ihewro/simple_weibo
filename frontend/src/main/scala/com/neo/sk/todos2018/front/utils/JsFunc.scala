package com.neo.sk.todos2018.front.utils

import scala.scalajs.js
import scala.scalajs.js.annotation._

/**
  * User: Taoz
  * Date: 11/30/2016
  * Time: 10:24 PM
  */
@js.native
@JSGlobalScope
object JsFunc extends js.Object{

  def decodeURI(str: String): String = js.native

  def decodeURIComponent(str: String): String = js.native

  def unecape(str: String): String = js.native

  def alert(msg: String): Unit = js.native

  def prompt(str: String, placehold: String) : String = js.native

  def showMessage(str: String): Unit = js.native

  def closeDialog(select :String) :Unit = js.native


}
