import tkinter as tk
from tkinter import ttk
from icon_resizer_panel import IconResizerPanel
from image_editor_panel import ImageEditorPanel
from data_converter_panel import DataConverterPanel
from charset_crypto_panel import CharsetCryptoPanel

class MasterToolPanel(tk.Frame):
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
        # Header title
        hdr = tk.Frame(self, bg=self.theme["side"], padx=20, pady=12)
        hdr.pack(fill="x")

        tk.Label(hdr, text="🛠️ HTTH MASTER TOOL STUDIO", bg=self.theme["side"], fg=self.theme["accent"], font=("Segoe UI", 16, "bold")).pack(side="left")
        tk.Label(hdr, text="  |  Bộ Công Cụ Đồ Họa, Xử Lý Ảnh, Xóa Nền, Resizer & Giải Mã Dữ Liệu Toàn Diện", bg=self.theme["side"], fg=self.theme["dim"], font=("Segoe UI", 10)).pack(side="left", pady=(4, 0))

        # Main Notebook
        self.notebook = ttk.Notebook(self)
        self.notebook.pack(fill="both", expand=True, padx=10, pady=10)

        # Tab 1: Image & Icon Studio (Editor, Smart BG Remover, Canvas, Sprite Sheet)
        self.tab_editor = ImageEditorPanel(self.notebook, self.theme)
        self.notebook.add(self.tab_editor, text=" 🖌️ Studio Đồ Họa & Vẽ Icon ")

        # Tab 2: Icon Resizer (x1..x4)
        self.tab_resizer = IconResizerPanel(self.notebook, self.theme)
        self.notebook.add(self.tab_resizer, text=" ⚡ Icon Resizer (x1..x4) ")

        # Tab 3: Data & Binary Converter
        self.tab_data = DataConverterPanel(self.notebook, self.theme)
        self.notebook.add(self.tab_data, text=" 🧬 Binary & SQL/JSON Data ")

        # Tab 4: Charset & Crypto Lab
        self.tab_charset = CharsetCryptoPanel(self.notebook, self.theme)
        self.notebook.add(self.tab_charset, text=" 🔠 Bảng Mã & Crypto ")
