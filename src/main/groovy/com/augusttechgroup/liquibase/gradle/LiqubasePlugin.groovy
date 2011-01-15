
package com.augusttechgroup.liquibase.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin


class LiquibasePlugin
  implements Plugin<Project> {
    def void apply(Project project) {
      def liquibaseConvention = new LiquibaseConvention(project)
      project.convention.plugins.put('liquibase', liquibaseConvention)
    }
}
