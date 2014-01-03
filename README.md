Example
-------
For an example of how to use this plugin, see the [Liquibase Workshop](https://github.com/tlberglund/liquibase-workshop) repo. That project contains a `build.gradle` showing exactly how to configure the plugin, and an example directory setup as well.

News
----
*Note:* Version 1.0.0 of this plugin uses Liquibase 3 instead of Liquibase 2.
Before upgrading, we strongly recommend the following procedure for upgrading:

 1. Make sure all databases are up to date using the older version of the
    plugin by running ```gradle update``` on all databases.

 2. Create a new, throw away database and use the new plugin to run all of yor
    change sets by running ```gradle update``` on the new database.  This is
    because Liquibase 3 introduces some subtle differences in the way SQL is
    generated.  For example, adding a default value to a boolean column in
    MySql using ```defaultValue: "0"``` worked fine in Liquibase 2, but in
    Liquibase 3, it generates invalid SQL.  ```defaultValueNumeric: 0```
    needs to be used instead.

 3. When you are sure all the change sets are correct for Liquibase 3, clear
    all checksums calculated by Liquibase 2 by running ```gradle clearChecksums```
    in all databases.

 4. Finally, run ```gradle changeLogSync``` on all databases to calculate new
    checksums.

Version 0.7 and later of the Liquibase plugin changed the way the plugin is
configured in the build.gradle file.  Basically, the Liquibase configuration now
goes in a ```liquibase``` block, instead of separate blocks, and the
defaultChangeLogs variable has gone away.

For example:

```groovy
changelogs {
  main {
    file = file('src/main/db/changelogs.groovy')
  }
}

databases {
  myDB {
    url = 'jdbc:mysql://localhost:3306/my_db'
	username= 'myusername'
	password = 'mypassword'
  }
defaultDatabase = databases.myDB
defaultChangeLogs = changelogs
```

Became:

```groovy
liquibase {
  changelogs {
    main {
      file = file('src/main/db/changelogs.groovy')
    }
  }
  
  databases {
    myDB {
      url = 'jdbc:mysql://localhost:3306/my_db'
	  username= 'myusername'
	  password = 'mypassword'
    }
  defaultDatabase = databases.myDB
}
```

