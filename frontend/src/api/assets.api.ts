import { Asset } from "@/types/models.types";
import apiClient from "@/api/client.api";

export async function getAsset(assetId: number): Promise<Asset> {
  const response = await apiClient.get(`/assets/${assetId}`);
  return response.data;
}