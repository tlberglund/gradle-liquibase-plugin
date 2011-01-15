
package com.augusttechgroup.liquibase.gradle;


/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
public interface DatabaseConfiguration {

  public void driver(String driver);
  public void url(String url);
  public void username(String username);
  public void password(String password);

  public void setUrl(String url);
  public String getUrl();

  public void setDriver(String driver);
  public String getDriver();

  public void setUsername(String username);
  public String getUsername();

  public void setPassword(String password);
  public String getPassword();
}
