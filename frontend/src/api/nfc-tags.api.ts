import apiClient from "@/api/client.api";
import { NfcTagResponse } from "@/types/response.types";

export async function getNfcTags(): Promise<NfcTagResponse> {
  const response = await apiClient.get<NfcTagResponse>("/nfc-tags");
  return response.data;
}