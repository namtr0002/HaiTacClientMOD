import tkinter as tk
from tkinter import ttk, messagebox
import os, datetime, threading, hashlib, base64, ctypes, zlib
from github import Github, GithubException, Auth
from tool_tab import MasterToolPanel

# ── CONFIG ──────────────────────────────────────────────────────────────────
REPO_NAME     = "namtr0002/HTTH"
KEY_FILE_PATH = "KEY.txt"

# ── DESIGN TOKENS ────────────────────────────────────────────────────────────
C_BG      = "#08080c"
C_CARD    = "#0f0f16"
C_SIDE    = "#0a0a10"
C_ACCENT  = "#0078ff"
C_ACCENT2 = "#7800dc"
C_TEXT    = "#e6e6f0"
C_DIM     = "#64647a"
C_INPUT   = "#14141e"
C_BORDER  = "#22222e"
C_OK      = "#00d278"
C_ERR     = "#ff4646"
C_WARN    = "#ffa528"

THEME = {
    "bg": C_BG,
    "card": C_CARD,
    "side": C_SIDE,
    "accent": C_ACCENT,
    "accent2": C_ACCENT2,
    "text": C_TEXT,
    "dim": C_DIM,
    "input": C_INPUT,
    "border": C_BORDER,
    "ok": C_OK,
    "err": C_ERR,
    "warn": C_WARN,
}

F_TITLE = ("Segoe UI", 16, "bold")
F_BOLD  = ("Segoe UI", 9,  "bold")
F_MAIN  = ("Segoe UI", 9)
F_MONO  = ("Consolas", 9)

# ── HTTH-V5 QUANTUM SHIELD CRYPTO ENGINE ─────────────────────────────────────
_SALT_PRIMARY_V5   = b"HTTH_V5_QUANTUM_PRIME_2026_MASTER_SECRET_KEY!@#$"
_SALT_SECONDARY_V5 = hashlib.sha512(b"HTTH_NEXUS_ULTIMATE_SBOX_SEED_V5").digest()

_SALT_PRIMARY_V4   = b"HTTH_V4_NEXUS_PRIME_KEY_2026!@#$"
_SALT_SECONDARY_V4 = hashlib.sha256(b"HTTH_NEXUS_2026_SBOX_SEED").digest()

def _build_sbox_v5():
    box = list(range(256))
    raw = _SALT_SECONDARY_V5
    j = 0
    for i in range(256):
        j = (j + box[i] + raw[i % len(raw)]) & 0xFF
        box[i], box[j] = box[j], box[i]
    inv = [0] * 256
    for i in range(256):
        inv[box[i]] = i
    return bytes(box), bytes(inv)

def _build_sbox_v4():
    box = list(range(256))
    raw = _SALT_SECONDARY_V4
    j = 0
    for i in range(256):
        j = (j + box[i] + raw[i % len(raw)]) & 0xFF
        box[i], box[j] = box[j], box[i]
    inv = [0] * 256
    for i in range(256):
        inv[box[i]] = i
    return bytes(box), bytes(inv)

SBOX_V5, SBOX_INV_V5 = _build_sbox_v5()
SBOX_V4, SBOX_INV_V4 = _build_sbox_v4()

def htth_v5_encrypt(text: str) -> str:
    """HTTH-V5 Quantum Shield Encrypt — mirrors Java KeyAuth.encryptV5()"""
    if not text:
        return ""
    b   = text.strip().encode("utf-8")
    sp  = len(_SALT_PRIMARY_V5)
    ss  = len(_SALT_SECONDARY_V5)
    out = bytearray(len(b))
    for i, byte in enumerate(b):
        val = byte & 0xFF
        val ^= _SALT_PRIMARY_V5[i % sp]
        shift = ((i * 3 + 5) % 7) + 1
        val = ((val << shift) | (val >> (8 - shift))) & 0xFF
        val = SBOX_V5[val]
        val ^= _SALT_SECONDARY_V5[ss - 1 - ((i * 2) % ss)]
        val = (~val) & 0xFF
        out[i] = val

    crc = zlib.crc32(out) & 0xFFFFFFFF
    tag = crc.to_bytes(4, byteorder='big')
    full = out + tag
    return "V5$" + base64.urlsafe_b64encode(bytes(full)).rstrip(b"=").decode("ascii")

