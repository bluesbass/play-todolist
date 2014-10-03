

# Update feature 2

# --- !Ups

CREATE TABLE task_user (
   usuario varchar(50) PRIMARY KEY
);

 
 ALTER TABLE task ADD  usuario varchar(50);
 ALTER TABLE task ADD FOREIGN KEY(usuario) REFERENCES task_user(usuario);
         
   

 INSERT INTO task_user VALUES('Magic');
 INSERT INTO task_user VALUES('Domingo');
 INSERT INTO task_user VALUES('Jesus');
 INSERT INTO task (label,usuario) VALUES('Prueba de tarea creada por Magic','Magic');
 INSERT INTO task (label,usuario) VALUES('Prueba 2 de tarea creada por Magic','Magic');
 INSERT INTO task (label,usuario) VALUES('Prueba de tarea creada por Jesus','Jesus');

# --- !Downs

DROP TABLE task_user;