## **alternative-mongodb-client** ##

**Dependencies**
-
* java: v: 1.8 or higher.
* mongodb: v: 3.4.3.
* maven: v: 3.3.9.

**Installing**
-
1. git clone https://github.com/max40a/mongo-client
2. cd folder_of_project
3. mvn -Dmaven.test.skip=true clean install

**Run application**
-
1. cd target
2. java -jar .\alternative-mongo-client.jar.

**Run Tests**
-
1. For run all the test should be installed the mongo database.
2. cd target\classes\database.properties - _set the URL, test database name and test database collection name_
3. mvn test

**Usage**
-
1. When the application is starting, input full URI (`it's necessary to correct application works`) to the required database. 
    Examples:  
    * -url mongodb://localhost:27017/test
    * -u mongodb://localhost:27017/test
    * where : 
        * mongodb:// - protocol name
        * localhost:27017 - hostname and port of database
        * /test - the name of some mongo database
2. Enter SQL use command keys: -q __query__ or --query __query__.
    Examples of input SQL query:
    * -q SELECT * FROM testDb
    * -query SELECT a, b, d FROM testDb
3. If you need to see a current database URI, input one of the commands: 
    * -c
    * -current-db-url
4. Show a help menu:
    * -h
    * -help
5. Quit application:
    * -e
    * -exit    
    
***Supported query format***
-
    SELECT [<Projections>] [ FROM <Target>]
    [ WHERE <Condition>]
    [ ORDER BY <Fields> [ ASC | DESC ]]
    [ SKIP <SkipRecords>]
    [ LIMIT <MaxRecords>]
        
***SQL Commands examples***
-
    SELECT * FROM db
    SELECT id, title FROM db WHERE id > 3 AND title = Test tile
    SELECT * FROM db WHERE id > 2 OR title <> Test title ORDER BY id ASC title DESC SKIP 10 LIMIT 10
 
***Limitations*** 
-
* List of base words: SELECT, FROM, WHERE, ORDER BY, SKIP, LIMIT.
* Base words should be in upper case.
* Base words must be used only one time.
* Support only SELECT query.
* Words AND, OR, DESC, ASC can be used as many times as necessary, but only the right query context.
* Each a word or a symbol in query should be separated by space.
* Supports only field query(*, name, id etc.).