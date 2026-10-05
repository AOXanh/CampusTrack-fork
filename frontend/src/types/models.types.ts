// Nfc Tag Model
export type NfcTag = {
  id: number;
  createdAt: string;
  assetId: number;
  uid: string;
  status: string;

  // Relationships
  asset: Asset | null;
};

// Asset Model
export type Asset = {
  "id": number;
  "roomId": number;
  "name": string;
  "brand": string | null;
  "model": string | null;
  "serialNumber": string | null;
  "category": string;
  "status": string;
  "condition": string;
  "criticality": string;
  "createdAt": string;

  // Relationships
};

// Building Model

// Incident Model

// Maintenance Record Model

// Room Model

// User Model

// Auth Model