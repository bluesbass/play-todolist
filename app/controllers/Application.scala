package controllers

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
    Ok(views.html.index(Task.all(), taskForm))
  }

  //Funcion que obtiene la consulta de la tarea por id y muestra el json
  def consultaTask(id: Long) = Action {
    val json = Json.toJson(Task.consultaTarea(id))
    Ok(json)
   } 

   //recibe todas las tareas y las muestra en el formato json
  def tasks = Action {
    val json = Json.toJson(Task.all())
    Ok(json)
   }

  def newTask = Action { implicit request =>
  taskForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Task.all(), errors)),
    label => {
      Task.create(label)
      Redirect(routes.Application.index)
      }
    )
   }  

  def deleteTask(id: Long) = Action {
      Task.delete(id)
      Redirect(routes.Application.index)
   }

}