#Práctica 1 - API REST
---

Esta práctica desarrollada en **Play Framework** se compone de 3 pequeños hitos o features que engloban una serie de funcionalidades que se han ido añadiendo progresivamente al sistema.
El código de partida es el obtenido tras concluir el tutorial de "mi primera aplicación en Play"  **play-todolist**.

A continuación se detalla una lista con las funciones implementadas en los diferentes features hasta la finalización de la práctica.

##Lista de funciones
---

La lista de funciones se ha dividido en 2 apartados, las funciones implementadas en la capa de **Controlador** y las funciones implementadas en la capa de **Modelo**.

###Capa Modelo

- **`consultaTarea(id: Long) : List[Task]`** => El cometido de esta función es devolver la tarea con el id pasado por parámetro en el caso de que existiera. Devuelve una lista de Tareas.
- **`all : List[Task]`** => Devuelve todas las tareas insertadas en la Base de Datos.
- **`all_user(login: String) : List[Task]`** => Devuelve las tareas creadas por el usuario pasado por parámetro.
- **`all_magic : List[Task]`** => Devuelve las tareas creadas por el usuario anónimo Magic ( que es el usuario por defecto en la BDD).
- **`all_user_fecha_orden(login: String, fecha: Date) : List[Task]`** => Devuelve las tareas creadas por un usuario con fecha de terminación igual o superior a la pasada como parámetro ordenadas por fecha.
- **`all_user_fecha(login: String, fecha: Date) : List[Task]`** => Devuelve las tareas creadas por un usuario con la fecha de terminación pasada por parámetro.
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