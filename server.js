import express from 'express';
import { readFile, writeFile, mkdir } from 'fs/promises';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const DATA_DIR = join(__dirname, 'data');
const DATA_FILE = join(DATA_DIR, 'store.json');
const PORT = 3001;

const app = express();
app.use(express.json({ limit: '10mb' }));

// Ensure data directory exists on startup
await mkdir(DATA_DIR, { recursive: true });

app.get('/api/state', async (_req, res) => {
  try {
    const raw = await readFile(DATA_FILE, 'utf-8');
    res.json(JSON.parse(raw));
  } catch {
    // File doesn't exist yet — return null so the client uses defaults
    res.json(null);
  }
});

app.post('/api/state', async (req, res) => {
  try {
    await writeFile(DATA_FILE, JSON.stringify(req.body, null, 2), 'utf-8');
    res.json({ ok: true });
  } catch (err) {
    console.error('Failed to save state:', err);
    res.status(500).json({ error: 'Failed to save' });
  }
});

app.listen(PORT, () => {
  console.log(`Store server running on http://localhost:${PORT}`);
});
