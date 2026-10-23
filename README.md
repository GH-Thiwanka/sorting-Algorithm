# Java Application for Sorting Algorithm Performance Evaluation

**Course:** IC4302 – Programming III  
**Institution:** Department of ICT, Faculty of Technology, University of Colombo  
**Assignment:** Group Assignment: Sorting Algorithm Performance Evaluation  

---

## 📋 Project Overview

This Java desktop application provides an end-to-end benchmarking and performance evaluation platform for five fundamental sorting algorithms:
1. **Insertion Sort**
2. **Shell Sort** (Knuth Gap Sequence: $h = 3h + 1$)
3. **Merge Sort** (Divide-and-Conquer)
4. **Quick Sort** (Median-of-Three Pivot Partitioning)
5. **Heap Sort** (Binary Max-Heap Heapify)

Users can upload any comma-separated values (`.csv`) dataset (such as those from [Kaggle](https://www.kaggle.com/datasets)), automatically detect and select numeric columns, benchmark the sorting algorithms with high-precision nanosecond timing (`System.nanoTime()`), visualize execution time comparisons on an interactive custom bar chart, and identify the champion (fastest) algorithm.

---

## 👥 Group Members & Role Delegation

In accordance with the IC4302 assignment guidelines, responsibilities are cleanly divided into three distinct modules:

| Member | Assigned Responsibility | Key Classes & Packages |
| :--- | :--- | :--- |
| **Member 1** | **Dataset Upload & Handling** | `com.uoc.sorting.io.CSVReader`<br>`com.uoc.sorting.io.CSVParserException`<br>`com.uoc.sorting.model.Dataset`<br>`com.uoc.sorting.model.ColumnInfo` |
| **Member 2** | **Sorting Algorithms Implementation** | `com.uoc.sorting.algorithms.SortAlgorithm`<br>`com.uoc.sorting.algorithms.InsertionSort`<br>`com.uoc.sorting.algorithms.ShellSort`<br>`com.uoc.sorting.algorithms.MergeSort`<br>`com.uoc.sorting.algorithms.QuickSort`<br>`com.uoc.sorting.algorithms.HeapSort` |
| **Member 3** | **Performance Evaluation & User Interface (GUI)** | `com.uoc.sorting.benchmark.PerformanceEvaluator`<br>`com.uoc.sorting.benchmark.EvaluationReport`<br>`com.uoc.sorting.ui.MainFrame`<br>`com.uoc.sorting.ui.ChartPanel`<br>`com.uoc.sorting.ui.DataTableModel`<br>`com.uoc.sorting.ui.ResultsTableModel` |

---

## 🏛️ Architecture & Component Design

The application follows a clean **Model-View-Controller / Modular Layered Architecture**:

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
    ├── io/                              [Member 1: File & CSV Engine]
    │   ├── CSVReader.java               (RFC 4180 state parser, quoted token handler)
    │   └── CSVParserException.java      (Custom parsing exception handler)
    │
    ├── algorithms/                      [Member 2: Sorting Algorithms]
    │   ├── SortAlgorithm.java           (Common algorithm contract)
    │   ├── InsertionSort.java           (O(n²) incremental insertion)
    │   ├── ShellSort.java               (O(n^1.5) Knuth gap sequence)
    │   ├── MergeSort.java               (O(n log n) divide & conquer)
    │   ├── QuickSort.java               (O(n log n) median-of-three pivot)
    │   └── HeapSort.java                (O(n log n) binary max-heap)
    │
    ├── benchmark/                       [Member 3: Performance Evaluator]
    │   ├── PerformanceEvaluator.java    (Precision nanoTime runner & JIT warmup)
    │   └── EvaluationReport.java        (Ranking, champion detection, speedup)
    │
    └── ui/                              [Member 3: Swing User Interface]
        ├── UITheme.java                 (Design tokens, colors, typography)
        ├── MainFrame.java               (Main application window & tabs)
        ├── ChartPanel.java              (Custom Java2D bar chart canvas)
        ├── DataTableModel.java          (CSV preview table model)
        └── ResultsTableModel.java      (Benchmark ranking table model)
```

---

## ⚡ Sorting Algorithms & Complexity Matrix

| Algorithm | Best Time | Average Time | Worst Time | Space | Stable? | Key Mechanic |
| :--- | :---: | :---: | :---: | :---: | :---: | :--- |
| **Insertion Sort** | $O(n)$ | $O(n^2)$ | $O(n^2)$ | $O(1)$ | Yes | Shifts larger elements right to position current key. Excellent for small or nearly-sorted datasets. |
| **Shell Sort** | $O(n \log n)$ | $O(n^{1.25}) \sim O(n^{1.5})$ | $O(n^{1.5})$ | $O(1)$ | No | Diminishing gap sort using Knuth's sequence ($h = 3h + 1$), allowing distant elements to swap rapidly. |
| **Merge Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(n)$ | Yes | Recursively divides arrays into halves and merges in linear time. Consistent $O(n \log n)$ regardless of input order. |
| **Quick Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n^2)$ | $O(\log n)$ | No | Partition-exchange with **Median-of-Three** pivot selection to guard against worst-case on pre-sorted data. |
| **Heap Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(1)$ | No | Builds binary max-heap, extracts root (maximum) to the end, and sifts down to restore heap order in-place. |

---

## 📁 Sample Datasets (Included)

The project includes four pre-generated, realistic Kaggle-style CSV datasets in the `datasets/` directory:

1. `student_marks.csv` (120 rows): Academic marks across 5 subjects and GPA.
2. `product_sales.csv` (1,000 rows): Retail transaction data with unit prices, discounts, and total revenues.
3. `housing_prices.csv` (3,000 rows): Real-estate metrics with square footage, bedrooms, bathrooms, and prices.
4. `sensor_readings.csv` (10,000 rows): IoT sensor telemetry with temperature, humidity, pressure, and voltages.

---

## 🚀 How to Build and Run

### Prerequisites
- Java JDK 11 or higher (`java -version`, `javac -version`)

### Quick Start (Windows Batch Scripts)
Double-click or run from Command Prompt:
```cmd
compile.bat    :: Compiles all 19 source and test files to build/
run.bat        :: Launches the Graphical User Interface
test.bat       :: Runs the 40 automated verification & edge case tests
```

### Quick Start (PowerShell)
```powershell
.\compile.ps1  # Compiles the application
.\run.ps1      # Launches GUI
.\test.ps1     # Runs tests
```

### Command-Line Benchmark Mode (CLI)
You can also run benchmarks directly in headless/CLI mode:
```bash
java -cp build com.uoc.sorting.Main --benchmark datasets/housing_prices.csv 7
```

---

## 🛡️ Edge Cases Handled Gracefully

1. **RFC 4180 Quoted Commas:** Handles CSV fields containing commas enclosed in quotation marks (e.g., `"Colombo, Sri Lanka"`).
2. **Non-Numeric Column Prevention:** Auto-detects numeric vs text columns. If a user attempts to sort a text column, an alert modal explains that only numeric columns can be evaluated.
3. **Empty / Corrupt Files:** Rejects 0-byte or header-only files with clear user feedback.
4. **Already-Sorted / Reverse-Sorted Data:** QuickSort utilizes **Median-of-Three** pivot selection to prevent recursive call stack degradation.
5. **JVM Warm-Up:** Eliminates JIT compilation bias by executing a warm-up cycle before recording benchmark timings.
6. **Large Dataset Rendering:** Previews up to 5,000 rows in the GUI preview table to prevent UI memory stalls on massive files.

---

## 📹 Presentation Guide

Refer to [PRESENTATION_SCRIPT.md](file:///D:/projects/SortingAlgorithmPerformanceEvaluation/PRESENTATION_SCRIPT.md) for the complete, word-for-word Zoom recording script for all three group members.
