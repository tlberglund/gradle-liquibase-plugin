Example
-------
For an example of how to use this plugin, see the
[Liquibase Workshop](https://github.com/tlberglund/liquibase-workshop) repo.
That project contains a `build.gradle` showing exactly how to configure the
plugin, and an example directory setup as well.

News
----
Version 1.0.0 is under development, and we hope to have a release by the middle
of February.  Until then, use caution when building and using a 1.0.0 snapshot
because there are still a few things that need to be changed, and there could be
some breaking changes.
**IMPORTANT NOTE FOR USERS UPGRADING FROM A PRE 1.0.0 RELEASE OF THE GROOVY DSL:**

Version 1.0.0 of the Liquibase plugin uses Liquibase 3, instead of Liquibase
2, and several things have been deprecated from the Groovy DSL to maintain
compatibility with Liquibase XML. A list of deprecated items can be found in the
*Usage* sectionof the Groovy DSL project.  To upgrade to version 1.0.0, we
strongly recommend the following procedure:

1. Make sure all of your Liquibase managed databases are up to date by running
   ```gradle update``` on them *before upgrading the Liquibase plugin*.

2. Create a new, throw away database and test your Liquibase change sets by
   running ```gradle update``` on this new database with the latest version of
   the Liquibase plugin.  This is important because of the deprecated items in
   the Groovy DSL, and because there are some subtle differences in the ways
   the different Liquibase versions generate SQL.  For example, adding a default
   value to a boolean column in MySql using ```defaultValue: "0"``` worked fine
   in Liquibase 2, but in Liquibase 3, it generates SQL that doesn't work for
   MySql; ```defaultValueNumeric: 0``` needs to be used instead.  Here is a tip
   to help find deprecation warnings: redirect stderr to a file.  Most of what
   Liquibase tells you is on stderr, but not deprecation warnings.

3. Once you are sure all of your change sets work with the latest Liquibase
   plugin and Liquibase 3, clear all checksums that were calculated by Liquibase
   2 by running ```gradle clearChecksums``` against all databases.

4. Finally, run ```gradle changeLogSync``` on all databases to calculate new
   checksums.

Version 0.7 and later of the Liquibase plugin changed the way the plugin is
configured in the build.gradle file, and version 1.0.0 changes it even further.
Basically, the Liquibase configuration now goes in a ```liquibase``` block,
instead of separate blocks, and the ```changelogs``` and ```database``` closures
have been merged into a single ```activities``` closure with methods instead of
variables.  The ```defaultDatabase``` and ```defaultChangelogs``` variables have been replaced
with the optional ```runList``` variable.

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
  activities {
    main {
      changeLogFile 'src/main/db/changelogs.groovy'
      url 'jdbc:mysql://localhost:3306/my_db'
	  username 'myusername'
	  password 'mypassword'
    }
  }
}
```

## Usage
The Liquibase plugin uses the Groovy DSL syntax intended to mirror the Liquibase
XML syntax directly, such that mapping elements and attributes from the
Liquibase documentation to Groovy builder syntax will result in a valid
changelog. Hence this DSL is not documented separately from the Liquibase XML
format.  However there are some minor differences or enhancements to the XML
format, and there are some gaping holes in Liquibase's documentation of the XML.
Those holes are filled, and differences explained in the documentation for the
[Groovy Liquibase DSL](https://github.com/tlberglund/groovy-liquibase) project
page.

When the Liquibase plugin is applied, it creates a Gradle task for every command
Liquibase supports.  See the
[Liquibase Documentation](http://www.liquibase.org/documentation/command_line.html)
For details on what commands are available, and the parameters they can take.
The specific behavior of a Gradle Liquibase task configured in a ```liquibase```
block inside the build.gradle file.  This block contains a series of,
"activities", each defining a series of Liquibase parameters.  It also has a
"runList", which determines which activities are run for each task.  If no
runList is defined, the Liquibase Plugin will run all the activities.  NOTE:
the order of execution when there is no runList is not guaranteed.

*Example:*

Let's suppose that for each deployment, you need to update the data model for
your application's database, and wou also need to run some SQL statements
in a separate database used for security.  The ```liquibase``` block might
look like this:

```groovy
liquibase {
  activities {
    main {
      changeLogFile 'src/main/db/main.groovy'
      url project.ext.mainUrl
      username project.ext.mainUsername
      password project.ext.mainPassword
    }
    security {
      changeLogFile 'src/main/db/security.groovy'
      url project.ext.securityUrl
      username project.ext.securityUsername
      password project.ext.mainPassword
     }
  }
  runList = project.ext.runList
}
```

Some things to keep in mind when setting up the ```liquibase``` block:

1. We only need one activity for each type of activity.  In the example above,
   the database credentials are driven by build properties so that the correct
   database can be specified at build time so that you don't need a separate
   activity for each database.

2. By making the value of ```runList``` a property, you can determine the
   activities that get run at build time.  For example, if you didn't need to
   run the security updates in the CI environment, you could type
   ```gradle update -PrunList=main``` For environments where you do need the
   security updates, you would use ```gradle update -PrunList='main,security'```
   This use of properties is the reason the runList is a string and not an array.

3. The methods in each activity are meant to be pass-throughs to Liquibase.
   Any valid Liquibase command parameter is a legal method here.  For example,
   if you wanted to increase the log level, you could add ```logLevel debug```
   to the activity.

4. In addition to the command pass-through methods of an activity, there is a
   ```changeLogParameters``` method.  This method takes a map, and is used to
   setup token substitution in the changeLogs.  See the Liquibase documentation
   for more details on token substitution.

5. Some Liquibase commands like ```tag``` and ```rollback``` require a value,
   in this case a tag name.  Since the value will likely change from run to run,
   the command value is not configured in the ```liquibase``` block.  To supply
   a command value, add ```-PlilquibaseCommandValue=<value>``` to the gradle
   command.

