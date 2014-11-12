#Práctica 2 - Pruebas en Play Framework
---

Esta segunda parte de la práctica desarrollada en **Play Framework** se compone de 2 partes:
- Elaboración de los tests en Spec necesarios para comprobar el funcionamiento de los 3 features de la práctica anterior
- Elaboración de un nuevo feature añadiendo nuevas funcionalidades a la aplicación usando TDD.

El código de partida es el obtenido tras concluir la práctica 1 "API-REST"  **play-todolist**.

##Tests de la práctica 1
---
A continuación se detalla una lista de los tests implementados para cada uno de los features de los que se componía la práctica 1. 

En cada feature se han dividido los tests en 2 apartados, tests de las funciones implementadas en la capa de **Controlador** y tests de las funciones implementadas en la capa de **Modelo**.
###Feature 1
#####Modelo
- **`Crear y Consultar tarea`** => Crea una tarea y consulta que se haya creado correctamente.
- **`Consultar tarea insexistente`** => Se comprueba que al consultar una tarea inexistente la consulta devuelve Nil.
- **`Consultar total de tareas`** => Se consulta que al crear tareas se incrementa el numero de tareas en la BDD.
- **`Borrar una tarea existente`** => Se comprueba que se elimina una tarea que existe en la BDD.
- **`Borrar una tarea inexistente`** => Se comprueba que al intentar eliminar una tarea inexistente, el estado de la BDD es el mismo.
 

#####Controlador
- **`Consultar tarea creada desde la capa del modelo`** => Se crea una tarea con la funcion del modelo 'create' y se consulta si se ha creado con una funcion implementada en la capa del Controlador.
- **`Consultar tarea inexistente`** => Se consulta una tarea con un id inexistente y se compara el resultado con el error esperado.
- **`Crear y comprobar una tarea`** => Se crea una tarea y se comprueba que el json que devuelve y el estado son correctos.
- **`Eliminar tarea inexistente`** => Se intenta eliminar una tarea inexistente y se compara el resultado con el error esperado.
- **`Eliminar tarea existente`** => Se elimina una tarea y se compara el resultado obtenido con lo que se espera que devuelva.
- **`Comprobar que devuelve 404 en una peticion erronea`** => Se realiza una peticiopn erronea y se comprueba que devuelve 404.
- **`Comprobar peticion de index`** => Se realiza una peticion a la ruta / y se comprueba que devuelva el resultado esperado.
- **`Comprobar funcionamiento de la funcion que devuelve todas las tareas (Ya modificada con el usuario anonimo 'Magic')`** => Se comprueba que se devuelvan todas las tareas del usuario anónimo Magic.

###Feature 2
#####Modelo
- **`Crear y Consultar tarea con usuario`** => Crea una tarea con usuario existente y consulta que se haya creado correctamente.
- **`Comprobar si el usuario existe`** => Se comprueba si el usuario existe en la BDD.
- **`Consultar total de tareas del usuario anónimo 'Magic'`** => Se consulta que el número de tareas del usuario anónimo sea correcto.
- **`Consultar total de tareas de un usuario distinto del Anonimo`** => Se consulta que el número de tareas del usuario distinto al anónimo sea correcto.
- **`Consultar total de tareas de un usuario inexistente`** => Se comprueba que al consultar las tareas de un usuairo que no existe la consulta devuelve Nil.
 

#####Controlador
- **`Crear tarea con usuario correcto y consultar que se ha creado`** => Crear una tarea con usuario y comprobar que el json y el estado que devuelve son correctos.
- **`Crear tarea con usuario inexistente`** => Se comprueba que al intentar crear una tarea con un usuario que no existe el error que devuelve es correcto.
- **`Consultar tareas de un usuario existente y comprueba que tiene la ultima que se ha creado`** => Se crea una tarea y se comprueba que esta en la lista de tareas de dicho usuario.
- **`Consultar tareas de un existente y comprueba que tiene la ultima que se ha creado (Desde GET)`** => Igual que el test anterior pero esta vez se llama a la funcion que comprueba las tareas de un usuario desde GET.
- **`Consultar tareas de un usuario inexistente`** => Comprobar que el error que se devuelve al intentar consultar tareas de un usuario inexistente es correcto.
- **`Consultar tareas de un usuario inexistente desde GET`** => Igual que el test anterior pero desde GET.


