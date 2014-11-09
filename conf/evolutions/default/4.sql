

# Update feature 4

# --- !Ups
 
 CREATE TABLE categoria (
   nombre varchar(50) PRIMARY KEY
);

 CREATE TABLE user_categ (
   usuario varchar(50), 
   categoria varchar(50),
   PRIMARY KEY(usuario,categoria) 
);

ALTER TABLE user_categ ADD FOREIGN KEY(usuario) REFERENCES task_user(usuario);
ALTER TABLE user_categ ADD FOREIGN KEY(categoria) REFERENCES categoria(nombre);

ALTER TABLE task ADD  categoria varchar(50);
ALTER TABLE task ADD FOREIGN KEY(categoria) REFERENCES categoria(nombre);

# --- !Downs

DROP TABLE categoria;
DROP TABLE task;