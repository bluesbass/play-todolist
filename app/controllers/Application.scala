package controllers

import java.util.{Date}
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.text._
import models.Task


case class TaskData(label: String, fecha: String)
case class TaskCateg(label: String, fecha: String, categoria: String)

object Application extends Controller {

  //Estructura Json para mostrar las Tareas
  implicit val taskWrites: Writes[Task] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "label").write[String]
  )(unlift(Task.unapply))

  val taskForm = Form(
      "label" -> nonEmptyText
   ) 

  val taskCategoriaForm = Form(
     mapping( 
      "label" -> nonEmptyText,
      "fecha" -> nonEmptyText,
      "categoria" -> nonEmptyText
      )(TaskCateg.apply)(TaskCateg.unapply)
   )

  val taskDataForm = Form(
     mapping( 
      "label" -> nonEmptyText,
      "fecha" -> nonEmptyText
      )(TaskData.apply)(TaskData.unapply)
   ) 

  val categoriaForm = Form(
      "categoria" -> nonEmptyText
   ) 

  //Controlar que si el id no existe devuelva 404

  //Se ha puesto que al acceder a index se cargue el form de la practica anterior para poder seguir utilizandolo
  def index = Action {
    Ok(views.html.index(Task.all, taskForm))
  }

  //Funcion que obtiene la consulta de la tarea por id y muestra el json
  def consultaTask(id: Long) = Action {
    if(Task.consultaTarea(id) != Nil){
      val json = Json.toJson(Task.consultaTarea(id))
      Ok(json)
    }
    else
      NotFound("No existe una tarea con ese id")
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
      NotFound("El usuario no existe")
    }
   }

  //recibe todas las tareas del user a partir de una fecha y las muestra en el formato json ordenadas de forma decreciente
  def consultaTaskUserFechaOrden(login: String, fecha : String) = Action {
    if(Task.existeUser(login)!=0 && Task.formatoFecha(fecha)==true){
      val formato = new SimpleDateFormat("dd-MM-yyyy")
      val fechaAux = formato.parse(fecha)
      val json = Json.toJson(Task.all_user_fecha_orden(login,fechaAux))
       Ok(json)  
    }
    else{
      NotFound("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
    }
   }

  //recibe todas las tareas del user en una fecha y las muestra en el formato json 
  def consultaTaskUserFecha(login: String, fecha : String) = Action {
    if(Task.existeUser(login)!=0 && Task.formatoFecha(fecha)==true){
      val formato = new SimpleDateFormat("dd-MM-yyyy")
      val fechaAux = formato.parse(fecha)
      val json = Json.toJson(Task.all_user_fecha(login,fechaAux))
       Ok(json)  
    }
    else{
      NotFound("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
    }
   }

   //Es una modificaci´on del metodo anterior en el que se crea y devuelve un json a partir de lo que devuelve la consulta de una tarea
  def newTaskUserFecha(login: String,fecha: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all_user(login), errors)),
      label => {
        if(Task.formatoFechaPost(fecha)==true && Task.existeUser(login)!=0)
        {
          val formato = new SimpleDateFormat("yyyy-MM-dd")
          val fechaAux = formato.parse(fecha)
          Task.create_user_fecha(label,login,fechaAux)
          val json = Json.toJson(Task.consultaTarea(Task.consultaId))
          Created(json)  
        }
        else
          NotFound("El usuario no existe o el formato de la fecha es incorrecto (yyyy-MM-dd)")
        
        }
      )
     }

   //Es una modificaci´on del metodo anterior en el que se crea y devuelve un json a partir de lo que devuelve la consulta de una tarea
  def newTaskUser(login: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all_user(login), errors)),
      label => {
        if(Task.existeUser(login)!=0)
        {
          Task.create_user(label,login)
          val json = Json.toJson(Task.consultaTarea(Task.consultaId))
          Created(json)
        }
        else
          NotFound("El usuario no existe")
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
      Ok("Tarea eliminada correctamente")
      //Redirect(routes.Application.tasks)
    }
    else{
      NotFound("La tarea que intentas eliminar no existe")
    }
      
   }


   //Funcion para crear una categoria asociada a un usuario
  def newCategoriaUser(login: String) = Action { implicit request =>
    categoriaForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all_user(login), errors)),
      categoria => {
        if(Task.existeUser(login)==0)
          NotFound("El usuario "+login+" no existe")        
        else if(Task.comprueba_categoria_user(login,categoria)!=0)
          NotFound("El usuario "+login+" ya tenia asociada la categoria "+categoria)          
        else
        {
          Task.create_categoria_user(login, categoria)
          val json = "Categoria asociada al usuario "+login 
          Created(json)  
        }
        
          
        
        }
      )
     }

   //Funcion para crear una tarea con categoria asociada a un usuario
  def newTaskCategoria(login: String, categoria: String) = Action { implicit request =>
    taskDataForm.bindFromRequest.fold(
      errors => BadRequest("Error en la peticion"),
      taskData => {

        if(Task.existeUser(login)==0)
          NotFound("El usuario "+login+" no existe")          
        else if (Task.comprueba_categoria_user(login,categoria)==0) 
          NotFound("El usuario "+login+" no teniene asociada la categoria "+categoria)
        else if (Task.formatoFechaPost(taskData.fecha)!=true)
          NotFound("El formato de la fecha "+taskData.fecha+" es incorrecto (yyyy-MM-dd)")
        else
        {
          val formato = new SimpleDateFormat("yyyy-MM-dd")
          val fechaAux = formato.parse(taskData.fecha)    

          Task.create_task_categoria(taskData.label,login,fechaAux,categoria)          
          val json = "Creada tarea del usuario "+login+" en la categoria "+categoria;
          Created(json)  
        }
      }
      )
     }

  //Funcion para modificar una tarea asociada a una categoria y a un user
  def modificarTask(id: Long,login: String,categoria: String) = Action { implicit request =>
    taskCategoriaForm.bindFromRequest.fold(
      errors => BadRequest("Error en la peticion"),
      taskCateg => {

        val id = Task.consultaId
        val resultado = Task.consultaTarea(id)
        val formato = new SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.parse(taskCateg.fecha)

        if(Task.existeUser(login)==0)
          NotFound("El usuario "+login+" no existe")
        else if(Task.comprueba_categoria_user(login,categoria)==0)
          NotFound("El usuario no tiene asociada la categoria "+categoria)
        else if(resultado==Nil)
          NotFound("La tarea con id "+id+" no existe")
        else if(Task.formatoFechaPost(taskCateg.fecha)!=true)
          NotFound("El formato de la fecha "+taskCateg.fecha+" es incorrecto (yyyy-MM-dd)")
        else if(Task.comprueba_categoria_user(login,taskCateg.categoria)==0)
          NotFound("El usuario no tiene asociada la categoria "+taskCateg.categoria)
        else
        {
          Task.modificar_task(id, taskCateg.label, fecha, taskCateg.categoria)          
          val json = "Modificada la tarea del usuario "+login+" en la categoria "+categoria
          Created(json)
        }

      }
        
      )
  }


}