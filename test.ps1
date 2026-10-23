# IC4302 - Run verification tests (PowerShell)
Write-Host "Running IC4302 Sorting Verification and Edge Case Tests..." -ForegroundColor Cyan
& java -cp build com.uoc.sorting.AlgorithmVerificationTest
