import type { ConfigResponse } from "./config";

export interface ConfigState {
  data: ConfigResponse|null;
  status: "idle"|"loading"|"succeeded"|"failed";
  error: string|null;
}