###Feature 3
#####Modelo
- **`Consultar formato de fecha correcto para GET (dd-mm-yyyy)`** => Se comprueba  el formato correcto de una fecha en una peticion GET.
- **`Consultar formato de fecha incorrecto para GET`** => Se comprueban todos los casos en los que una fecha seria incorrecta para la petición GET.
- **`Consultar formato de fecha correcto para POST (yyyy-mm-dd)`** => Se comprueba  el formato correcto de una fecha en una peticion POST.
- **`Consultar formato de fecha incorrecto para POST`** => Se comprueban todos los casos en los que una fecha seria incorrecta para la petición POST.
- **`Crear y consultar tarea con usuario y fecha`** => Se crea una tarea con usuatio y fecha correctos y se comprueba que se ha creado correctamente.
- **`Consultar total de tareas de un usuario existente con una fecha registrada`** => Se comprueba que el número de tareas de un usuario en una fecha sea correcto.
- **`Consultar total de tareas de un usuario existente a partir de una fecha`** => Se comprueba que el número de tareas de un usuario a partir de una fecha sea correcto.
- **`Consultar total de tareas de un usuario inexistente con una fecha registrada`** => Se comprueba que la consulta devuelve Nil
- **`Consultar total de tareas de un usuario existente con una fecha no registrada`** => Se comprueba que la consulta devuelve Nil
 

#####Controlador
- **`Crear tarea con usuario y fecha correctos y consultar que se ha creado`** => Se comprueba que el json y el estado devueltos son correctos.
- **`Crear tarea con usuario inexistente y fecha correcta`** => Se comprueba que devuelve el error pertinente.
- **`Crear tarea con usuario existente y fecha incorrecta`** => Se comprueba que devuelve el error pertinente.
- **`Consultar tareas de un usuario existente en una fecha y comprueba que tiene la ultima que se ha creado`** => Se crea una tarea con fecha, se consultan todas en esa fecha y se comprueba que esta última tarea se haya creado.
- **`Consultar tareas de un usuario existente en una fecha y comprueba que tiene la ultima que se ha creado (Desde GET)`** => Igual que el test anterior pero desde GET.
- **`Consultar tareas de un usuario inexistente en una fecha`** => Se comprueba que el error que devuelve es correcto.
- **`Consultar tareas de un usuario existente en una fecha incorrecta`** => Se comprueba que el error que devuelve es correcto.
- **`Consultar tareas de un usuario existente ordenadas por fecha`** => Se consultan las tareas de un usuario a partir de una fecha y se comprueba que el resultado es correcto.
- **`Consultar tareas de un usuario inexistente ordenadas por fecha`** => Se comprueba que el error que devuelve es correcto.
- **`Consulta tareas de un usuario existente ordenadas por fecha incorrecta`** => Se comprueba que el error que devuelve es correcto.

##Feature 4
---
Devido a que este apartado se ha desarollado usando TDD, en primer lugar se detallará una lista con los tests implementados, continuada por una lista con las funciones implementadas necesarias para que superaran con exito dichos tests.

####Tests 
Se han dividido los tests en 2 apartados, tests de las funciones implementadas en la capa de Controlador y tests de las funciones implementadas en la capa de Modelo.

