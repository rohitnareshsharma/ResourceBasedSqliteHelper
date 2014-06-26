ResourceBasedSqliteHelper
=========================

Android Sqlite Open Helper Extension To Read Queries From File in Assets Folder

This is very handy in utilising the full ability of sqlite db layer in android apps.
As the syntax available with sqlite open helper will not allow you to easily 
create joins, triggers and other complex queries you would like to execute.

—How to use it
  
  - In the asset directory of your app put the db schema file.
  - Provide this file name to ResourceBasedSqliteHelper to create the db for you
  - Check the DbManager Class of our sample DbManager.init
  - Thats It