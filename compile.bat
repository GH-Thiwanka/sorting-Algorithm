@echo off
setlocal
echo =======================================================================
echo Compiling Sorting Algorithm Performance Evaluation Project...
echo =======================================================================

if not exist "build" mkdir "build"

echo Compiling source files...
javac -encoding UTF-8 -d build -sourcepath src src\com\uoc\sorting\Main.java src\com\uoc\sorting\model\*.java src\com\uoc\sorting\io\*.java src\com\uoc\sorting\algorithms\*.java src\com\uoc\sorting\benchmark\*.java src\com\uoc\sorting\ui\*.java

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed!
    exit /b %ERRORLEVEL%
)

echo Compiling unit test files...
javac -encoding UTF-8 -d build -cp build test\com\uoc\sorting\AlgorithmVerificationTest.java

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Test compilation failed!
    exit /b %ERRORLEVEL%
)

echo Packaging executable JAR...
jar cfe SortingPerformanceEvaluator.jar com.uoc.sorting.Main -C build .

echo =======================================================================
echo Build SUCCESSFUL!
echo Generated:
echo   - build\ (class files)
echo   - SortingPerformanceEvaluator.jar (Executable JAR)
echo.
echo To run application: run.bat or double-click SortingPerformanceEvaluator.jar
echo To run verification tests: test.bat
echo =======================================================================