#####Modelo
- **`Crear una categoria sin usuario asociado`** => Se crea una categoria en la tabla Categoria y se comprueba que se ha realizado la acción correctamente.
- **`Crear categoria asociada a un usuario`** => Se crea una categoria y se asocia a un usuario, luego se comprueba que se ha realizado la acción con éxito.
- **`Crear la misma categoria asociada a diferentes usuario`** => Se comprueba que al asociar una misma categoria a distintos usuario no hay ningun problema.
- **`Crear una tarea a un usuario con una categoria determinada`** => Se crea una tarea con un usuario y una categoria y se comprueba que la acción se ha realizado con éxito.
- **`Modificar una tarea de un usuario con una categoria determinada`** => Se modifica una tarea de un usuario de una categoria y se comprueba que la acción ha tenido éxito.
- **`Modificar una tarea inexistente`** => Se trata de modificar una tarea que no existe y se comprueba que no ha habido cambio en la BDD.
- **`Listar tareas de un usuario de una categoria`** => Se listan todas las tareas que tiene un usuario en una categoria y se comprueba que el reslutado es correcto.
- **`Listar tareas de un usuario inexistente de una categoria`** => Se comprueba que al hacer la consulta devuelve Nil
- **`Listar tareas de un usuario de una categoria inexistente`** => Se comprueba que la consulta devuelve Nil
 

#####Controlador
- **`Crear una categoria asociada a un usuario existente`** => Se crea una categoria asociada a un user y se que comprueba el mensaje devuelto al ejecutar la funcion pertinente es correcto.
- **`Crear una categoria asociada a varios usuarios existentes`** => Se comprueba que la categoria se asocia correctamente a los dos usuarios.
- **`Crear una categoria asociada a un usuario inexistente`** => Se comprueba que devuelve el error pertinente.
- **`Crear una asociada a un usuario que ya la tenia asociada`** => Se comprueba que el error que devuelve es el esperado.
- **`Crear una tarea a un usuario en una categoria determinada`** => Se crea una tarea a un usuario en una categoria y se comprueba que el resultado obtenido es correcto.
- **`Crear una tarea a un usuario inexistente en una categoria determinada`** => Se comprueba que el error que devuelve es el esperado.
- **`Crear una tarea a un usuario en una categoria no asociada previamente`** => Se comprueba que el error que devuelve es el esperado.
- **`Crear una tarea a un usuario en una categoria determinada, con el formato de fecha incorrecto`** => Se comprueba que el error que devuelve es el esperado.
- **`Modificar una tarea de un usuario`** => Se modifica la tarea de un usuario en una determinada categoria y se comprueba que el resultado obetenido es el correcto.
- **`Modificar una tarea de un usuario inexistente`** => Se comprueba que el error que devuelve es correcto.
- **`Modificar una tarea de un usuario que no tiene asociada la categoria que se pasa por parametro`** => Se comprueba que el error que devuelve es correcto.
- **`Modificar una tarea de un usuario que no tiene asociada la categoria a la que se quiere modificar`** => Se comprueba que el error que devuelve es correcto.
- **`Modificar una tarea que no existe de un usuario en una categoria`** => Se comprueba que el error que devuelve es correcto.
- **`Modificar una tarea de un usuario con un formato de fecha incorrecto`** => Se comprueba que el error que devuelve es correcto.
- **`Listar tareas de un usuario dentro de una determinada categoria`** => Se obtiene el json con las tareas de un usuario en una categoria y se comprueba que el reslutado es correcto.
- **`Listar tareas de un usuario dentro de una determinada categoria (GET)`** => Igual que el test anterior pero desde GET.
- **`Listar tareas de un usuario dentro de una determinada categoria (No tiene tareas creadas)`** => Se comprueba que el error que devuelve es el esperado.
- **`Listar tareas de un usuario inexistente dentro de una determinada categoria`** => Se comprueba que el error que devuelve es el esperado
- **`Listar tareas de un usuario dentro de una determinada categoria que no tiene asociada`** => Se comprueba que el error que devuelve es el esperado

####Lista de funciones

La lista de funciones se ha dividido en 2 apartados, las funciones implementadas en la capa de **Controlador** y las funciones implementadas en la capa de **Modelo**.

#####Capa Modelo

