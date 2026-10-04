import apiClient from "@/api/client.api";
import { GetAllNfcTagResponse } from "@/types/response.types";

export async function getAllNfcTags(page: number = 1, size: number = 10): Promise<GetAllNfcTagResponse> {
  const response = await apiClient.get<GetAllNfcTagResponse>(`/nfc-tags?page=${page}&size=${size}`);
  return response.data;
}