def htth_v5_decrypt(token: str) -> str:
    """HTTH-V5 Quantum Shield Decrypt — mirrors Java KeyAuth.decryptV5()"""
    if not token:
        return ""
    try:
        raw_token = token.strip()
        if raw_token.startswith("V5$"):
            raw_token = raw_token[3:]
        padding = (4 - len(raw_token) % 4) % 4
        if padding != 4 and padding > 0:
            raw_token += "=" * padding
        full = bytearray(base64.urlsafe_b64decode(raw_token))
        if len(full) < 4:
            return token
        raw = full[:-4]
        sp  = len(_SALT_PRIMARY_V5)
        ss  = len(_SALT_SECONDARY_V5)
        out = bytearray(len(raw))
        for i, byte in enumerate(raw):
            val = byte & 0xFF
            val = (~val) & 0xFF
            val ^= _SALT_SECONDARY_V5[ss - 1 - ((i * 2) % ss)]
            val = SBOX_INV_V5[val]
            shift = ((i * 3 + 5) % 7) + 1
            val = ((val >> shift) | (val << (8 - shift))) & 0xFF
            val ^= _SALT_PRIMARY_V5[i % sp]
            out[i] = val
        return out.decode("utf-8")
    except Exception:
        return token

def htth_v4_encrypt(text: str) -> str:
    if not text: return ""
    b   = text.encode("utf-8")
    sp  = len(_SALT_PRIMARY_V4)
    ss  = len(_SALT_SECONDARY_V4)
    out = bytearray(len(b))
    for i, byte in enumerate(b):
        val = byte & 0xFF
        val ^= _SALT_PRIMARY_V4[i % sp]
        shift = (i % 7) + 1
        val = ((val << shift) | (val >> (8 - shift))) & 0xFF
        val = SBOX_V4[val]
        val ^= _SALT_SECONDARY_V4[ss - 1 - (i % ss)]
        val = (~val) & 0xFF
        out[i] = val
    return base64.urlsafe_b64encode(bytes(out)).rstrip(b"=").decode("ascii")

def htth_v4_decrypt(token: str) -> str:
    if not token: return ""
    try:
        padding = (4 - len(token) % 4) % 4
        if padding != 4 and padding > 0:
            token += "=" * padding
        raw = bytearray(base64.urlsafe_b64decode(token))
        sp  = len(_SALT_PRIMARY_V4)
        ss  = len(_SALT_SECONDARY_V4)
        out = bytearray(len(raw))
        for i, byte in enumerate(raw):
            val = byte & 0xFF
            val = (~val) & 0xFF
            val ^= _SALT_SECONDARY_V4[ss - 1 - (i % ss)]
            val = SBOX_INV_V4[val]
            shift = (i % 7) + 1
            val = ((val >> shift) | (val << (8 - shift))) & 0xFF
            val ^= _SALT_PRIMARY_V4[i % sp]
            out[i] = val
        return out.decode("utf-8")
    except Exception:
        return token

def htth_encrypt(text: str) -> str:
    return htth_v5_encrypt(text)

def htth_decrypt(token: str) -> str:
    if not token:
        return ""
    t = token.strip()
    if t.startswith("V5$"):
        return htth_v5_decrypt(t)
    dec5 = htth_v5_decrypt(t)
    if dec5 != t and dec5 != "[decrypt error]":
        return dec5
    dec4 = htth_v4_decrypt(t)
    if dec4 != t:
        return dec4
    return t

# ── WINDOWS DARK MODE ─────────────────────────────────────────────────────────
def _dark(win):
    try:
        win.update()
        hwnd  = ctypes.windll.user32.GetParent(win.winfo_id())
        value = ctypes.c_int(2)
        ctypes.windll.dwmapi.DwmSetWindowAttribute(hwnd, 20, ctypes.byref(value), ctypes.sizeof(value))
    except: pass

