# POI Pricing

A little experiment to see how we can dynamically calculate pricing with [Apache POI](https://poi.apache.org/) + a spreadsheet

First, `cd api`. Then:

### Setup

1. `./gradlew bootRun`

2. GET `http://localhost:8080/calculate/780` or any number `http://localhost:8080/calculate/{value}`

### Run the load test

1. Install [k6](https://k6.io/)

2. Run with 100 concurrent users over 60s - `k6 run loadtest.js --vus 100 --duration 60s`

3. Tested on a machine with 6 cores - Got 9-10K rps. See this [PR](https://github.com/geekyme/poi-pricing/pull/3)
