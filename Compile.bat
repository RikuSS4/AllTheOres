@ECHO off
REM SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_40
SET PATH=%PATH%;C:\Tools\Git\cmd;C:\Tools\Ant\Bin;C:\Tools\Gradle\Bin


CALL gradlew build
REM CALL ant
REM PAUSE