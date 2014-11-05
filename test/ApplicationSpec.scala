import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._

import models.Task
import controllers.Application


@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    /* TESTS FEATURE 1 */    

   "Consultar tarea creada desde la capa del modelo- Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create("Test")
        val id = Task.consultaId
        val result = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(result) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
        status(result) must equalTo(OK)
        
      }      
    }

    "Consultar tarea inexistente - Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create("Test")
        val id = Task.consultaId+1
        val result = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(result) must contain("No existe una tarea con ese id")
        status(result) must equalTo(NOT_FOUND)
        
      }      
    }

    "Crear y comprobar una tarea - Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val result = Application.newTask(  
          FakeRequest(POST, "/tasks").withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId
        val tarea = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(tarea) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
        status(result) must equalTo(CREATED)
        
      }      
    }

   "Eliminar tarea inexistente - Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create("Test")
        val id = Task.consultaId+1
        
        val result = Application.deleteTask(id)(FakeRequest(DELETE, "/tasks/"+id))

        contentAsString(result) must contain("La tarea que intentas eliminar no existe")
        status(result) must equalTo(NOT_FOUND)
        
      }      
    }

    "Eliminar tarea existente - Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create("Test")
        val id = Task.consultaId
        
        val result = Application.deleteTask(id)(FakeRequest(DELETE, "/tasks/"+id))

        contentAsString(result) must contain("Tarea eliminada correctamente")
        status(result) must equalTo(OK)
        
      }      
    }

    "Comprobar que devuelve 404 en una peticion erronea - Feature 1" in {  
      running(FakeApplication()) {  
        route(FakeRequest(GET, "/fail")) must beNone  
        
      } 
    }

     "Comprobar peticion de index - Feature 1" in {  
      running(FakeApplication()) {

        val Some(home) = route(FakeRequest(GET, "/"))

        contentType(home) must beSome.which(_ == "text/html")  
        
        status(home) must equalTo(OK)  
        
      }
    }

    "Comprobar funcionamiento de la funcion que devuelve todas las tareas (Ya modificada con el usuario anonimo 'Magic') - Feature 1" in {  
      running(FakeApplication()) {

        val result = Application.tasks(FakeRequest())
        val Some(home) = route(FakeRequest(GET, "/tasks"))
        status(result) must equalTo(OK)
        status(home) must equalTo(OK)
        
      }
    }

  }
}