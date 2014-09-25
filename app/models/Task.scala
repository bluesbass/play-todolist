package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {
  
   val task = {
      get[Long]("id") ~ 
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }

  //Funcion para consultar una tarea por el id de la Base de datos
  def consultaTarea(id: Long): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where id={id}").on('id -> id).as(task *)
   }

  //Funcion para consultar todas las tareas añadidas en la Base de Datos
  def all(): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task").as(task *)
   }

  //Funcion para crear una tarea
  def create(label: String) {
   DB.withConnection { implicit c =>
    SQL("insert into task (label) values ({label})").on(
      'label -> label
    ).executeUpdate()
    }
   }  
  
  //Funcion para borrar una tarea
  def delete(id: Long) {
   DB.withConnection { implicit c =>
    SQL("delete from task where id = {id}").on(
      'id -> id
    ).executeUpdate()
    }
   }
  
}