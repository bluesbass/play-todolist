import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Task
import controllers.Application

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    /* TESTS FEATURE 1 */    

   "Crear y Consultar tarea - Feature 1" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create("Test")
        val id = Task.consultaId
        val result = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(result) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
        
      }      
    }

  }
}
