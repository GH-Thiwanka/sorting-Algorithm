@echo off
setlocal
echo Launching Sorting Algorithm Performance Evaluation GUI...
if exist "SortingPerformanceEvaluator.jar" (
    java -jar SortingPerformanceEvaluator.jar
) else (
    java -cp build com.uoc.sorting.Main
)
