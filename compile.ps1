Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host "Compiling Sorting Algorithm Performance Evaluation Project..." -ForegroundColor Cyan
Write-Host "=======================================================================" -ForegroundColor Cyan

if (!(Test-Path 'build')) {
    New-Item -ItemType Directory -Path 'build' -Force | Out-Null
}

$sourceFiles = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
Write-Host "Compiling $($sourceFiles.Count) source files..." -ForegroundColor Yellow

& javac -encoding UTF-8 -d build -sourcepath src $sourceFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Source compilation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}

$testFiles = Get-ChildItem -Path "test" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
if ($testFiles) {
    Write-Host "Compiling test files..." -ForegroundColor Yellow
    & javac -encoding UTF-8 -d build -cp build $testFiles
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Test compilation failed!" -ForegroundColor Red
        exit $LASTEXITCODE
    }
}

Write-Host "Packaging executable JAR..." -ForegroundColor Yellow
& jar cfe SortingPerformanceEvaluator.jar com.uoc.sorting.Main -C build .

Write-Host "=======================================================================" -ForegroundColor Green
Write-Host "Build SUCCESSFUL!" -ForegroundColor Green
Write-Host "  - build/ (classes)" -ForegroundColor Green
Write-Host "  - SortingPerformanceEvaluator.jar (Executable JAR)" -ForegroundColor Green
Write-Host "Run application with: .\run.ps1 or double-click SortingPerformanceEvaluator.jar" -ForegroundColor Green
Write-Host "Run verification test with: .\test.ps1" -ForegroundColor Green
Write-Host "=======================================================================" -ForegroundColor Green
