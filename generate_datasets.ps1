$datasetsDir = 'D:\projects\SortingAlgorithmPerformanceEvaluation\datasets'
if (!(Test-Path $datasetsDir)) {
    New-Item -ItemType Directory -Path $datasetsDir -Force | Out-Null
}

# 1. student_marks.csv (~120 rows)
$students = [System.Collections.Generic.List[string]]::new()
$students.Add('ID,Student_Name,Mathematics,Science,Programming_III,English,GPA')
$firstNames = @('Kamal','Nimal','Sunil','Amara','Kasun','Saman','Dilani','Sanduni','Pradeep','Tharindu','Anushka','Chathura','Nuwan','Sachini','Isuru')
$lastNames = @('Perera','Silva','Fernando','Jayasinghe','Bandara','Dissanayake','Gunaratne','Rajapaksha','Wickramasinghe','Senanayake')

for ($i = 1; $i -le 120; $i++) {
    $fn = $firstNames[$i % $firstNames.Length]
    $ln = $lastNames[$i % $lastNames.Length]
    $math = [Math]::Round(45 + ($i * 17) % 55, 1)
    $sci = [Math]::Round(40 + ($i * 23) % 58, 1)
    $prog = [Math]::Round(50 + ($i * 31) % 50, 1)
    $eng = [Math]::Round(55 + ($i * 13) % 44, 1)
    $gpa = [Math]::Round(2.0 + (($prog * 0.4 + $math * 0.3 + $sci * 0.2 + $eng * 0.1) / 100.0) * 2.0, 2)
    $students.Add(('STU{0:D4},"{1} {2}",{3},{4},{5},{6},{7}' -f $i, $fn, $ln, $math, $sci, $prog, $eng, $gpa))
}
[System.IO.File]::WriteAllLines((Join-Path $datasetsDir 'student_marks.csv'), $students, [System.Text.Encoding]::UTF8)

# 2. product_sales.csv (~1,000 rows)
$sales = [System.Collections.Generic.List[string]]::new()
$sales.Add('Transaction_ID,Category,Unit_Price,Quantity,Discount_Percent,Total_Revenue')
$cats = @('Electronics','Groceries','Stationery','Clothing','Furniture','Hardware')
for ($i = 1; $i -le 1000; $i++) {
    $cat = $cats[$i % $cats.Length]
    $price = [Math]::Round(10.5 + (($i * 37) % 490), 2)
    $qty = 1 + ($i * 7) % 25
    $disc = [Math]::Round((($i * 3) % 15) * 1.5, 1)
    $rev = [Math]::Round($price * $qty * (1.0 - ($disc / 100.0)), 2)
    $sales.Add(('TRX{0:D6},{1},{2},{3},{4},{5}' -f $i, $cat, $price, $qty, $disc, $rev))
}
[System.IO.File]::WriteAllLines((Join-Path $datasetsDir 'product_sales.csv'), $sales, [System.Text.Encoding]::UTF8)

# 3. housing_prices.csv (~3,000 rows)
$housing = [System.Collections.Generic.List[string]]::new()
$housing.Add('Property_ID,City,Bedrooms,Bathrooms,Square_Feet,Year_Built,Lot_Size_Acres,Price_USD')
$cities = @('Colombo','Kandy','Galle','Kurunegala','Negombo','Matara','Gampaha','Jaffna')
for ($i = 1; $i -le 3000; $i++) {
    $city = $cities[$i % $cities.Length]
    $bed = 1 + ($i % 5)
    $bath = [Math]::Round(1.0 + (($i * 3) % 4) * 0.5, 1)
    $sqft = 650 + (($i * 47) % 4200)
    $yr = 1975 + ($i % 49)
    $lot = [Math]::Round(0.05 + (($i * 11) % 95) * 0.05, 2)
    $price = [Math]::Round(35000 + ($sqft * 125.5) + ($bed * 15000) + ($bath * 12000) - ((2024 - $yr) * 500), 2)
    $housing.Add(('PROP{0:D5},"{1}",{2},{3},{4},{5},{6},{7}' -f $i, $city, $bed, $bath, $sqft, $yr, $lot, $price))
}
[System.IO.File]::WriteAllLines((Join-Path $datasetsDir 'housing_prices.csv'), $housing, [System.Text.Encoding]::UTF8)

# 4. sensor_readings.csv (~10,000 rows)
$sensor = [System.Collections.Generic.List[string]]::new()
$sensor.Add('Reading_ID,Device_Code,Temperature_C,Humidity_Percent,Pressure_hPa,Battery_Voltage')
for ($i = 1; $i -le 10000; $i++) {
    $dev = 'DEV-' + (101 + ($i % 20))
    $temp = [Math]::Round(18.0 + (($i * 19) % 220) / 10.0, 2)
    $hum = [Math]::Round(30.0 + (($i * 31) % 650) / 10.0, 2)
    $pres = [Math]::Round(980.0 + (($i * 13) % 400) / 10.0, 2)
    $batt = [Math]::Round(3.2 + (($i * 7) % 100) / 100.0, 2)
    $sensor.Add(('{0},{1},{2},{3},{4},{5}' -f $i, $dev, $temp, $hum, $pres, $batt))
}
[System.IO.File]::WriteAllLines((Join-Path $datasetsDir 'sensor_readings.csv'), $sensor, [System.Text.Encoding]::UTF8)

Write-Host "Sample datasets successfully generated in $datasetsDir"
