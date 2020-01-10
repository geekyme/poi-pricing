import { check, group } from "k6";
import http from "k6/http";

export function loadtestAdvanced() {
  // http://localhost:8080/calculate/advanced?medicard=0.1&managedCare=0.1&privateInsurance=0.2&selfPay=0.6
  const medicard = Math.random() * 0.25;
  const managedCare = Math.random() * 0.25;
  const privateInsurance = Math.random() * 0.25;
  const selfPay = 1 - medicard - managedCare - privateInsurance;
  let res = http.get(
    `http://localhost:8080/calculate/advanced?medicard=${medicard}&managedCare=${managedCare}&privateInsurance=${privateInsurance}&selfPay=${selfPay}`
  );
  // TODO: need to check whether correct response is given based on the input
  // there must be no race conditions
}

export default function() {
  group("advanced", loadtestAdvanced);
}
