package controllers

import java.util.{Date}
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.Task


object Application extends Controller {

  //Estructura Json para mostrar las Tareas
  implicit val taskWrites: Writes[Task] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "label").write[String]
  )(unlift(Task.unapply))

  val taskForm = Form(
      "label" -> nonEmptyText
   ) 

  //Se ha puesto que al acceder a index se cargue el form de la practica anterior para poder seguir utilizandolo
  def index = Action {
    Ok(views.html.index(Task.all, taskForm))
  }

  //Funcion que obtiene la consulta de la tarea por id y muestra el json
  def consultaTask(id: Long) = Action {
    val json = Json.toJson(Task.consultaTarea(id))
    Ok(json)
   } 

   //recibe todas las tareas y las muestra en el formato json 
  def tasks = Action {
    val json = Json.toJson(Task.all_magic)
    Ok(json)
   }

   //recibe todas las tareas del user y las muestra en el formato json 
  def consultaTaskUser(login: String) = Action {
    if(Task.existeUser(login)!=0){
      val json = Json.toJson(Task.all_user(login))
      Ok(json)  
    }
    else{
      NotFound
    }
   }


  //recibe todas las tareas del user en una fecha y las muestra en el formato json 
  def consultaTaskUserFecha(login: String, fecha : String) = Action {
    Task.formatoFecha(fecha)
    if(Task.existeUser(login)!=0 && Task.formatoFecha(fecha)==true){
      val json = Json.toJson(Task.all_user(login))
       Ok(json)  
    }
    else{
      NotFound
    }
   }

   //Es una modificaci´on del metodo anterior en el que se crea y devuelve un json a partir de lo que devuelve la consulta de una tarea
  def newTaskUser(login: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all_user(login), errors)),
      label => {
        Task.create_user(label,login)
        val json = Json.toJson(Task.consultaTarea(Task.consultaId))
        Created(json)
        }
      )
     }

  //Es una modificaci´on del metodo anterior en el que se crea y devuelve un json a partir de lo que devuelve la consulta de una tarea
  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all_magic, errors)),
      label => {
        Task.create(label)
        val json = Json.toJson(Task.consultaTarea(Task.consultaId))
        Created(json)
        }
      )
     }

  def deleteTask(id: Long) = Action {
    if(Task.consultaTarea(id) != Nil){
      Task.delete(id)
      Redirect(routes.Application.tasks)
    }
    else{
      NotFound
    }
      
   }

/*def newTask = Action { implicit request =>
  taskForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Task.all(), errors)),
    label => {
      Task.create(label)
      Redirect(routes.Application.index)
      }
    )
   }  */

  /*def deleteTask(id: Long) = Action {
      Task.delete(id)
      Redirect(routes.Application.index)
   }*/



}