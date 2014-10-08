

# Update feature 3

# --- !Ups
 
 ALTER TABLE task ADD fecha varchar(50) default  'Fecha no establecida';         

 INSERT INTO task (label,usuario,fecha) VALUES('Prueba de tarea con fecha por Magic','Magic','08-10-2014');


# --- !Downs

DROP TABLE task;
DROP TABLE task_user;