

# Update feature 3

# --- !Ups
 
 ALTER TABLE task ADD fecha Date;         

 INSERT INTO task (label,usuario,fecha) VALUES('Prueba de tarea con fecha por Magic 08-10-2014','Magic','2014-10-08');
 INSERT INTO task (label,usuario,fecha) VALUES('Prueba de tarea con fecha por Magic 14-10-2014','Magic','2014-10-14');

# --- !Downs

DROP TABLE task;
DROP TABLE task_user;