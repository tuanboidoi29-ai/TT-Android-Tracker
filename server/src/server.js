import express from 'express';
import cors from 'cors';
import Database from 'better-sqlite3';

const app = express();
const db = new Database(process.env.DB_PATH || 'tracker.db');
const ADMIN_TOKEN = process.env.ADMIN_TOKEN || 'CHANGE_ME_ADMIN_TOKEN';

db.exec(`CREATE TABLE IF NOT EXISTS devices (device_id TEXT PRIMARY KEY, last_seen INTEGER, lat REAL, lon REAL, accuracy REAL); CREATE TABLE IF NOT EXISTS locations (id INTEGER PRIMARY KEY AUTOINCREMENT, device_id TEXT NOT NULL, lat REAL NOT NULL, lon REAL NOT NULL, accuracy REAL, ts INTEGER NOT NULL);`);
app.use(cors());
app.use(express.json({limit:'32kb'}));

function admin(req,res,next){ if(req.get('authorization') !== `Bearer ${ADMIN_TOKEN}`) return res.status(401).json({error:'unauthorized'}); next(); }

app.get('/health', (_,res)=>res.json({ok:true}));
app.post('/api/location',(req,res)=>{
  const {device_id,latitude,longitude,accuracy,timestamp}=req.body||{};
  if(!device_id || typeof latitude!=='number' || typeof longitude!=='number') return res.status(400).json({error:'invalid payload'});
  const ts=Number.isFinite(timestamp)?timestamp:Date.now();
  db.prepare('INSERT INTO locations(device_id,lat,lon,accuracy,ts) VALUES(?,?,?,?,?)').run(device_id,latitude,longitude,accuracy??null,ts);
  db.prepare('INSERT INTO devices(device_id,last_seen,lat,lon,accuracy) VALUES(?,?,?,?,?) ON CONFLICT(device_id) DO UPDATE SET last_seen=excluded.last_seen,lat=excluded.lat,lon=excluded.lon,accuracy=excluded.accuracy').run(device_id,ts,latitude,longitude,accuracy??null);
  res.json({ok:true});
});
app.get('/api/devices',admin,(_,res)=>res.json(db.prepare('SELECT * FROM devices ORDER BY last_seen DESC').all()));
app.get('/api/devices/:id/history',admin,(req,res)=>res.json(db.prepare('SELECT lat,lon,accuracy,ts FROM locations WHERE device_id=? ORDER BY ts ASC').all(req.params.id)));

const port=Number(process.env.PORT||3000);
app.listen(port,()=>console.log(`TT Tracker API listening on ${port}`));
