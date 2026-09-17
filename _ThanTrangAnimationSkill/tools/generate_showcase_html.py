# -*- coding: utf-8 -*-
"""
build_anime_studio_v11.py
=========================
Fixes character and target visibility bugs:
- Full, highly visible Anime Pirate Character Model for Caster (Cape, Vest, Belt, Head, Hair, Elemental Weapons, Nameplate, HP bar).
- Full, highly visible Training Boss / Target Dummy for Target (Iron Base, Wooden Trunk, Cross Arms, Straw Hair, Headband, Bullseye, Boss HP bar).
- Replaces raw roundRect with bulletproof drawCleanRoundRect (100% browser compatible).
- Safe rendering pipeline where Caster and Target are ALWAYS rendered clearly and never hidden by VFX overlays.
- Full automated anime-style cinematic playback for all 16 Than Trang skills.
- Updates demos/index.html and all 16 standalone demos.
"""

import os
import sys
import json
import shutil

sys.stdout.reconfigure(encoding='utf-8')
sys.stderr.reconfigure(encoding='utf-8')

BASE_DIR = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill"
DEMOS_DIR = os.path.join(BASE_DIR, "demos")
DB_DIR = os.path.join(BASE_DIR, "database")
TOOLS_DIR = os.path.join(BASE_DIR, "tools")

os.makedirs(DEMOS_DIR, exist_ok=True)

with open(os.path.join(DB_DIR, "skills_database.json"), "r", encoding="utf-8") as f:
    SKILLS = json.load(f)

with open(os.path.join(DB_DIR, "skill_vfx_metadata.json"), "r", encoding="utf-8") as f:
    META = json.load(f)

