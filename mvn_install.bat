REM Set your variables

SET OLD_PATH=%PATH%
SET PATH=%PATH%;C:\apache\apache-maven-3.0.4\bin
SET JAVA_HOME=C:\Program Files\Java\jdk1.6.0_25
SET LIB_DIR=C:\usertemp\SDK\Tridion\lib

REM Install Tridion libraries in your local Maven Repository

call mvn install:install-file -Dfile="%LIB_DIR%\cd_broker.jar" -DgroupId=com.tridion -DartifactId=cd_broker -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_cache.jar" -DgroupId=com.tridion -DartifactId=cd_cache -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_core.jar" -DgroupId=com.tridion -DartifactId=cd_core -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_datalayer.jar" -DgroupId=com.tridion -DartifactId=cd_datalayer -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_deployer.jar" -DgroupId=com.tridion -DartifactId=cd_deployer -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_linking.jar" -DgroupId=com.tridion -DartifactId=cd_linking -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_model.jar" -DgroupId=com.tridion -DartifactId=cd_model -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_tcdl.jar" -DgroupId=com.tridion -DartifactId=cd_tcdl -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_undo.jar" -DgroupId=com.tridion -DartifactId=cd_undo -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_upload.jar" -DgroupId=com.tridion -DartifactId=cd_upload -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\cd_wrapper.jar" -DgroupId=com.tridion -DartifactId=cd_wrapper -Dversion=7.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile="%LIB_DIR%\easylicense.jar" -DgroupId=com.vs.ezlicrun -DartifactId=easy-license -Dversion=25.0 -Dpackaging=jar

REM Restore Variable

SET PATH=%OLD_PATH%