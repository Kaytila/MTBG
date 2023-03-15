@echo off
rem https://stackoverflow.com/questions/20484151/redirecting-output-from-within-batch-file
call :sub > %build_path%\build.log 2>&1
exit /b


:sub
set zip_path="C:\Program Files\7-Zip"
set tmp_path=%build_path%\tmp
set build_path="C:\Users\Claus\eclipse-workspace\MyTurnBasedGame\demo"
set java_path="C:\Program Files\jdk-19.0.2\bin"
set jar=MyTurnBasedGame.jar
set jar2=demo.jar
set zip=demo.zip
set assets=%build_path%\out_orig
set robocopy=c:\Windows\SysWOW64\Robocopy.exe

mkdir %tmp_path%

echo clean up
del %build_path%\%jar2%
del %build_path%\%zip%
rem rmdir %build_path%\out /S /Q


%zip_path%\7z.exe x %build_path%\%jar% -o%tmp_path% -y -r

rem clean up zip contents
rmdir %tmp_path%\artifacts /s /q
rmdir %tmp_path%\backup_music /s /q
rmdir %tmp_path%\nackup_maps /s /q
rmdir %tmp_path%\graphics /s /q
rmdir %tmp_path%\items /s /q
rmdir %tmp_path%\maps /s /q
rmdir %tmp_path%\music /s /q
rmdir %tmp_path%\soundeffects /s /q
rmdir %tmp_path%\npcs /s /q


rem %zip_path%\7z.exe a %build_path%\%jar2% -tzip -mx9 -spf -r %tmp_path%
%java_path%\jar cmf %tmp_path%\META-INF\MANIFEST.mf %build_path%\%jar2% -C %tmp_path% .
rem clean tmp dir
rem rmdir %tmp_path% /s /q

echo jar file finished
echo ========================
echo add music
mkdir %build_path%\out
mkdir %build_path%\out\music
mkdir %build_path%\out\music\VICTORY
mkdir %build_path%\out\music\WORLD
%robocopy% %assets%\music\VICTORY\ %build_path%\out\music\VICTORY /S /E
copy %assets%\music\WORLD\Ultima_remix_-_Traveling.au %build_path%\out\music\WORLD

echo add soundeffects
mkdir %build_path%\out\soundeffects
%robocopy% %assets%\soundeffects\ %build_path%\out\soundeffects /S /E

echo add rest
mkdir %build_path%\out\graphics
%robocopy% %assets%\graphics\ %build_path%\out\graphics /E /S
mkdir %build_path%\out\npcs
%robocopy% %assets%\npcs\ %build_path%\out\npcs /S /E
mkdir %build_path%\out\items
%robocopy% %assets%\items\ %build_path%\out\items /S /E
mkdir %build_path%\out\maps
%robocopy% %assets%\maps\ %build_path%\out\maps /S /E

copy %assets%\log4j2.xml %build_path%\out
copy %assets%\policy.txt %build_path%\out

%zip_path%\7z.exe a %build_path%\%zip% -tzip -mx=9 %build_path%\%jar2%
%zip_path%\7z.exe a %build_path%\%zip% -tzip -mx=9 %build_path%\out
%zip_path%\7z.exe a %build_path%\%zip% -tzip %build_path%\start.bat

echo clean up
rmdir %tmp_path% /S /Q
del %build_path%\%jar%