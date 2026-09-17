import os
import io
import struct
import json
import base64
import binascii
import csv
import tkinter as tk
from tkinter import ttk, filedialog, messagebox

class DataConverterPanel(tk.Frame):
    def __init__(self, parent, theme=None):
        super().__init__(parent, bg=theme.get("bg", "#08080c") if theme else "#08080c")
        self.theme = theme or {
            "bg": "#08080c",
            "card": "#0f0f16",
            "side": "#0a0a10",
            "accent": "#0078ff",
            "text": "#e6e6f0",
            "dim": "#64647a",
            "input": "#14141e",
            "border": "#22222e",
            "ok": "#00d278",
            "err": "#ff4646",
            "warn": "#ffa528",
        }
        self._build_ui()

    def _build_ui(self):
        container = tk.Frame(self, bg=self.theme["bg"], padx=14, pady=10)
        container.pack(fill="both", expand=True)

        # Header Title
        hdr = tk.Frame(container, bg=self.theme["bg"])
        hdr.pack(fill="x", pady=(0, 10))
        tk.Label(hdr, text="⚡ HTTH DATA & BINARY STUDIO (Stream Parser, SQL, JSON, Arrays)", bg=self.theme["bg"], fg=self.theme["accent"], font=("Segoe UI", 14, "bold")).pack(anchor="w")
        tk.Label(hdr, text="Giải mã nhị phân Java DataInputStream, HTTH Effect Data, Hex Dump, Mảng Code Byte Array (Java, C, Python), JSON <-> SQL Generator",
                 bg=self.theme["bg"], fg=self.theme["dim"], font=("Segoe UI", 9)).pack(anchor="w")

        # Main Notebook for sub-tools
        nb = ttk.Notebook(container)
        nb.pack(fill="both", expand=True)

        # ── TAB 1: Stream Parser & Binary Inspector ──────────────────
        tab_stream = tk.Frame(nb, bg=self.theme["card"], padx=12, pady=10)
        nb.add(tab_stream, text="🧬 Giải Mã Binary (DataInputStream)")
        self._build_stream_parser_tab(tab_stream)

        # ── TAB 2: HTTH Effect Binary Decoder ────────────────────────
        tab_eff = tk.Frame(nb, bg=self.theme["card"], padx=12, pady=10)
        nb.add(tab_eff, text="✨ HTTH Effect & Skill Decoder")
        self._build_effect_decoder_tab(tab_eff)

        # ── TAB 3: Universal Byte Array & Hex Converter ──────────────
        tab_arrays = tk.Frame(nb, bg=self.theme["card"], padx=12, pady=10)
        nb.add(tab_arrays, text="🔄 Hex <-> Byte Arrays (Java/C/Python)")
        self._build_arrays_tab(tab_arrays)

        # ── TAB 4: JSON / CSV <-> SQL Generator ──────────────────────
        tab_sql = tk.Frame(nb, bg=self.theme["card"], padx=12, pady=10)
        nb.add(tab_sql, text="📊 JSON / CSV <-> SQL Generator")
        self._build_sql_tab(tab_sql)

    # ─────────────────────────────────────────────────────────────────
    # SUB-TAB 1: JAVA DATAINPUTSTREAM PARSER
    # ─────────────────────────────────────────────────────────────────
    def _build_stream_parser_tab(self, parent):
        top_bar = tk.Frame(parent, bg=self.theme["card"])
        top_bar.pack(fill="x", pady=(0, 8))

        tk.Button(top_bar, text="📁 Mở File Binary...", bg=self.theme["input"], fg=self.theme["text"], font=("Segoe UI", 9, "bold"), relief="flat", padx=10, command=self._load_binary_file).pack(side="left", padx=2)
        tk.Button(top_bar, text="⚡ Phân Tích Chuỗi Hex Đang Nhập", bg=self.theme["accent"], fg="white", font=("Segoe UI", 9, "bold"), relief="flat", padx=12, command=self._parse_hex_stream).pack(side="left", padx=6)
        tk.Button(top_bar, text="💾 Xuất File Binary Từ JSON", bg=self.theme["input"], fg=self.theme["ok"], font=("Segoe UI", 9), relief="flat", padx=10, command=self._export_binary_from_json).pack(side="left", padx=2)

        # Two panel layout
        panes = tk.Frame(parent, bg=self.theme["card"])
        panes.pack(fill="both", expand=True)
        panes.columnconfigure(0, weight=1)
        panes.columnconfigure(1, weight=1)
        panes.rowconfigure(0, weight=1)

        # Left Input (Hex / Raw)
        left_box = tk.Frame(panes, bg=self.theme["input"], bd=1, relief="solid", padx=8, pady=8)
        left_box.grid(row=0, column=0, sticky="nsew", padx=(0, 6))

        tk.Label(left_box, text="Dữ liệu Nhị Phân / Chuỗi Hex Đầu Vào:", bg=self.theme["input"], fg=self.theme["warn"], font=("Segoe UI", 9, "bold")).pack(anchor="w")
        self.txt_stream_in = tk.Text(left_box, bg="#08080c", fg="#a6e3a1", font=("Consolas", 9), bd=0, insertbackground="white")
        self.txt_stream_in.pack(fill="both", expand=True, pady=4)

        # Right Output (Decoded structure / JSON / Tree)
        right_box = tk.Frame(panes, bg=self.theme["input"], bd=1, relief="solid", padx=8, pady=8)
        right_box.grid(row=0, column=1, sticky="nsew", padx=(6, 0))

        tk.Label(right_box, text="Kết Quả Giải Mã Tuần Tự (Decoded Types):", bg=self.theme["input"], fg=self.theme["accent"], font=("Segoe UI", 9, "bold")).pack(anchor="w")
        self.txt_stream_out = tk.Text(right_box, bg="#08080c", fg="#cdd6f4", font=("Consolas", 9), bd=0, insertbackground="white")
        self.txt_stream_out.pack(fill="both", expand=True, pady=4)

    def _load_binary_file(self):
        p = filedialog.askopenfilename(title="Chọn file nhị phân", filetypes=[("All files", "*.*")])
        if p: