from dotenv import load_dotenv
from supabase import Client, create_client
import os

load_dotenv()

SUPABASE_URL: str | None = os.getenv("SUPABASE_URL")
SUPABASE_KEY: str | None = os.getenv("SUPABASE_KEY")

def get_supabase_client() -> Client:
  return create_client(
    SUPABASE_URL if SUPABASE_URL is not None else "None",
    SUPABASE_KEY if SUPABASE_KEY is not None else "None"
  )