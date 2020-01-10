import { loadtestSimple } from "./simple.js";
import { loadtestAdvanced } from "./advanced.js";
import { group } from "k6";

export default function() {
  group("simple", loadtestSimple);
  group("advanced", loadtestAdvanced);
}
