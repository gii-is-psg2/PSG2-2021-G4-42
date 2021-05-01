SET SONAR_TOKEN=10faa48ecc4286b1fc55994d1d2c806cde697042
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dmaven.test.failure.ignore=true -Djacoco.haltonfailure=false