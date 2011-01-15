//
// com.augusttechgroup.liquibase.gradle
// Copyright (C) 2011 
// ALL RIGHTS RESERVED
//

package com.augusttechgroup.liquibase.gradle

import org.gradle.api.Project
import org.gradle.api.internal.ClassGenerator
import com.augusttechgropu.liquibase.gradle.DefaultDatabaseConfigurationContainer
import com.augusttechgropu.liquibase.gradle.DatabaseConfigurationContainer


/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public class LiquibaseConvention
{
  DatabaseConfigurationContainer databases

  LiquibaseConvention(Project project) {
    def classGenerator = project.services.get(ClassGenerator)
    databases = classGenerator.newInstance(DefaultDatabaseConfigurationContainer.class, project.fileResolver, classGenerator)
  }

}