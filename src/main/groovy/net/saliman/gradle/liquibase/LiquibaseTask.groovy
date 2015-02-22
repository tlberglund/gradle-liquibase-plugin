/*
 * Copyright 2011-2014 Tim Berglund and Steven C. Saliman
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
 */

package net.saliman.gradle.liquibase

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import liquibase.integration.commandline.Main

/**
 * Gradle task that calls Liquibase to run a command.
 */
class LiquibaseTask extends DefaultTask {
	/**
	 * The Liquibase command to run.
	 */
	def command
	/**
	 * Whether or not the command needs a value, such as "tag" or "rollbackCount"
	 */
	def requiresValue = false

	@TaskAction
	def liquibaseAction() {

		def activities = project.liquibase.activities
		def runList = project.liquibase.runList

		if ( runList != null && runList.trim().size() > 0 ) {
			runList.split(',').each { activityName ->
				activityName = activityName.trim()
				def activity = activities.find { it.name == activityName }
				if ( activity == null ) {
					throw new RuntimeException("No activity named '${activityName}' is defined the liquibase configuration")
				}
				runLiquibase(activity)
			}
		} else
			activities.each { activity ->
				runLiquibase(activity)
			}
	}

	/**
	 * Build the proper command line and call Liquibase.
	 * @param activity the activity holding the Liquibase particulars.
	 */
	def runLiquibase(activity) {
		def args = []
		activity.arguments.each {
			args += "--${it.key}=${it.value}"
		}

		if ( command ) {
			args += command
		}


		if ( requiresValue ) {
			def value = project.properties.get("liquibaseCommandValue")

			// Special case for the dbDoc command.  This is the only command that
			// has a default value in the plugin.
			if ( !value && command == "dbDoc" ) {
				value = project.file("${project.buildDir}/database/docs")
			}

			if ( !value ) {
				throw new RuntimeException("The Liquibase '${command}' command requires a value")
			}
			args += value
		}

		activity.parameters.each {
			args += "-D${it.key}=${it.value}"
		}

		println "liquibase-plugin: Running the '${activity.name}' activity..."
		Main.run(args as String[])

	}
}
