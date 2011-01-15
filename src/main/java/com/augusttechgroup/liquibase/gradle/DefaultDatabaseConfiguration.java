//
// com.augusttechgroup.liquibase.gradle
// Copyright (C) 2011 
// ALL RIGHTS RESERVED
//

package com.augusttechgroup.liquibase.gradle;

/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public class DefaultDatabaseConfiguration
  implements DatabaseConfiguration
{
  String driver;
  String url;
  String username;
  String password;

  public void driver(String driver) {
    this.driver = driver;
  }

  public void url(String url) {
    this.url = url;
  }

  public void username(String username) {
    this.username = username;
  }


  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void password(String password) {
    this.password = password;
  }
}