- **`create_categoria(categoria: String)`** => El cometido de esta función es crear una categoria en la table 'categoria'.
- **`comprueba_categoria(categoria: String) : Long`** => El cometido de esta función es el de comprobar si una categoria determinada ya está creada en la tabla 'categoria'.
- **`create_categoria_user(login: String, categoria: String)`** => Crea una categoria en la tabla categoria, en caso de que no existiera y asocia dicha categoria con el usuario en al tabla user_categ.
- **`comprueba_categoria_user(login: String, categoria: String) : Long`** => Comprueba si existe la asociación del usuario y categoria pasados por parámetros.
- **`create_task_categoria(label: String,login: String,fecha: Date, categoria: String)`** => Crea una tarea con label, usuario,fecha y categoria, siempre que los parámetros sean correctos.
- **`modificar_task(id: Long, label: String, fecha: Date, categoria: String)`** => Modifica la tarea con el id pasado por parámetro con los valores de los parámetros del resto de campos .
- 
AHDLASHFKJDHFKJDSHFKJLSDHFKJLSDHF KJLSDFSDFSDF
SDF
SDF

- **`formatoFechaPost(fecha: String) : Boolean`** => Esta función se encarga de comprobar si el formato de la fecha introducida al hacer una petición **POST** es correcto, en cuyo caso devolvería true. El formato correcto para realizar una petición **POST** con fecha es (yyyy-MM-dd).
- **`formatoFecha(fecha: String) : Boolean`** => Esta función se encarga de comprobar si el formato de la fecha introducida al hacer una petición **GET** es correcto, en cuyo caso devolvería true. El formato correcto para realizar una petición **GET** con fecha es (dd-MM-yyyy).
- **`existeUser(login: String) : Long`** => Función que comprueba la existencia de un usuario en la Base de Datos mediante una consulta sql en la que se realiza un *count* del número de usuarios con ese nombre en la tabla *task_user*, en caso de devolver 0 significará que el usuario no existe.
- **`consultaId : Long`** => Esta función devuelve el id de la última tarea creada.
- **`create(label: String)`** => Inserta en la BDD una nueva tarea con la descripcion pasada por parámetro.
- **`create_user(label: String, login: String)`** => Inserta en la BDD una nueva tarea con descripcion y el usuario que la ha creado a partir de los parámetros.
- **`create_user_fecha(label: String, login: String, fecha: String)`** => Inserta en la BDD una nueva tarea con descripcion, usuario y fecha de terminación a partir de los parámetros.
- **`delete(id: Long)`** => Elimina la tarea con el id pasado como parámetro, en caso de que existiera.

###Capa Controlador

