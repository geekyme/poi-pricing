# POI Pricing

A little experiment to see how we can dynamically calculate pricing with [Apache POI](https://poi.apache.org/) + a spreadsheet

First, `cd api`. Then:

### Setup

1. `./gradlew bootRun`

2. GET `http://localhost:8080/calculate/780` or any number `http://localhost:8080/calculate/{value}`

### The spreadsheets

SimpleCalculation.xlsx - Just a simple spreadsheet with 4 values to be summed up.
AdvancedCalculation.xlsx - Advanced spreadsheet that calculates projected revenue of a clinic. With colors, macros, lots of values and formulas - A real scenario of what an actuary will produce.

### Run the load test

1. Install [k6](https://k6.io/)

2. Run the simple scenario with 100 concurrent users over 60s - `k6 run loadtest/simple.js --vus 100 --duration 60s`

3. Tested on a machine with 6 cores - Got 9-10K rps. See this [PR](https://github.com/geekyme/poi-pricing/pull/3)

|      Loadtest       |      Monitor      |
| :-----------------: | :---------------: |
| ![](./loadtest.png) | ![](./memory.png) |

4. Optionally, to run the advanced spreadsheet - `k6 run loadtest/advanced.js --vus 100 --duration 60s`. This one clocks about 250 rps

5. Optionally, to run the all spreadsheets - `k6 run loadtest/index.js --vus 100 --duration 60s`
