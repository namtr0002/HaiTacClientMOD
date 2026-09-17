import os
import re
import math
import threading
import tkinter as tk
from tkinter import ttk, filedialog, messagebox
from PIL import Image, ImageTk

def java_round(val):
    return int(math.floor(val + 0.5))

def parse_id_range(range_str):
    if not range_str or not range_str.strip():
        return None
    ids = set()
    parts = range_str.replace(";", ",").replace(" ", "").split(",")
    for part in parts:
        if not part:
            continue
        if "-" in part:
            sub = part.split("-")
            if len(sub) == 2 and sub[0].isdigit() and sub[1].isdigit():
                start, end = int(sub[0]), int(sub[1])
                if start > end:
                    start, end = end, start
                for i in range(start, end + 1):
                    ids.add(i)
        elif part.isdigit():
            ids.add(int(part))
    return ids

def extract_id_from_filename(filename):
    m = re.search(r'\d+', filename)
    if m:
        return int(m.group())
    return None

class IconResizerPanel(tk.Frame):
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
        self.preview_image_ref = None
        self._build_ui()

    def _build_ui(self):
        # Container
        container = tk.Frame(self, bg=self.theme["bg"], padx=20, pady=15)
        container.pack(fill="both", expand=True)