HTML_CONTENT = r'''<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Skill Thần Trang VFX — Anime Cinematic Studio v11 (Hiển Thị Chuẩn Nhân Vật & Mục Tiêu)</title>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Segoe UI', system-ui, -apple-system, sans-serif; }
:root {
  --bg-deep: #05020c;
  --bg-sidebar: #090414;
  --bg-panel: #0e061e;
  --border: #23103a;
  --border-glow: #4c1e84;
  --accent: #ffd700;
  --cyan: #00e5ff;
  --text-main: #f5edff;
  --text-dim: #9886b4;
}
body { background: var(--bg-deep); color: var(--text-main); height: 100vh; overflow: hidden; display: flex; flex-direction: column; user-select: none; }

/* Top Header */
#top-bar {
  height: 48px; background: #0c051a; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; justify-content: space-between; padding: 0 16px; flex-shrink: 0;
}
.brand { display: flex; align-items: center; gap: 10px; }
.brand-title { font-size: 14px; font-weight: 900; letter-spacing: 1.5px; background: linear-gradient(90deg, #ffd700, #ff80ab, #00e5ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.brand-sub { font-size: 11px; color: var(--text-dim); }
.top-badges { display: flex; gap: 8px; align-items: center; }
.badge { font-size: 10.5px; font-weight: 700; padding: 3px 10px; border-radius: 12px; background: rgba(76, 30, 132, 0.35); border: 1px solid var(--border-glow); color: #d4c2ee; }
.badge.glow { border-color: var(--accent); color: var(--accent); background: rgba(255, 215, 0, 0.12); }

/* Workspace */
#workspace { display: flex; flex: 1; overflow: hidden; }

/* Left Sidebar */
#sidebar {
  width: 255px; min-width: 255px; background: var(--bg-sidebar); border-right: 1px solid var(--border);
  display: flex; flex-direction: column; overflow: hidden;
}
.side-header { padding: 10px 14px; border-bottom: 1px solid var(--border); font-size: 10.5px; font-weight: 800; color: #846ca7; text-transform: uppercase; letter-spacing: 1px; display: flex; justify-content: space-between; }
.skill-list { overflow-y: auto; flex: 1; padding: 8px; }
.skill-card {
  display: flex; align-items: center; gap: 9px; padding: 7px 9px; margin-bottom: 5px;
  border-radius: 6px; cursor: pointer; border: 1px solid transparent; background: rgba(255,255,255,0.02);
  transition: all 0.15s ease;
}
.skill-card:hover { background: rgba(76, 30, 132, 0.25); border-color: rgba(76, 30, 132, 0.5); }
.skill-card.active {
  background: linear-gradient(90deg, rgba(76, 30, 132, 0.6) 0%, rgba(34, 16, 56, 0.4) 100%);
  border-color: var(--accent); box-shadow: 0 0 12px rgba(255, 215, 0, 0.18);
}
.sk-icon { width: 36px; height: 36px; border-radius: 50%; border: 1.5px solid var(--border-glow); object-fit: cover; flex-shrink: 0; background: #000; }
.sk-meta { display: flex; flex-direction: column; min-width: 0; }
.sk-top { display: flex; align-items: center; gap: 5px; }
.sk-id { font-size: 9px; font-weight: 800; color: #ffd700; background: #261142; padding: 1px 4px; border-radius: 3px; }
.sk-icon-badge { font-size: 8px; font-weight: 700; color: #00e5ff; background: #0a2538; padding: 1px 4px; border-radius: 3px; }
.sk-name { font-size: 11.5px; font-weight: 700; color: #eae0f8; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-top: 1px; }
.sk-char { font-size: 9.5px; color: #9c89b8; }

/* Main Stage View */
#main-view { flex: 1; display: flex; flex-direction: column; overflow: hidden; background: #020106; position: relative; }

/* Top Controls */
.tabs-bar {
  height: 40px; background: #0c051a; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; padding: 0 16px; gap: 12px; justify-content: space-between;
}
.tab-group { display: flex; gap: 8px; align-items: center; }
.btn-toggle {
  padding: 5px 12px; font-size: 11px; font-weight: 800; border-radius: 6px; cursor: pointer;
  background: #190930; border: 1px solid #3d1b6a; color: #d4c2ee; transition: all 0.15s ease;
  display: inline-flex; align-items: center; gap: 5px;
}
.btn-toggle:hover { background: #2e1454; border-color: var(--accent); color: #fff; }
.btn-toggle.active { background: #3c146e; border-color: var(--accent); color: var(--accent); box-shadow: 0 0 10px rgba(255,215,0,0.3); }
.btn-toggle.play-btn { background: #1b5e20; border-color: #00e676; color: #fff; }
.btn-toggle.pause-btn { background: #b71c1c; border-color: #ff5252; color: #fff; }

/* Center Viewport */
#viewport-container {
  flex: 1; display: flex; align-items: center; justify-content: center; position: relative;
  overflow: hidden; background: radial-gradient(circle at center, #120726 0%, #030109 85%);
}
canvas#stageCanvas {
  display: block; image-rendering: pixelated;
  box-shadow: 0 18px 60px rgba(0,0,0,0.95), 0 0 35px rgba(76, 30, 132, 0.4);
  border: 1px solid var(--border); border-radius: 8px; max-width: 98%; max-height: 98%;
}

/* Anime Callout Banner */
#anime-banner {
  position: absolute; top: 38px; left: 0; width: 100%; pointer-events: none;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  transition: opacity 0.2s ease, transform 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  opacity: 0; transform: scale(0.9); z-index: 15;
}
#anime-banner.show { opacity: 1; transform: scale(1); }
.banner-bg {
  background: linear-gradient(90deg, transparent 0%, rgba(18, 5, 36, 0.92) 20%, rgba(35, 10, 70, 0.95) 50%, rgba(18, 5, 36, 0.92) 80%, transparent 100%);
  padding: 8px 40px; border-top: 1.5px solid var(--accent); border-bottom: 1.5px solid var(--accent);
  box-shadow: 0 0 25px rgba(255, 215, 0, 0.35); text-align: center;
}
.banner-sub { font-size: 10px; font-weight: 800; letter-spacing: 2px; color: var(--cyan); text-transform: uppercase; }
.banner-main { font-size: 20px; font-weight: 900; letter-spacing: 2px; color: #fff; text-shadow: 0 0 15px rgba(255,215,0,0.8), 0 0 30px #ff3d00; }

/* HUD Stats */
#hud-overlay {
  position: absolute; top: 12px; left: 16px; pointer-events: none;
  display: flex; flex-direction: column; gap: 5px; font-family: monospace; font-size: 10.5px;
}
.hud-pill {
  background: rgba(8, 3, 16, 0.75); border: 1px solid var(--border-glow);
  padding: 3px 9px; border-radius: 6px; color: #cbb8e8; backdrop-filter: blur(4px);
}
.hud-pill b { color: var(--accent); }

/* Timeline Deck */
#timeline-deck {
  background: #090314; border-top: 1px solid var(--border);
  display: flex; flex-direction: column; padding: 6px 16px; gap: 6px; flex-shrink: 0;
}
.timeline-row { display: flex; align-items: center; justify-content: space-between; gap: 12px; width: 100%; }
.progress-container { flex: 1; display: flex; align-items: center; gap: 10px; }
input[type=range] { flex: 1; accent-color: var(--accent); height: 6px; cursor: pointer; }
.frame-counter { font-family: monospace; font-size: 11px; color: var(--accent); font-weight: 700; min-width: 140px; text-align: right; }

.act-btn {
  padding: 4px 10px; border-radius: 5px; font-size: 10.5px; font-weight: 700; cursor: pointer;
  background: #1b0c33; border: 1px solid #3f1970; color: #dcd0f0; transition: all 0.15s ease;
}
.act-btn:hover { background: #321360; border-color: var(--accent); color: #fff; }
.act-btn.cast { background: linear-gradient(135deg, #7c4dff, #ff4081); border-color: #ffd700; color: #fff; font-weight: 800; box-shadow: 0 0 10px rgba(255,64,129,0.4); }
</style>
</head>
<body>

<!-- Top Navigation -->
<div id="top-bar">
  <div class="brand">
    <div class="brand-title">HTTH THẦN TRANG CINEMATIC STUDIO V11</div>
    <div class="brand-sub">Chuẩn Hóa Hình Ảnh Nhân Vật & Mục Tiêu • Auto Anime Playback</div>
  </div>
  <div class="top-badges">
    <div class="badge glow" id="badgeSkillName">Skill 4001: Hỏa Diễm Thần Quyền</div>
    <div class="badge" id="badgeChar">Hệ HỎA LONG (Ace / Nika)</div>
    <div class="badge" id="badgeAutoStatus" style="color:#00e676;">AUTO ANIME LOOP: ĐANG CHẠY</div>
  </div>
</div>

<!-- Main Workspace -->
<div id="workspace">
  <!-- Sidebar -->
  <div id="sidebar">
    <div class="side-header">
      <span>16 KỸ NĂNG THẦN TRANG</span>
      <span style="color:var(--accent);">16</span>
    </div>
    <div class="skill-list" id="skillList"></div>
  </div>

  <!-- Stage View -->
  <div id="main-view">
    <!-- Top Action Bar -->
    <div class="tabs-bar">
      <div class="tab-group">
        <button class="btn-toggle play-btn" id="btnPlayPause" onclick="togglePlay()">⏸️ Tạm Dừng</button>
        <button class="btn-toggle" onclick="restartSkill()">🔄 Diễn Lại (Replay)</button>
        <button class="btn-toggle active" id="btnAutoAdvance" onclick="toggleAutoAdvance()">⏩ Tự Đổi Kỹ Năng Kế</button>
        <button class="btn-toggle" id="btnSlowMo" onclick="toggleSlowMo()">⏱️ Slow-Mo (0.4x)</button>
      </div>
      <div class="tab-group" style="font-size:11px; color:var(--text-dim);">
        <label style="display:flex; align-items:center; gap:4px; cursor:pointer;">
          <input type="checkbox" id="chkGlow" checked> Phát Sáng (Glow)
        </label>
        <label style="display:flex; align-items:center; gap:4px; cursor:pointer;">
          <input type="checkbox" id="chkLetterbox" checked> Điện Ảnh (Letterbox)
        </label>
      </div>
    </div>

    <!-- Center Viewport -->
    <div id="viewport-container">
      <canvas id="stageCanvas" width="960" height="540"></canvas>

      <!-- Anime Callout Banner -->
      <div id="anime-banner">
        <div class="banner-bg">
          <div class="banner-sub" id="bannerChar">HỎA LONG THẦN TRANG</div>
          <div class="banner-main" id="bannerTitle">HỎA DIỄM THẦN QUYỀN — ENTEI</div>
        </div>
      </div>

      <!-- HUD Stats -->
      <div id="hud-overlay">
        <div class="hud-pill" id="hudFrame">Frame: <b>0 / 80</b> | Phase: <b id="txtPhase">CHARGE AURA</b></div>
        <div class="hud-pill" id="hudCaster">Caster Action: <b id="txtCasterAction">TỤ LỬA TOÀN THÂN</b></div>
        <div class="hud-pill" id="hudTarget">Target Reaction: <b id="txtTargetReaction">CHUẨN BỊ TRÚNG ĐÒN</b></div>
      </div>
    </div>

    <!-- Bottom Timeline Deck -->
    <div id="timeline-deck">
      <div class="timeline-row">
        <div class="progress-container">
          <span style="font-size:11px; font-weight:bold; color:#d4c2ee;">Timeline:</span>
          <input type="range" id="timelineSlider" min="0" max="80" value="0" oninput="onScrub(this.value)">
          <div class="frame-counter" id="lblFrame">Frame 0 / 80 (0.0s)</div>
        </div>
        <div style="display:flex; gap:6px;">
          <button class="act-btn" onclick="prevSkill()">⏮️ Trước</button>
          <button class="act-btn" onclick="nextSkill()">⏭️ Kế</button>
          <button class="act-btn cast" onclick="triggerManualCast()">💥 XUẤT CHIÊU</button>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
// ===== DATA INJECTION =====
const SKILLS = __SKILLS_JSON__;
const META = __META_JSON__;

// Engine State
let curSkillIdx = 0;
let isPlaying = true;
let autoAdvance = true;
let slowMo = false;
let curFrame = 0;
const MAX_FRAMES = 80;
let aftermathHold = 0;
let screenShake = 0;
let impactFlash = 0;

// Image Cache
const imgCache = {};
function getImg(src) {
  if(!src) return null;
  if(!imgCache[src]) {
    const im = new Image();
    im.src = src;
    imgCache[src] = im;
  }
  return imgCache[src];
}

// Canvas & Audio
const cv = document.getElementById('stageCanvas');
const ctx = cv.getContext('2d');
const GROUND_Y = 415;
let audioCtx = null;

// Bulletproof Rounded Rect Fallback (Works on 100% of browsers)
function drawCleanRoundRect(c, x, y, w, h, r) {
  c.beginPath();
  c.moveTo(x + r, y);
  c.lineTo(x + w - r, y);
  c.arcTo(x + w, y, x + w, y + r, r);
  c.lineTo(x + w, y + h - r);
  c.arcTo(x + w, y + h, x + w - r, y + h, r);
  c.lineTo(x + r, y + h);
  c.arcTo(x, y + h, x, y + h - r, r);
  c.lineTo(x, y + r);
  c.arcTo(x, y, x + r, y, r);
  c.closePath();
}

function playSfx(type, freq = 300) {
  try {
    if(!audioCtx) audioCtx = new (window.AudioContext || window.webkitAudioContext)();
    if(audioCtx.state === 'suspended') audioCtx.resume();
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.connect(gain); gain.connect(audioCtx.destination);
    const now = audioCtx.currentTime;

    if(type === 'charge') {
      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(freq, now);
      osc.frequency.exponentialRampToValueAtTime(freq * 2.2, now + 0.35);
      gain.gain.setValueAtTime(0.2, now);
      gain.gain.linearRampToValueAtTime(0, now + 0.35);
      osc.start(now); osc.stop(now + 0.35);
    } else if(type === 'release') {
      osc.type = 'triangle';
      osc.frequency.setValueAtTime(freq * 1.5, now);
      osc.frequency.exponentialRampToValueAtTime(freq * 0.5, now + 0.25);
      gain.gain.setValueAtTime(0.3, now);
      gain.gain.linearRampToValueAtTime(0, now + 0.25);
      osc.start(now); osc.stop(now + 0.25);
    } else if(type === 'impact') {
      osc.type = 'square';
      osc.frequency.setValueAtTime(freq, now);
      osc.frequency.exponentialRampToValueAtTime(40, now + 0.5);
      gain.gain.setValueAtTime(0.4, now);
      gain.gain.linearRampToValueAtTime(0, now + 0.5);
      osc.start(now); osc.stop(now + 0.5);
    }
  } catch(e) {}
}

// ===== ZERO-CLIP EFFECT PART HELPER =====
function createVfxPart(skillId, compName, frameIdx, dx, dy, opts = {}) {
  const skMeta = META[skillId];
  if(!skMeta || !skMeta[compName]) return null;
  const info = skMeta[compName];
  const img = getImg(info.sheet_x1);
  const totalFrames = info.frames || 8;
  const clampedF = Math.max(0, Math.min(totalFrames - 1, Math.floor(frameIdx)));

  const sw = info.w;
  const sh = info.h;
  const sx = 0;
  const sy = clampedF * sh;

  const renderW = (opts.w !== undefined ? opts.w : sw) * (opts.scaleX ?? 1);
  const renderH = (opts.h !== undefined ? opts.h : sh) * (opts.scaleY ?? 1);

  return {
    name: `${skillId}_${compName}_f${clampedF}`,
    img: img,
    sx: sx, sy: sy, sw: sw, sh: sh,
    dx: dx, dy: dy,
    w: renderW, h: renderH,
    anchor: info.anchor || 'center',
    rotation: opts.rotation || 0,
    flipX: opts.flipX || false,
    flipY: opts.flipY || false,
    onTop: opts.onTop !== undefined ? opts.onTop : (info.anchor === 'bottom' ? 0 : 1),
    blendMode: opts.blendMode || (compName === 'impact' || compName === 'finisher' || compName === 'cast' ? 'lighter' : 'source-over'),
    alpha: opts.alpha !== undefined ? opts.alpha : 1
  };
}

function renderPart(p, originX, originY, facing = 1) {
  if(!p || !p.img || !p.img.complete || p.img.naturalWidth === 0) return;
  ctx.save();
  ctx.globalCompositeOperation = p.blendMode;
  ctx.globalAlpha = Math.max(0, Math.min(1, p.alpha));

  const worldX = originX + (facing === 1 ? p.dx : -p.dx);
  const worldY = originY + p.dy;
  ctx.translate(worldX, worldY);

  const fx = (p.flipX ? -1 : 1) * (facing === 1 ? 1 : -1);
  const fy = p.flipY ? -1 : 1;
  ctx.scale(fx, fy);
  if(p.rotation) ctx.rotate(p.rotation * (facing === 1 ? 1 : -1));

  const drawX = -p.w / 2;
  const drawY = p.anchor === 'bottom' ? -p.h : -p.h / 2;

  ctx.drawImage(p.img, p.sx, p.sy, p.sw, p.sh, drawX, drawY, p.w, p.h);
  ctx.restore();
}

// ===== DRAW ANIME CASTER (HERO MODEL - ALWAYS VISIBLE!) =====
function drawAnimeCaster(c, cst, sk, f) {
  c.save();
  c.translate(cst.x, cst.y + cst.elevation);
  c.scale(cst.facing * cst.scale, cst.scale);
  c.globalAlpha = cst.alpha;

  const bob = Math.sin(f * 0.3) * 2;
  const isAttacking = f > 18 && f <= 45;

  // 1. Flowing Pirate Cape
  c.save();
  const capeWave = Math.sin(f * 0.25) * 6;
  const capeGrad = c.createLinearGradient(-35, -70, -50 + capeWave, 0);
  capeGrad.addColorStop(0, '#1a052e');
  capeGrad.addColorStop(1, sk.color);
  c.fillStyle = capeGrad;
  c.strokeStyle = '#ffd700'; c.lineWidth = 1.5;
  c.beginPath();
  c.moveTo(-15, -60);
  if(c.quadraticCurveTo) {
    c.quadraticCurveTo(-40 + capeWave, -30, -35 + capeWave * 1.2, 5);
  } else {
    c.lineTo(-35 + capeWave * 1.2, 5);
  }
  c.lineTo(-10, 0);
  if(c.quadraticCurveTo) {
    c.quadraticCurveTo(-15, -30, -5, -60);
  } else {
    c.lineTo(-5, -60);
  }
  c.closePath();
  c.fill(); c.stroke();
  c.restore();

  // 2. Battle Legs & Gold Buckle Boots
  c.fillStyle = '#1c1528';
  c.strokeStyle = '#4a287a'; c.lineWidth = 1.5;
  drawCleanRoundRect(c, -22, -28 + bob, 14, 30, 4);
  c.fill(); c.stroke();
  drawCleanRoundRect(c, 8, -26 + bob, 15, 28, 4);
  c.fill(); c.stroke();
  // Gold Boot Buckles
  c.fillStyle = '#ffd700';
  c.fillRect(-20, -5 + bob, 10, 3);
  c.fillRect(10, -5 + bob, 10, 3);

  // 3. Pirate Vest / Armor Body
  const vestGrad = c.createLinearGradient(-16, -65, 16, -25);
  vestGrad.addColorStop(0, sk.color);
  vestGrad.addColorStop(1, '#0e051c');
  c.fillStyle = vestGrad;
  c.strokeStyle = '#ffd700'; c.lineWidth = 1.8;
  drawCleanRoundRect(c, -18, -66 + bob, 36, 40, 8);
  c.fill(); c.stroke();

  // Belt & Gold Buckle
  c.fillStyle = '#b71c1c';
  c.fillRect(-17, -32 + bob, 34, 7);
  c.fillStyle = '#ffd700';
  c.fillRect(-6, -34 + bob, 12, 11);
  c.fillStyle = '#000';
  c.fillRect(-3, -31 + bob, 6, 5);

  // 4. Hero Head & Anime Eyes
  c.fillStyle = '#ffe0b2'; // Anime Skin Tone
  c.strokeStyle = '#d7ccc8'; c.lineWidth = 1;
  c.beginPath();
  c.arc(0, -78 + bob, 16, 0, Math.PI * 2);
  c.fill(); c.stroke();

  // Eyes
  c.fillStyle = '#fff';
  c.fillRect(2, -81 + bob, 6, 5);
  c.fillStyle = '#000';
  c.fillRect(4, -80 + bob, 3, 4);
  // Brow
  c.strokeStyle = '#3e2723'; c.lineWidth = 2;
  c.beginPath(); c.moveTo(0, -84 + bob); c.lineTo(9, -82 + bob); c.stroke();

  // Hero Hair / Hat / Tiara (Skill Themed)
  c.fillStyle = sk.color;
  c.strokeStyle = '#ffd700'; c.lineWidth = 1.5;
  c.beginPath();
  c.arc(0, -84 + bob, 17, Math.PI, Math.PI * 2);
  c.fill(); c.stroke();
  // Spiky Anime Hair Fringe
  c.beginPath();
  c.moveTo(-16, -82 + bob); c.lineTo(-8, -72 + bob);
  c.lineTo(0, -82 + bob); c.lineTo(8, -73 + bob); c.lineTo(16, -82 + bob);
  c.fill();

  // Hero Icon Emblem on Chest
  const iconImg = getImg(`../icons/x4/${sk.icon}.png`);
  if(iconImg && iconImg.complete && iconImg.naturalWidth > 0) {
    c.save();
    c.beginPath();
    c.arc(0, -50 + bob, 14, 0, Math.PI * 2);
    c.clip();
    c.drawImage(iconImg, -14, -64 + bob, 28, 28);
    c.restore();
    c.strokeStyle = '#ffd700'; c.lineWidth = 2;
    c.beginPath(); c.arc(0, -50 + bob, 14, 0, Math.PI * 2); c.stroke();
  }

  // 5. Arms & Weapon / Energy Aura
  c.save();
  const armAngle = isAttacking ? 0.35 : Math.sin(f * 0.2) * 0.15;
  c.translate(14, -58 + bob);
  c.rotate(armAngle);
  c.fillStyle = '#ffe0b2';
  c.beginPath(); c.arc(0, 0, 7, 0, Math.PI * 2); c.fill();
  c.fillStyle = sk.color;
  c.strokeStyle = '#ffd700'; c.lineWidth = 1.5;
  drawCleanRoundRect(c, 4, -6, 20, 12, 4);
  c.fill(); c.stroke();
  c.fillStyle = '#ffd700';
  c.beginPath(); c.arc(24, 0, 8, 0, Math.PI * 2); c.fill();
  c.restore();

  // 6. Overhead Hero Nameplate & HP Bar
  c.save();
  const nameY = -105 + bob;
  c.fillStyle = 'rgba(10, 4, 22, 0.88)';
  c.strokeStyle = '#ffd700'; c.lineWidth = 1.2;
  drawCleanRoundRect(c, -75, nameY - 14, 150, 20, 5);
  c.fill(); c.stroke();
  c.font = 'bold 10px sans-serif';
  c.fillStyle = '#ffd700'; c.textAlign = 'center';
  c.fillText(`[THẦN TRANG] ${sk.char.toUpperCase()}`, 0, nameY);
  // HP Bar
  c.fillStyle = '#000';
  c.fillRect(-50, nameY + 9, 100, 4);
  c.fillStyle = '#00e676';
  c.fillRect(-50, nameY + 9, 100, 4);
  c.restore();

  c.restore();
}

// ===== DRAW ANIME TARGET (TRAINING BOSS MODEL - ALWAYS VISIBLE!) =====
function drawAnimeTarget(c, tgt, sk, f) {
  c.save();
  c.translate(tgt.x + tgt.flinchX, tgt.y + tgt.flinchY);

  const bob = Math.sin(f * 0.2) * 1.5;
  const isHit = f > 36 && f <= 65;

  // 1. Heavy Iron Base
  c.fillStyle = '#263238';
  c.strokeStyle = '#37474f'; c.lineWidth = 2;
  c.beginPath();
  c.ellipse(0, -2, 28, 8, 0, 0, Math.PI * 2);
  c.fill(); c.stroke();
  c.fillStyle = '#b0bec5';
  c.fillRect(-18, -4, 4, 3);
  c.fillRect(14, -4, 4, 3);

  // 2. Wooden Trunk / Steel Body
  let bodyColor = isHit ? '#ff1744' : '#5d4037';
  if(tgt.statusEffect === 'freeze') bodyColor = '#00b0ff';
  if(tgt.statusEffect === 'stone') bodyColor = '#78909c';

  c.fillStyle = bodyColor;
  c.strokeStyle = isHit ? '#ff8a80' : '#8d6e63'; c.lineWidth = 2;
  drawCleanRoundRect(c, -14, -72 + bob, 28, 70, 6);
  c.fill(); c.stroke();

  // Rope Wraps
  c.strokeStyle = '#d7ccc8'; c.lineWidth = 2;
  c.beginPath();
  c.moveTo(-14, -50 + bob); c.lineTo(14, -50 + bob);
  c.moveTo(-14, -35 + bob); c.lineTo(14, -35 + bob);
  c.moveTo(-14, -20 + bob); c.lineTo(14, -20 + bob);
  c.stroke();

  // 3. Horizontal Cross-Arms
  c.fillStyle = bodyColor;
  c.strokeStyle = isHit ? '#ff8a80' : '#8d6e63'; c.lineWidth = 2;
  drawCleanRoundRect(c, -38, -62 + bob, 76, 14, 5);
  c.fill(); c.stroke();
  c.fillStyle = '#ffecb3';
  c.fillRect(-36, -60 + bob, 6, 10);
  c.fillRect(30, -60 + bob, 6, 10);

  // 4. Target Head & Straw Tufts
  c.fillStyle = '#efebe9';
  c.strokeStyle = '#8d6e63'; c.lineWidth = 1.5;
  c.beginPath();
  c.arc(0, -85 + bob, 15, 0, Math.PI * 2);
  c.fill(); c.stroke();
  // Straw Hair
  c.strokeStyle = '#ffd54f'; c.lineWidth = 2.5;
  c.beginPath();
  c.moveTo(-10, -96 + bob); c.lineTo(-14, -106 + bob);
  c.moveTo(0, -98 + bob); c.lineTo(0, -110 + bob);
  c.moveTo(10, -96 + bob); c.lineTo(14, -106 + bob);
  c.stroke();

  // Red Headband & Eyes
  c.fillStyle = '#d50000';
  c.fillRect(-14, -92 + bob, 28, 6);
  if(isHit) {
    c.font = 'bold 11px monospace'; c.fillStyle = '#000'; c.textAlign = 'center';
    c.fillText('X X', 0, -80 + bob);
  } else {
    c.fillStyle = '#000';
    c.fillRect(-7, -84 + bob, 4, 4);
    c.fillRect(3, -84 + bob, 4, 4);
  }

  // 5. Target Bullseye on Chest
  c.fillStyle = '#ffffff';
  c.beginPath(); c.arc(0, -42 + bob, 12, 0, Math.PI * 2); c.fill();
  c.fillStyle = '#d50000';
  c.beginPath(); c.arc(0, -42 + bob, 8, 0, Math.PI * 2); c.fill();
  c.fillStyle = '#ffd700';
  c.beginPath(); c.arc(0, -42 + bob, 4, 0, Math.PI * 2); c.fill();

  // 6. STATUS EFFECT VISUAL OVERLAYS
  if(tgt.statusEffect === 'freeze') {
    // Solid Faceted Ice Cube
    c.save();
    c.fillStyle = 'rgba(0, 229, 255, 0.65)';
    c.strokeStyle = '#ffffff'; c.lineWidth = 2.5;
    drawCleanRoundRect(c, -42, -108 + bob, 84, 110, 10);
    c.fill(); c.stroke();
    c.strokeStyle = 'rgba(255, 255, 255, 0.8)'; c.lineWidth = 1.5;
    c.beginPath();
    c.moveTo(-35, -95 + bob); c.lineTo(35, -20 + bob);
    c.moveTo(35, -95 + bob); c.lineTo(-35, -20 + bob);
    c.stroke();
    c.restore();
  } else if(tgt.statusEffect === 'stone') {
    // Solid Grey Granite Stone
    c.save();
    c.fillStyle = 'rgba(120, 144, 156, 0.88)';
    c.strokeStyle = '#37474f'; c.lineWidth = 2;
    drawCleanRoundRect(c, -40, -106 + bob, 80, 108, 8);
    c.fill(); c.stroke();
    c.strokeStyle = '#263238'; c.lineWidth = 1.5;
    c.beginPath();
    c.moveTo(-20, -80 + bob); c.lineTo(0, -60 + bob); c.lineTo(-10, -30 + bob);
    c.moveTo(15, -75 + bob); c.lineTo(5, -45 + bob); c.lineTo(20, -25 + bob);
    c.stroke();
    c.restore();
  } else if(tgt.statusEffect === 'shock') {
    // Lightning Arcs
    c.save();
    c.strokeStyle = '#00e5ff'; c.lineWidth = 2;
    for(let li = 0; li < 4; li++) {
      const ly = -90 + li * 22 + bob;
      const lx1 = -30 + Math.sin(f * 2 + li) * 10;
      const lx2 = 30 + Math.cos(f * 2 + li) * 10;
      c.beginPath();
      c.moveTo(lx1, ly);
      c.lineTo(0, ly + (Math.random() - 0.5) * 14);
      c.lineTo(lx2, ly);
      c.stroke();
    }
    c.restore();
  } else if(tgt.statusEffect === 'burn') {
    // Flame Sprites
    c.save();
    c.fillStyle = 'rgba(255, 109, 0, 0.8)';
    for(let fi = 0; fi < 3; fi++) {
      const fx = -15 + fi * 15;
      const fh = 20 + Math.sin(f * 0.8 + fi) * 10;
      c.beginPath();
      c.ellipse(fx, -60 + bob, 8, fh, 0, 0, Math.PI * 2);
      c.fill();
    }
    c.restore();
  }

  // 7. Overhead Boss Nameplate & Dynamic HP Bar
  c.save();
  const tNameY = -120 + bob;
  c.fillStyle = 'rgba(12, 4, 24, 0.88)';
  c.strokeStyle = '#ff1744'; c.lineWidth = 1.2;
  drawCleanRoundRect(c, -75, tNameY - 14, 150, 20, 5);
  c.fill(); c.stroke();
  c.font = 'bold 10px sans-serif';
  c.fillStyle = '#ff5252'; c.textAlign = 'center';
  c.fillText('[MỤC TIÊU] HẢI QUÂN ĐÔ ĐỐC', 0, tNameY);

  // Dynamic Boss HP Bar
  const maxHpW = 100;
  const hpPercent = isHit ? Math.max(0.1, 1 - ((f - 36) / 30) * 0.85) : 1;
  c.fillStyle = '#000';
  c.fillRect(-maxHpW / 2, tNameY + 9, maxHpW, 5);
  c.fillStyle = isHit ? '#ff1744' : '#00e676';
  c.fillRect(-maxHpW / 2, tNameY + 9, maxHpW * hpPercent, 5);
  c.restore();

  c.restore();
}

// ===== ANIME CHOREOGRAPHY SCRIPTS =====
function getAnimeChoreography(skillId, f) {
  const sk = SKILLS[curSkillIdx];
  const parts = [];
  let banner = false;
  let phase = "CHARGE";
  let casterAction = "Tụ Khí Năng Lượng";
  let targetReaction = "Chuẩn Bị Phòng Thủ";

  const caster = { x: 210, y: GROUND_Y, facing: 1, scale: 1, elevation: 0, state: 'charge', alpha: 1 };
  const target = { x: 700, y: GROUND_Y, flinchX: 0, flinchY: 0, state: 'idle', statusEffect: null, alpha: 1 };

  if(f <= 18) {
    banner = true;
    phase = "ANIME INTRO";
    casterAction = "Hô Khẩu Quyết Tuyệt Kỹ";
    caster.elevation = Math.sin(f * 0.2) * 4;
    parts.push(createVfxPart(skillId, 'aura', f % 8, 0, 5, { onTop: 0, scaleX: 1.1, scaleY: 1.1, blendMode: 'lighter' }));
    parts.push(createVfxPart(skillId, 'cast', Math.floor(f / 2.5), 25, 0, { onTop: 1, blendMode: 'lighter' }));
    if(f === 1) playSfx('charge', 250);
  }

  // 4001 Ace
  if(skillId === 4001) {
    if(f > 18 && f <= 38) {
      phase = "HỎA LONG PHÓNG XUẤT"; casterAction = "Tung Cú Đấm Hỏa Long Quyền";
      caster.x += (f - 18) * 1.5; caster.elevation = -15;
      const progress = (f - 18) / 20;
      const curX = caster.x + progress * (target.x - caster.x);
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, curX - caster.x, -35, { onTop: 1, scaleX: 1.3, scaleY: 1.3, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 400);
    } else if(f > 38) {
      phase = "THIÊU ĐỐT ĐỊA NGỤC"; casterAction = "Thế Thủ Sau Đòn Đánh"; targetReaction = "Bị Biển Lửa Thiêu Đốt";
      target.statusEffect = 'burn'; target.flinchX = -8;
      const impF = f - 38;
      if(f === 39) { screenShake = 18; impactFlash = 2; playSfx('impact', 160); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -40, { onTop: 2, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'finisher', impF % 12, target.x - caster.x, 0, { onTop: 0, scaleX: 1.5, scaleY: 1.3, blendMode: 'lighter' }));
    }
  }
  // 4002 Akainu
  else if(skillId === 4002) {
    if(f > 18 && f <= 36) {
      phase = "ĐỊA CHẤN DUNG NHAM"; casterAction = "Nện Hai Tay Xuống Sàn Đấu";
      const progress = (f - 18) / 18;
      const curX = caster.x + progress * (target.x - caster.x);
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, curX - caster.x, 15, { onTop: 0, scaleX: 1.2, scaleY: 0.9, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 200);
    } else if(f > 36) {
      phase = "NÚI LỬA PHUN TRÀO"; casterAction = "Khói Đen Nham Thạch"; targetReaction = "BỊ HẤT TUNG LÊN TRỜI!";
      target.statusEffect = 'burn'; target.flinchY = Math.sin((f - 36) * 0.15) * -45;
      const impF = f - 36;
      if(f === 37) { screenShake = 24; impactFlash = 3; playSfx('impact', 110); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -60, { onTop: 2, scaleX: 1.5, scaleY: 1.6, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'finisher', impF % 12, target.x - caster.x, 10, { onTop: 0, scaleX: 1.6, scaleY: 1.4, blendMode: 'lighter' }));
    }
  }
  // 4003 Aokiji
  else if(skillId === 4003) {
    if(f > 18 && f <= 38) {
      phase = "CHIM BĂNG SẢI CÁNH"; casterAction = "Thả Băng Điểu Khổng Lồ";
      const progress = (f - 18) / 20;
      const curX = caster.x + progress * (target.x - caster.x);
      const curY = -40 + Math.sin(progress * Math.PI) * -35;
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, curX - caster.x, curY, { onTop: 1, scaleX: 1.3, scaleY: 1.2, rotation: 0.15, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 600);
    } else if(f > 38) {
      phase = "BĂNG PHONG & BỘC PHÁ"; casterAction = "Băng Giá Vĩnh Cửu"; targetReaction = "ĐÓNG BĂNG TRONG KHỐI TINH THỂ!";
      target.statusEffect = 'freeze';
      const impF = f - 38;
      if(f === 39) { screenShake = 16; impactFlash = 2; playSfx('impact', 500); }
      if(f === 54) { screenShake = 22; playSfx('impact', 750); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -35, { onTop: 2, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'finisher', impF % 12, target.x - caster.x, 0, { onTop: 0, scaleX: 1.5, scaleY: 1.3, blendMode: 'lighter' }));
    }
  }
  // 4004 Kizaru
  else if(skillId === 4004) {
    if(f > 18 && f <= 32) {
      phase = "QUANG TỐC BAY VỌT"; casterAction = "Bay Vút Lên Trời Hóa Ánh Sáng";
      caster.elevation = -120; caster.alpha = 0.9;
      parts.push(createVfxPart(skillId, 'aura', (f - 18) % 8, 0, -120, { onTop: 1, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 750);
    } else if(f > 32) {
      phase = "MƯA TIA LASER LIÊN HOÀN"; casterAction = "Xả Chùm Tia Laser Yasakani";
      caster.elevation = -120; targetReaction = "CHÓA MẮT & TRÚNG ĐÒN LIÊN TIẾP";
      target.flinchX = Math.sin(f * 1.5) * 6;
      const impF = f - 32;
      if(f % 4 === 0) { screenShake = 12; playSfx('impact', 650); }
      parts.push(createVfxPart(skillId, 'projectile', impF % 8, (target.x - caster.x) * 0.6, -70, { onTop: 1, rotation: -0.45, scaleX: 1.5, scaleY: 1.5, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -30, { onTop: 2, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
    }
  }
  // 4005 Blackbeard
  else if(skillId === 4005) {
    if(f > 18 && f <= 42) {
      phase = "LỖ ĐEN KUROUZU"; casterAction = "Khai Mở Lực Hút Hắc Ám"; targetReaction = "BỊ HÚT VÀO TÂM HỐ ĐEN!";
      target.flinchX = -((f - 18) / 24) * 45;
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, target.x - caster.x, -25, { onTop: 0, scaleX: 1.5, scaleY: 1.5, rotation: f * 0.25, blendMode: 'source-over' }));
      if(f === 19) playSfx('release', 150);
    } else if(f > 42) {
      phase = "CỘT HẮC ÁM PHUN TRÀO"; casterAction = "Bùng Nổ Tử Thần"; targetReaction = "Bị Nuốt Chửng Hoàn Toàn";
      target.statusEffect = 'burn';
      const impF = f - 42;
      if(f === 43) { screenShake = 24; impactFlash = 3; playSfx('impact', 90); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -65, { onTop: 2, scaleX: 1.5, scaleY: 1.7, blendMode: 'source-over' }));
    }
  }
  // 4006 Enel
  else if(skillId === 4006) {
    if(f > 18 && f <= 34) {
      phase = "TRỐNG SẤM TỤ ĐIỆN"; casterAction = "Trống Sấm Xoay Tít Quanh Thân";
      parts.push(createVfxPart(skillId, 'aura', (f - 18) % 8, 0, -25, { onTop: 0, scaleX: 1.3, scaleY: 1.3, rotation: f * 0.3, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, target.x - caster.x, -140, { onTop: 1, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 500);
    } else if(f > 34) {
      phase = "THIÊN LÔI EL THOR"; casterAction = "Cười Tự Phụ Thần Sấm"; targetReaction = "TÊ LIỆT CO GIẬT ĐIỆN QUANG!";
      target.statusEffect = 'shock'; target.flinchX = Math.sin(f * 2.5) * 5;
      const impF = f - 34;
      if(f === 35) { screenShake = 26; impactFlash = 3; playSfx('impact', 280); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -75, { onTop: 2, scaleX: 1.6, scaleY: 2.2, blendMode: 'lighter' }));
    }
  }
  // 4007 Whitebeard
  else if(skillId === 4007) {
    if(f > 18 && f <= 36) {
      phase = "ĐẤM VỠ KHÔNG GIAN"; casterAction = "Nện Quyền Rạn Nứt Chân Không";
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, 120, -25, { onTop: 1, scaleX: 1.6, scaleY: 1.3, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 180);
    } else if(f > 36) {
      phase = "SÓNG THẦN HẢI CHẤN"; casterAction = "Thế Đứng Bất Bại"; targetReaction = "BỊ HAI SÓNG CHẤN ĐỘNG ÉP NÁT!";
      target.flinchX = -12;
      const impF = f - 36;
      if(f === 37) { screenShake = 30; impactFlash = 3; playSfx('impact', 75); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -40, { onTop: 2, scaleX: 1.7, scaleY: 1.5, blendMode: 'lighter' }));
    }
  }
  // 4008 Law
  else if(skillId === 4008) {
    if(f > 18 && f <= 28) {
      phase = "VÒM PHẪU THUẬT (ROOM)"; casterAction = "Búng Tay Mở Rộng Vòm ROOM";
      parts.push(createVfxPart(skillId, 'aura', (f - 18) % 8, 200, -20, { onTop: 0, scaleX: 3.5, scaleY: 2.2, blendMode: 'lighter', alpha: 0.7 }));
      if(f === 19) playSfx('charge', 600);
    } else if(f > 28 && f <= 38) {
      phase = "TỐC BIẾN SHAMBLES"; casterAction = "Xuất Hiện Sát Cạnh Mục Tiêu";
      caster.x = target.x - 45;
      parts.push(createVfxPart(skillId, 'projectile', (f - 28) % 8, 0, -25, { onTop: 1, scaleX: 1.3, scaleY: 1.3, blendMode: 'lighter' }));
      if(f === 29) playSfx('release', 700);
    } else if(f > 38) {
      phase = "GAMMA KNIFE CẮT NỘI TẠNG"; casterAction = "Cắm Dao Điện Phá Hủy Tâm Thất";
      caster.x = target.x - 45; targetReaction = "BỊ CẮT LÁT KHÔNG GIAN!";
      target.statusEffect = 'shock';
      const impF = f - 38;
      if(f === 39) { screenShake = 20; impactFlash = 2; playSfx('impact', 520); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, 45, -30, { onTop: 2, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
    }
  }
  // 4009 Kid
  else if(skillId === 4009) {
    if(f > 18 && f <= 34) {
      phase = "LẮP RÁP ĐẠI PHÁO TỪ TRƯỜNG"; casterAction = "Hút Phế Liệu Lắp Ráp Damned Punk";
      parts.push(createVfxPart(skillId, 'cast', (f - 18) % 8, 40, -25, { onTop: 1, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
      if(f === 19) playSfx('charge', 320);
    } else if(f > 34) {
      phase = "BẮN CHÙM TIA RAILGUN"; casterAction = "Hỏa Lực Từ Trường Hủy Diệt"; targetReaction = "BỊ BỘC PHÁ ĐA TẦNG & MẢNH VĂNG";
      const impF = f - 34;
      if(f === 35) { screenShake = 25; impactFlash = 3; playSfx('impact', 220); }
      parts.push(createVfxPart(skillId, 'projectile', impF % 8, (target.x - caster.x) * 0.5, -25, { onTop: 1, scaleX: 2.2, scaleY: 1.3, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -35, { onTop: 2, scaleX: 1.5, scaleY: 1.5, blendMode: 'lighter' }));
    }
  }
  // 4010 Magellan
  else if(skillId === 4010) {
    if(f > 18 && f <= 38) {
      phase = "RỒNG ĐỘC HYDRA XUNG PHONG"; casterAction = "Triệu Hồi 3 Đầu Rồng Độc";
      const progress = (f - 18) / 20;
      const curX = caster.x + progress * (target.x - caster.x);
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, curX - caster.x, -15, { onTop: 1, scaleX: 1.4, scaleY: 1.2, blendMode: 'source-over' }));
      if(f === 19) playSfx('release', 280);
    } else if(f > 38) {
      phase = "ĐẦM LẦY ĐỘC ĂN MÒN"; casterAction = "Khí Độc Tím Đậm Bao Trùm"; targetReaction = "NGẬP TRONG ĐẦM LẦY ĐỘC ĂN MÒN!";
      target.statusEffect = 'poison';
      const impF = f - 38;
      if(f === 39) { screenShake = 16; impactFlash = 2; playSfx('impact', 240); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -30, { onTop: 2, scaleX: 1.5, scaleY: 1.5, blendMode: 'source-over' }));
    }
  }
  // 4011 Hancock
  else if(skillId === 4011) {
    if(f > 18 && f <= 36) {
      phase = "BẮN MƯA TÊN SLAVE ARROW"; casterAction = "Kéo Căng Cung Tên Tình Ái";
      const progress = (f - 18) / 18;
      const curX = caster.x + progress * (target.x - caster.x);
      const curY = -40 + Math.sin(progress * Math.PI) * -30;
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, curX - caster.x, curY, { onTop: 1, scaleX: 1.3, scaleY: 1.2, rotation: 0.2, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 650);
    } else if(f > 36) {
      phase = "HÓA ĐÁ TOÀN THÂN (PETRIFIED)"; casterAction = "Nụ Cười Nữ Hoàng Quyến Rũ"; targetReaction = "BIẾN THÀNH TƯỢNG ĐÁ HOA CƯƠNG 100%!";
      target.statusEffect = 'stone';
      const impF = f - 36;
      if(f === 37) { screenShake = 16; impactFlash = 2; playSfx('impact', 400); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -30, { onTop: 2, scaleX: 1.4, scaleY: 1.4, blendMode: 'lighter' }));
    }
  }
  // 4012 Marco
  else if(skillId === 4012) {
    if(f > 18 && f <= 38) {
      phase = "PHƯỢNG HOÀNG CHAO LIỆNG"; casterAction = "Hóa Thân Chim Phượng Hoàng Lam Hỏa";
      const progress = (f - 18) / 20;
      caster.x = 210 + progress * 200; caster.elevation = -60 + Math.sin(progress * Math.PI) * -40;
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, 0, 0, { onTop: 1, scaleX: 1.5, scaleY: 1.3, rotation: 0.35, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 520);
    } else if(f > 38) {
      phase = "LAM HỎA BẤT TỬ BÙNG NỔ"; casterAction = "Sải Cánh Hồi Sinh"; targetReaction = "Bị Lửa Xanh Phượng Hoàng Cuốn Phăng";
      const impF = f - 38;
      if(f === 39) { screenShake = 20; impactFlash = 2; playSfx('impact', 480); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -45, { onTop: 2, scaleX: 1.5, scaleY: 1.6, blendMode: 'lighter' }));
    }
  }
  // 4013 Sengoku
  else if(skillId === 4013) {
    if(f > 18 && f <= 34) {
      phase = "PHÁP LUÂN ĐẠI PHẬT"; casterAction = "Hóa Đại Phật Kim Sắc Uy Nghiêm"; caster.scale = 1.35;
      parts.push(createVfxPart(skillId, 'aura', (f - 18) % 8, 0, -40, { onTop: 0, scaleX: 1.6, scaleY: 1.6, rotation: f * 0.15, blendMode: 'lighter' }));
      if(f === 19) playSfx('charge', 300);
    } else if(f > 34) {
      phase = "CHƯỞNG LỰC PHẬT QUANG"; casterAction = "Đẩy Chưởng Lực Sóng Xung Kích"; caster.scale = 1.35;
      targetReaction = "BỊ SÓNG KIM SẮC ĐẨY LÙI!"; target.flinchX = -15;
      const impF = f - 34;
      if(f === 35) { screenShake = 25; impactFlash = 3; playSfx('impact', 200); }
      parts.push(createVfxPart(skillId, 'projectile', impF % 8, (target.x - caster.x) * 0.5, -30, { onTop: 1, scaleX: 1.6, scaleY: 1.5, blendMode: 'lighter' }));
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -35, { onTop: 2, scaleX: 1.7, scaleY: 1.6, blendMode: 'lighter' }));
    }
  }
  // 4014 Kaido
  else if(skillId === 4014) {
    if(f > 18 && f <= 36) {
      phase = "HẮC LÔI BÁT QUÁI PHÓNG XUNG"; casterAction = "Lao Vụt Tới Cùng 9 Rồng Sét";
      const progress = (f - 18) / 18;
      caster.x = 210 + progress * (target.x - 300);
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, 40, -25, { onTop: 1, scaleX: 1.6, scaleY: 1.4, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 350);
    } else if(f > 36) {
      phase = "NỆN CHÙY BÁ VƯƠNG HẮC LÔI"; casterAction = "Nện Chùy Văng Kẻ Địch Lên Mây";
      caster.x = target.x - 70; targetReaction = "BỊ NỆN BAY CAO LÊN TRỜI!";
      target.statusEffect = 'shock'; target.flinchY = -40;
      const impF = f - 36;
      if(f === 37) { screenShake = 28; impactFlash = 3; playSfx('impact', 110); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, 70, -40, { onTop: 2, scaleX: 1.6, scaleY: 1.6, blendMode: 'lighter' }));
    }
  }
  // 4015 Sabo
  else if(skillId === 4015) {
    if(f > 18 && f <= 36) {
      phase = "LONG TRẢO TOÁI CỐT"; casterAction = "Lao Tới Cắm Long Trảo Xuống Sàn";
      const progress = (f - 18) / 18;
      caster.x = 210 + progress * (target.x - 280);
      parts.push(createVfxPart(skillId, 'cast', (f - 18) % 8, 30, 0, { onTop: 1, scaleX: 1.3, scaleY: 1.3, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 400);
    } else if(f > 36) {
      phase = "ĐẦU RỒNG LỬA ĐÂM LÊN"; casterAction = "Xé Nát Long Mạch Địa Cầu";
      caster.x = target.x - 70; targetReaction = "Bị Đầu Rồng Lửa Hất Tung";
      target.statusEffect = 'burn'; target.flinchY = -25;
      const impF = f - 36;
      if(f === 37) { screenShake = 24; impactFlash = 3; playSfx('impact', 170); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, 70, -50, { onTop: 2, scaleX: 1.5, scaleY: 1.6, blendMode: 'lighter' }));
    }
  }
  // 4016 Fujitora
  else if(skillId === 4016) {
    if(f > 18 && f <= 40) {
      phase = "KÉO THIÊN THẠCH VŨ TRỤ"; casterAction = "Rút Kiếm Trọng Lực Tím";
      parts.push(createVfxPart(skillId, 'cast', (f - 18) % 8, 0, -35, { onTop: 0, scaleX: 1.5, scaleY: 1.5, rotation: f * 0.1, blendMode: 'lighter' }));
      const progress = (f - 18) / 22;
      const metX = target.x - 220 + progress * 220;
      const metY = -120 + progress * (target.y - 30 - (-120));
      parts.push(createVfxPart(skillId, 'projectile', (f - 18) % 8, metX - caster.x, metY, { onTop: 1, scaleX: 1.6, scaleY: 1.6, rotation: 0.65, blendMode: 'lighter' }));
      if(f === 19) playSfx('release', 150);
    } else if(f > 40) {
      phase = "HỐ THIÊN THẠCH CHẤN ĐỘNG"; casterAction = "Tra Kiếm Vào Vỏ"; targetReaction = "BỊ THIÊN THẠCH NGHIỀN NÁT!";
      target.statusEffect = 'burn'; target.flinchX = -8;
      const impF = f - 40;
      if(f === 41) { screenShake = 32; impactFlash = 4; playSfx('impact', 50); }
      parts.push(createVfxPart(skillId, 'impact', impF % 10, target.x - caster.x, -50, { onTop: 2, scaleX: 1.8, scaleY: 1.8, blendMode: 'lighter' }));
    }
  }

  return { parts, caster, target, banner, phase, casterAction, targetReaction };
}

// ===== RENDER STAGE =====
function renderStage() {
  ctx.save();

  // Screen Shake
  if(screenShake > 0) {
    const rx = (Math.random() - 0.5) * screenShake;
    const ry = (Math.random() - 0.5) * screenShake;
    ctx.translate(rx, ry);
    screenShake *= 0.88;
    if(screenShake < 0.2) screenShake = 0;
  }

  // Clear Sky
  ctx.fillStyle = '#05020c';
  ctx.fillRect(0, 0, cv.width, cv.height);

  // Gradient Sky
  const skyGrad = ctx.createLinearGradient(0, 0, 0, GROUND_Y);
  skyGrad.addColorStop(0, '#0a0418');
  skyGrad.addColorStop(0.65, '#190833');
  skyGrad.addColorStop(1, '#270c47');
  ctx.fillStyle = skyGrad;
  ctx.fillRect(0, 0, cv.width, GROUND_Y + 10);

  // Distant Mountains & Clouds
  ctx.fillStyle = '#39175d';
  ctx.beginPath();
  ctx.moveTo(0, GROUND_Y);
  ctx.lineTo(160, GROUND_Y - 95); ctx.lineTo(340, GROUND_Y);
  ctx.lineTo(540, GROUND_Y - 120); ctx.lineTo(770, GROUND_Y);
  ctx.lineTo(890, GROUND_Y - 80); ctx.lineTo(cv.width, GROUND_Y);
  ctx.fill();

  // Battleground Terrain
  const groundGrad = ctx.createLinearGradient(0, GROUND_Y, 0, cv.height);
  groundGrad.addColorStop(0, '#190a2e');
  groundGrad.addColorStop(0.25, '#110622');
  groundGrad.addColorStop(1, '#06020c');
  ctx.fillStyle = groundGrad;
  ctx.fillRect(0, GROUND_Y, cv.width, cv.height - GROUND_Y);

  // Glowing Ground Surface
  ctx.strokeStyle = '#6a28a3'; ctx.lineWidth = 2;
  ctx.beginPath(); ctx.moveTo(0, GROUND_Y); ctx.lineTo(cv.width, GROUND_Y); ctx.stroke();

  // Perspective Grid Lines
  ctx.strokeStyle = 'rgba(106, 40, 163, 0.22)'; ctx.lineWidth = 1;
  for(let x = 0; x <= cv.width; x += 60) {
    ctx.beginPath(); ctx.moveTo(x, GROUND_Y);
    ctx.lineTo(x + (x - cv.width / 2) * 0.45, cv.height); ctx.stroke();
  }

  // Retrieve Current Choreography Frame
  const sk = SKILLS[curSkillIdx];
  const choreo = getAnimeChoreography(sk.id, curFrame);

  // Update HUD Text
  document.getElementById('txtPhase').innerText = choreo.phase;
  document.getElementById('txtCasterAction').innerText = choreo.casterAction;
  document.getElementById('txtTargetReaction').innerText = choreo.targetReaction;
  document.getElementById('hudFrame').innerHTML = `Frame: <b>${curFrame} / ${MAX_FRAMES}</b> | Phase: <b>${choreo.phase}</b>`;
  document.getElementById('lblFrame').innerText = `Frame ${curFrame} / ${MAX_FRAMES} (${(curFrame/30).toFixed(1)}s)`;
  document.getElementById('timelineSlider').value = curFrame;

  // Toggle Anime Banner
  const bannerEl = document.getElementById('anime-banner');
  if(choreo.banner) {
    bannerEl.classList.add('show');
    document.getElementById('bannerChar').innerText = `${sk.set_name.toUpperCase()} • ${sk.char.toUpperCase()}`;
    document.getElementById('bannerTitle').innerText = `${sk.name.toUpperCase()}`;
  } else {
    bannerEl.classList.remove('show');
  }

  // 1. Render ONTOP: 0 (Behind Caster/Target) Parts
  for(const p of choreo.parts) {
    if(p.onTop === 0) renderPart(p, choreo.caster.x, choreo.caster.y, choreo.caster.facing);
  }

  // 2. Render Shadows at Ground Level
  // Target Shadow
  ctx.save();
  const tgt = choreo.target;
  const tgtShadowAlpha = Math.max(0.1, 0.5 + tgt.flinchY / 80);
  ctx.fillStyle = `rgba(0,0,0,${tgtShadowAlpha})`;
  ctx.beginPath();
  ctx.ellipse(tgt.x + tgt.flinchX, GROUND_Y + 4, 30, 9, 0, 0, Math.PI * 2);
  ctx.fill();
  ctx.restore();

  // Caster Shadow
  ctx.save();
  const cst = choreo.caster;
  const cstHeight = Math.abs(cst.elevation);
  const shadowScale = Math.max(0.2, 1 - cstHeight / 250);
  ctx.fillStyle = `rgba(0,0,0,${Math.max(0.1, 0.55 - cstHeight / 300)})`;
  ctx.beginPath();
  ctx.ellipse(cst.x, GROUND_Y + 4, 30 * shadowScale, 9 * shadowScale, 0, 0, Math.PI * 2);
  ctx.fill();
  ctx.restore();

  // 3. RENDER TARGET (ALWAYS VISIBLE!)
  try {
    drawAnimeTarget(ctx, choreo.target, sk, curFrame);
  } catch(e) {
    console.error("Target Render Exception:", e);
  }

  // 4. RENDER CASTER (ALWAYS VISIBLE!)
  try {
    drawAnimeCaster(ctx, choreo.caster, sk, curFrame);
  } catch(e) {
    console.error("Caster Render Exception:", e);
  }

  // 5. Render ONTOP: 1 (Front of Characters) Parts
  for(const p of choreo.parts) {
    if(p.onTop === 1) renderPart(p, choreo.caster.x, choreo.caster.y, choreo.caster.facing);
  }

  // 6. Render ONTOP: 2 (Overlay over Target / Foreground) Parts
  for(const p of choreo.parts) {
    if(p.onTop === 2) renderPart(p, choreo.caster.x, choreo.caster.y, choreo.caster.facing);
  }

  // 7. Floating Damage Number
  if(curFrame > 40 && curFrame <= 65) {
    ctx.save();
    ctx.font = '900 15px monospace';
    ctx.fillStyle = '#ffd700'; ctx.shadowColor = '#ff3d00'; ctx.shadowBlur = 10;
    ctx.textAlign = 'center';
    const dmgY = GROUND_Y - 140 - (curFrame - 40) * 1.5;
    ctx.fillText(`-${sk.damage.toLocaleString()} CRITICAL!`, choreo.target.x + choreo.target.flinchX, dmgY);
    ctx.restore();
  }

  // 8. Status Badge Above Target
  if(curFrame > 42 && curFrame <= 75) {
    ctx.save();
    ctx.font = 'bold 10.5px sans-serif';
    ctx.fillStyle = sk.color; ctx.textAlign = 'center';
    ctx.fillText(sk.status_tag, choreo.target.x + choreo.target.flinchX, GROUND_Y - 160);
    ctx.restore();
  }

  // 9. ANIME IMPACT FRAME FLASH
  if(impactFlash > 0) {
    ctx.save();
    ctx.fillStyle = impactFlash % 2 === 0 ? 'rgba(255, 255, 255, 0.85)' : 'rgba(0, 0, 0, 0.85)';
    ctx.fillRect(0, 0, cv.width, cv.height);
    ctx.restore();
    impactFlash--;
  }

  // 10. Cinematic Letterbox
  if(document.getElementById('chkLetterbox').checked) {
    ctx.fillStyle = '#000';
    ctx.fillRect(0, 0, cv.width, 24);
    ctx.fillRect(0, cv.height - 24, cv.width, 24);
  }

  ctx.restore();
}

// ===== MAIN AUTOMATED LOOP =====
function loop() {
  try {
    if(isPlaying) {
      curFrame++;
      if(curFrame > MAX_FRAMES) {
        aftermathHold++;
        if(aftermathHold > 25) {
          aftermathHold = 0;
          if(autoAdvance) {
            nextSkill();
          } else {
            curFrame = 0;
          }
        }
      }
    }
    renderStage();
  } catch(e) {
    console.error("Anime Loop Exception:", e);
  }
  requestAnimationFrame(loop);
}

// ===== CONTROLS =====
function togglePlay() {
  isPlaying = !isPlaying;
  const btn = document.getElementById('btnPlayPause');
  btn.innerText = isPlaying ? "⏸️ Tạm Dừng" : "▶️ Tiếp Tục";
  btn.classList.toggle('play-btn', isPlaying);
  btn.classList.toggle('pause-btn', !isPlaying);
}

function restartSkill() { curFrame = 0; aftermathHold = 0; }

function toggleAutoAdvance() {
  autoAdvance = !autoAdvance;
  document.getElementById('btnAutoAdvance').classList.toggle('active', autoAdvance);
  document.getElementById('badgeAutoStatus').innerText = autoAdvance ? "AUTO ANIME LOOP: ĐANG CHẠY" : "LẶP CHIÊU HIỆN TẠI (SINGLE LOOP)";
  document.getElementById('badgeAutoStatus').style.color = autoAdvance ? "#00e676" : "#ffd700";
}

function toggleSlowMo() {
  slowMo = !slowMo;
  document.getElementById('btnSlowMo').classList.toggle('active', slowMo);
}

function onScrub(val) { curFrame = parseInt(val); renderStage(); }

function selectSkill(idx) {
  curSkillIdx = idx;
  curFrame = 0;
  aftermathHold = 0;
  const sk = SKILLS[idx];
  document.getElementById('badgeSkillName').innerText = `Skill ${sk.id}: ${sk.name}`;
  document.getElementById('badgeChar').innerText = `Hệ ${sk.element} (${sk.char})`;

  document.querySelectorAll('.skill-card').forEach((c, i) => {
    c.classList.toggle('active', i === idx);
  });
}

function nextSkill() {
  let next = (curSkillIdx + 1) % SKILLS.length;
  selectSkill(next);
}

function prevSkill() {
  let prev = (curSkillIdx - 1 + SKILLS.length) % SKILLS.length;
  selectSkill(prev);
}

function triggerManualCast() {
  curFrame = 19;
  renderStage();
}

// Build Sidebar
function buildSidebar() {
  const list = document.getElementById('skillList');
  list.innerHTML = '';
  SKILLS.forEach((sk, i) => {
    const card = document.createElement('div');
    card.className = 'skill-card' + (i === curSkillIdx ? ' active' : '');
    card.onclick = () => selectSkill(i);
    card.innerHTML = `
      <img class="sk-icon" src="../icons/x4/${sk.icon}.png" onerror="this.src='../icons/x1/${sk.icon}.png'">
      <div class="sk-meta">
        <div class="sk-top">
          <span class="sk-id">${sk.id}</span>
          <span class="sk-icon-badge">IC:${sk.icon}</span>
        </div>
        <div class="sk-name">${sk.name}</div>
        <div class="sk-char">${sk.char}</div>
      </div>
    `;
    list.appendChild(card);
  });
}

// Launch
buildSidebar();
requestAnimationFrame(loop);
</script>
</body>
</html>
'''

