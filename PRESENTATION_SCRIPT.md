# IC4302 – Programming III: Group Presentation Script
## Java Application for Sorting Algorithm Performance Evaluation
**Department of ICT • Faculty of Technology • University of Colombo**

---

## ⏱️ Presentation Overview & Pacing (Target: 10–12 Minutes)

| Segment | Speaker | Topic | Target Time |
| :--- | :--- | :--- | :--- |
| **Part 1** | **All Members** | Group Introduction & Project Overview | 0:00 – 1:30 |
| **Part 2** | **Member 1** | File Upload Functionality & Dataset Handling | 1:30 – 4:30 |
| **Part 3** | **Member 2** | Implementation of 5 Sorting Algorithms | 4:30 – 8:00 |
| **Part 4** | **Member 3** | Performance Evaluation & Application Demonstration | 8:00 – 11:30 |
| **Part 5** | **All Members** | Conclusion & Closing | 11:30 – 12:00 |

---

## 🎙️ PART 1: Introduction (All Group Members)

> *Screen Share: Title slide or the open application home screen.*

**Member 1:**
> "Good day everyone. We are Group [Number], presenting our IC4302 Programming III assignment for the Department of ICT, Faculty of Technology, University of Colombo.  
> Our project is the **Java Application for Sorting Algorithm Performance Evaluation**.  
> The objective of this application is to allow users to upload datasets in `.csv` format, select a numeric column to sort, apply five classical sorting algorithms, evaluate their execution times, and identify the best-performing algorithm.  
> Let us begin with mandatory self-introductions."

**Member 1:**
> "I'm [Member 1 Name], and my student number is [T085XX]. In this assignment, I was responsible for the **Dataset Upload and File Handling module**, including RFC 4180 CSV parsing, column type inference, and edge-case validation."

**Member 2:**
> "I'm [Member 2 Name], and my student number is [T085YY]. In this project, I was responsible for the **Implementation of the Sorting Algorithms**, specifically Insertion Sort, Shell Sort, Merge Sort, Quick Sort, and Heap Sort, as well as their time and space complexity analysis."

**Member 3:**
> "I'm [Member 3 Name], and my student number is [T085ZZ]. In this project, I was responsible for the **Performance Evaluation engine, benchmark timing, and the Graphical User Interface (GUI)** built with Java Swing and custom 2D visualization charts, along with conducting today's live demonstration."

---

## 🎙️ PART 2: Member 1 – File Upload & Dataset Handling

> *Screen Share: Code in IDE showing `com.uoc.sorting.io` and `com.uoc.sorting.model` packages.*

**Member 1:**
> "Thank you. Now I will explain the dataset handling and file upload architecture that I developed.
>
> In our design, all data handling is encapsulated inside two packages: `com.uoc.sorting.io` and `com.uoc.sorting.model`.
>
> 1. **RFC 4180 CSV State Parser (`CSVReader.java`):**
>    - Rather than relying on simplistic `String.split(",")`, which fails whenever a CSV contains commas inside quoted text (for example, address fields like `'Colombo, Sri Lanka'`), I implemented an RFC 4180 compliant finite-state token parser in the `parseLine` method.
>    - It tracks quotation boundaries (`inQuotes`), supports escaped quotes (`""`), and trims extraneous whitespace.
>    - It also handles Byte Order Marks (BOM) gracefully to ensure compatibility across different operating systems.
>
> 2. **Dataset Model & Numerical Analysis (`Dataset.java` & `ColumnInfo.java`):**
>    - When a file is parsed, `Dataset.java` automatically inspects every column through `analyzeColumns()`.
>    - It examines data rows to verify whether every entry in that column can be successfully parsed into a standard `double`.
>    - If so, it marks `isNumeric = true`, records valid row counts, and calculates minimum and maximum bounds.
>    - This directly satisfies the assignment requirement that users must always select numeric columns to sort.
>
> 3. **Error Handling & Edge Cases (`CSVParserException.java`):**
>    - We gracefully handle invalid scenarios: empty files (0 bytes), missing headers, mismatched column counts, and files containing only text.
>    - If a user uploads a malformed file, our custom `CSVParserException` pinpoints the exact line number where the anomaly occurred and presents a user-friendly modal dialog.
>
> Now I will hand over to Member 2 to explain the implementation of our sorting algorithms."

---

## 🎙️ PART 3: Member 2 – Implementation of Sorting Algorithms

> *Screen Share: Code in IDE showing `com.uoc.sorting.algorithms` package.*

**Member 2:**
> "Thank you, [Member 1 Name].
>
> I implemented the five core sorting algorithms required by IC4302, all adhering to a clean, decoupled `SortAlgorithm` interface. Let me walk you through each algorithm:
>
> 1. **Insertion Sort (`InsertionSort.java`):**
>    - An incremental comparison-based algorithm. It iterates through the array from index 1 to $n-1$, treating the sub-array to the left as sorted, and shifts larger elements to the right to place the current element into its correct position.
>    - **Complexity:** Best case is $O(n)$ when already sorted; Average and Worst cases are $O(n^2)$. Space complexity is $O(1)$.
>
> 2. **Shell Sort (`ShellSort.java`):**
>    - An optimization over Insertion Sort that compares elements separated by a gap distance, allowing elements to make large jumps.
>    - To achieve optimal theoretical and empirical performance, I implemented **Knuth’s gap sequence**: $h = 3h + 1$ ($1, 4, 13, 40, 121, \dots$).
>    - **Complexity:** Reduces worst-case time to $O(n^{1.5})$ with $O(1)$ auxiliary space.
>
> 3. **Merge Sort (`MergeSort.java`):**
>    - A classic divide-and-conquer algorithm. It recursively splits the array into two halves until single-element sub-arrays are obtained, then merges the halves back in sorted order using an auxiliary array.
>    - **Complexity:** Guarantees $O(n \log n)$ time across best, average, and worst cases. Its space complexity is $O(n)$ due to the merge buffer.
>
> 4. **Quick Sort (`QuickSort.java`):**
>    - Another divide-and-conquer algorithm that partitions the array around a pivot element.
>    - To safeguard against the catastrophic $O(n^2)$ worst-case on already sorted or reverse-sorted CSV datasets, I implemented the **Median-of-Three** pivot selection strategy. It inspects the first, middle, and last elements and chooses the median value as pivot.
>    - **Complexity:** $O(n \log n)$ average time, $O(\log n)$ recursive call-stack space.
>
> 5. **Heap Sort (`HeapSort.java`):**
>    - Utilizes a binary max-heap data structure. In Phase 1, it reorganizes the array into a max-heap where every parent is greater than its children. In Phase 2, it repeatedly swaps the root (the maximum element) to the end of the unsorted partition and calls `heapify()` to sift down the new root.
>    - **Complexity:** Guarantees $O(n \log n)$ time with strictly $O(1)$ auxiliary space.
>
> All algorithms sort primitive `double[]` arrays in-place in ascending order.
> Now, Member 3 will explain our performance benchmarking, the GUI, and demonstrate the running application."

