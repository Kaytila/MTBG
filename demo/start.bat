rem @echo off
echo %JAVA_HOME%
set java_path="C:\Program Files\jdk-19.0.2\bin"
set vmargs=-cp . -splash:out/graphics/splash/splash.jpg 
rem -Djava.security.manager -Djava.security.policy==policy.txt
set args=startPosition:3@0

%java_path%\java %vmargs% -jar demo.jar  %args%