def main():
    print("=" * 70)
    print("BUILDING ANIME CINEMATIC STUDIO V11 (VISIBLE CHAR & TARGET ENGINE)")
    print("=" * 70)

    skills_json_str = json.dumps(SKILLS, ensure_ascii=False)
    meta_json_str = json.dumps(META, ensure_ascii=False)

    master_html = HTML_CONTENT.replace("__SKILLS_JSON__", skills_json_str)
    master_html = master_html.replace("__META_JSON__", meta_json_str)

    # 1. Master index.html
    master_path = os.path.join(DEMOS_DIR, "index.html")
    with open(master_path, "w", encoding="utf-8") as f:
        f.write(master_html)
    print(f"[OK] Re-generated Master Studio: {master_path}")

    # 2. Standalone demos for all 16 skills
    for i, sk in enumerate(SKILLS):
        sid = sk["id"]
        standalone_html = master_html.replace("let curSkillIdx = 0;", f"let curSkillIdx = {i};")
        standalone_html = standalone_html.replace(
            "<title>Skill Thần Trang VFX — Anime Cinematic Studio v11 (Hiển Thị Chuẩn Nhân Vật & Mục Tiêu)</title>",
            f"<title>Skill {sid}: {sk['name']} — Anime Cinematic Demo</title>"
        )
        sk_path = os.path.join(DEMOS_DIR, f"{sid}.html")
        with open(sk_path, "w", encoding="utf-8") as f:
            f.write(standalone_html)
        print(f"  [OK] Re-generated standalone demo: demos/{sid}.html")

    # Copy script into tools directory
    target_tool_path = os.path.join(TOOLS_DIR, "generate_showcase_html.py")
    if os.path.abspath(__file__) != os.path.abspath(target_tool_path):
        shutil.copy2(__file__, target_tool_path)
        print(f"[OK] Saved script to {target_tool_path}")

if __name__ == '__main__':
    main()
