import { NfcTag } from "@/types/models.types";

export type NfcTagResponse = {
  content: NfcTag[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  }
};