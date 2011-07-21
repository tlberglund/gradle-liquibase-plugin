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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import liquibase.integration.commandline.Main


class LiquibaseBaseTask extends DefaultTask {
  Database database
  def changeLogFile
  def command
  def options = []

  @TaskAction
  def liquibaseAction() {

    if(database == null) {
      database = project.workingDatabase
    }

    if(changeLogFile == null) {
      changeLogFile = project.changelogs.main.file
    }

    def args = [ "--url=${database.url}", "--password=${database.password}", "--username=${database.username}" ]

    if(changeLogFile) {
      args += "--changeLogFile=${changeLogFile.absolutePath}"
    }

    if(project.context) {
      args += "--contexts=${project.context}"
    }

    if(command) {
      args += command
    }

    args += (options ? options : [])

    Main.main(args as String[])
  }
}
