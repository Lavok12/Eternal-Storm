@echo off
set total=0
set files=0
for /f "delims=" %%i in ('dir /s /b *.kt ^| findstr /v /i /c:".gradle" /c:"build" /c:"bin" /c:".idea"') do (
    set /a files+=1
    for /f %%j in ('find /v /c "" ^< "%%i"') do (
        set /a total+=%%j
    )
)
echo Total Files: %files%
echo Total Lines: %total%
