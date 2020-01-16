import { check, group } from "k6";
import http from "k6/http";

export function loadtestAdvanced() {
  // http://localhost:8080/calculate/advanced?medicard=0.1&managedCare=0.1&privateInsurance=0.2&selfPay=0.6
  const medicard = Math.random() * 0.25;
  const managedCare = Math.random() * 0.25;
  const privateInsurance = Math.random() * 0.25;
  const selfPay = 1 - medicard - managedCare - privateInsurance;
  http.get(
    `http://localhost:8080/calculate/advanced/pool?medicard=${medicard}&managedCare=${managedCare}&privateInsurance=${privateInsurance}&selfPay=${selfPay}`
  );

  // this acts as a control to see whether there is thread safety
  const res = http.get(
    "http://localhost:8080/calculate/advanced/pool?medicard=0.1&managedCare=0.1&privateInsurance=0.2&selfPay=0.6"
  );
  check(res, {
    "is correct response": r => {
      if (r.status === 200) {
        return parseInt(r.body) === 90156;
      } else {
        // ignoring non-200 checks as we just want to know whether response is correct for successful requests
        return true;
      }
    }
  });
}

export default function() {
  group("advanced", loadtestAdvanced);
}
