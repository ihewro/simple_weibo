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
          dom.window.location.hash = s"#/Comment/"+id
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

  def getMyList: Unit = {
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

  val taskListRx = taskList.map {
    case Nil => <div style ="margin: 30px; font-size: 17px;">暂无微博</div>
    case list => <div style ="margin: 20px; font-size: 17px;">
      <table>
        <tr>
          <th class={th.htmlClass}>发布者</th>
          <th class={th.htmlClass}>内容</th>
          <th class={th.htmlClass}>创建时间</th>
          <th class={th.htmlClass}>点赞数</th>
          <th class={th.htmlClass}>操作</th>
        </tr>
        {list.map {l =>
        <tr>
          <td class={td.htmlClass}>匿名</td>
          <td class={td.htmlClass}>{l.content}</td>
          <td class={td.htmlClass}>{TimeTool.dateFormatDefault(l.time)}</td>
          <td class={td.htmlClass}>0</td>
          <td class={td.htmlClass}>{getDeleteButton(l.id)}</td>
          <td class={td.htmlClass}>{getCommentButton(l.id)}</td>
          <td class={td.htmlClass}>{getLikeButton(l.id)}</td>
        </tr>
      }
        }

      </table>

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

  def concenteList: Unit ={

  }

  def fansList: Unit ={

  }


  def myList: Unit ={
    getMyList
  }

  def allList: Unit ={
    getMyList
  }

  def app: xml.Node = {
   getMyList
  <div class={container.htmlClass}>
    <div style="margin:30px;">
      <div style="font-size:25px;">SIMPLE · 微博</div>
    </div>
    <div>
      <button class={addButton.htmlClass} onclick={()=>logout()}>退出</button>
    </div>

    <div style="margin-bottom:30px;margin-top:30px">
      <button class={addButton.htmlClass} onclick={()=>logout()}>关注TA</button>
      <button class={addButton.htmlClass} onclick={()=>concenteList}>关注列表</button>
      <button class={addButton.htmlClass} onclick={()=>fansList}>粉丝列表</button>
    </div>


    <div>
      <textarea id ="taskInput" class={textArea.htmlClass}></textarea>
    </div>
    <button class={addButton.htmlClass} onclick={()=>addRecord}>发表</button>

    <div style="margin-top:30px">
      <button class={addButton.htmlClass} onclick={()=>allList}>我的微博</button>
      <button class={addButton.htmlClass} onclick={()=>allList}>发现广场</button>
    </div>
    {taskListRx}
  </div>
  }

}
