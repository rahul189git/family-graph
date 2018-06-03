@echo off
rem
rem Running the builder application
rem

setlocal

set JAVA_HOME=%cd%\jdk1.8.0_111
rem echo JAVA_HOME: %JAVA_HOME%

if exist "%JAVA_HOME%\bin" goto okJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
pause
exit
:okJavaHome


rem add current JVM to Path
set PATH=%JAVA_HOME%\bin;%PATH%

cd %~dp0


rem run the program
java -jar family-all-1.0.jar
rem pause

:setenv
set CLASSPATH=%CLASSPATH%;%~1

endlocal