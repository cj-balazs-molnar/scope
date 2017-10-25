echo off
if "%1"=="" GOTO :BLANK
type %1 | java -jar WorkshopTracker.jar
GOTO :DONE
:BLANK
java -jar WorkshopTracker.jar
:DONE
pause

