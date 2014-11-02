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

                val tarea = Task.consultaTarea(Task.consultaId+1)
                tarea must equalTo(Nil)
            }
        }

        /* Esta funcion depende de el contenido de la BDD */
        "Consultar total de tareas - Feature 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                val tareas = Task.all
                tareas.length must equalTo(Task.consultaId)
            }
        }

        
           

    }  
}