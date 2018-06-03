# family-graph
Contains Graph Based Data Structure To Store Family Graph. 

The data is persisted into sqlite database. 

Cache:

For performance, data is cached into an in-memory graph on first request, thereby avoiding database calls for
all read based operations. When user adds member or relation, the data is saved into database
and cache is updated with new record.

Logging:

Logging is enabled using logback. A log file "family.log" is created at the root level of utility.



        Build
          To build project - run "gradlew clean build" at the root level.
          
          To build fatjar containing all dependencies - run "gradlew fatjar". 
          It shall create executable jar that can be used for console based application.
          
          The jar family-all-1.0.jar is created under the build/libs folder.
