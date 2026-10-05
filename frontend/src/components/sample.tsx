"use client";

import { useEffect, useState } from "react";
import { getAllNfcTags } from "@/api/nfc-tags.api";
import { Asset, NfcTag } from "@/types/models.types";
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { getAsset } from "@/api/assets.api";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from "@/components/ui/pagination";
import { Skeleton } from "@/components/ui/skeleton";

function RowSkeleton() {
  return (
    <TableRow>
      <TableCell><Skeleton className="h-4 w-[50px]" /></TableCell>
      <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
      <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
      <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
      <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
    </TableRow>
  )
}

export default function Sample() {
  // States
  const [nfcTags, setNfcTags] = useState<NfcTag[]>([]);
  const [selectedAsset, setSelectedAsset] = useState<Asset | null>(null);
  const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });
  const [currentPage, setCurrentPage] = useState(1);

  // Constants
  const { totalPages } = pageInfo;

  // Use effects
  useEffect(() => {
    async function fetchNfcTags() {
      setPageInfo({ totalPages: 0, totalElements: 0 });
      setNfcTags([]);

      const response = await getAllNfcTags(currentPage);
      const mappedContent = await Promise.all(
        response.content.map(async (item) => {
          const assetResponse = await getAsset(item.assetId);
          return { ...item, asset: assetResponse };
        })
      );

      setPageInfo({ totalPages: response.page.totalPages, totalElements: response.page.totalElements });
      setNfcTags(mappedContent);
    }

    fetchNfcTags();
  }, [currentPage]);

  // Functions
  function openAssetModal(asset: Asset | null) {
    if (!asset) return;
    setSelectedAsset(asset);
  }

  function getPageNumbers(current: number, total: number) {
    const pages = new Set<number | string>();

    for (let i = 1; i < total; i++) {
      if (i <= 3 || i === total - 1 || Math.abs(i - current) <= 1)
        pages.add(i);
      else
        pages.add("...");
    }

    return [...pages];
  }

  function setPage(page: number) {
    setCurrentPage(page);
  }

  return (
    <>
      <Dialog open={!!selectedAsset}>
        <DialogContent showCloseButton={false}>
          <DialogHeader>
            <DialogTitle>{selectedAsset && selectedAsset.name}</DialogTitle>
          </DialogHeader>

          <ul className="space-y-4">
            <li><span className="font-semibold">Asset ID:</span> {selectedAsset && selectedAsset.id}</li>
            <li><span className="font-semibold">Created At:</span> {selectedAsset && selectedAsset.createdAt}</li>
            <li><span className="font-semibold">Name:</span> {selectedAsset && selectedAsset.name}</li>
            <li><span className="font-semibold">Brand:</span> {selectedAsset && (selectedAsset.brand ?? "No brand")}</li>
            <li><span className="font-semibold">Model:</span> {selectedAsset && (selectedAsset.model ?? "No model")}</li>
            <li><span className="font-semibold">Serial Number:</span> {selectedAsset && (selectedAsset.serialNumber ?? "No serial number")}</li>
            <li><span className="font-semibold">Category:</span> {selectedAsset && selectedAsset.category}</li>
            <li><span className="font-semibold">Status:</span> {selectedAsset && selectedAsset.status}</li>
            <li><span className="font-semibold">Condition:</span> {selectedAsset && selectedAsset.condition}</li>
            <li><span className="font-semibold">Criticality:</span> {selectedAsset && selectedAsset.criticality}</li>
          </ul>

          <DialogFooter>
            <Button variant="outline" onClick={() => setSelectedAsset(null)}>Close</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <div className="space-y-6">
        <Table>
          <TableCaption>Para sa sample code please check ko ngadto sa <code>/components/sample.tsx</code> for reference.</TableCaption>
          <TableHeader>
            <TableRow>
              <TableHead>ID</TableHead>
              <TableHead>Created At</TableHead>
              <TableHead>Asset</TableHead>
              <TableHead>UID</TableHead>
              <TableHead>Status</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {nfcTags.length > 0 && nfcTags.map(item => {
              const formattedCreatedAt = new Date(item.createdAt).toLocaleString("en-US", {
                month: "long",
                day: "numeric",
                year: "numeric",
                hour: "numeric",
                minute: "2-digit"
              });

              return (
                <TableRow key={`nfc-tag-item-${item.id}`}>
                  <TableCell>{item.id}</TableCell>
                  <TableCell>{formattedCreatedAt}</TableCell>
                  <TableCell>{item.asset ? (<Button variant="link" onClick={() => openAssetModal(item.asset)}>{item.asset.name}</Button>) : "Asset not found"}</TableCell>
                  <TableCell>{item.uid}</TableCell>
                  <TableCell>{item.status}</TableCell>
                </TableRow>
              )
            })}

            {nfcTags.length < 1 && (
              <>
                <RowSkeleton/>
                <RowSkeleton/>
                <RowSkeleton/>
                <RowSkeleton/>
                <RowSkeleton/>
              </>
            )}
          </TableBody>
        </Table>

        <Pagination>
          <PaginationContent>
            <PaginationItem hidden={currentPage == 1}>
              <PaginationPrevious onClick={() => setPage(currentPage - 1)} />
            </PaginationItem>

            {getPageNumbers(currentPage, totalPages).map((item, index) => {
              if (typeof item === "number")
                return (
                  <PaginationItem key={`pagination-index-${index}`}>
                    <PaginationLink onClick={() => setPage(item)} className={`${(currentPage === item) && "bg-primary text-white"}`}>{item}</PaginationLink>
                  </PaginationItem>
                )
              else
                return (
                  <PaginationItem key={`pagination-index-elipsis-${index}`}>
                    <PaginationEllipsis />
                  </PaginationItem>
                )
            })}

            <PaginationItem hidden={currentPage == totalPages - 1}>
              <PaginationNext onClick={() => setPage(currentPage + 1)} />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      </div>
    </>
  )
}