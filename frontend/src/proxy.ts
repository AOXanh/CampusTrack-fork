import { NextRequest, NextResponse } from "next/server";

/**
 * Please refer lang mo regarding sa next.js middleware/proxy:
 * https://nextjs.org/docs/app/getting-started/proxy
 */

export function proxy(request: NextRequest) {
  console.log("Hi from middleware");

  return NextResponse.next();
}

export const config = {
  matcher: []
}