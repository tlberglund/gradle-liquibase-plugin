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

package com.augusttechgroup.gradle.liquibase.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.augusttechgroup.gradle.liquibase.Main
import com.augusttechgroup.gradle.liquibase.Database


class LiquibaseBaseTask extends DefaultTask {
  Database database
  def changeLogs
  def command
  def options = []

  @TaskAction
  def liquibaseAction() {

    if(database == null) {
      database = project.liquibase.defaultDatabase
    }

    if(changeLogs == null) {
      changeLogs = project.liquibase.changelogs
    }
    
    changeLogs.each { changeLog ->
      def args = [ 
        "--url=${database.url}", 
        "--password=${database.password}", 
        "--username=${database.username}",
        "--changeLogFile=${changeLog.file.absolutePath}"
      ]
      
      if(project.liquibase.context) {
        args += "--contexts=${project.liquibase.context}"
      }
      
      if (options) {
        args += options
      }

      if(command) {
        args += command
      }

      Main.main(args as String[])
    }
  }
}
