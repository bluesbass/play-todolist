package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.Task

class ModelSpec extends Specification {

    "Models" should {

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

        /* Esta funcion depende de el contenido de la BDD */
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

    }  
}