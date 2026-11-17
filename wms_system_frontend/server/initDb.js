import fs from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { createPoolWithOverrides } from "./db.js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function main() {
  const schemaPath = path.join(__dirname, "schema.sql");
  const sql = await fs.readFile(schemaPath, "utf8");
  const pool = createPoolWithOverrides({ database: undefined });

  try {
    await pool.query(sql);
    console.log("MySQL schema initialized successfully.");
  } finally {
    await pool.end();
  }
}

main().catch((error) => {
  console.error("Failed to initialize MySQL schema.");
  console.error(error.message);
  process.exit(1);
});
