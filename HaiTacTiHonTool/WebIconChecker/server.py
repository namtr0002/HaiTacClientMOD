# -*- coding: utf-8 -*-
import os
import sys
import json
import pymysql
from flask import Flask, jsonify, request, send_file, Response

app = Flask(__name__)

# Base directories
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
SERVER_DIR = os.path.join(BASE_DIR, "HaiTacTiHonServer")
ICON_DIR_PRIMARY = os.path.join(SERVER_DIR, "data", "icon")
ICON_DIR_RELEASE = os.path.join(SERVER_DIR, "release", "data", "icon")
DATA_FROM_SERVER = os.path.join(SERVER_DIR, "data", "datafromserver")
EFFECT_DIR = os.path.join(SERVER_DIR, "data", "Effect")

# MySQL Configuration
DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "",
    "database": "haitacz",
    "charset": "utf8mb4",
    "cursorclass": pymysql.cursors.DictCursor,
    "autocommit": True
}

def get_db():
    return pymysql.connect(**DB_CONFIG)

file_cache = {}

def scan_icon_files():
    global file_cache
    file_cache = {}
    for base in [ICON_DIR_RELEASE, ICON_DIR_PRIMARY]:
        if not os.path.exists(base):
            continue
        for root, dirs, files in os.walk(base):
            for f in files:
                if f.lower().endswith(".png"):
                    name = f[:-4]
                    if name.isdigit():
                        fid = int(name)
                        rel = os.path.relpath(root, base).replace("\\", "/")
                        parts = rel.split("/")
                        zoom = 4
                        if len(parts) >= 1 and parts[0].isdigit():
                            zoom = int(parts[0])
                        full_path = os.path.join(root, f)
                        file_cache[(zoom, fid)] = full_path
                        if (4, fid) not in file_cache:
                            file_cache[(4, fid)] = full_path

scan_icon_files()

def resolve_icon_path(file_id, zoom=4):
    try:
        file_id = int(file_id)
        zoom = int(zoom)
    except (ValueError, TypeError):
        return None

    if (zoom, file_id) in file_cache and os.path.exists(file_cache[(zoom, file_id)]):
        return file_cache[(zoom, file_id)]
    
    if (4, file_id) in file_cache and os.path.exists(file_cache[(4, file_id)]):
        return file_cache[(4, file_id)]

    for z in [4, 3, 2, 1]:
        if (z, file_id) in file_cache and os.path.exists(file_cache[(z, file_id)]):
            return file_cache[(z, file_id)]

    candidates = [
        os.path.join(ICON_DIR_PRIMARY, str(zoom), f"{file_id}.png"),
        os.path.join(ICON_DIR_RELEASE, str(zoom), f"{file_id}.png"),
        os.path.join(ICON_DIR_PRIMARY, "4", f"{file_id}.png"),
        os.path.join(ICON_DIR_RELEASE, "4", f"{file_id}.png"),
        os.path.join(ICON_DIR_PRIMARY, "1", f"{file_id}.png"),
        os.path.join(ICON_DIR_RELEASE, "1", f"{file_id}.png"),
        os.path.join(ICON_DIR_PRIMARY, f"{file_id}.png"),
        os.path.join(ICON_DIR_RELEASE, f"{file_id}.png"),
    ]

    if 24000 <= file_id < 26000:
        eff_id = file_id - 24000 if file_id < 25000 else file_id - 25000
        candidates.extend([
            os.path.join(DATA_FROM_SERVER, f"x{zoom}", "eff", f"g{eff_id}.png"),
            os.path.join(DATA_FROM_SERVER, "x4", "eff", f"g{eff_id}.png"),
            os.path.join(DATA_FROM_SERVER, f"x{zoom}", "eff", f"{eff_id}.png"),
            os.path.join(DATA_FROM_SERVER, "x4", "eff", f"{eff_id}.png"),
            os.path.join(EFFECT_DIR, f"x{zoom}", f"{eff_id}.png"),
            os.path.join(EFFECT_DIR, "x4", f"{eff_id}.png"),
        ])

    for path in candidates:
        if os.path.exists(path):
            file_cache[(zoom, file_id)] = path
            return path

    return None