- **`index = Action`** => Accede a la Vista y muestra todas las tareas de la BDD en un taskForm. Se envia un codigo http 200 oK.  
```sh 
GET  /   controllers.Application.index
```
- **`consultaTask(id: Long) = Action`** => Esta funcion se encarga de mostrar la tarea pasada por parámetro en formato json. En caso de que no existiera el id de tarea devolvería un código 404 con el mensaje "No existe una tarea con ese id". En su cuerpo se realiza la invocación de la función de la capa de modelo `consultaTarea(id)` anteriormente descrita. Se envia un código http 200 oK.
```sh 
GET  /tasks/:id   controllers.Application.consultaTask(id: Long)
```
- **`tasks = Action`** => Se encarga de mostrar todas las tareas de la BDD creadas por el usuario anónimo *Magic* en formato json invocando a la función de la capa de modelo `all_magic` anteriormente descrita. Se envia un código 200 oK.
```sh 
GET  /tasks   controllers.Application.tasks
```
- **`consultaTaskUser(login: String) = Action`**=> Devuelve en formato json las tareas del usuario pasado como parámetro. Primero se comprueba que el usuario existe invocando a la función `existeUser(login)` y en el caso afirmativo se invoca a la función `all_user(login)` y se envia un código 200 oK. En caso de que el usuario pasado como parámetro no exista se envía un código 404 con el mensaje "El usuario no existe".
```sh 
GET  /:login/tasks  vcontrollers.Application.consultaTaskUser(login: String)
```
- **`consultaTaskUserFecha(login: String, fecha: String) = Action`**=> Devuelve en formato json las tareas del usuario pasado como parámetro con fecha de finalización igual que la pasada a la función. Se comprueba si el usuario existe mediante la función `existeUser(login)`y si el formato de la fecha es correcto mediante la función `formatoFecha(fecha)`, en caso afirmativo se parsea el String de fecha ,se invoca a la función `all_user_fecha(login,fechaAux)` y se envía un código 200 oK. En caso de que el usuario no exista o la fecha esté mal formateada se envía un 404 con el mensaje "El usuario no existe o la fecha está mal construida (dd-MM-yyyy).
```sh 
GET  /:login/tasks/:fecha   controllers.Application.consultaTaskUserFecha(login: String, fecha: String)
```
- **`consultaTaskUserFechaOrden(login: String, fecha: String) = Action`**=> Devuelve en formato json las tareas del usuario pasado como parámetro con fecha de finalización igual o mayor que la pasada a la función. Se comprueba si el usuario existe mediante la función `existeUser(login)`y si el formato de la fecha es correcto mediante la función `formatoFecha(fecha)`, en caso afirmativo se parsea el String de fecha ,se invoca a la función `all_user_fecha_orden(login,fechaAux)` y se envía un código 200 oK. En caso de que el usuario no exista o la fecha esté mal formateada se envía un 404 con el mensaje "El usuario no existe o la fecha está mal construida (dd-MM-yyyy).
```sh 
GET  /:login/tasks/:fecha/orden   controllers.Application.consultaTaskUserFechaOrden(login: String, fecha: String)
```
- **`newTaskUserFecha(login: String, fecha: String) = Action`** => Función que se utiliza para crear una nueva tarea para un usuario con una determinada fecha de finalización. En primer lugar se comprueba si la fecha esta bien formateada mediante la función `formatoFechaPost(fecha)`y si el usuario existe, en caso afirmativo se llama a la función `create_user_fecha(label,login,fecha)` anteriormente descrita en la capa modelo y luego se devuelve un json con los datos obtenido de la funcion `consultaTarea(id)` que recibe como parámetro la función `consultaId` que devolverá el id de la tarea recién creada. En caso de éxito se enviará un código 201 Created, en caso de fracaso se envia un 404 con el mensaje de error "El usuario no existe o el formato de la fecha es incorrecto (yyyy-MM-dd)".
```sh
POST  /:login/tasks/:fecha   controllers.Application.newTaskUserFecha(login: String, fecha: String)
```
- **`newTasUser(login: String) = Action`** => Función que se utiliza para crear una nueva tarea para un usuario. En su cuerpo se invoca a la función `create_user(label, login)` que creará en la BDD el nuevo registro y a continuación se devuelve un json con la información obtenida de la función `consultaTarea(id)` en la que se le pasa como parámetro el id de la tarea recién creada obtenido de la función `consultaId` y se envía un código 201 created. En caso de que el usuario no existiera se devolvería el código de error 404 con el mensaje "El usuario no existe".
```sh
POST  /:login/tasks   controllers.Application.newTaskUser(login: String)
```
- **`newTask = Action`** => Función que se utiliza para crear una nueva tarea al usuario anónimo Magic. Se invoca a la función `create(label)` y a continuación se envia un json junto con el código 201 created que con contiene el resultado obtenido al invocar la  función `consultaTask(id)` donde el id es el de la última tarea creada que se saca de la función `consultaId`.
```sh
POST  /tasks   controllers.Application.newTask
```
- **`deleteTask(id: Long) = Action`** => Elimina la tarea pasada como parámetro. En primer lugar se comprueba que la tarea existe en cuyo caso se invoca a `delete(id)` y se hace una redireccion a tasks. En caso de no poder eliminar la tarea se envia un código de error 404 con el mensaje "La tarea que intentas eliminar no existe".
```sh
DELETE  /tasks/:id                  controllers.Application.deleteTask(id: Long)
```