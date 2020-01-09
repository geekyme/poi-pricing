import { check } from "k6";
import http from "k6/http";

export default function() {
  const value = Math.ceil(Math.random() * 10000);
  let res = http.get("http://localhost:8080/calculate/" + value);
  check(res, {
    "is correct response": r => {
      return parseInt(r.body) === 90000 + value;
    }
  });
}
