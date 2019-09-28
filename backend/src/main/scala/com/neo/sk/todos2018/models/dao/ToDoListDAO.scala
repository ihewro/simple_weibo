package com.neo.sk.todos2018.models.dao

import com.neo.sk.todos2018.models.Bean.BRecord
import com.neo.sk.todos2018.models.SlickTables._
import com.neo.sk.todos2018.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.todos2018.Boot.executor


import scala.concurrent.Future

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:17
  * changed by Xu Si-ran, delete user
  * update by Tao 2019-3-23, add Record class and rename list to recordList.
  * 以下用slick操作数据库，若出现找不到表的情况，需要修改配置文件数据库的路径为绝对路径（没有..）
  */
//case class Record(id: Int, author: String, content: String, time: Long)
//
//trait FetchInfoDAOTable{
//  import com.neo.sk.todos2018.utils.DBUtil.driver.api._
//
//  class RecordInfoTable(tag: Tag) extends Table[Record](tag, "RECORD_INFO") {
//    def * = (id, author, content, time) <> (Record.tupled, Record.unapply)
//
//    val id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
//    val author = column[String]("AUTHOR")
//    val content = column[String]("CONTENT")
//    val time = column[Long]("TIME")
//
//  }
//
//  protected val recordInfoTableQuery = TableQuery[RecordInfoTable]
//}


object ToDoListDAO{
  private val log = LoggerFactory.getLogger(this.getClass)

  def addRecord(author: Int, content: String): Future[Int] = {
    try {
      if (author == 0 ) {
        log.error(s"empty author")
        Future.successful(-1)
      } else if (content.length == 0) {
        log.error(s"empty content")
        Future.successful(-1)
      } else {
        val test = tRecordInfo.map(t => (t.userid, t.content, t.time))
        db.run(test += (author, content, System.currentTimeMillis()))
      }
    } catch {
      case e: Throwable =>
        log.error(s"add record error with error $e")
        Future.successful(-1)
    }
  }

  def delRecord(id: Int): Future[Int] = {
    try {
      // 操作数据库删除
      db.run(tRecordInfo.filter(_.id === id).delete)
//      Future.successful(1)
    } catch {
      case e: Throwable =>
        log.error(s"del record error with error $e")
        Future.successful(-1)
    }
  }

  def getRecordById(id: Int):Future[Seq[rRecordInfo]] = {
    try{
      db.run{
        tRecordInfo.filter(t=> t.id === id).result
      }

    }catch {
      case e: Throwable =>
        log.error("get record error")
        Future.successful(Nil)
    }
  }

  def getFocusRecordByUser(id:Int):Future[Seq[rRecordInfo]] = {

    for{
     j <-  db.run{tUserRelationship.filter(t=> t.dageId === id).result}
     i <- db.run{tRecordInfo.filter(t=> j.contains(t.userid.asColumnOf[Int])).result}
    }yield i
  }



  def getRecordListByUser(author: Int): Future[Seq[rRecordInfo]] = {
    try {
      db.run(tRecordInfo.filter(t => t.userid === author).result)
    } catch {
      case e: Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }

  def getCommentListByRecordId(recordid: Int):Future[Seq[rComment]] = {
    try{
      db.run(tComment.filter(t => t.recordid === recordid).result)
    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }

  def addComment(recordId:Int, userId:Int, content:String):Future[Int] = {
    try{
      val test = tComment.map(t => (t.content,t.userid,t.recordid,t.time))
      val ok = (Some(content),Some(userId),Some(recordId),Some(System.currentTimeMillis()))
      db.run(test += ok)
    }catch {
      case e:Throwable =>
        Future.successful(-1)
    }


  }

}
