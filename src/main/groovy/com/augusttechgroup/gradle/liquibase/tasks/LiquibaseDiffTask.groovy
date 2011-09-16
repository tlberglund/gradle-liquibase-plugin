/*
 * Copyright 2011 Tim Berglund
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Tim Berglund
 *  The August Technology Group
 *  http://augusttechgroup.com/tim/about
 */

package com.augusttechgroup.gradle.liquibase.tasks

import com.augusttechgroup.gradle.liquibase.Database

/**
 * <p></p>
 * 
 * @author Tim Berglund
 */
class LiquibaseDiffTask
  extends LiquibaseBaseTask
{
  Database referenceDatabase

  void setReferenceDatabase(Database referenceDatabase) {
    this.referenceDatabase = referenceDatabase
    options = [ "--referenceUrl=${referenceDatabase.url}", "--referenceUsername=${referenceDatabase.username}", "--referencePassword=${referenceDatabase.password}", 'diff' ]
  }
}