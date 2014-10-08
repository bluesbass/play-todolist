package models

import java.util.{Date}

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String, fecha: String)

object Task {
  
   val task = {
      get[Long]("id") ~ 
      get[String]("label") ~
      get[String]("fecha") map {
         case id~label~fecha => Task(id, label, fecha)
      }
   }

  def existeUser(login: String) : Long = DB.withConnection { implicit c =>
    SQL("select count(*) from task_user where usuario={login}").on('login -> login).as(scalar[Long].single)
  }

  //Funcion para consultar una tarea por el id de la Base de datos
  def consultaTarea(id: Long): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where id={id}").on('id -> id).as(task *)
   }

  //Funcion para consultar el ultimo id de la tarea creada
  def consultaId : Long = DB.withConnection { implicit c =>
      SQL("select max(id) from task").as(scalar[Long].single)
   }
   //Funcion que devuelve todas las tareas sin filtrar
   def all : List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task").as(task *)
   }

  //Funcion para consultar todas las tareas añadidas en la Base de Datos por un user
  def all_user(login: String): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario={login}").on('login -> login).as(task *)
   }

   //Funcion para consultar todas las tareas añadidas en la Base de Datos por magic
  def all_magic : List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario='Magic'").as(task *)
   }

   //Funcion para crear una tarea al usuario por defecto Magic
  def create_user(label: String,login: String) {
   DB.withConnection { implicit c =>
    SQL("insert into task (label,usuario) values ({label},{login})").on(
      'label -> label,'login -> login
    ).executeUpdate()
    }
   }  

  //Funcion para crear una tarea al usuario por defecto Magic
  def create(label: String) {
   DB.withConnection { implicit c =>
    SQL("insert into task (label,usuario) values ({label},'Magic')").on(
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