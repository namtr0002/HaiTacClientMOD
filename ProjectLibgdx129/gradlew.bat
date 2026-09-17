@echo off
setlocal

set DIR=%~dp0
set APP_BASE=%DIR%
set CLASSPATH=%APP_BASE%gradle\wrapper\gradle-wrapper.jar

set JAVA_EXE=java.exe
if defined JAVA_HOME set JAVA_EXE=%JAVA_HOME%\bin\java.exe

"%JAVA_EXE%" -Xmx64m -Xms64m -Dorg.gradle.appname=Gradle -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal

