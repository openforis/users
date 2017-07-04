# of-users

## INSTALLATION

## Generate JOOQ sources from command line
The JOOQ codegen maven plugin is used to generate basic pojos and daos. In order to use it you must use the profile `code-gen` and execute the goal `generate-sources`.  
From command line you can run:

```bash
mvn generate-sources -Pcode-gen
```

## CONFIGURATION

### Tomcat

1. Add the following library inside the lib path: [h2-1.4.193.jar](http://repo2.maven.org/maven2/com/h2database/h2/1.4.193/h2-1.4.193.jar)

2. Add the following line inside the tag GlobalNamingResources of server.xml:

```xml
<Resource auth="Container" driverClassName="org.h2.Driver" factory="org.apache.tomcat.jdbc.pool.DataSourceFactory" name="jdbc/of-users-ds" type="javax.sql.DataSource" url="jdbc:h2:${user.home}/dev/temp/of-suite/data/users"/>
```
