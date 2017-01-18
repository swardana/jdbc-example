# JDBC Example
Show how to connect into database and perform simple insert, update, delete, and read data from and
into database.

## Environment

### Database
To be able running this project first step is to setup the database, in this project used mariaDB
database. Database was named `demo` with username: `demo`, password: `demo`, to make it simple
run command below:

```
CREATE DATABASE IF NOT EXISTS demo;
ALTER DATABASE demo DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON demo.* TO demo@localhost IDENTIFIED BY 'demo';
```

### Schema & Data
DDL schema and data was stored in `src/main/resources/db`, execute command below from project directory

```
mysql --user="demo" --password="demo" --database="demo" < src/main/resources/db/schema.sql
mysql --user="demo" --password="demo" --database="demo" < src/main/resources/db/data.sql
```

## References
* [Java Connect to Database](http://stackoverflow.com/a/30826155)
* [Java Logging](http://stackoverflow.com/a/16448421)
* [DataSource vs DriverManager](http://zetcode.com/tutorials/jeetutorials/datasource/)