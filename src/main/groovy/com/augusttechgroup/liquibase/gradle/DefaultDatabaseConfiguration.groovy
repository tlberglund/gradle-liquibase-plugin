//
// com.augusttechgroup.liquibase.gradle
// Copyright (C) 2011 
// ALL RIGHTS RESERVED
//

package com.augusttechgroup.liquibase.gradle

/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
class DefaultDatabaseConfiguration
  implements DatabaseConfiguration
{
  String driver
  String url
  String username
  String password

  def void driver(String driver) {
    this.driver = driver
  }

  def void url(String url) {
    this.url = url
  }

  def void username(String username) {
    this.username = username
  }

  def void password(String password) {
    this.password = password
  }
}