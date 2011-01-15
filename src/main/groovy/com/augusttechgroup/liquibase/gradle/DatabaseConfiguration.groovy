
package com.augusttechgroup.liquibase.gradle


/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
interface DatabaseConfiguration {

  void driver(String driver)
  void url(String url)
  void username(String username)
  void password(String password)

  void setUrl(String url)
  String getUrl()

  void setDriver(String driver)
  String getDriver()

  void setUsername(String username)
  String getUsername()

  void setPassword(String password)
  String getPassword()
}
