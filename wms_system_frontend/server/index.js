import express from "express";
import cors from "cors";
import { createPool } from "./db.js";
import { getModuleDefinition, moduleDefinitions } from "./modules.js";

const app = express();
const port = Number(process.env.PORT || 3001);
const pool = createPool();

app.use(cors());
app.use(express.json());

function buildSelectSql(moduleKey) {
  const definition = getModuleDefinition(moduleKey);
  const columns = ["id", ...definition.fields, "created_at AS createdAt", "updated_at AS updatedAt"].join(", ");
  return `SELECT ${columns} FROM ${definition.table} ORDER BY updated_at DESC, created_at DESC`;
}

function sanitizePayload(moduleKey, payload) {
  const definition = getModuleDefinition(moduleKey);
  const nextPayload = {};

  definition.fields.forEach((field) => {
    if (payload[field] === undefined) return;
    nextPayload[field] = payload[field];
  });

  return nextPayload;
}

function createRecordId(moduleKey) {
  return `${moduleKey}-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`;
}

async function ensureSeedData() {
  for (const [moduleKey, definition] of Object.entries(moduleDefinitions)) {
    const [rows] = await pool.query(`SELECT COUNT(*) AS total FROM ${definition.table}`);
    if (rows[0].total > 0) continue;

    for (const row of definition.seedRows) {
      const columns = ["id", ...definition.fields];
      const placeholders = columns.map(() => "?").join(", ");
      const values = columns.map((column) => row[column] ?? null);
      await pool.query(
        `INSERT INTO ${definition.table} (${columns.join(", ")}) VALUES (${placeholders})`,
        values
      );
    }
  }
}

app.get("/api/health", async (_req, res) => {
  try {
    await pool.query("SELECT 1");
    res.json({ ok: true, message: "MySQL connection is available." });
  } catch (error) {
    res.status(500).json({ ok: false, message: error.message });
  }
});

app.get("/api/crud/:moduleKey", async (req, res) => {
  const definition = getModuleDefinition(req.params.moduleKey);
  if (!definition) {
    res.status(404).json({ message: "Unknown module." });
    return;
  }

  try {
    const [rows] = await pool.query(buildSelectSql(req.params.moduleKey));
    res.json(rows);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.post("/api/crud/:moduleKey", async (req, res) => {
  const definition = getModuleDefinition(req.params.moduleKey);
  if (!definition) {
    res.status(404).json({ message: "Unknown module." });
    return;
  }

  const payload = sanitizePayload(req.params.moduleKey, req.body);
  const id = req.body.id || createRecordId(req.params.moduleKey);
  const columns = ["id", ...Object.keys(payload)];
  const placeholders = columns.map(() => "?").join(", ");
  const values = [id, ...Object.values(payload)];

  try {
    await pool.query(`INSERT INTO ${definition.table} (${columns.join(", ")}) VALUES (${placeholders})`, values);
    const [createdRows] = await pool.query(
      `SELECT id, ${definition.fields.join(", ")}, created_at AS createdAt, updated_at AS updatedAt FROM ${definition.table} WHERE id = ?`,
      [id]
    );
    res.status(201).json(createdRows[0]);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.put("/api/crud/:moduleKey/:id", async (req, res) => {
  const definition = getModuleDefinition(req.params.moduleKey);
  if (!definition) {
    res.status(404).json({ message: "Unknown module." });
    return;
  }

  const payload = sanitizePayload(req.params.moduleKey, req.body);
  const entries = Object.entries(payload);

  if (!entries.length) {
    res.status(400).json({ message: "No valid fields provided." });
    return;
  }

  const setClause = entries.map(([field]) => `${field} = ?`).join(", ");
  const values = [...entries.map(([, value]) => value), req.params.id];

  try {
    await pool.query(`UPDATE ${definition.table} SET ${setClause} WHERE id = ?`, values);
    const [rows] = await pool.query(
      `SELECT id, ${definition.fields.join(", ")}, created_at AS createdAt, updated_at AS updatedAt FROM ${definition.table} WHERE id = ?`,
      [req.params.id]
    );

    if (!rows.length) {
      res.status(404).json({ message: "Record not found." });
      return;
    }

    res.json(rows[0]);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.delete("/api/crud/:moduleKey/:id", async (req, res) => {
  const definition = getModuleDefinition(req.params.moduleKey);
  if (!definition) {
    res.status(404).json({ message: "Unknown module." });
    return;
  }

  try {
    await pool.query(`DELETE FROM ${definition.table} WHERE id = ?`, [req.params.id]);
    res.status(204).end();
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.post("/api/crud/:moduleKey/reset", async (req, res) => {
  const definition = getModuleDefinition(req.params.moduleKey);
  if (!definition) {
    res.status(404).json({ message: "Unknown module." });
    return;
  }

  const connection = await pool.getConnection();

  try {
    await connection.beginTransaction();
    await connection.query(`DELETE FROM ${definition.table}`);

    for (const row of definition.seedRows) {
      const columns = ["id", ...definition.fields];
      const placeholders = columns.map(() => "?").join(", ");
      const values = columns.map((column) => row[column] ?? null);
      await connection.query(
        `INSERT INTO ${definition.table} (${columns.join(", ")}) VALUES (${placeholders})`,
        values
      );
    }

    await connection.commit();
    const [rows] = await pool.query(buildSelectSql(req.params.moduleKey));
    res.json(rows);
  } catch (error) {
    await connection.rollback();
    res.status(500).json({ message: error.message });
  } finally {
    connection.release();
  }
});

app.listen(port, async () => {
  try {
    await ensureSeedData();
    console.log(`API server running at http://localhost:${port}`);
  } catch (error) {
    console.error("Server started, but MySQL seed initialization failed.");
    console.error(error.message);
  }
});
