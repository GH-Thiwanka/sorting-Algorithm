# Sorting Algorithm Performance Evaluation Application

A Java desktop application that provides a benchmarking and performance evaluation platform for five fundamental sorting algorithms:
1. **Insertion Sort**
2. **Shell Sort** (Knuth Gap Sequence: $h = 3h + 1$)
3. **Merge Sort** (Divide-and-Conquer)
4. **Quick Sort** (Median-of-Three Pivot Partitioning)
5. **Heap Sort** (Binary Max-Heap Heapify)

Users can upload any comma-separated values (`.csv`) dataset, automatically detect and select numeric columns, benchmark the sorting algorithms with high-precision nanosecond timing (`System.nanoTime()`), visualize execution time comparisons on an interactive custom bar chart, and identify the fastest performing algorithm.

---

## Architecture & Component Design

The application follows a clean, modular architecture:

```
src/
└── com/uoc/sorting/
    ├── Main.java                        [Application Entry Point (GUI & CLI)]
    │
    ├── model/                           [Data Models & Metadata]
    │   ├── Dataset.java                 (Tabular data holder & column analyzer)
    │   ├── ColumnInfo.java              (Column metadata & numeric detection)
    │   └── SortResult.java              (Benchmark output & metrics model)
    │
    ├── io/                              [File & CSV Engine]
    │   ├── CSVReader.java               (RFC 4180 state parser, quoted token handler)
    │   └── CSVParserException.java      (Custom parsing exception handler)
    │
    ├── algorithms/                      [Sorting Algorithms]
    │   ├── SortAlgorithm.java           (Common algorithm contract)
    │   ├── InsertionSort.java           (O(n²) incremental insertion)
    │   ├── ShellSort.java               (O(n^1.5) Knuth gap sequence)
    │   ├── MergeSort.java               (O(n log n) divide & conquer)
    │   ├── QuickSort.java               (O(n log n) median-of-three pivot)
    │   └── HeapSort.java                (O(n log n) binary max-heap)
    │
    ├── benchmark/                       [Performance Evaluator]
    │   ├── PerformanceEvaluator.java    (Precision nanoTime runner & JIT warmup)
    │   └── EvaluationReport.java        (Ranking, champion detection, speedup)
    │
    └── ui/                              [Swing User Interface]
        ├── UITheme.java                 (Design tokens, colors, typography)
        ├── MainFrame.java               (Main application window & tabs)
        ├── ChartPanel.java              (Custom Java2D bar chart canvas)
        ├── DataTableModel.java          (CSV preview table model)
        └── ResultsTableModel.java      (Benchmark ranking table model)
```

---

## Sorting Algorithms & Complexity Matrix

| Algorithm | Best Time | Average Time | Worst Time | Space | Stable? | Key Mechanic |
| :--- | :---: | :---: | :---: | :---: | :---: | :--- |
| **Insertion Sort** | $O(n)$ | $O(n^2)$ | $O(n^2)$ | $O(1)$ | Yes | Shifts larger elements right to position current key. Excellent for small or nearly-sorted datasets. |
| **Shell Sort** | $O(n \log n)$ | $O(n^{1.25}) \sim O(n^{1.5})$ | $O(n^{1.5})$ | $O(1)$ | No | Diminishing gap sort using Knuth's sequence ($h = 3h + 1$), allowing distant elements to swap rapidly. |
| **Merge Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(n)$ | Yes | Recursively divides arrays into halves and merges in linear time. Consistent $O(n \log n)$ regardless of input order. |
| **Quick Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n^2)$ | $O(\log n)$ | No | Partition-exchange with **Median-of-Three** pivot selection to guard against worst-case on pre-sorted data. |
| **Heap Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(1)$ | No | Builds binary max-heap, extracts root (maximum) to the end, and sifts down to restore heap order in-place. |

---

## Sample Datasets (Included)

The project includes four pre-generated CSV datasets in the `datasets/` directory:

1. `student_marks.csv` (120 rows): Academic marks across 5 subjects and GPA.
2. `product_sales.csv` (1,000 rows): Retail transaction data with unit prices, discounts, and total revenues.
3. `housing_prices.csv` (3,000 rows): Real-estate metrics with square footage, bedrooms, bathrooms, and prices.
4. `sensor_readings.csv` (10,000 rows): Sensor telemetry with temperature, humidity, pressure, and voltages.

---

## How to Build and Run

### Prerequisites
- Java JDK 11 or higher (`java -version`, `javac -version`)

### Running the Application (Executable JAR)
The application is pre-packaged as a standalone executable JAR:
- **GUI Mode:** Simply double-click `SortingPerformanceEvaluator.jar` or run:
  ```cmd
  java -jar SortingPerformanceEvaluator.jar
  ```
- **CLI Benchmark Mode:** Run headless benchmark on any dataset from the command line:
  ```cmd
  java -jar SortingPerformanceEvaluator.jar --benchmark datasets/housing_prices.csv 7
  ```

### Compiling from Source (Optional)
If you wish to recompile the project manually:
```cmd
javac -encoding UTF-8 -d build -sourcepath src src/com/uoc/sorting/Main.java src/com/uoc/sorting/model/*.java src/com/uoc/sorting/io/*.java src/com/uoc/sorting/algorithms/*.java src/com/uoc/sorting/benchmark/*.java src/com/uoc/sorting/ui/*.java
jar cfe SortingPerformanceEvaluator.jar com.uoc.sorting.Main -C build .
```

### Running Automated Tests
```cmd
javac -encoding UTF-8 -d build -cp build test/com/uoc/sorting/AlgorithmVerificationTest.java
java -cp build com.uoc.sorting.AlgorithmVerificationTest
```

---

## Edge Cases Handled

1. **RFC 4180 Quoted Commas:** Handles CSV fields containing commas enclosed in quotation marks (e.g., `"Colombo, Sri Lanka"`).
2. **Non-Numeric Column Detection:** Automatically distinguishes numeric vs text columns. If a user attempts to sort a text column, an informative alert explains that a numeric column is required.
3. **Empty / Corrupt Files:** Rejects 0-byte or header-only files with clear user feedback.
4. **Already-Sorted / Reverse-Sorted Data:** QuickSort utilizes **Median-of-Three** pivot selection to prevent recursive call stack degradation.
5. **JVM Warm-Up:** Eliminates JIT compilation bias by executing a warm-up cycle before recording benchmark timings.
6. **Large Dataset Rendering:** Previews up to 5,000 rows in the GUI preview table to prevent UI memory stalls on large files.
