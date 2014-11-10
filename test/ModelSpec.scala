package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._
import java.util.{Date}
import java.text._

import models.Task

class ModelSpec extends Specification {

    "Feature 1" should {

        /* TESTS FEATURE 1 */ 

        "Crear y Consultar tarea - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create("Test")

                val tarea = Task.consultaTarea(Task.consultaId)
                tarea.head.label must equalTo("Test")  
            }
        }

        "Consultar tarea insexistente - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                Task.create("Test")
                val tarea = Task.consultaTarea(Task.consultaId+1)
                tarea must equalTo(Nil)
            }
        }

        "Consultar total de tareas - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create("Test")
                val tareas = Task.all
                tareas.length must equalTo(Task.consultaId)
            }
        }

        
        "Borrar una tarea existente - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create("Test")
                Task.create("Test2")
                val tareas = Task.all
                /* Borro la ultima tarea */                
                Task.delete(Task.consultaId)

                tareas.length must equalTo(Task.consultaId+1)
            }
        }  

        "Borrar una tarea inexistente - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create("Test")
                val tareas = Task.all
                val count = Task.consultaId
                /* Intento borrar una tarea que no existe */                
                Task.delete(count+1)

                tareas.length must equalTo(count)
            }
        }   
    }

    "Feature 2" should {

        /* TESTS FEATURE 2 */ 

        "Crear y Consultar tarea con usuario- Feature 2" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create_user("Test","Jesus")

                val tarea = Task.consultaTarea(Task.consultaId)
                tarea.head.label must equalTo("Test")  
            }
        }

        "Comprobar si el usuario existe- Feature 2" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                val result = Task.existeUser("Azimuth")
                //Esto se comprueba en el aplication
                result must equalTo(0)
            }
        }

        "Consultar total de tareas del usuario Anonimo 'Magic'- Feature 2" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create_user("Test","Magic")
                val tareas = Task.all_magic
                Task.create_user("Test2","Magic")
                val tareas2 = Task.all_magic
                tareas.length must equalTo(tareas2.length-1)
            }
        }

        "Consultar total de tareas de un usuario distinto al Anonimo- Feature 2" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                Task.create_user("Test","Jesus")
                val tareas = Task.all_user("Jesus")
                Task.create_user("Test2","Jesus")
                val tareas2 = Task.all_user("Jesus")
                tareas.length must equalTo(tareas2.length-1)
            }
        }

        "Consultar total de tareas de un usuario inexistente- Feature 2" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                val tareas = Task.all_user("Azimuth")
                tareas must equalTo(Nil)
                
            }
        }
    }

    "Feature 3" should {

        /* TESTS FEATURE 3 */ 

        "Consultar formato de fecha correcto para GET (dd-mm-yyyy) - Feature 3" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val result = Task.formatoFecha("07-11-2014")
                result must equalTo(true)
            }
        }

        "Consultar formato de fecha incorrecto para GET - Feature 3" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                Task.formatoFecha("7-11-2014") must equalTo(false)
                Task.formatoFecha("077-11-2014") must equalTo(false)
                Task.formatoFecha("07-1-2014") must equalTo(false)
                Task.formatoFecha("07-111-2014") must equalTo(false)
                Task.formatoFecha("07-11-14") must equalTo(false)
                Task.formatoFecha("07-11-20145") must equalTo(false)
            }
        }

        "Consultar formato de fecha correcto para POST (dd-mm-yyyy) - Feature 3" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val result = Task.formatoFechaPost("2014-11-07")
                result must equalTo(true)
            }
        }

        "Consultar formato de fecha incorrecto para POST - Feature 3" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                Task.formatoFechaPost("14-11-07") must equalTo(false)
                Task.formatoFechaPost("20145-11-07") must equalTo(false)
                Task.formatoFechaPost("2014-1-07") must equalTo(false)
                Task.formatoFechaPost("2014-111-07") must equalTo(false)
                Task.formatoFechaPost("2014-11-7") must equalTo(false)
                Task.formatoFechaPost("2014-11-077") must equalTo(false)
            }
        }

        "Crear y Consultar tarea con usuario y fecha- Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")
                Task.create_user_fecha("Test","Jesus",fecha)

                val tarea = Task.consultaTarea(Task.consultaId)
                tarea.head.label must equalTo("Test")  
            }
        }

        "Consultar total de tareas de un usuario existente con una fecha registrada - Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")
                Task.create_user_fecha("Test","Magic",fecha)
                val tareas = Task.all_user_fecha("Magic",fecha)
                Task.create_user_fecha("Test","Magic",fecha)
                val tareas2 = Task.all_user_fecha("Magic",fecha)
                tareas.length must equalTo(tareas2.length-1)
            }
        }

        "Consultar total de tareas de un usuario existente a partir de una fecha - Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                Task.eliminarTaskUser("Magic")
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("06-11-2014")
                val fecha1 = formato.parse("07-11-2014")
                Task.create_user_fecha("Test","Magic",fecha)
                Task.create_user_fecha("Test","Magic",fecha1)
                val tareas = Task.all_user_fecha_orden("Magic",fecha)                
                val tareas2 = Task.all_user_fecha_orden("Magic",fecha1)
                tareas2.length must equalTo(tareas.length-1)
            }
        }

        "Consultar total de tareas de un usuario inexistente con una fecha registrada- Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")
                val tareas = Task.all_user_fecha("Azimuth",fecha)
                tareas must equalTo(Nil)
            }
        }        

        "Consultar total de tareas de un usuario existente con una fecha no registrada- Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")
                val fecha2 = formato.parse("08-11-2014")
                Task.create_user_fecha("Test","Magic",fecha)
                val tareas = Task.all_user_fecha("Magic",fecha2)
                tareas must equalTo(Nil)
            }
        } 

        "Consultar total de tareas de un usuario existente con una fecha no registrada- Feature 3" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")
                val fecha2 = formato.parse("08-11-2014")
                Task.create_user_fecha("Test","Magic",fecha)
                val tareas = Task.all_user_fecha("Magic",fecha2)
                tareas must equalTo(Nil)
            }
        }        

    }  

    "Feature 4" should{

         /* TESTS FEATURE 4 */ 

         "Crear una categoria sin usuario asociado - Feature 4" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val categoria = "Software"                
                Task.create_categoria(categoria)
                val cat = Task.comprueba_categoria(categoria)
                cat must equalTo(1)
            }
        }  

        "Crear categoria asociada a un usuario - Feature 4" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val usuario = "Jesus"
                val categoria = "Software"
                Task.create_categoria_user(usuario,categoria)
                val cat = Task.comprueba_categoria_user(usuario,categoria)
                cat must equalTo(1)
            }
        } 

        "Crear la misma categoria asociada a diferentes usuarios - Feature 4" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val usuario = "Jesus"
                val usuario2 = "Domingo"
                val categoria = "Software"
                Task.create_categoria_user(usuario,categoria)
                Task.create_categoria_user(usuario2,categoria)
                val cat = Task.comprueba_categoria_user(usuario,categoria)
                cat must equalTo(1)
            }
        }

        "Crear una tarea a un usuario con una categoria determinada - Feature 4" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val formato = new SimpleDateFormat("dd-MM-yyyy")
                val fecha = formato.parse("07-11-2014")              
                val usuario = "Jesus"
                val categoria = "Software"
                val label = "Test categoria"
                Task.create_categoria_user(usuario,categoria)
                Task.create_task_categoria(label,usuario,fecha,categoria)

                val tarea = Task.consultaTarea(Task.consultaId)
                tarea.head.label must equalTo("Test categoria")

            }
        }

    }
}