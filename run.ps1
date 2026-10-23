# IC4302 - Run application script (PowerShell)
Write-Host "Launching IC4302 Sorting Algorithm Performance Evaluator..." -ForegroundColor Cyan
if (Test-Path "SortingPerformanceEvaluator.jar") {
    & java -jar SortingPerformanceEvaluator.jar
} else {
    & java -cp build com.uoc.sorting.Main
}
