@echo off
setlocal enabledelayedexpansion
set total=0
set files=0
for /r src %%i in (*.kt) do (
    set /a files+=1
    for /f %%j in ('find /v /c "" ^< "%%i"') do (
        set /a total+=%%j
    )
)
echo Files in src: %files%
echo Lines in src: %total%
