//
// com.augusttechgroup.liquibase.gradle
// Copyright (C) 2011
// ALL RIGHTS RESERVED
//

package com.augusttechgropu.liquibase.gradle;

import groovy.lang.Closure;
import org.gradle.api.NamedDomainObjectCollection;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.InvalidUserDataException;
import com.augusttechgroup.liquibase.gradle.DatabaseConfiguration;

/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public interface DatabaseConfigurationContainer
  extends NamedDomainObjectContainer<DatabaseConfiguration>,
          NamedDomainObjectCollection<DatabaseConfiguration> {

  public DatabaseConfiguration add(String name) throws InvalidUserDataException;

  public DatabaseConfiguration add(String name, Closure configureClosure) throws InvalidUserDataException;
}