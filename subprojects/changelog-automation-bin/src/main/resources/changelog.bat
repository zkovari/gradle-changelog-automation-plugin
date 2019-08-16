@ECHO OFF

setlocal enableDelayedExpansion
set VERSION=0.1.2

set OUTPUT_DIR=changelogs\unreleased
set TITLE=
set REFERENCE=
set AUTHOR=
set TYPE=
set USE_GIT_USERNAME=false
set DRY_RUN=false

set IS_TYPE=false
set IS_REFERENCE=false
set CONTINUE=0

for %%0 in (%*) do (
	if [!IS_TYPE!]==[true] (
		set TYPE=%%~x
		set IS_TYPE=false
		set /A CONTINUE=1
	)

	if [%%~x]==[-t] (
		SET IS_TYPE=true
		set /A CONTINUE=1
	)
	if [%%~x]==[--type] (
		SET IS_TYPE=true
		set /A CONTINUE=1
	)

	if [!IS_REFERENCE!]==[true] (
		set REFERENCE=%%~x
		SET IS_REFERENCE=false
		set /A CONTINUE=1
	)
	if [%%~x]==[-r] (
		set IS_REFERENCE=true
		set /A CONTINUE=1
	)
	if [%%~x]==[--reference] (
		set IS_REFERENCE=true
		set /A CONTINUE=1
	)

	if [%%~x]==[-u] (
		set USE_GIT_USERNAME=true
		set /A CONTINUE=1
	)
	if [%%~x]==[--git-username] (
		set USE_GIT_USERNAME=true
		set /A CONTINUE=1
	)

	if [%%~x]==[-v] (
		echo Version : %VERSION%
		exit /B 0
	)
	if [%%~x]==[--version] (
		echo Version : %VERSION%
		exit /B 0
	)


	if [%%~x]==[--dry-run] (
		set DRY_RUN=true
		set /A CONTINUE=1
	)

	if [%%~x]==[-h] (
		call :DisplayHelp
		exit /B 0
	)
	if [%%~x]==[--help] (
		call :DisplayHelp
		exit /B 0
	)

	if !CONTINUE! EQU 0 (
		set TITLE=%%~x
	)
	set /A CONTINUE=0
)

set /A HAS_MISSING_PARAMS=0
call :CheckEmptyArg "%TITLE%" Title
call :CheckEmptyArg "%TYPE%" Type
if [%HAS_MISSING_PARAMS%] gtr [0] (
	echo See options and examples in --help:
	echo.
	call :DisplayHelp
)

set /A IS_TYPE_VALID=0
call :CheckType %TYPE%
if [%IS_TYPE_VALID%] EQU [0] (
	exit /B 1
)

if [%USE_GIT_USERNAME%]==[true] (
	for /f "delims=" %%1 in ('git config --get user.name') do set AUTHOR=%%i
)

if [%DRY_RUN%]==[true] (
	echo ---
	echo title: %TITLE%
	echo reference: %REFERENCE%
	echo author: %AUTHOR%
	echo type: %TYPE%
	exit /B 0
)

if not exist %OUTPUT_DIR% (
	mkdir %OUTPUT_DIR%
)

for /f "delims=" %%i in ('git rev-parse --abbrev-ref HEAD') do set FILENAME=%%i
set FILENAME=%FILENAME:/=_%
set FILENAME=%FILENAME%.yml
set FILEPATH=%OUTPUT_DIR%\%FILENAME%

if exist %FILEPATH% (
	set CUR_YYY=%date:~6,4%
	set CUR_MM=%date:~3,2%
	set CUR_DD=%date:~0,2%
	if "%TIME:~1,1%"=":" (
		set CUR_HH=0%TIME:~0,1%
		set CUR_NN=%TIME:~2,2%
		set CUR_SS=%TIME:~5,2%
	) else (
		set CUR_HH=0%TIME:~0,2%
		set CUR_NN=%TIME:~3,2%
		set CUR_SS=%TIME:~6,2%		
	)
	set TIMESTRING=!CUR_YYY!!CUR_MM!!CUR_DD!-!CUR_HH!!CUR_NN!!CUR_SS!
	set FILENAME=!TIMESTRING!_%FILENAME%
	set FILEPATH=%OUTPUT_DIR%\!FILENAME!
)

echo --- > %FILEPATH%
echo title: %TITLE% >> %FILEPATH%
echo reference: %REFERENCE% >> %FILEPATH%
echo author: %AUTHOR% >> %FILEPATH%
echo type: %TYPE% >> %FILEPATH%
echo New Changelog was generated to: %FILEPATH%

exit /b %ERRORLEVEL%

:DisplayHelp
echo Usage: changelog.bat [OPTION]... --type [TYPE] [TITLE]
echo.
echo Generate a .yml file that corresponds to a new changelog entry
echo The .yml file is generated under 'changelogs/unreleased' directory
echo relative to the current working directory where this script is executed.
echo.
echo Example: changelog.bat --type added "My new feature"
echo.
echo -----------------------------------------------------------------------------
echo.
echo Obligatory:
echo.
echo  -t ^| --type [TYPE]     the type of the changelog entry. Available values:
echo                        added, changed, deprecated, fixed, removed, security
echo.
echo Options for changelog content generation:
echo.
echo  -r ^| --reference [REF] write an extra reference to the changelog.
echo                        Typically reference of an issue or a merge/pull request
echo  -u ^| --git-username    write the current git user.name to the changelog
echo.
echo Miscellaneous:
echo.
echo  -h ^| --help            display this help text and exit
echo  -v ^| --version         display version information and exit
echo  --dry-run             display the .yml changelog entry without 
echo                        generating the file
echo.
echo ------------------------------------------------------------------------------
echo.
echo Examples:
echo.
echo # 'added' changelog entry
echo changelog.bat --type added "Add new feature"
echo.
echo # 'removed' changelog entry
echo changelog.bat -t removed "Removed legacy behaviour"
echo.
echo # 'added' changelog entry with extra reference and git user name.
echo # Reference '18' could point to an issue or a merge/pull request
echo changelog.bat -t added -r 18 -u "Add new feature"
echo.
echo ------------------------------------------------------------------------------
echo.
exit /B 0

:CheckEmptyArg
if "%~1!"=="" (
	echo %~2 must be specified.
	set /A HAS_MISSING_PARAMS=%HAS_MISSING_PARAMS%+1
)
exit /B 0

:CheckType
set /A IS_VALID=0
if ["%~1"]==["added"] (
	set /A IS_TYPE_VALID=1
)
if ["%~1"]==["changed"] (
	set /A IS_TYPE_VALID=1
)
if ["%~1"]==["deprecated"] (
	set /A IS_TYPE_VALID=1
)
if ["%~1"]==["fixed"] (
	set /A IS_TYPE_VALID=1
)
if ["%~1"]==["removed"] (
	set /A IS_TYPE_VALID=1
)
if ["%~1"]==["security"] (
	set /A IS_TYPE_VALID=1
)
if %IS_TYPE_VALID% EQU 0 (
	echo "Invalid value was specified for type --type: %~1. Accepted values are: added, changed, deprecated, fixed, removed, security"
	echo "See options and examples in --help"
)
exit /B 0
