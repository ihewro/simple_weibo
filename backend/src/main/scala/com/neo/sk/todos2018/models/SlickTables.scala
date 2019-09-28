package com.neo.sk.todos2018.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.H2Profile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(tAvatar.schema, tComment.schema, tRecordInfo.schema, tRecordLikeRelationship.schema, tUserInfo.schema, tUserRelationship.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tAvatar
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param url Database column URL SqlType(VARCHAR), Length(300,true) */
  case class rAvatar(id: Int, url: String)
  /** GetResult implicit for fetching rAvatar objects using plain SQL queries */
  implicit def GetResultrAvatar(implicit e0: GR[Int], e1: GR[String]): GR[rAvatar] = GR{
    prs => import prs._
      rAvatar.tupled((<<[Int], <<[String]))
  }
  /** Table description of table AVATAR. Objects of this class serve as prototypes for rows in queries. */
  class tAvatar(_tableTag: Tag) extends profile.api.Table[rAvatar](_tableTag, "AVATAR") {
    def * = (id, url) <> (rAvatar.tupled, rAvatar.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(url))).shaped.<>({r=>import r._; _1.map(_=> rAvatar.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column URL SqlType(VARCHAR), Length(300,true) */
    val url: Rep[String] = column[String]("URL", O.Length(300,varying=true))
  }
  /** Collection-like TableQuery object for table tAvatar */
  lazy val tAvatar = new TableQuery(tag => new tAvatar(tag))

  /** Entity class storing rows of table tComment
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param content Database column CONTENT SqlType(VARCHAR), Length(20000,true)
   *  @param userid Database column USERID SqlType(INTEGER)
   *  @param recordid Database column RECORDID SqlType(INTEGER)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rComment(id: Int, content: Option[String], userid: Option[Int], recordid: Option[Int], time: Option[Long])
  /** GetResult implicit for fetching rComment objects using plain SQL queries */
  implicit def GetResultrComment(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Option[Long]]): GR[rComment] = GR{
    prs => import prs._
      rComment.tupled((<<[Int], <<?[String], <<?[Int], <<?[Int], <<?[Long]))
  }
  /** Table description of table COMMENT. Objects of this class serve as prototypes for rows in queries. */
  class tComment(_tableTag: Tag) extends profile.api.Table[rComment](_tableTag, "COMMENT") {
    def * = (id, content, userid, recordid, time) <> (rComment.tupled, rComment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), content, userid, recordid, time)).shaped.<>({r=>import r._; _1.map(_=> rComment.tupled((_1.get, _2, _3, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column CONTENT SqlType(VARCHAR), Length(20000,true) */
    val content: Rep[Option[String]] = column[Option[String]]("CONTENT", O.Length(20000,varying=true))
    /** Database column USERID SqlType(INTEGER) */
    val userid: Rep[Option[Int]] = column[Option[Int]]("USERID")
    /** Database column RECORDID SqlType(INTEGER) */
    val recordid: Rep[Option[Int]] = column[Option[Int]]("RECORDID")
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Option[Long]] = column[Option[Long]]("TIME")

    /** Foreign key referencing tRecordInfo (database name COMMENT_RECORD_INFO_ID_FK) */
    lazy val tRecordInfoFk = foreignKey("COMMENT_RECORD_INFO_ID_FK", recordid, tRecordInfo)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing tUserInfo (database name COMMENT_USER_INFO__ID) */
    lazy val tUserInfoFk = foreignKey("COMMENT_USER_INFO__ID", userid, tUserInfo)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table tComment */
  lazy val tComment = new TableQuery(tag => new tComment(tag))

  /** Entity class storing rows of table tRecordInfo
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param author Database column AUTHOR SqlType(VARCHAR), Length(63,true)
   *  @param content Database column CONTENT SqlType(VARCHAR), Length(1023,true)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rRecordInfo(id: Int, author: String, content: String, time: Long)
  /** GetResult implicit for fetching rRecordInfo objects using plain SQL queries */
  implicit def GetResultrRecordInfo(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rRecordInfo] = GR{
    prs => import prs._
      rRecordInfo.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table RECORD_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tRecordInfo(_tableTag: Tag) extends profile.api.Table[rRecordInfo](_tableTag, "RECORD_INFO") {
    def * = (id, author, content, time) <> (rRecordInfo.tupled, rRecordInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(content), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rRecordInfo.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column AUTHOR SqlType(VARCHAR), Length(63,true) */
    val author: Rep[String] = column[String]("AUTHOR", O.Length(63,varying=true))
    /** Database column CONTENT SqlType(VARCHAR), Length(1023,true) */
    val content: Rep[String] = column[String]("CONTENT", O.Length(1023,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")
  }
  /** Collection-like TableQuery object for table tRecordInfo */
  lazy val tRecordInfo = new TableQuery(tag => new tRecordInfo(tag))

  /** Entity class storing rows of table tRecordLikeRelationship
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param recordId Database column RECORD_ID SqlType(INTEGER)
   *  @param userId Database column USER_ID SqlType(INTEGER) */
  case class rRecordLikeRelationship(id: Int, recordId: Int, userId: Int)
  /** GetResult implicit for fetching rRecordLikeRelationship objects using plain SQL queries */
  implicit def GetResultrRecordLikeRelationship(implicit e0: GR[Int]): GR[rRecordLikeRelationship] = GR{
    prs => import prs._
      rRecordLikeRelationship.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table RECORD_LIKE_RELATIONSHIP. Objects of this class serve as prototypes for rows in queries. */
  class tRecordLikeRelationship(_tableTag: Tag) extends profile.api.Table[rRecordLikeRelationship](_tableTag, "RECORD_LIKE_RELATIONSHIP") {
    def * = (id, recordId, userId) <> (rRecordLikeRelationship.tupled, rRecordLikeRelationship.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(recordId), Rep.Some(userId))).shaped.<>({r=>import r._; _1.map(_=> rRecordLikeRelationship.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column RECORD_ID SqlType(INTEGER) */
    val recordId: Rep[Int] = column[Int]("RECORD_ID")
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")

    /** Foreign key referencing tRecordInfo (database name RECORD_LIKE_RELATIONSHIP_RECORD_INFO_ID_FK) */
    lazy val tRecordInfoFk = foreignKey("RECORD_LIKE_RELATIONSHIP_RECORD_INFO_ID_FK", recordId, tRecordInfo)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing tUserInfo (database name RECORD_LIKE_RELATIONSHIP_USER_INFO_ID_FK) */
    lazy val tUserInfoFk = foreignKey("RECORD_LIKE_RELATIONSHIP_USER_INFO_ID_FK", userId, tUserInfo)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table tRecordLikeRelationship */
  lazy val tRecordLikeRelationship = new TableQuery(tag => new tRecordLikeRelationship(tag))

  /** Entity class storing rows of table tUserInfo
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param name Database column NAME SqlType(VARCHAR), Length(63,true)
   *  @param password Database column PASSWORD SqlType(VARCHAR), Length(1023,true)
   *  @param time Database column TIME SqlType(BIGINT)
   *  @param avatarId Database column AVATAR_ID SqlType(INTEGER), Default(1) */
  case class rUserInfo(id: Int, name: String, password: String, time: Long, avatarId: Int = 1)
  /** GetResult implicit for fetching rUserInfo objects using plain SQL queries */
  implicit def GetResultrUserInfo(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rUserInfo] = GR{
    prs => import prs._
      rUserInfo.tupled((<<[Int], <<[String], <<[String], <<[Long], <<[Int]))
  }
  /** Table description of table USER_INFO. Objects of this class serve as prototypes for rows in queries. */
  class tUserInfo(_tableTag: Tag) extends profile.api.Table[rUserInfo](_tableTag, "USER_INFO") {
    def * = (id, name, password, time, avatarId) <> (rUserInfo.tupled, rUserInfo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(password), Rep.Some(time), Rep.Some(avatarId))).shaped.<>({r=>import r._; _1.map(_=> rUserInfo.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column NAME SqlType(VARCHAR), Length(63,true) */
    val name: Rep[String] = column[String]("NAME", O.Length(63,varying=true))
    /** Database column PASSWORD SqlType(VARCHAR), Length(1023,true) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(1023,varying=true))
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")
    /** Database column AVATAR_ID SqlType(INTEGER), Default(1) */
    val avatarId: Rep[Int] = column[Int]("AVATAR_ID", O.Default(1))

    /** Uniqueness Index over (name) (database name USER_INFO_NAME_UINDEX) */
    val index1 = index("USER_INFO_NAME_UINDEX", name, unique=true)
  }
  /** Collection-like TableQuery object for table tUserInfo */
  lazy val tUserInfo = new TableQuery(tag => new tUserInfo(tag))

  /** Entity class storing rows of table tUserRelationship
   *  @param id Database column ID SqlType(INTEGER), AutoInc
   *  @param dageId Database column DAGE_ID SqlType(INTEGER)
   *  @param xiaodiId Database column XIAODI_ID SqlType(INTEGER)
   *  @param time Database column TIME SqlType(BIGINT) */
  case class rUserRelationship(id: Int, dageId: Int, xiaodiId: Int, time: Long)
  /** GetResult implicit for fetching rUserRelationship objects using plain SQL queries */
  implicit def GetResultrUserRelationship(implicit e0: GR[Int], e1: GR[Long]): GR[rUserRelationship] = GR{
    prs => import prs._
      rUserRelationship.tupled((<<[Int], <<[Int], <<[Int], <<[Long]))
  }
  /** Table description of table USER_RELATIONSHIP. Objects of this class serve as prototypes for rows in queries. */
  class tUserRelationship(_tableTag: Tag) extends profile.api.Table[rUserRelationship](_tableTag, "USER_RELATIONSHIP") {
    def * = (id, dageId, xiaodiId, time) <> (rUserRelationship.tupled, rUserRelationship.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(dageId), Rep.Some(xiaodiId), Rep.Some(time))).shaped.<>({r=>import r._; _1.map(_=> rUserRelationship.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc)
    /** Database column DAGE_ID SqlType(INTEGER) */
    val dageId: Rep[Int] = column[Int]("DAGE_ID")
    /** Database column XIAODI_ID SqlType(INTEGER) */
    val xiaodiId: Rep[Int] = column[Int]("XIAODI_ID")
    /** Database column TIME SqlType(BIGINT) */
    val time: Rep[Long] = column[Long]("TIME")
  }
  /** Collection-like TableQuery object for table tUserRelationship */
  lazy val tUserRelationship = new TableQuery(tag => new tUserRelationship(tag))
}
