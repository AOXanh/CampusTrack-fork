import { NfcTag } from "@/types/models.types";

// ========== Nfc Tag Responses ==========
export type GetAllNfcTagResponse = {
  content: NfcTag[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  }
};

// ========== Asset Responses ==========

// ========== Building Responses ==========

// ========== Incident Responses ==========

// ========== Maintenance Record Responses ==========

// ========== Room Responses ==========

// ========== User Responses ==========

// ========== Auth Responses ==========