# ── UI HELPERS ────────────────────────────────────────────────────────────────
class Inp(tk.Entry):
    def __init__(self, master, **kw):
        super().__init__(master, bg=C_INPUT, fg=C_TEXT, insertbackground=C_TEXT,
                         relief="flat", bd=0, font=F_MONO, **kw)

def mk_btn(parent, text, bg, fg, cmd, **kw):
    b = tk.Button(parent, text=text, bg=bg, fg=fg, font=F_BOLD,
                  relief="flat", cursor="hand2", command=cmd, **kw)
    return b

# ── MAIN APP ──────────────────────────────────────────────────────────────────
class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("HTTH Tool Suite & Admin Console  —  V5 Quantum Shield")
        self.geometry("1360x860")
        self.minsize(1050, 700)
        self.configure(bg=C_BG)
        _dark(self)

        self.github_client = None
        self.repo          = None
        self.rows = []

        self._styles()
        self._build()

    # ── Styles ────────────────────────────────────────────────────────
    def _styles(self):
        s = ttk.Style()
        s.theme_use("clam")
        s.configure(".",          background=C_BG,   foreground=C_TEXT, borderwidth=0)
        s.configure("TNotebook", background=C_BG, borderwidth=0)
        s.configure("TNotebook.Tab", background=C_CARD, foreground=C_TEXT, font=("Segoe UI", 9, "bold"), padding=[14, 8])
        s.map("TNotebook.Tab", background=[("selected", C_ACCENT)], foreground=[("selected", "white")])
        s.configure("Treeview",   background=C_CARD, foreground=C_TEXT,
                    fieldbackground=C_CARD, rowheight=36, font=F_MAIN)
        s.configure("Treeview.Heading", background=C_SIDE, foreground=C_ACCENT,
                    font=F_BOLD, padding=8)
        s.map("Treeview", background=[("selected", C_ACCENT)])

    # ── Layout ────────────────────────────────────────────────────────
    def _build(self):
        # SIDEBAR
        self.side = tk.Frame(self, bg=C_SIDE, width=240)
        self.side.pack(side="left", fill="y")
        self.side.pack_propagate(False)

        tk.Label(self.side, text="HTTH STUDIO", bg=C_SIDE, fg=C_ACCENT, font=F_TITLE)\
          .pack(pady=(24, 2), padx=20, anchor="w")
        tk.Label(self.side, text="Quantum V5 • Master Suite", bg=C_SIDE, fg=C_DIM, font=F_MAIN)\
          .pack(padx=20, anchor="w")

        # Navigation Buttons
        nav_frm = tk.Frame(self.side, bg=C_SIDE)
        nav_frm.pack(fill="x", pady=(20, 10), padx=12)

        self.btn_nav_tool = tk.Button(nav_frm, text="🛠️ TOOL MASTER", bg=C_ACCENT, fg="white", font=F_BOLD,
                                      relief="flat", cursor="hand2", anchor="w", padx=12, pady=10,
                                      command=self.show_tool_view)
        self.btn_nav_tool.pack(fill="x", pady=3)

        self.btn_nav_license = tk.Button(nav_frm, text="👑 LICENSE MANAGER", bg=C_CARD, fg=C_TEXT, font=F_BOLD,
                                         relief="flat", cursor="hand2", anchor="w", padx=12, pady=10,
                                         command=self.show_license_view)
        self.btn_nav_license.pack(fill="x", pady=3)

        # Connection card (for GitHub License Manager)
        self.card_conn = tk.Frame(self.side, bg=C_CARD, padx=14, pady=14)
        self.card_conn.pack(fill="x", pady=(10, 10), padx=12)

        tk.Label(self.card_conn, text="GITHUB TOKEN", bg=C_CARD, fg=C_DIM, font=F_BOLD).pack(anchor="w")
        self.v_token = tk.StringVar()
        Inp(self.card_conn, textvariable=self.v_token, show="*").pack(fill="x", pady=(6, 8), ipady=6)
        mk_btn(self.card_conn, "⚡ CONNECT REPO", C_ACCENT, "white", self.connect).pack(fill="x", ipady=6)
        self.lbl_conn = tk.Label(self.card_conn, text="Disconnected", bg=C_CARD, fg=C_ERR, font=F_MAIN, pady=4)
        self.lbl_conn.pack()

        # Crypto Engine Info Card
        info = tk.Frame(self.side, bg=C_CARD, padx=14, pady=12)
        info.pack(fill="x", padx=12)
        tk.Label(info, text="SECURITY V5", bg=C_CARD, fg=C_DIM, font=F_BOLD).pack(anchor="w")
        tk.Label(info, text="HTTH Quantum Shield", bg=C_CARD, fg=C_OK, font=F_MAIN).pack(anchor="w", pady=2)
        tk.Label(info, text="S-Box + 32-bit CRC Tag", bg=C_CARD, fg=C_DIM, font=F_MAIN).pack(anchor="w")

        # CONTENT AREA CONTAINER
        self.content_area = tk.Frame(self, bg=C_BG)
        self.content_area.pack(side="right", fill="both", expand=True)

        # 1. TOOL MASTER VIEW
        self.frame_tool = MasterToolPanel(self.content_area, THEME)

        # 2. LICENSE MANAGER VIEW
        self.frame_license = tk.Frame(self.content_area, bg=C_BG, padx=28, pady=24)
        self._build_license_ui(self.frame_license)

        # Default show TOOL tab
        self.show_tool_view()

    def show_tool_view(self):
        self.frame_license.pack_forget()
        self.frame_tool.pack(fill="both", expand=True)
        self.btn_nav_tool.config(bg=C_ACCENT, fg="white")
        self.btn_nav_license.config(bg=C_CARD, fg=C_TEXT)

    def show_license_view(self):
        self.frame_tool.pack_forget()
        self.frame_license.pack(fill="both", expand=True)
        self.btn_nav_license.config(bg=C_ACCENT, fg="white")
        self.btn_nav_tool.config(bg=C_CARD, fg=C_TEXT)

    # ── License Manager UI ────────────────────────────────────────────
    def _build_license_ui(self, parent):
        tk.Label(parent, text="License Manager  —  V5 Quantum Shield", bg=C_BG, fg=C_TEXT, font=F_TITLE)\
          .pack(anchor="w")

        # Button row
        br = tk.Frame(parent, bg=C_BG, pady=14)
        br.pack(fill="x")

        mk_btn(br, "+ ADD",         C_ACCENT,  "white", self.add_key,    padx=14).pack(side="left", ipady=8)
        mk_btn(br, "✏ EDIT",        "#1e1e28", C_WARN,  self.edit_key,   padx=14).pack(side="left", padx=8, ipady=8)
        mk_btn(br, "🗑 DELETE",     "#1e1e28", C_ERR,   self.delete_key, padx=14).pack(side="left", padx=8, ipady=8)
        mk_btn(br, "💾 SYNC GITHUB", C_OK,      C_BG,    self.save_github, padx=14).pack(side="right", ipady=8)
        mk_btn(br, "🔄 LOAD",       "#1e1e28", C_TEXT,  self.load_keys,  padx=14).pack(side="right", padx=8, ipady=8)

        # Table
        cols = ("Key", "Type", "HWID", "Start Time", "Expires At", "Note")
        tbl_frame = tk.Frame(parent, bg=C_BG)
        tbl_frame.pack(fill="both", expand=True)

        self.tree = ttk.Treeview(tbl_frame, columns=cols, show="headings")
        for c in cols:
            self.tree.heading(c, text=c)
            w = 200 if c == "Key" else 90 if c == "Type" else 130 if c == "HWID" else 130
            self.tree.column(c, width=w, anchor="center")
        sb = ttk.Scrollbar(tbl_frame, orient="vertical", command=self.tree.yview)
        self.tree.configure(yscrollcommand=sb.set)
        self.tree.pack(side="left", fill="both", expand=True)
        sb.pack(side="right", fill="y")
        self.tree.bind("<Double-1>", lambda e: self.edit_key())

        self.lbl_count = tk.Label(parent, text="0 keys", bg=C_BG, fg=C_DIM, font=F_MAIN)
        self.lbl_count.pack(side="bottom", pady=4)

    # ── GitHub License Handlers ───────────────────────────────────────
    def connect(self):
        token = self.v_token.get().strip()
        if not token: return
        def _bg():
            try:
                auth = Auth.Token(token)
                self.github_client = Github(auth=auth)
                self.repo = self.github_client.get_repo(REPO_NAME)
                user = self.github_client.get_user().login
                self.after(0, lambda: self.lbl_conn.config(text=f"✔ {user}", fg=C_OK))
                self.load_keys()
            except Exception as e:
                self.after(0, lambda: messagebox.showerror("Connection Error", str(e)))
        threading.Thread(target=_bg, daemon=True).start()

    def load_keys(self):
        if not self.repo: return
        def _bg():
            try:
                raw = self.repo.get_contents(KEY_FILE_PATH).decoded_content.decode("utf-8")
                self.rows = []
                for line in raw.splitlines():
                    line = line.strip()
                    if not line or line.startswith("#"): continue
                    parts = line.split("|")
                    if len(parts) >= 2:
                        plain_key  = htth_decrypt(parts[0].strip())
                        plain_type = htth_decrypt(parts[1].strip()) if len(parts) > 1 else "map"
                        plain_uid  = htth_decrypt(parts[2].strip()) if len(parts) > 2 else "ALL"
                        start_time = parts[3].strip() if len(parts) > 3 else ""
                        end_time   = parts[4].strip() if len(parts) > 4 else ""
                        note       = "|".join(p.strip() for p in parts[5:]) if len(parts) > 5 else ""
                        
                        if len(parts) == 4:
                            if any(char.isdigit() for char in start_time) or "unlimited" in start_time.lower() or "vinhvien" in start_time.lower():
                                end_time = start_time
                                start_time = ""
                            else:
                                note = start_time
                                start_time = ""
                        elif len(parts) == 5:
                            if not any(char.isdigit() for char in end_time) and "unlimited" not in end_time.lower() and "vinhvien" not in end_time.lower():
                                note = end_time
                                end_time = start_time
                                start_time = ""

                        self.rows.append([plain_key, plain_type, plain_uid,
                                          start_time, end_time, note])
                self.after(0, self._refresh)
            except Exception as e:
                self.after(0, lambda: messagebox.showerror("Load Error", str(e)))
        threading.Thread(target=_bg, daemon=True).start()

    def _refresh(self):
        for i in self.tree.get_children(): self.tree.delete(i)
        for r in self.rows:
            self.tree.insert("", "end", values=(r[0], r[1], r[2], r[3], r[4], r[5]))
        self.lbl_count.config(text=f"{len(self.rows)} keys loaded")

    # ── Shared key dialog (add OR edit) ──────────────────────────────
    def _key_dialog(self, title_text, defaults, on_confirm):
        dlg = tk.Toplevel(self)
        dlg.title(title_text)
        dlg.geometry("520x680")
        dlg.configure(bg=C_SIDE)
        dlg.grab_set()
        _dark(dlg)

        frm = tk.Frame(dlg, bg=C_SIDE, padx=38, pady=34)
        frm.pack(fill="both", expand=True)

        tk.Label(frm, text=title_text, bg=C_SIDE, fg=C_ACCENT, font=F_TITLE)\
          .pack(anchor="w", pady=(0, 16))

        LABELS = [
            ("key",   "License Key"),
            ("type",  "Type  (map / part / effect / effectauto / skill / admin)"),
            ("hwid",  "Machine ID (HWID / ALL)"),
            ("start", "Start Date  (YYYY-MM-DD / HH:MM DD/MM/YYYY)"),
            ("end",   "Expiry Date (YYYY-MM-DD / HH:MM DD/MM/YYYY)"),
            ("note",  "Admin Note"),
        ]
        vs = {}
        for (k, lbl), default in zip(LABELS, defaults):
            tk.Label(frm, text=lbl, bg=C_SIDE, fg=C_DIM, font=F_BOLD)\
              .pack(anchor="w", pady=(8, 3))
            v = tk.StringVar(value=default)
            vs[k] = v
            e = Inp(frm, textvariable=v)
            e.pack(fill="x", ipady=8)

        def do_confirm():
            k = vs["key"].get().strip()
            h = vs["hwid"].get().strip()
            if not k or not h:
                messagebox.showerror("Error", "Key và HWID là bắt buộc!", parent=dlg)
                return
            row = [k, vs["type"].get().strip(), h,
                   vs["start"].get().strip(), vs["end"].get().strip(),
                   vs["note"].get().strip()]
            on_confirm(row)
            self._refresh()
            dlg.destroy()

        mk_btn(frm, "✔ CONFIRM", C_ACCENT, "white", do_confirm)\
          .pack(fill="x", pady=(22, 0), ipady=12)

    def add_key(self):
        now = datetime.datetime.now()
        defaults = [
            f"KEY-{now.strftime('%H%M%S')}",
            "map",
            "ALL",
            now.strftime("%Y-%m-%d"),
            (now + datetime.timedelta(days=30)).strftime("%Y-%m-%d"),
            "Active User",
        ]
        def on_add(row):
            self.rows.append(row)
        self._key_dialog("Add License Key  —  V5", defaults, on_add)

    def edit_key(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo("Edit", "Chọn một key trong bảng trước!")
            return
        idx   = self.tree.index(sel[0])
        if idx >= len(self.rows): return
        old   = self.rows[idx]
        def on_edit(row):
            self.rows[idx] = row
        self._key_dialog("Edit License Key  —  V5", list(old), on_edit)

    def delete_key(self):
        sel = self.tree.selection()
        if not sel: return
        if not messagebox.askyesno("Confirm", "Delete selected key(s)?"): return
        for item in sel:
            vals = self.tree.item(item, "values")
            self.rows = [r for r in self.rows
                         if not (r[0] == vals[0] and r[2] == vals[2])]
        self._refresh()

    def save_github(self):
        if not self.repo:
            messagebox.showwarning("Not Connected", "Connect to GitHub first.")
            return
        lines = ["# HTTH-V5 Format: EncKey|EncType|EncUID|StartTime|EndTime|Note\n"]
        for r in self.rows:
            enc_k = htth_v5_encrypt(r[0])
            enc_t = htth_v5_encrypt(r[1])
            enc_u = htth_v5_encrypt(r[2])
            lines.append(f"{enc_k}|{enc_t}|{enc_u}|{r[3]}|{r[4]}|{r[5]}\n")
        content = "".join(lines)

        def _bg():
            try:
                curr = self.repo.get_contents(KEY_FILE_PATH)
                self.repo.update_file(
                    KEY_FILE_PATH,
                    f"[V5] Update keys {datetime.datetime.now().strftime('%Y-%m-%d %H:%M')}",
                    content, curr.sha
                )
                self.after(0, lambda: messagebox.showinfo("Success",
                    "✔ Keys synced to GitHub with V5 Quantum Shield encryption!"))
            except GithubException as e:
                msg = str(e)
                if "403" in msg:
                    msg = "403 Forbidden — Token thiếu quyền 'repo' hoặc 'Contents: Read & Write'."
                self.after(0, lambda: messagebox.showerror("GitHub Error", msg))
            except Exception as e:
                self.after(0, lambda: messagebox.showerror("Error", str(e)))
        threading.Thread(target=_bg, daemon=True).start()

if __name__ == "__main__":
    app = App()
    app.mainloop()