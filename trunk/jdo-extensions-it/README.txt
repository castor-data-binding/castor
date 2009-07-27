Castor JDO extension integration tests

File:    README.txt
Version: 1.3-SNAPSHOT

As we don't generate the .classpath file with Maven2 yet, 
you need to add various libraries manually to run the 
integration tests properly. These are as follows:

    <classpathentry kind="src" path="jdo-extensions-it/src/test/java"/>
    <classpathentry kind="src" path="jdo-extensions-it/src/test/resources"/>
    <classpathentry kind="src" path="jdo-extensions-it/target/generated-sources/castor"/>

    <classpathentry kind="var" path="M2_REPO/org/codehaus/castor/spring-orm/1.3-SNAPSHOT/spring-orm-1.3-SNAPSHOT.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-aop/2.5.4/spring-aop-2.5.4.jar"/>
    <classpathentry kind="var" path="M2_REPO/aopalliance/aopalliance/1.0/aopalliance-1.0.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-beans/2.5.5/spring-beans-2.5.5.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-core/2.5.5/spring-core-2.5.5.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-orm/2.5.4/spring-orm-2.5.4.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-context/2.5.5/spring-context-2.5.5.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-tx/2.5.4/spring-tx-2.5.4.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/springframework/spring-jdbc/2.5.4/spring-jdbc-2.5.4.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/dbunit/dbunit/2.2.3/dbunit-2.2.3.jar"/>
    <classpathentry kind="var" path="M2_REPO/junit-addons/junit-addons/1.4/junit-addons-1.4.jar"/>
    <classpathentry kind="var" path="M2_REPO/poi/poi/2.5.1-final-20040804/poi-2.5.1-final-20040804.jar"/>
    <classpathentry kind="var" path="M2_REPO/commons-collections/commons-collections/3.1/commons-collections-3.1.jar"/>
    <classpathentry kind="var" path="M2_REPO/commons-lang/commons-lang/2.1/commons-lang-2.1.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-api/1.4.3/slf4j-api-1.4.3.jar"/>
    <classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-nop/1.4.3/slf4j-nop-1.4.3.jar"/>
    
End of README.txt file.