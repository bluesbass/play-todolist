package models

import java.util.{Date}

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

   def formatoFechaPost(fecha: String) : Boolean = { 
    val campos  = fecha.split("-")
    if(campos.length == 3)
    {
      if(campos(0).length!=4 || campos(1).length!=2 || campos(2).length!=2)
        return false;
      else
        return true;
    }
    else
      return false
  }

  def formatoFecha(fecha: String) : Boolean = { 
    val campos  = fecha.split("-")
    if(campos.length == 3)
    {
      if(campos(0).length!=2 || campos(1).length!=2 || campos(2).length!=4)
        return false;
      else
        return true;
    }
    else
      return false
  }
  
  //Funcion para borrar todas las tareas de un usuario determinado
  def eliminarTaskUser( login: String ) {
   DB.withConnection { implicit c =>
    SQL("delete from task where usuario={login}").on('login -> login).executeUpdate()
    }
   }

  //Funcion para contar el numero de tareas que tiene un usuario
  def tareasUser( login: String ) : Long = DB.withConnection { implicit c =>
    SQL("select count(*) from task where usuario={login}").on('login -> login).as(scalar[Long].single)
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

   //Funcion para consultar todas las tareas añadidas en la Base de Datos por un user a partir de una fecha
  def all_user_fecha_orden(login: String, fecha: Date): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario={login} and fecha>={fecha} order by fecha ASC").on('login -> login, 'fecha -> fecha).as(task *)
   }

   //Funcion para consultar todas las tareas añadidas en la Base de Datos por un user en una fecha
  def all_user_fecha(login: String, fecha: Date): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario={login} and fecha={fecha}").on('login -> login, 'fecha -> fecha).as(task *)
   }

  //Funcion para consultar todas las tareas añadidas en la Base de Datos por un user
  def all_user(login: String): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario={login}").on('login -> login).as(task *)
   }

   //Funcion para consultar todas las tareas añadidas en la Base de Datos por magic
  def all_magic : List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario='Magic'").as(task *)
   }

   //Funcion para crear una tarea al usuario login
  def create_user_fecha(label: String,login: String,fecha: Date) {
   DB.withConnection { implicit c =>
    SQL("insert into task (label,usuario,fecha) values ({label},{login},{fecha})").on(
      'label -> label,'login -> login,'fecha -> fecha
    ).executeUpdate()
    }
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

   def create_categoria(categoria: String) {
    DB.withConnection { implicit c =>
        SQL("insert into categoria (nombre) values({categoria})").on(
        'categoria -> categoria
    ).executeUpdate() 
    }
  }

  def comprueba_categoria(categoria: String) : Long = DB.withConnection { implicit c =>
    SQL("select count(*) from categoria where nombre={categoria}").on('categoria -> categoria).as(scalar[Long].single)
  }

   //Funcion para crear una categoria asociada a un usuario
  def create_categoria_user(login: String, categoria: String) {
   DB.withConnection { implicit c =>
    if(comprueba_categoria(categoria)!=1)
      create_categoria(categoria)

    SQL("insert into user_categ (usuario, categoria) values ({login}, {categoria})").on(
               'login -> login,
               'categoria -> categoria
            ).executeUpdate()     

    }
   }

  def comprueba_categoria_user(login: String, categoria: String) : Long = DB.withConnection { implicit c =>
    SQL("select count(*) from user_categ where usuario={login} and categoria={categoria}").on('login -> login,'categoria -> categoria).as(scalar[Long].single)
  }

  //Funcion para crear una tarea con categoria
  def create_task_categoria(label: String,login: String,fecha: Date, categoria: String) {
   DB.withConnection { implicit c =>
    SQL("insert into task (label,usuario,fecha,categoria) values ({label},{login},{fecha},{categoria})").on(
      'label -> label,'login -> login,'fecha -> fecha,'categoria->categoria
    ).executeUpdate()
    }
   } 

  //Funcion para crear una tarea con categoria
  def modificar_task(id: Long, label: String, fecha: Date, categoria: String) {
   DB.withConnection { implicit c =>
    SQL("update task set label={label},fecha={fecha},categoria={categoria} where id={id}").on(
      'id -> id,'label -> label,'fecha -> fecha,'categoria->categoria
    ).executeUpdate()
    }
   }

   def  eliminar_categoria_user(usuario: String, categoria: String) {
    DB.withConnection { implicit c => 
      SQL("delete from user_categ where usuario={usuario} and categoria={categoria}").on(
        'usuario -> usuario, 'categoria -> categoria
      ).executeUpdate()
      }
    }


    def consultaTareaCategoria(usuario: String, categoria: String): List[Task] = DB.withConnection { implicit c =>
      SQL("select * from task where usuario={usuario} and categoria={categoria}").on('usuario -> usuario, 'categoria -> categoria).as(task *)
   }

   def consultaTareaCategoriaId(usuario: String, categoria: String, id: Long): Long = DB.withConnection { implicit c =>
      SQL("select count(*) from task where usuario={usuario} and categoria={categoria} and id={id}").on('usuario -> usuario, 'categoria -> categoria, 'id ->id).as(scalar[Long].single)
  }
}