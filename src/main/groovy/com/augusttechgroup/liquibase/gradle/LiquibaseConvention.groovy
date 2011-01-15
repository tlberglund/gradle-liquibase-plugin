
package com.augusttechgroup.liquibase.gradle

import org.gradle.api.Project
import org.gradle.api.internal.ClassGenerator


/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public class LiquibaseConvention
{
  DatabaseConfigurationContainer databases

  public LiquibaseConvention(Project project) {
    def classGenerator = project.services.get(ClassGenerator)
    databases = classGenerator.newInstance(DefaultDatabaseConfigurationContainer.class, project.fileResolver, classGenerator)
  }
}