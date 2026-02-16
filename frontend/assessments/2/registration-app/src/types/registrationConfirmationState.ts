import type { ConfirmationResponse } from "./registrationConfirmation";

export interface ConfirmationState {
  data: ConfirmationResponse|null;
  status: "queued"|"loading"|"succeeded"|"failed";
  error: string|null;
}