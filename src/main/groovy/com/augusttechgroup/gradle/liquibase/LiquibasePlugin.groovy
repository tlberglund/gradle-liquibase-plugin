/*
 * Copyright 2011 Tim Berglund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Tim Berglund
 * The August Technology Group
 * http://augusttechgroup.com/tim/about
 *
 */

package com.augusttechgroup.gradle.liquibase

import org.gradle.api.Project
import org.gradle.api.Plugin
import com.augusttechgroup.gradle.liquibase.tasks.LiquibaseBaseTask
import com.augusttechgroup.gradle.liquibase.tasks.LiquibaseDbDocTask
import com.augusttechgroup.gradle.liquibase.tasks.LiquibaseDiffTask


class LiquibasePlugin
  implements Plugin<Project> {


  void apply(Project project) {
    applyExtension(project)
    applyTasks(project)
  }


  void applyExtension(Project project) {
    def databases = project.container(Database) { name -> 
      new Database(name) 
    }
    def changelogs = project.container(ChangeLog) { name -> 
      new ChangeLog(name) 
    }
    project.configure(project) {
      extensions.create("liquibase", LiquibaseExtension, databases, changelogs)
    }
  }

  
  void applyTasks(Project project) {
    // LiquibaseBaseTasks that don't need input
    [
        ['update', 'Updates database to current version'],
        ['updateSQL', 'Writes SQL to update database to current version to STDOUTs'],
        ['status', 'Outputs count of unrun changesets'],
        ['validate', 'Checks changelog for errors'],
        ['changelogSync', 'Mark all changes as executed in the database'],
        ['changelogSyncSQL', 'Writes SQL to mark all changes as executed  in the database to STDOUT'],
        ['listLocks', 'Lists who currently has locks on the database changelog'],
        ['releaseLocks', 'Releases all locks on the database changelog'],
        ['markNextChangesetRan', 'Mark the next change changes as executed in the database'],
        ['markNextChangesetRanSQL', 'Writes SQL to mark the next change as executed in the database to STDOUT'],
        ['dropAll', 'Drop all database objects owned by user'],
        ['clearChecksums', 'Removes all saved checksums from database log. Useful for \'MD5Sum Check Failed\' errors'],
        ['generateChangelog', 'Writes Change Log XML to copy the current state of the database to STDOUT'],
        ['futureRollbackSQL', 'Writes SQL to roll back the database to the current state after the changes in the changeslog have been applied'],
        ['updateTestingRollback', 'Updates database, then rolls back changes before updating again. Useful for testing rollback support']
    ].each { item ->
      def (taskName, desc) = item
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
        description = desc
      }
    }

    // Tasks that need input or are implemented as a specialized Task
    project.task('updateCount', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      command = 'updateCount'
      description = 'Applies next NUM changes to the database (specified with -Dliquibase.count=NUM)'
      options = [ System.properties['liquibase.count'] ]
    }

    project.task('rollback', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      description = 'Rolls back the database to a previous state according to supplied parameter.' +
                    '\n\tSet -Dliquibase.count=NUM to roll back the last NUM change sets' +
                    '\n\tSet -Dliquibase.date=DATE/TIME to roll back to the given date/time. Format: yyyy-MM-dd\'T\'HH:mm:ss' +
                    '\n\tSet -Dliquibase.tag=TAG to roll back to the given tag'
      if(System.properties['liquibase.count']) {
        command = 'rollbackCount'
        options = [ System.properties['liquibase.count'] ]
      }
      else if(System.properties['liquibase.date']) {
        command = 'rollbackDate'
        options = [ System.properties['liquibase.date'] ]
      }
      else {
        command = 'rollback'
        options = [ System.properties['liquibase.tag'] ]
      }
    }

    project.task('rollbackSQL', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      description = 'Writes SQL to STDOUT to roll back the database to a previous state according to supplied parameter.' +
                    '\n\tFor valid parameters check the \'rollback\' task'
      if(System.properties['liquibase.count']) {
        command = 'rollbackCountSQL'
        options = [ System.properties['liquibase.count'] ]
      }
      else if(System.properties['liquibase.date']) {
        command = 'rollbackDateSQL'
        options = [ System.properties['liquibase.date'] ]
      }
      else {
        command = 'rollbackSQL'
        options = [ System.properties['liquibase.tag'] ]
      }
    }

    project.task('diff', type: LiquibaseDiffTask) {
      command = 'diff'
      description = 'Writes description of differences to STDOUT'
      group = 'Liquibase'
    }

    project.task('tag', type: LiquibaseBaseTask) {
      command = 'tag'
      group = 'Liquibase'
      description = '\'Tags\' the current database state for future rollback (the TAG is specified via -Dliquibase.tag=TAG)'
      options = [ "${System.properties['liquibase.tag']}" ]
    }

    project.task('dbDoc', type: LiquibaseDbDocTask) {
      command = 'dbDoc'
      group = 'Liquibase'
      description = 'Generates Javadoc-like documentation based on current database and change log (written to ${project.buildDir}/database/docs)'
      docDir = project.file("${project.buildDir}/database/docs")
    }
  }

}

