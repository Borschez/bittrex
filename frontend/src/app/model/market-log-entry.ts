import { Market } from "./market";

export class MarketLogEntry {
  id: number;
  fromStamp: Date;
  toStamp: Date;
  percent: number;
  importance: number;
  market: Market;
}
