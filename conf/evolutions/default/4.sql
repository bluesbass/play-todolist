

# Update feature 4

# --- !Ups
 
 CREATE TABLE categoria (
   nombre varchar(50) PRIMARY KEY
);

ALTER TABLE task ADD  categoria varchar(50);
ALTER TABLE task ADD FOREIGN KEY(categoria) REFERENCES categoria(nombre);


# --- !Downs

DROP TABLE categoria;
DROP TABLE task;