def check_icon_exists(file_id, zoom=4):
    return resolve_icon_path(file_id, zoom) is not None

def get_part_file_id(sub_icon_id):
    if sub_icon_id >= 10000:
        return sub_icon_id - 10000 + 26000
    return sub_icon_id + 10000

OFFSET_MAP = {
    "parts": {"base": 10000, "formula": "icon + 10000 (>=10000 -> icon-10000+26000)", "target": "data/icon/{zoom}/{id}.png"},
    "item3": {"base": 3000, "formula": "icon + 3000", "target": "data/icon/{zoom}/{id+3000}.png"},
    "item4": {"base": 2000, "formula": "icon + 2000", "target": "data/icon/{zoom}/{id+2000}.png"},
    "item7": {"base": 6500, "formula": "icon + 6500", "target": "data/icon/{zoom}/{id+6500}.png"},
    "item8": {"base": 2000, "formula": "icon + 2000 (hoặc +3000)", "target": "data/icon/{zoom}/{id+2000}.png"},
    "skill_main": {"base": 4000, "formula": "icon + 4000", "target": "data/icon/{zoom}/{icon+4000}.png"},
    "skill_mini": {"base": 4500, "formula": "icon + 4500", "target": "data/icon/{zoom}/{icon+4500}.png"},
    "clan_icon": {"base": 7000, "formula": "id + 7000", "target": "data/icon/{zoom}/{id+7000}.png"},
    "clan_hanhtrinh_small": {"base": 7000, "formula": "icon + 7000", "target": "data/icon/{zoom}/{icon+7000}.png"},
    "clan_hanhtrinh_banner": {"base": 22000, "formula": "icon + 22000", "target": "data/icon/{zoom}/{icon+22000}.png"},
    "fashion": {"base": 20000, "formula": "icon + 20000", "target": "data/icon/{zoom}/{icon+20000}.png"},
    "pet": {"base": 23000, "formula": "icon + 23000", "target": "data/icon/{zoom}/{icon+23000}.png"},
    "mobs": {"base": 1000, "formula": "mob_icon_id + 1000", "target": "data/icon/{zoom}/{id+1000}.png"},
    "danhhieu": {"base": 24000, "formula": "idEff + 24000", "target": "data/icon/{zoom}/{idEff+24000}.png"}
}

PART_TYPES = {
    0: "Đầu / Mặt (Head)",
    1: "Thân / Áo (Body)",
    2: "Chân / Quần (Leg)",
    3: "Vũ khí (Weapon)",
    4: "Nón / Phụ kiện (Hat)",
    5: "Tóc (Hair)"
}

@app.route("/api/icon/<int:file_id>")
def serve_icon(file_id):
    zoom = request.args.get("zoom", 4, type=int)
    path = resolve_icon_path(file_id, zoom)
    if path and os.path.exists(path):
        return send_file(path, mimetype="image/png")
    transparent_png = b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15c4\x00\x00\x00\rIDATx\x9cc`\x00\x00\x00\x02\x00\x01H\xaf\xa4q\x00\x00\x00\x00IEND\xaeB`\x82'
    return Response(transparent_png, mimetype="image/png", status=404)