---

## 🎙️ PART 4: Member 3 – Performance Evaluation & Live Demonstration

> *Screen Share: Launch the application by running `run.bat` or `run.ps1`.*

**Member 3:**
> "Thank you, [Member 2 Name].
>
> I was responsible for the **Performance Evaluation Engine** (`PerformanceEvaluator.java`) and the **Graphical User Interface** (`MainFrame.java`, `ChartPanel.java`).
>
> 1. **Scientific Benchmark Methodology:**
>    - When measuring algorithm execution time, relying on `System.currentTimeMillis()` is insufficient because modern sorting takes fractions of a millisecond on small-to-medium datasets. Therefore, we use high-resolution `System.nanoTime()`.
>    - Furthermore, to ensure fair evaluation, our engine clones the raw array so every algorithm sorts identical, untouched data.
>    - We also run a lightweight JIT warm-up pass before timing, preventing Java HotSpot JIT compilation bias from penalizing the first algorithm tested.
>    - After each sort, `verifySorted()` validates that every element satisfies $A[i] \le A[i+1]$.
>
> 2. **Application Walkthrough & Demonstration:**
>    *(Member 3 demonstrates the running application on screen)*
>
>    - **Step 1: Uploading the CSV Dataset:**
>      - 'Let us click the **📁 Upload CSV File** button. We can also use **📊 Load Sample Data** which comes bundled with real Kaggle-style datasets. Let's select `housing_prices.csv`, which contains 3,000 real-estate records.'
>      - 'Notice how the dataset information card immediately updates: 3,000 rows, 8 columns, and 6 detected numeric columns.'
>
>    - **Step 2: Inspecting the Data:**
>      - 'Under the **📄 Dataset Preview** tab, the lecturer can see the full tabular dataset rendered with row numbers and column headers.'
>
>    - **Step 3: Column Selection & Validation:**
>      - 'In the top toolbar, the **Sort Column** dropdown automatically identifies and tags numeric columns. If we were to attempt sorting a non-numeric column like City, our system displays a validation warning explaining that only numeric columns are allowed.'
>      - 'Let us select `Price_USD`, containing 3,000 floating-point numbers.'
>
>    - **Step 4: Running the Performance Benchmark:**
>      - 'We select `★ Compare All (All 5 Algorithms)` and click **▶ Run Performance Benchmark**.'
>      - 'Instantly, the results are calculated and presented:'
>        1. The **Trophy Champion Banner** highlights the fastest algorithm (for example: *Merge Sort or Quick Sort with execution time of ~0.35 ms and a 15.7x speedup over Insertion Sort*).
>        2. The **Custom Interactive Bar Chart** dynamically renders relative execution time bars with color coding (emerald green for the winner, blue for runners-up, red for the slowest).
>        3. The **Results Table** displays exact ranks (1st to 5th), millisecond and nanosecond times, pass/fail verification, and theoretical Big-O complexities.
>        4. Under the **🔢 Sorted Data Preview** tab, the application displays the sorted numerical values in verified ascending order.
>
>    - **Step 5: Exporting the Report:**
>      - 'Users can also click **💾 Export Report** to save a formal evaluation report to a `.txt` file.'
>
>    - **Step 6: Testing with a Large Dataset (10,000 rows):**
>      - 'Let's now test `sensor_readings.csv` with 10,000 rows sorting `Temperature_C`.'
>      - 'Here, the difference between $O(n^2)$ and $O(n \log n)$ becomes dramatically apparent: Insertion Sort takes ~31.8 ms, whereas Merge Sort takes only ~0.96 ms—over 32 times faster!'
>
>    - **Step 7: Automated Unit Tests:**
>      - 'Finally, we have an automated test suite with 40 regression tests verifying edge cases including empty arrays, single elements, reverse-sorted arrays, identical values, and large random arrays, all of which pass 100%.'"

---

## 🎙️ PART 5: Conclusion (All Members)

**Member 1:**
> "To conclude, our application successfully satisfies all requirements specified in the IC4302 coursework document. We have robust CSV uploading, automated numeric column detection, and thorough error handling."

**Member 2:**
> "We implemented and verified all five algorithms—Insertion, Shell, Merge, Quick, and Heap Sort—analyzing their empirical runtimes against theoretical Big-O bounds."

**Member 3:**
> "And our GUI provides an intuitive, user-friendly benchmarking dashboard with high-resolution nanosecond timing, visual bar chart comparisons, and champion algorithm identification.
> Thank you for your time, and we welcome any questions!"
