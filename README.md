Example
-------
For an example of how to use this plugin, see the [Liquibase Workshop](https://github.com/tlberglund/liquibase-workshop) repo. That project contains a `build.gradle` showing exactly how to configure the plugin, and an example directory setup as well.

News
----
Version 1.0.0 is under development, and we hope to have a release by the end of
January.  Until then, use caution when building and using a 1.0.0 snapshot
because there are still a few things that need to be changed, and there could be
some breaking changes.
**IMPORTANT NOTE FOR USERS UPGRADING FROM A PRE 1.0.0 RELEASE OF THE GROOVY DSL:**

Version 1.0.0 of the Liquibase plugin uses Liquibase 3, instead of Liquibase
2, and several things have been deprecated from the Groovy DSL to maintain
compatibility with Liquibase XML. A list of deprecated items can be found in the
*Usage* section.  To upgrade to version 1.0.0, we strongly recommend the
following procedure:

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

