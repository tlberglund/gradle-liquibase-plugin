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
    [
      'status': 'Outputs count (list if --verbose) of unrun change sets.',
			'validate': 'Checks the changelog for errors.',
			'changelogSync': 'Mark all changes as executed in the database.',
			'changelogSyncSQL': 'Writes SQL to mark all changes as executed in the database to STDOUT.',
      'listLocks': 'Lists who currently has locks on the database changelog.',
			'releaseLocks': 'Releases all locks on the database changelog.',
			'markNextChangesetRan': 'Mark the next change set as executed in the database.',
      'markNextChangesetRanSQL': 'Writes SQL to mark the next change set as executed in the database to STDOUT.',
			'dropAll': 'Drops all database objects owned by the user. Note that functions, procedures and packages are not dropped (limitation in 1.8.1).',
			'clearChecksums': 'Removes current checksums from database. On next run checksums will be recomputed.',
      'generateChangelog': 'generateChangeLog of the database to standard out. v1.8 requires the dataDir parameter currently.',
      'futureRollbackSQL': 'Writes SQL to roll back the database to the current state after the changes in the changeslog have been applied.',
			'updateTestingRollback': 'Updates the database, then rolls back changes before updating again.'
    ].each { taskName, taskDescription ->
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
	      description = taskDescription
      }
    }

    [
			'update': 'Updates the database to the current version.',
			'updateSQL': 'Writes SQL to update the database to the current version to STDOUT.'
    ].each { taskName, taskDescription ->
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
	      description = taskDescription
      }
    }

	  [
	    'updateCount': 'Applies the next <liquibase.count> change sets.',
			'updateCountSql' : 'Writes SQL to apply the next <liquibase.count> change sets to STDOUT.'
	  ].each { taskName, taskDescription ->
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
        options = [ System.properties['liquibase.count'] ]
	      description = taskDescription
      }
	  }

    project.task('rollback', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      if(System.properties['liquibase.count']) {
        command = 'rollbackCount'
        options = [ System.properties['liquibase.count'] ]
	      description = 'Rolls back the last <liquibase.count> change sets'
      }
      else if(System.properties['liquibase.date']) {
        command = 'rollbackDate'
        options = [ System.properties['liquibase.date'] ]
	      description = 'Rolls back the database to the state it was in at <liquibase.date>'
      }
      else {
        command = 'rollback'
        options = [ System.properties['liquibase.tag'] ]
	      description = 'Rolls back the database to the state it was in when <liquibase.tag> was applied.'
      }
    }

    project.task('rollbackSQL', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      if(System.properties['liquibase.count']) {
        command = 'rollbackCountSQL'
        options = [ System.properties['liquibase.count'] ]
	      description = 'Writes SQL to roll back the last <liquibase.count> change sets to STDOUT.'
      }
      else if(System.properties['liquibase.date']) {
        command = 'rollbackDateSQL'
        options = [ System.properties['liquibase.date'] ]
	      description = 'Writes SQL to roll back the database to the state it was in at <liquibase.date> to STDOUT.'
      }
      else {
        command = 'rollbackSQL'
        options = [ System.properties['liquibase.tag'] ]
	      description = 'Writes SQL to roll back the database to the state it was in when <liquibase.tag> was applied to STDOUT.'
      }
    }

    project.task('diff', type: LiquibaseDiffTask) {
      command = 'diff'
      group = 'Liquibase'
	    description = 'Writes description of differences to standard out.'
    }

    project.task('tag', type: LiquibaseBaseTask) {
      command = 'tag'
      group = 'Liquibase'
      options = [ "${System.properties['liquibase.tag']}" ]
	    description = 'Tags the current database state for future rollback'
    }

    project.task('dbDoc', type: LiquibaseDbDocTask) {
      command = 'dbDoc'
      group = 'Liquibase'
      docDir = project.file("${project.buildDir}/database/docs")
	    description = 'Generates Javadoc-like documentation based on current database and change log'
    }
  }

}

