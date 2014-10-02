

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

# --- !Downs

DROP TABLE task_user;