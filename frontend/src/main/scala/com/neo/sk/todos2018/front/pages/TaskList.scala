package com.neo.sk.todos2018.front.pages

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.styles.ListStyles._
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, GoToCommentReq, TaskRecord}
import io.circe.generic.auto._
import io.circe.syntax._
import mhtml._
import org.scalajs.dom
import org.scalajs.dom.html.{Input, TextArea}

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by haoshuhan on 2018/6/4.
  * changed by Xu Si-ran on 2019/3/21
  * update by zhangtao, 2019-3-23: record id.
  */
object TaskList{

  val url = "#/" + "List"

  val taskList = Var(List.empty[TaskRecord])

  def getDeleteButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>deleteRecord(id)}>删除</button>
  def getCommentButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>commentRecord(id)}>评论</button>
  def getLikeButton(id: Int) =  <button class={deleteButton.htmlClass} onclick={()=>addLike(id)}>点赞</button>

  def addRecord: Unit = {
    val data = dom.document.getElementById("taskInput").asInstanceOf[TextArea].value
    if (data == ""){
      JsFunc.alert("输入框不能为空！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.List.addRecord, AddRecordReq(data).asJson.noSpaces).map {
        case Right(rsp) =>
          if(rsp.errCode == 0) {
            JsFunc.alert("添加成功！")
            getMyList
          } else {
            JsFunc.alert("添加失败！")
            println(rsp.msg)
          }

        case Left(error) =>
          println(s"parse error,$error")
      }
    }
  }


  def addLike(id: Int): Unit ={

  }

  def commentRecord(id: Int): Unit = {

    //把当前跳转的评论id存到session里面以便评论页面调用
    val data = GoToCommentReq(id).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.goComment,data).map {
      case Right(rsp) =>
        println(rsp)
        if(rsp.errCode == 0){
          //跳转到评论页面
          dom.window.location.hash = s"#/Detail/"+id
        } else {
          JsFunc.alert(rsp.msg)
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")
    }

  }

  def deleteRecord(id: Int): Unit = {

    val data = DelRecordReq(id).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.List.delRecord, data).map {
      case Right(rsp) =>
        println(rsp)
        JsFunc.alert("删除成功")
        getMyList

      case Left(error) =>
        println(s"parse error,$error")

    }

  }

  def getMyList(): Unit = {
    println("getMyList")
    Http.getAndParse[GetListRsp](Routes.List.getMyList).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          taskList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }
  }

  def getAllList: Unit = {
    Http.getAndParse[GetListRsp](Routes.List.getMyList).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          taskList := rsp.list.get
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get task list error,$error")
    }
  }

  val taskMyListRx = taskList.map {
    case Nil => <div id="example3-tab2" style ="margin: 30px; font-size: 17px;">暂无微博</div>
    case list =>

      <div style ="margin: 20px; font-size: 17px;">
        {list.map {l =>
        <a class="item" href={"/todos2018#/Detail/"+l.id}>
          <div class="content">
            <div class="title">
              {l.content}
            </div>
            <div class="meta">
              <div class="username">
                ihewro
              </div>
              <div class="answer_time" title="2019-09-26 17:11:16">
                发布于 {l.time}
              </div>
            </div>
          </div>
        </a>

      }
        }

      </div>
  }

  val taskAllListRx = taskList.map {
    case Nil => <div id="example3-tab2" style ="margin: 30px; font-size: 17px;">暂无微博</div>
    case list =>

      <div style ="margin: 20px; font-size: 17px;">
        {list.map {l =>

        <div class="mdui-row">
          <div class="mdui-col-sm-12">
            <div class="mdui-card">
              <div class="mdui-card-header">
                  <div class="mdui-card-header-title">Title</div>
                  <div class="mdui-card-header-subtitle">Subtitle</div>
                </div>

                <div class="mdui-card-content">{l.content}</div>
                <div class="mdui-card-actions">
                  <button class="mdui-btn mdui-ripple" onclick={()=>deleteRecord(l.id)}>删除</button>
                  <button class="mdui-btn mdui-ripple" onclick={()=>commentRecord(l.id)}>评论 </button>
                  <button class="mdui-btn mdui-ripple" onclick={()=>addLike(l.id)}>>点赞 </button>
                </div>
              </div>
            </div>
          </div>
      }
        }

    </div>
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



  def myList: Unit ={
    getMyList
  }

  def allList: Unit ={
    getMyList
  }

  def app: xml.Node = {
   getMyList
  <div>

    <div>
      <button id="submitButton" onclick={()=>logout} value="记录" class=" mdui-ripple  mdui-btn mdui-btn-raised mdui-center">个人主页</button>
      <button id="submitButton" onclick={()=>logout} value="记录" class=" mdui-ripple  mdui-btn mdui-btn-raised mdui-center">退出</button>
    </div>

    <div>
      <textarea id ="taskInput" class="mdui-textfield-input" rows="4" placeholder="Message"></textarea>
    </div>
    <button  onclick={()=>addRecord} value="记录" class=" mdui-ripple  mdui-btn mdui-btn-raised mdui-center">
    发表
    </button>

    <div style="margin-top:30px">
      <div class="mdui-bottom-nav mdui-bottom-nav-text-auto">
        <a onclick={()=> getMyList()} class="mdui-ripple mdui-bottom-nav-active">
          <i class="mdui-icon material-icons">live_tv</i>
          <label>我的微博</label>
        </a>

        <a onclick={()=> getMyList()} class="mdui-ripple">
          <i class="mdui-icon material-icons">music_note</i>
          <label>发现广场</label>
        </a>
      </div>
    </div>
    {taskMyListRx}
  </div>

  }

}
