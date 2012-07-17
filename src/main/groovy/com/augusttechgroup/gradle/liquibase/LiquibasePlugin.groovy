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
      'status', 'validate', 'changelogSync', 'changelogSyncSQL',
      'listLocks', 'releaseLocks', 'markNextChangesetRan',
      'markNextChangesetRanSQL', 'dropAll', 'clearChecksums',
      'generateChangelog', 'changeLogSync',
      'futureRollbackSQL', 'updateTestingRollback'
    ].each { taskName ->
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
      }
    }

    [ 'update', 'updateSQL' ].each { taskName ->
      project.task(taskName, type: LiquibaseBaseTask) {
        group = 'Liquibase'
        command = taskName
      }
    }

    project.task('updateCount', type: LiquibaseBaseTask) {
      group = 'Liquibase'
      command = 'updateCount'
      options = [ System.properties['liquibase.count'] ]
    }

    project.task('rollback', type: LiquibaseBaseTask) {
      group = 'Liquibase'
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
      group = 'Liquibase'
    }

    project.task('tag', type: LiquibaseBaseTask) {
      command = 'tag'
      group = 'Liquibase'
      options = [ "${System.properties['liquibase.tag']}" ]
    }

    project.task('dbDoc', type: LiquibaseDbDocTask) {
      command = 'dbDoc'
      group = 'Liquibase'
      docDir = project.file("${project.buildDir}/database/docs")
    }
  }

}