@app.route("/api/stats")
def get_stats():
    conn = get_db()
    cursor = conn.cursor()
    stats = {}
    
    table_queries = {
        "parts": "SELECT id, data FROM parts",
        "item3": "SELECT id, icon FROM item3",
        "item4": "SELECT id, icon FROM item4",
        "item7": "SELECT id, icon FROM item7",
        "item8": "SELECT id, icon FROM item8",
        "skill": "SELECT id, icon FROM skill",
        "clan_icon": "SELECT id FROM clan_icon",
        "clan_hanhtrinh": "SELECT id, icon FROM clan_hanhtrinh_template",
        "fashion": "SELECT id, icon FROM fashiontemplate",
        "pet": "SELECT id, icon FROM pet_template",
        "hair": "SELECT id, icon FROM itemhair",
        "face": "SELECT id, icon FROM itemface",
        "mobs": "SELECT id, idicon FROM mobs",
        "danhhieu": "SELECT id, idEff FROM danhhieu"
    }

    for key, q in table_queries.items():
        try:
            cursor.execute(q)
            rows = cursor.fetchall()
            total = len(rows)
            missing = 0
            
            for r in rows:
                if key == "parts":
                    try:
                        frames = json.loads(r["data"]) if r["data"] else []
                        has_missing_sub = False
                        for f in frames:
                            sub_id = f[0]
                            fid = get_part_file_id(sub_id)
                            if not check_icon_exists(fid):
                                has_missing_sub = True
                                break
                        if has_missing_sub:
                            missing += 1
                    except Exception:
                        pass
                elif key == "item3":
                    fid = (r["icon"] or 0) + 3000
                    if not check_icon_exists(fid): missing += 1
                elif key == "item4":
                    fid = (r["icon"] or 0) + 2000
                    if not check_icon_exists(fid): missing += 1
                elif key == "item7":
                    fid = (r["icon"] or 0) + 6500
                    if not check_icon_exists(fid): missing += 1
                elif key == "item8":
                    fid = (r["icon"] or 0) + 2000
                    if not check_icon_exists(fid) and not check_icon_exists((r["icon"] or 0) + 3000):
                        missing += 1
                elif key == "skill":
                    fid_main = (r["icon"] or 0) + 4000
                    if not check_icon_exists(fid_main): missing += 1
                elif key == "clan_icon":
                    fid = r["id"] + 7000
                    if not check_icon_exists(fid): missing += 1
                elif key == "clan_hanhtrinh":
                    fid = (r["icon"] or 0) + 7000
                    if not check_icon_exists(fid): missing += 1
                elif key == "fashion":
                    fid = (r["icon"] or 0) + 20000
                    if not check_icon_exists(fid): missing += 1
                elif key == "pet":
                    fid = (r["icon"] or 0) + 23000
                    if not check_icon_exists(fid): missing += 1
                elif key == "danhhieu":
                    fid = (r["idEff"] or 0) + 24000
                    if not check_icon_exists(fid): missing += 1

            stats[key] = {
                "total": total,
                "missing": missing,
                "found": total - missing
            }
        except Exception as e:
            stats[key] = {"total": 0, "missing": 0, "found": 0, "error": str(e)}

    conn.close()
    return jsonify({
        "stats": stats,
        "offset_map": OFFSET_MAP
    })

