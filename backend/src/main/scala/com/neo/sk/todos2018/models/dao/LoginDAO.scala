package com.neo.sk.todos2018.models.dao

import com.neo.sk.todos2018.models.SlickTables.{rUserInfo, tRecordInfo, tUserInfo, _}
import com.neo.sk.todos2018.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object LoginDAO {

  private val log = LoggerFactory.getLogger(this.getClass)

  def addUser(name :String, password:String): Future[Int] = {
    try {
      if (name.length == 0 ) {
        log.error(s"empty author")
        Future.successful(-1)
      } else if (password.length == 0) {
        log.error(s"empty content")
        Future.successful(-1)
      } else {
        val user = rUserInfo(-1,name,password,System.currentTimeMillis())
        db.run(tUserInfo += user)
      }
    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }


  def isUser(name: String,password:String): Future[Seq[rUserInfo]] = {
    try {
      db.run(tUserInfo.filter(t => t.name === name).filter(t => t.password === password).result)
    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }

  }


  def getRecordList(author: String): Future[Seq[rRecordInfo]] = {
    try {
      db.run(tRecordInfo.filter(t => t.author === author).result)
    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }




}
