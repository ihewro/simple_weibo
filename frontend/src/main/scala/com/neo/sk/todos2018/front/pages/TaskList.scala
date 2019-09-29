package com.neo.sk.todos2018.front.pages

import java.util.concurrent.TimeUnit

import com.neo.sk.todos2018.front.Routes
import com.neo.sk.todos2018.front.styles.ListStyles._
import com.neo.sk.todos2018.front.utils.{Http, JsFunc, TimeTool}
import com.neo.sk.todos2018.shared.ptcl.SuccessRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.{AddRecordReq, DelRecordReq, GetListRsp, GoToCommentReq, TaskRecord}
import io.circe.generic.auto._
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import mhtml._
import org.scalajs.dom
import org.scalajs.dom.html.{Input, TextArea}

import scala.xml.Elem
/**
  * Created by haoshuhan on 2018/6/4.
  * changed by Xu Si-ran on 2019/3/21
  * update by zhangtao, 2019-3-23: record id.
  */
object TaskList{

  val url = "#/" + "List"

  val taskList = Var(List.empty[TaskRecord])


  def addRecord: Unit = {
    val data = dom.document.getElementById("taskInput").asInstanceOf[TextArea].value
    if (data == ""){
      JsFunc.showMessage("还没填写任何内容！")
    }
    else{
      Http.postJsonAndParse[SuccessRsp](Routes.List.addRecord, AddRecordReq(data).asJson.noSpaces).map {
        case Right(rsp) =>
          if(rsp.errCode == 0) {
            dom.document.getElementById("taskInput").asInstanceOf[TextArea].value = ""//清空文本
            JsFunc.showMessage("发布成功！")
            getMyList
          } else {
            dom.window.location.hash = s"#/Login"
            JsFunc.showMessage(rsp.msg)
            println(rsp.msg)
          }
        case Left(error) =>
          println(s"parse error,$error")
      }
    }
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
          JsFunc.showMessage(rsp.msg)
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get  error,$error")
    }

  }



  def getMyList: Unit = {
    println("getMyList")
    Http.getAndParse[GetListRsp](Routes.List.getRecordListByLoginUser).map {
      case Right(rsp) =>
        if(rsp.errCode == 0){
          taskList := rsp.list.get
        } else {
          JsFunc.showMessage(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        JsFunc.showMessage("没有内容")
        println(s"get task list error,$error")
    }
  }


  /**
   * 根据微博记录返回一个HTML node 内容
   * @param list
   * @return
   */
  def returnRecordRX (list : Var[List[TaskRecord]]): Rx[Elem] = {
    list.map {
      case Nil =>
        <div class="mc-empty"><div class="title">暂无微博</div>
          <div class="description">可以点击近期热门，发现更有趣的瞬间。</div>
        </div>
      case list =>
        <div style="font-size: 17px;">
          {list.map {l =>
          <a class="item" href={"/todos2018#/Detail/"+l.id}>
            <div class="avatar" style={"background-image: url("+l.userInfo.avatar.url+");"}></div>
            <div class="content">
              <div class="title">
                {l.content}
              </div>
              <div class="meta">
                <div class="username">
                  {l.userInfo.userName}
                </div>
                <div class="answer_time" title="2019-09-26 17:11:16">
                  发布于 {TimeTool.dateFormatDefault(l.time)}
                </div>
              </div>
            </div>
          </a>
        }
          }
        </div>
    }

  }
  val taskMyListRx: Rx[Elem] = returnRecordRX(taskList)



  def app: xml.Node = {
   getMyList
  <div id="page-questions" class="mdui-container main">
    <div id="page-question">
      <div class="mdui-card mdui-center question mdui-p-t-3 mdui-p-b-1">
        <textarea id ="taskInput" class="mdui-textfield-input" rows="4" placeholder="分享你的灵光时刻"></textarea>
        <div class="actions mdui-m-t-2"><button onclick={()=>addRecord}  class="mdui-btn mdui-btn-raised mdui-text-color-blue">发表</button></div>
      </div>
      <div class="mdui-typo-headline answers-count">我的微博</div>
      {taskMyListRx}
    </div>
  </div>
  }

}
