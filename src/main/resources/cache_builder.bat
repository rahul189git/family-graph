@echo off
rem
rem Running the builder application
rem

setlocal

rem set your jdk location
if not "%JAVA_HOME%" == "" goto gotJavaHome
set JAVA_HOME=%ENV_JAVA_HOME%
:gotJavaHome

echo JAVA_HOME: %JAVA_HOME%

if exist "%JAVA_HOME%\bin" goto okJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
pause
exit
:okJavaHome


rem add current JVM to Path
set PATH=%JAVA_HOME%\bin;%PATH%

cd %~dp0

rem set the classpath
set CLASSPATH=.
for %%i in (lib\*.jar) do call :setenv "%%~fi"
echo Classpath: %CLASSPATH%



echo.

rem run the program
java %JAVA_OPTS% com.maverick.family.input.InputCollector
rem pause

:setenv
set CLASSPATH=%CLASSPATH%;%~1

endlocal