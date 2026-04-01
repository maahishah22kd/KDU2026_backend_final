export interface ConfigResponse {
  cleaningTypes: {
    id: string;
    label: string;
    basePrice: number;
  }[];

  frequencies: {
    id: string;
    label: string;
  }[];

  extras: {
    id: string;
    label: string;
    price: number;
  }[];

  timeSlots: {
    id: string;
    label: string;
    available: boolean;
  }[];
}

