Changes for 1.2.0
================
- Updated the DSL to support most of Liquibase 3.4.2 (Issue #4 and Issue #6)

Changes for 1.1.1
=================
- Added support for Liquibase 3.3.5

- Fixed the task descriptions to correctly identify the property that is used
  to pass values to commands (Issue #2)
  
Changes for 1.1.0
=================
- Refactored the project to fit into the Liquibase organization.

Changes for 1.0.2
=================
- Bumped the dependency on the Groovy DSL to a version that works with Java
  versions before JKD8 (Issue #27)

Changes for 1.0.1
=================
- Added support for prefixes for liquibase task names (Issue #20)

- Added support for Liquibase 3.3.2.

- Fixed the ```status``` and ```unexpectedChangeSets``` commands to support the
  ```--verbose``` command value.
