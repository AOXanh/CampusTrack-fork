"use client";

import { useEffect, useState } from "react";
import { getNfcTags } from "@/api/nfc-tags.api";
import { NfcTag } from "@/types/models.types";
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

export default function Sample() {
  // States
  const [nfcTags, setNfcTags] = useState<NfcTag[]>([]);

  // Use effects
  useEffect(() => {
    async function fetchNfcTags() {
      const response = await getNfcTags();
      setNfcTags(response.content);
    }

    if (nfcTags.length < 1)
      fetchNfcTags().then().catch();
  }, [nfcTags]);

  return (
    <div>

      <Table>
        <TableCaption>Para sa sample code please check ko ngadto sa <code>/components/sample.tsx</code> for reference.</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Created At</TableHead>
            <TableHead>Asset ID</TableHead>
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
                <TableCell>{item.assetId}</TableCell>
                <TableCell>{item.uid}</TableCell>
                <TableCell>{item.status}</TableCell>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>

    </div>
  )
}