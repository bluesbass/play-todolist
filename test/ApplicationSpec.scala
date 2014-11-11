import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.matcher._

import org.junit.runner._
import play.api.libs.json.{Json, JsValue}

import play.api.test._
import play.api.test.Helpers._
import java.util.{Date}
import java.text._

import play.api.libs.json._

import models.Task
import controllers.Application


@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with JsonMatchers{

  "Feature 1" should {

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
        //val tarea = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(result) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
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

  "Feature 2" should {

    /* TESTS FEATURE 2 */  

    "Crear tarea con usuario correcto y Consultar que se ha creado - Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Jesus"
        val result = Application.newTaskUser(login)(  
          FakeRequest(POST, "/"+login+"/tasks").withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId
        //val tarea = Application.consultaTask(id)(FakeRequest())
        
        contentAsString(result) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
        status(result) must equalTo(CREATED)
        
      }      
    }

    "Crear tarea con usuario inexistente - Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Pascualinex"
        val result = Application.newTaskUser(login)(  
          FakeRequest(POST, "/"+login+"/tasks").withFormUrlEncodedBody(("label","Test"))  
          )
        
        contentAsString(result) must equalTo("El usuario no existe")
        status(result) must equalTo(NOT_FOUND)
        
      }      
    }

    "Consultar tareas de un usuario existente y comprueba que tiene la ultima que se ha creado - Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create_user("Test","Jesus")
        val tareas = Application.consultaTaskUser("Jesus")(FakeRequest())
        val id = Task.consultaId // Obtenemos el ultimo id

        contentType(tareas) must beSome.which(_ == "application/json")

        val resultString = contentAsString(tareas) 
        resultString must contain("\"id\":"+ id)
        resultString must contain("\"label\":\"Test\"")

        status(tareas) must equalTo(OK)
      }      
    }

    "Consultar tareas de un usuario existente y comprueba que tiene la ultima que se ha creado (Desde GET)- Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.create_user("Test","Jesus")        
        val Some(tareas) = route(FakeRequest(GET, "/Jesus/tasks"))
        val id = Task.consultaId // Obtenemos el ultimo id


        contentType(tareas) must beSome.which(_ == "application/json")

        val resultString = contentAsString(tareas) 
        resultString must contain("\"id\":"+ id)
        resultString must contain("\"label\":\"Test\"")

        status(tareas) must equalTo(OK)
        //status(tasks) must equalTo(OK)
      }      
    }

    "Consultar tareas de un usuario inexistente - Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val result = Application.consultaTaskUser("Pascualinex")(FakeRequest())
        contentAsString(result) must equalTo("El usuario no existe")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

    "Consultar tareas de un usuario inexistente desde GET- Feature 2" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(result) = route(FakeRequest(GET, "/Pascualinex/tasks"))
        contentAsString(result) must equalTo("El usuario no existe")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

  }

  "Feature 3" should {

    /* TESTS FEATURE 3 */  

    "Crear tarea con usuario y fecha correctos y Consultar que se ha creado - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val fecha = "2014-11-07"
        val login = "Jesus"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId
        
        contentAsString(result) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"}]")
        status(result) must equalTo(CREATED)
        
      }      
    }

    "Crear tarea con usuario inexistente y fecha correcta - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val fecha = "2014-11-07"
        val login = "Pascualinex"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId
        
        contentAsString(result) must equalTo("El usuario no existe o el formato de la fecha es incorrecto (yyyy-MM-dd)")
        status(result) must equalTo(NOT_FOUND)
        
      }      
    }


    "Crear tarea con usuario existente y fecha incorrecta - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val fecha = "20145-11-07"
        val login = "Jesus"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId
        
        contentAsString(result) must equalTo("El usuario no existe o el formato de la fecha es incorrecto (yyyy-MM-dd)")
        status(result) must equalTo(NOT_FOUND)
        
      }      
    }

    "Consultar tareas de un usuario existente en una fecha y comprueba que tiene la ultima que se ha creado - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val fecha = "2014-11-07"
        val login = "Jesus"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val fechaget = "07-11-2014"
        val tareas = Application.consultaTaskUserFecha(login,fechaget)(FakeRequest())
        val id = Task.consultaId // Obtenemos el ultimo id

        contentType(tareas) must beSome.which(_ == "application/json")

        val resultString = contentAsString(tareas) 
        resultString must contain("\"id\":"+ id)
        resultString must contain("\"label\":\"Test\"")

        status(tareas) must equalTo(OK)
      }      
    }

    "Consultar tareas de un usuario existente en una fecha y comprueba que tiene la ultima que se ha creado (Desde GET) - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val fecha = "2014-11-07"
        val login = "Jesus"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val fechaget = "07-11-2014"
        val Some(tareas) = route(FakeRequest(GET, "/"+login+"/tasks/"+fechaget))
        val id = Task.consultaId // Obtenemos el ultimo id

        contentType(tareas) must beSome.which(_ == "application/json")

        val resultString = contentAsString(tareas) 
        resultString must contain("\"id\":"+ id)
        resultString must contain("\"label\":\"Test\"")

        status(tareas) must equalTo(OK)
      }      
    }

    "Consultar tareas de un usuario inexistente en una fecha - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Pascualinex"
        val fechaget = "07-11-2014"
        
        val result = Application.consultaTaskUserFecha(login,fechaget)(FakeRequest())

        contentAsString(result) must equalTo("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

    "Consultar tareas de un usuario existente en una fecha incorrecta - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Jesus"
        val fechaget = "2014-11-07"
        
        val result = Application.consultaTaskUserFecha(login,fechaget)(FakeRequest())

        contentAsString(result) must equalTo("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

    "Consultar tareas de un usuario existente ordenadas por fecha - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Task.eliminarTaskUser("Jesus")
        val fecha = "2014-11-07"
        val fecha2 = "2014-11-08"
        val login = "Jesus"
        val result = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha).withFormUrlEncodedBody(("label","Test"))  
          )

        val id = Task.consultaId

        val result2 = Application.newTaskUserFecha(login,fecha)(  
          FakeRequest(POST, "/"+login+"/tasks/"+fecha2).withFormUrlEncodedBody(("label","Test2"))  
          )

        val id2 = Task.consultaId

        val fechaget = "07-11-2014"
        val tareas = Application.consultaTaskUserFechaOrden(login,fechaget)(FakeRequest())

        contentType(tareas) must beSome.which(_ == "application/json")

        contentAsString(tareas) must contain("[{\"id\":"+ id + ",\"label\":\"Test\"},{\"id\":"+ id2 + ",\"label\":\"Test2\"}]")

        status(tareas) must equalTo(OK)
      }      
    }

    "Consultar tareas de un usuario inexistente ordenadas por fecha - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Pascualinex"
        val fechaget = "07-11-2014"
        
        val result = Application.consultaTaskUserFechaOrden(login,fechaget)(FakeRequest())

        contentAsString(result) must equalTo("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

    "Consultar tareas de un usuario existente ordenadas por fecha incorrecta - Feature 3" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val login = "Jesus"
        val fechaget = "2014-11-07"
        
        val result = Application.consultaTaskUserFechaOrden(login,fechaget)(FakeRequest())

        contentAsString(result) must equalTo("El usuario no existe o la fecha esta mal construida (dd-MM-yyyy)")
        status(result) must equalTo(NOT_FOUND)
      }      
    }

  }


  "Feature 4" should {

    /* TESTS FEATURE 4 */ 

    "Crear una categoria asociada a un usuario existente - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val categoria = "Software"

        Task.eliminar_categoria_user(login,categoria)

        val result = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        contentAsString(result) must equalTo("Categoria asociada al usuario "+login)
        status(result) must equalTo(CREATED)

      }
    }

    "Crear una categoria asociada a varios usuarios existentes - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val login2 = "Domingo"
        val categoria = "Software"

        Task.eliminar_categoria_user(login,categoria)

        val result = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )
        val result2 = Application.newCategoriaUser(login2)(  
          FakeRequest(POST, "/"+login2+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        contentAsString(result) must equalTo("Categoria asociada al usuario "+login)
        status(result) must equalTo(CREATED)

        contentAsString(result2) must equalTo("Categoria asociada al usuario "+login2)
        status(result2) must equalTo(CREATED)

      }
    }

    "Crear una categoria asociada a un usuario inexistente - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Pascualinex"
        val categoria = "Software"

        Task.eliminar_categoria_user(login,categoria)

        val result = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        contentAsString(result) must equalTo("El usuario "+login+" no existe o ya tenia asociada la categoria "+categoria)
        status(result) must equalTo(NOT_FOUND)


      }
    }

    "Crear una categoria asociada a un usuario que ya la tenia asociada - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val categoria = "Software"

        Task.eliminar_categoria_user(login,categoria)

        val result = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        val result2 = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        contentAsString(result) must equalTo("Categoria asociada al usuario "+login)
        status(result) must equalTo(CREATED)

        contentAsString(result2) must equalTo("El usuario "+login+" no existe o ya tenia asociada la categoria "+categoria)
        status(result2) must equalTo(NOT_FOUND)


      }
    }

    "Crear una tarea a un usuario en una categoria determinada - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val fecha = "2014-11-07"              
        val categoria = "Software"
        val label = "Test tarea categoria"

        Task.eliminar_categoria_user(login,categoria)

        val resultaux = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        val result = Application.newTaskCategoria(login,categoria)(  
          FakeRequest(POST, "/"+login+"/"+categoria+"/tasks").withFormUrlEncodedBody(("label",label),("fecha",fecha))  
          )                

        contentAsString(result) must equalTo("Creada tarea del usuario "+login+" en la categoria "+categoria)
        status(result) must equalTo(CREATED)

      }
    }

    "Crear una tarea a un usuario inexistente en una categoria determinada - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Pascualinex"
        val fecha = "2014-11-07"              
        val categoria = "Software"
        val label = "Test tarea categoria"

        val result = Application.newTaskCategoria(login,categoria)(  
          FakeRequest(POST, "/"+login+"/"+categoria+"/tasks").withFormUrlEncodedBody(("label",label),("fecha",fecha))  
          )                

        contentAsString(result) must equalTo("El usuario "+login+" no existe, ya tenia asociada la categoria "+categoria+", o ha construido mal la fecha (yyyy-MM-dd)")
        status(result) must equalTo(NOT_FOUND)

      }
    }

    "Crear una tarea a un usuario en una categoria no asociada previamente - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val fecha = "2014-11-07"              
        val categoria = "Software"
        val label = "Test tarea categoria"

        Task.eliminar_categoria_user(login,categoria)

        val result = Application.newTaskCategoria(login,categoria)(  
          FakeRequest(POST, "/"+login+"/"+categoria+"/tasks").withFormUrlEncodedBody(("label",label),("fecha",fecha))  
          )                

        contentAsString(result) must equalTo("El usuario "+login+" no existe, ya tenia asociada la categoria "+categoria+", o ha construido mal la fecha (yyyy-MM-dd)")
        status(result) must equalTo(NOT_FOUND)

      }
    }

    "Crear una tarea a un usuario en una categoria determinada, con el formato de fecha incorrecto - Feature 4" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val login = "Jesus"
        val fecha = "07-11-2014"              
        val categoria = "Software"
        val label = "Test tarea categoria"

        Task.eliminar_categoria_user(login,categoria)

        val resultaux = Application.newCategoriaUser(login)(  
          FakeRequest(POST, "/"+login+"/NuevaCategoria").withFormUrlEncodedBody(("categoria",categoria))  
          )

        val result = Application.newTaskCategoria(login,categoria)(  
          FakeRequest(POST, "/"+login+"/"+categoria+"/tasks").withFormUrlEncodedBody(("label",label),("fecha",fecha))  
          )                

        contentAsString(result) must equalTo("El usuario "+login+" no existe, ya tenia asociada la categoria "+categoria+", o ha construido mal la fecha (yyyy-MM-dd)")
        status(result) must equalTo(NOT_FOUND)

      }
    }

  }

}