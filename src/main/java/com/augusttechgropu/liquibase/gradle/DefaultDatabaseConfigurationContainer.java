//
// com.augusttechgroup.liquibase.gradle
// Copyright (C) 2011 
// ALL RIGHTS RESERVED
//

package com.augusttechgropu.liquibase.gradle;

import org.gradle.api.internal.AutoCreateDomainObjectContainer;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.ClassGenerator;

import com.augusttechgroup.liquibase.gradle.DatabaseConfiguration;
import com.augusttechgroup.liquibase.gradle.DefaultDatabaseConfiguration;

/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public class DefaultDatabaseConfigurationContainer
  extends AutoCreateDomainObjectContainer<DatabaseConfiguration>
  implements DatabaseConfigurationContainer
{
  FileResolver fileResolver;
  ClassGenerator classGenerator;


  public DefaultDatabaseConfigurationContainer(FileResolver fileResolver, ClassGenerator classGenerator) {
    super(DatabaseConfiguration.class, classGenerator);
    this.fileResolver = fileResolver;
    this.classGenerator = classGenerator;
  }

  
  protected DatabaseConfiguration create(String s) {
    return new DefaultDatabaseConfiguration();
  }

}