@app.route("/api/category/<cat_name>")
def get_category_data(cat_name):
    page = request.args.get("page", 1, type=int)
    limit = request.args.get("limit", 100, type=int)
    search = request.args.get("search", "", type=str).strip().lower()
    status_filter = request.args.get("status", "all", type=str)
    zoom = request.args.get("zoom", 4, type=int)

    conn = get_db()
    cursor = conn.cursor()
    items = []

    try:
        if cat_name == "parts":
            cursor.execute("SELECT id, type, data FROM parts ORDER BY id ASC")
            for r in cursor.fetchall():
                pid = r["id"]
                ptype = r["type"]
                type_label = PART_TYPES.get(ptype, f"Type {ptype}")
                frames = []
                try:
                    frames = json.loads(r["data"]) if r["data"] else []
                except Exception:
                    pass
                
                sub_icons = []
                has_missing = False
                for f in frames:
                    sub_id = f[0]
                    dx = f[1] if len(f) > 1 else 0
                    dy = f[2] if len(f) > 2 else 0
                    fid = get_part_file_id(sub_id)
                    is_ok = check_icon_exists(fid, zoom)
                    if not is_ok:
                        has_missing = True
                    sub_icons.append({
                        "sub_id": sub_id,
                        "file_id": fid,
                        "dx": dx,
                        "dy": dy,
                        "exists": is_ok
                    })

                items.append({
                    "id": pid,
                    "type": ptype,
                    "type_label": type_label,
                    "frame_count": len(frames),
                    "sub_icons": sub_icons,
                    "exists": not has_missing and len(sub_icons) > 0,
                    "has_missing": has_missing
                })

        elif cat_name == "item3":
            cursor.execute("SELECT id, name, icon, part, typeequip, level, color, beri FROM item3 ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 3000
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "part": r["part"],
                    "type": r["typeequip"],
                    "level": r["level"],
                    "color": r["color"],
                    "price": r["beri"],
                    "exists": is_ok
                })

        elif cat_name == "item4":
            cursor.execute("SELECT id, name, icon, indexInfoPotion, istrade, hpmpother, timedelay, value, timeactive, nameuse FROM item4 ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 2000
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "price": r["value"],
                    "type": r["hpmpother"],
                    "nameuse": r["nameuse"],
                    "exists": is_ok
                })

        elif cat_name == "item7":
            cursor.execute("SELECT id, name, icon, type, price, priceruby FROM item7 ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 6500
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "price": r["price"],
                    "priceruby": r["priceruby"],
                    "type": r["type"],
                    "exists": is_ok
                })

        elif cat_name == "item8":
            cursor.execute("SELECT id, name, icon, info, price, priceruby, hpmpother, timeactive FROM item8 ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 2000
                is_ok = check_icon_exists(fid, zoom)
                if not is_ok and check_icon_exists(icon + 3000, zoom):
                    fid = icon + 3000
                    is_ok = True
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "info": r["info"],
                    "price": r["price"],
                    "priceruby": r["priceruby"],
                    "exists": is_ok
                })

        elif cat_name == "skill":
            cursor.execute("SELECT id, id_index, id_2, icon, typeSkill, typeBuff, name, typeEffSkill, Lv_RQ, info FROM skill ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                main_fid = icon + 4000
                mini_fid = icon + 4500
                main_ok = check_icon_exists(main_fid, zoom)
                mini_ok = check_icon_exists(mini_fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "main_file_id": main_fid,
                    "mini_file_id": mini_fid,
                    "main_exists": main_ok,
                    "mini_exists": mini_ok,
                    "exists": main_ok and mini_ok,
                    "id_2": r["id_2"],
                    "id_index": r["id_index"],
                    "typeSkill": r["typeSkill"],
                    "level": r["Lv_RQ"],
                    "info": r["info"]
                })

        elif cat_name == "clan_icon":
            cursor.execute("SELECT id, name, info, price, type, sell FROM clan_icon ORDER BY id ASC")
            for r in cursor.fetchall():
                fid = r["id"] + 7000
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "info": r["info"],
                    "file_id": fid,
                    "price": r["price"],
                    "type": r["type"],
                    "sell": r["sell"],
                    "exists": is_ok
                })

        elif cat_name == "clan_hanhtrinh":
            cursor.execute("SELECT id, name, icon, info, op, rd FROM clan_hanhtrinh_template ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                small_fid = icon + 7000
                banner_fid = icon + 22000
                s_ok = check_icon_exists(small_fid, zoom)
                b_ok = check_icon_exists(banner_fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "small_file_id": small_fid,
                    "banner_file_id": banner_fid,
                    "small_exists": s_ok,
                    "banner_exists": b_ok,
                    "exists": s_ok and b_ok,
                    "info": r["info"]
                })

        elif cat_name == "fashion":
            cursor.execute("SELECT id, name, icon, info, mwear, price, hsd FROM fashiontemplate ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 20000
                is_ok = check_icon_exists(fid, zoom)
                worn_parts = []
                try:
                    worn_parts = json.loads(r["mwear"]) if r["mwear"] else []
                except Exception:
                    pass
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "info": r["info"],
                    "worn_parts": worn_parts,
                    "price": r["price"],
                    "hsd": r["hsd"],
                    "exists": is_ok
                })

        elif cat_name == "pet":
            cursor.execute("SELECT id, name, icon, type, frame FROM pet_template ORDER BY id ASC")
            for r in cursor.fetchall():
                icon = r["icon"] or 0
                fid = icon + 23000
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "icon": icon,
                    "file_id": fid,
                    "type": r["type"],
                    "frame": r["frame"],
                    "exists": is_ok
                })

        elif cat_name == "hair":
            cursor.execute("SELECT id, type, name, icon, beri, ruby FROM itemhair ORDER BY id ASC")
            for r in cursor.fetchall():
                part_id = r["icon"]
                cursor2 = conn.cursor()
                cursor2.execute("SELECT data FROM parts WHERE id = %s", (part_id,))
                part_row = cursor2.fetchone()
                sub_icons = []
                has_missing = False
                if part_row and part_row["data"]:
                    try:
                        frames = json.loads(part_row["data"])
                        for f in frames:
                            sid = f[0]
                            fid = get_part_file_id(sid)
                            ok = check_icon_exists(fid, zoom)
                            if not ok: has_missing = True
                            sub_icons.append({"sub_id": sid, "file_id": fid, "dx": f[1], "dy": f[2], "exists": ok})
                    except Exception:
                        pass
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "part_id": part_id,
                    "sub_icons": sub_icons,
                    "price": r["beri"],
                    "priceruby": r["ruby"],
                    "exists": not has_missing and len(sub_icons) > 0
                })

        elif cat_name == "face":
            cursor.execute("SELECT id, name, icon, beri, ruby FROM itemface ORDER BY id ASC")
            for r in cursor.fetchall():
                part_id = r["icon"]
                cursor2 = conn.cursor()
                cursor2.execute("SELECT data FROM parts WHERE id = %s", (part_id,))
                part_row = cursor2.fetchone()
                sub_icons = []
                has_missing = False
                if part_row and part_row["data"]:
                    try:
                        frames = json.loads(part_row["data"])
                        for f in frames:
                            sid = f[0]
                            fid = get_part_file_id(sid)
                            ok = check_icon_exists(fid, zoom)
                            if not ok: has_missing = True
                            sub_icons.append({"sub_id": sid, "file_id": fid, "dx": f[1], "dy": f[2], "exists": ok})
                    except Exception:
                        pass
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "part_id": part_id,
                    "sub_icons": sub_icons,
                    "price": r["beri"],
                    "priceruby": r["ruby"],
                    "exists": not has_missing and len(sub_icons) > 0
                })

        elif cat_name == "mobs":
            cursor.execute("SELECT id, name, idicon, typemonster, level, hp FROM mobs ORDER BY id ASC")
            for r in cursor.fetchall():
                mob_icon_id = None
                raw_idicon = r["idicon"]
                try:
                    js = json.loads(raw_idicon)
                    if isinstance(js, list) and len(js) > 1:
                        mob_icon_id = js[1]
                except Exception:
                    pass
                fid = (mob_icon_id + 1000) if (mob_icon_id is not None and isinstance(mob_icon_id, int)) else None
                is_ok = check_icon_exists(fid, zoom) if fid is not None else False
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "idicon": raw_idicon,
                    "mob_icon_id": mob_icon_id,
                    "file_id": fid,
                    "type": r["typemonster"],
                    "level": r["level"],
                    "exists": is_ok
                })

        elif cat_name == "danhhieu":
            cursor.execute("SELECT id, name, info, idEff, coin FROM danhhieu ORDER BY id ASC")
            for r in cursor.fetchall():
                idEff = r["idEff"] or 0
                fid = idEff + 24000
                is_ok = check_icon_exists(fid, zoom)
                items.append({
                    "id": r["id"],
                    "name": r["name"],
                    "info": r["info"],
                    "idEff": idEff,
                    "file_id": fid,
                    "coin": r["coin"],
                    "exists": is_ok
                })

    except Exception as e:
        conn.close()
        return jsonify({"error": str(e), "items": [], "total": 0}), 500

    conn.close()

    filtered = []
    for it in items:
        if search:
            match = False
            sid = str(it.get("id", ""))
            sname = str(it.get("name", "")).lower()
            sicon = str(it.get("icon", ""))
            sfid = str(it.get("file_id", ""))
            if search in sid or search in sname or search in sicon or search in sfid:
                match = True
            if not match and "sub_icons" in it:
                for sub in it["sub_icons"]:
                    if search in str(sub["sub_id"]) or search in str(sub["file_id"]):
                        match = True
                        break
            if not match:
                continue

        if status_filter == "ok" and not it.get("exists", False):
            continue
        if status_filter == "missing" and it.get("exists", False):
            continue

        filtered.append(it)

    total_filtered = len(filtered)
    start = (page - 1) * limit
    end = start + limit
    paginated = filtered[start:end]

    return jsonify({
        "items": paginated,
        "total": total_filtered,
        "page": page,
        "limit": limit,
        "pages": (total_filtered + limit - 1) // limit if limit > 0 else 1
    })

