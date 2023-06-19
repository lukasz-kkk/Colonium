@echo off
setlocal EnableDelayedExpansion

set /p instances=Enter the number of instances to open: 
FOR /L %%G IN (1,1,%instances%) DO (
    set /a "randomNumber=!RANDOM! %% 1000000 + 1"
    start "" mvn exec:java -Dexec.mainClass=core.Launcher -Dexec.args="!randomNumber!"
)
