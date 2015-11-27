Liquibase Gradle Plugin
-----------------------
A plugin for [Gradle](http://gradle.org) that allows you to use [Liquibase]
(http://liquibase.org) to manage your database upgrades.  This project was 
created by Tim Berglund, and is currently maintained by Steve Saliman.

Usage
-----
Build script snippet for use in all Gradle versions:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.liquibase:liquibase-gradle-plugin:1.2.0"
    }
}
apply plugin: 'org.liquibase.gradle'
```

Build script snippet for the new, plugin mechanism introduced in Gradle 2.1:

```groovy
plugins {
  id 'org.liquibase.gradle' version '1.2.0'
}
```


Example
-------
For an example of how to configure and use this plugin, see the
[Liquibase Workshop](https://github.com/stevesaliman/liquibase-workshop) repo.
That project contains a `build.gradle` showing exactly how to configure the
plugin, and an example directory setup as well.

News
----
###November 30, 2015
The plugin has been updated to support Liquibase 3.4.2.

###August 3, 2015
The plugin has been updated to support Liquibase 3.3.5.

###May 16, 2015
We are proud to announce that the Liquibase Gradle Plugin is now a part of the 
Liquibase organization.  I will continue maintain the code, but bringing this 
project into the Liquibase organization will help keep all things Liquibase 
together in one place.  This will help promote Liquibase adoption by making it
easier for more people to use, and it will help people stay up to date with the
latest releases. As part of that move, the artifact name has changed from 
```net.saliman:gradle-liquibase-plugin``` to
```org.liquibase:liquibase-gradle-plugin``` to be consistent with the rest of the
Liquibase artifacts.  Also, starting with plugin version 1.1.0, the plugin is 
available via the official Gradle plugin portal.  A special thank you to Nathan
Voxland for his help and support in bringing the Liquibase project and the 
Groovy Plugin together into one home.

###March 9, 2015
The plugin has been updated to support Liquibase 3.3.2. Version 1.0.2 fixes a
bug that prevented Groovy changelogs from working in Java versions before JDK 8.
As part of this release, I've bumped the version of Groovy that the plugin uses.
This can cause issues in Gradle 1.x.  The workaround is to add
```classpath 'org.codehaus.groovy:groovy-backports-compat23:2.3.5'``` to the
buildscript dependencies.


###September 10, 2014
This plugin is designed to be a wrapper for the Liquibase project, so it 
creates tasks to match the various Liquibase commands.  This can cause conflicts
with tasks that other plugins create, so we've added the ability to add a 
prefix to all the tasks this plugin creates.  See the Usage section for more
details.

###June 15, 2014
We are proud to announce the long awaited release of version 1.0.0 of the 
Gradle Liquibase Plugin. Version 1.0.0 uses version the latest release of 
Liquibase (3.1.1), and it appears to work fine with both Gradle 1.x releases as
well as the upcoming Gradle 2.0 release.

Tim Berglund has asked me to take on the continued maintenance of this plugin,
so I've had to change the maven group ID to one for which I have permission to 
publish on Maven Central.  Going forward, this plugin will be available under 
the ```net.saliman``` group id.  The artifact ID, ```gradle-liquibase-plugin```
will remain the same.  

My thanks to Tim for the opportunity to help out with this great plugin.
 
Steve Saliman

Version 1.0.0 of the Liquibase plugin uses Liquibase 3, instead of Liquibase
2, and several things have been deprecated from the Groovy DSL to maintain
compatibility with Liquibase XML. A list of deprecated items can be found in
the README for the [Groovy DSL project](https://github.com/tlberglund/groovy-liquibase)
in the *Usage* section.  To upgrade to version 1.0.0, we strongly recommend the
following procedure:

1. Make sure all of your Liquibase managed databases are up to date by running
   ```gradle update``` on them *before upgrading to version 1.0.0 of the
   Liquibase plugin*.

2. Create a new, throw away database to test your Liquibase change sets.  Run
   ```gradle update``` on the new database using the latest version of
   the Liquibase plugin.  This is important because of the deprecated items in
   the Groovy DSL, and because there are some subtle differences in the ways
   the different Liquibase versions generate SQL.  For example, adding a default
   value to a boolean column in MySql using ```defaultValue: "0"``` worked fine
   in Liquibase 2, but in Liquibase 3, it generates SQL that doesn't work for
   MySql - ```defaultValueNumeric: 0``` needs to be used instead.

3. Once you are sure all of your change sets work with the latest Liquibase
   plugin, clear all checksums that were calculated by Liquibase 2 by running
   ```gradle clearChecksums``` against all databases.

4. Finally, run ```gradle changeLogSync``` on all databases to calculate new
   checksums.

Configuring the plugin in version 1.0.0 is different from previous versions.
The Liquibase configuration now goes in a ```liquibase``` block of the
build.gradle file instead of separate blocks. The ```changelogs``` and
```database``` closures have been merged into a single ```activities``` closure
with methods instead of variables.  The ```defaultDatabase``` and
```defaultChangelogs``` variables have been replaced with the optional
```runList``` variable.

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
The Liquibase plugin allows you to use Liquibase to manage database updates.
You can parse changesets using any Liquibase parser that is in the classpath.
Some parsers, such as the XML parser and the YAML parser, are part of Liquiabse
itself, although some parsers require you to add additional dependencies to the
buildscript.  For example, the YAML parser requires ```org.yaml:snakeyaml:1.15```.

This plugin adds support for the the Groovy DSL, which is a much nicer way to 
write changelogs, especially since Groovy is the language of Gradle scripts 
themselves.  The Groovy DSL syntax intended to mirror the Liquibase XML syntax
directly, such that mapping elements and attributes from the Liquibase
documentation to Groovy builder syntax will result in a valid changelog. Hence
this DSL is not documented separately from the Liquibase XML format.  However
there are some minor differences or enhancements to the XML format, and there
are some gaping holes in Liquibase's documentation of the XML. Those holes are
filled, and differences explained in the documentation on the
[Groovy Liquibase DSL](https://github.com/liquibase/liquibase-groovy-dsl) 
project page.  To use the Groovy DSL, simply specify a ```changeLogFile``` that
ends in .groovy.  For those who, for some reason, still prefer XML, JSON, or
Yaml, you can use these formats by specifying a ```changeLogFile``` that ends
in the appropriate extension, and Liquibase will find and use the correct 
parser.

The Liquibase plugin is meant to be a light weight font end for the Liquibase
command line utility.  When the liquibase plugin is applied, it creates a
Gradle task for each command supported by Liquibase. ```gradle tasks``` will
list out these tasks.  The
[Liquibase Documentation](http://www.liquibase.org/documentation/command_line.html)
describes what each command does and what parameters each command uses.  If you
want to prefix each task to avoid task name conflicts, set a value for the 
```liquibaseTaskPrefix``` property.  This will tell the liquibase plugin to 
capitalize the task name and prefix it with the given prefix.  For example,
if Gradle is invoked with ```-PliquibaseTaskPrefix=liquibase```, or you put
```liquibaseTaskPrefix=liquibase``` in ```gradle.properties``` then this 
plugin will create tasks named ```liquibaseUpdate```, ```liquibaseTag```, etc.

Parameters for the commands are configured in the ```liquibase``` block inside
the build.gradle file.  This block contains a series of, "activities", each
defining a series of Liquibase parameters.  Any method in an "activity" is
assumed to be a Liquibase parameter.  The ```liquibase``` block also has an
optional "runList", which determines which activities are run for each task.  If
no runList is defined, the Liquibase Plugin will run all the activities.  NOTE:
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
      password project.ext.securityPassword
     }
  }
  runList = project.ext.runList
}
```

Some things to keep in mind when setting up the ```liquibase``` block:

1. We only need one activity block for each type of activity.  In the example 
   above, the database credentials are driven by build properties so that the
   correct database can be specified at build time so that you don't need a
   separate activity for each database.

2. By making the value of ```runList``` a property, you can determine the
   activities that get run at build time.  For example, if you didn't need to
   run the security updates in the CI environment, you could type
   ```gradle update -PrunList=main``` For environments where you do need the
   security updates, you would use ```gradle update -PrunList='main,security'```
   This use of properties is the reason the runList is a string and not an array.

3. The methods in each activity block are meant to be pass-throughs to Liquibase.
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
   a command value, add ```-PliquibaseCommandValue=<value>``` to the gradle
   command.
   