@app.route("/api/part_data/<int:part_id>")
def get_part_data(part_id):
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT id, type, data FROM parts WHERE id = %s", (part_id,))
    row = cursor.fetchone()
    conn.close()
    if not row:
        return jsonify({"error": "Part not found"}), 404

    ptype = row["type"]
    type_label = PART_TYPES.get(ptype, f"Type {ptype}")
    frames = []
    try:
        frames = json.loads(row["data"]) if row["data"] else []
    except Exception:
        pass

    sub_icons = []
    for f in frames:
        sid = f[0]
        dx = f[1] if len(f) > 1 else 0
        dy = f[2] if len(f) > 2 else 0
        fid = get_part_file_id(sid)
        sub_icons.append({
            "sub_id": sid,
            "file_id": fid,
            "dx": dx,
            "dy": dy,
            "exists": check_icon_exists(fid)
        })

    return jsonify({
        "id": row["id"],
        "type": ptype,
        "type_label": type_label,
        "sub_icons": sub_icons
    })

@app.route("/api/reverse_lookup")
def reverse_lookup():
    file_id = request.args.get("file_id", type=int)
    if file_id is None:
        return jsonify({"error": "Missing file_id"}), 400

    conn = get_db()
    cursor = conn.cursor()
    results = []
    exists_disk = check_icon_exists(file_id)

    # 1. Parts check
    sub_icon_id = None
    if file_id >= 26000:
        sub_icon_id = file_id - 26000 + 10000
    elif file_id >= 10000:
        sub_icon_id = file_id - 10000

    if sub_icon_id is not None and sub_icon_id >= 0:
        cursor.execute("SELECT id, type, data FROM parts WHERE data LIKE %s LIMIT 10", (f"%[{sub_icon_id},%",))
        matching_parts = []
        for pr in cursor.fetchall():
            try:
                frames = json.loads(pr["data"])
                for f in frames:
                    if f[0] == sub_icon_id:
                        matching_parts.append({
                            "part_id": pr["id"],
                            "type": pr["type"],
                            "type_label": PART_TYPES.get(pr["type"], ""),
                            "dx": f[1],
                            "dy": f[2]
                        })
                        break
            except Exception:
                pass
        results.append({
            "category": "Parts (CharPart)",
            "offset_applied": "-10000" if file_id < 26000 else "-26000+10000",
            "db_id_or_icon": sub_icon_id,
            "matches_count": len(matching_parts),
            "details": matching_parts,
            "formula": f"{file_id} - 10000 = {sub_icon_id}"
        })

    # 2. Item3 check
    db_icon_item3 = file_id - 3000
    if db_icon_item3 >= 0:
        cursor.execute("SELECT id, name, icon, part FROM item3 WHERE icon = %s LIMIT 10", (db_icon_item3,))
        items = cursor.fetchall()
        results.append({
            "category": "Item3 (Trang bị)",
            "offset_applied": "-3000",
            "db_id_or_icon": db_icon_item3,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 3000 = {db_icon_item3}"
        })

    # 3. Item4 check
    db_icon_item4 = file_id - 2000
    if db_icon_item4 >= 0:
        cursor.execute("SELECT id, name, icon, value FROM item4 WHERE icon = %s LIMIT 10", (db_icon_item4,))
        items = cursor.fetchall()
        results.append({
            "category": "Item4 (Tiêu hao / Dược phẩm)",
            "offset_applied": "-2000",
            "db_id_or_icon": db_icon_item4,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 2000 = {db_icon_item4}"
        })

    # 4. Item7 check
    db_icon_item7 = file_id - 6500
    if db_icon_item7 >= 0:
        cursor.execute("SELECT id, name, icon, price FROM item7 WHERE icon = %s LIMIT 10", (db_icon_item7,))
        items = cursor.fetchall()
        results.append({
            "category": "Item7 (Nguyên liệu / Đá)",
            "offset_applied": "-6500",
            "db_id_or_icon": db_icon_item7,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 6500 = {db_icon_item7}"
        })

    # 5. Skill Main
    db_icon_skill_main = file_id - 4000
    if 0 <= db_icon_skill_main < 500:
        cursor.execute("SELECT id, name, icon FROM skill WHERE icon = %s LIMIT 10", (db_icon_skill_main,))
        items = cursor.fetchall()
        results.append({
            "category": "Skill (Icon chính)",
            "offset_applied": "-4000",
            "db_id_or_icon": db_icon_skill_main,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 4000 = {db_icon_skill_main}"
        })

    # 6. Skill Mini
    db_icon_skill_mini = file_id - 4500
    if db_icon_skill_mini >= 0:
        cursor.execute("SELECT id, name, icon FROM skill WHERE icon = %s LIMIT 10", (db_icon_skill_mini,))
        items = cursor.fetchall()
        results.append({
            "category": "Skill (Icon Mini)",
            "offset_applied": "-4500",
            "db_id_or_icon": db_icon_skill_mini,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 4500 = {db_icon_skill_mini}"
        })

    # 7. Clan Icon
    db_id_clan = file_id - 7000
    if db_id_clan >= 0:
        cursor.execute("SELECT id, name, price FROM clan_icon WHERE id = %s", (db_id_clan,))
        items = cursor.fetchall()
        results.append({
            "category": "Clan Icon (Cờ Bang)",
            "offset_applied": "-7000",
            "db_id_or_icon": db_id_clan,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 7000 = {db_id_clan}"
        })

    # 8. Clan Banner
    db_icon_banner = file_id - 22000
    if db_icon_banner >= 0:
        cursor.execute("SELECT id, name, icon FROM clan_hanhtrinh_template WHERE icon = %s", (db_icon_banner,))
        items = cursor.fetchall()
        results.append({
            "category": "Clan Hành Trình Banner (Huy hiệu lớn)",
            "offset_applied": "-22000",
            "db_id_or_icon": db_icon_banner,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 22000 = {db_icon_banner}"
        })

    # 9. Fashion
    db_icon_fashion = file_id - 20000
    if db_icon_fashion >= 0:
        cursor.execute("SELECT id, name, icon, mwear FROM fashiontemplate WHERE icon = %s", (db_icon_fashion,))
        items = cursor.fetchall()
        results.append({
            "category": "Fashion (Thời trang)",
            "offset_applied": "-20000",
            "db_id_or_icon": db_icon_fashion,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 20000 = {db_icon_fashion}"
        })

    # 10. Pet
    db_icon_pet = file_id - 23000
    if db_icon_pet >= 0:
        cursor.execute("SELECT id, name, icon FROM pet_template WHERE icon = %s", (db_icon_pet,))
        items = cursor.fetchall()
        results.append({
            "category": "Pet (Thú cưng)",
            "offset_applied": "-23000",
            "db_id_or_icon": db_icon_pet,
            "matches_count": len(items),
            "details": items,
            "formula": f"{file_id} - 23000 = {db_icon_pet}"
        })

    conn.close()

    return jsonify({
        "file_id": file_id,
        "exists_disk": exists_disk,
        "resolved_path": resolve_icon_path(file_id),
        "results": results
    })

@app.route("/")
def index():
    html_file = os.path.join(os.path.dirname(__file__), "index.html")
    with open(html_file, "r", encoding="utf-8") as f:
        return f.read()

if __name__ == "__main__":
    port = 8088
    print("===========================================================")
    print("  HAI TAC TI HON - WEB ICON CHECKER (CSDL: haitacz)")
    print(f"  Server running at: http://localhost:{port}")
    print("===========================================================")
    app.run(host="0.0.0.0", port=port, debug=False)
