# -*- coding: utf-8 -*-
"""
run_preview_server.py
=====================
High-performance Multi-Threaded HTTP web server to explore the 16 Skill Thần Trang Showcase.
Supports parallel image requests, Keep-Alive, CORS headers, and automatic browser launch.
Default port: 8088
"""

import http.server
import os
import sys
import webbrowser
import socket

PORT = 8088
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

class FastHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=BASE_DIR, **kwargs)

    def end_headers(self):
        # Allow cross-origin and ensure smooth asset streaming
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, OPTIONS")
        self.send_header("Cache-Control", "no-cache, must-revalidate")
        super().end_headers()

    def log_message(self, format, *args):
        # Silent or concise logging to keep terminal clean
        pass

def find_available_port(start_port=8088):
    for p in range(start_port, start_port + 10):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            if s.connect_ex(('127.0.0.1', p)) != 0:
                return p
    return start_port

def main():
    os.chdir(BASE_DIR)
    port = find_available_port(PORT)
    
    if hasattr(sys.stdout, 'reconfigure'):
        try:
            sys.stdout.reconfigure(encoding='utf-8')
        except Exception:
            pass

    print("=" * 72)
    print("   HAITACTIHON - 16 SKILL THAN TRANG MULTI-THREADED STUDIO SERVER")
    print("=" * 72)
    print(f"Root Directory: {BASE_DIR}")
    print(f"Local Server:   http://localhost:{port}")
    print(f"Master Studio:  http://localhost:{port}/demos/index.html")
    print(f"Icon Gallery:   http://localhost:{port}/demos/preview_icons_showcase.html")
    print("-" * 72)
    print("Server is RUNNING (Multi-threaded). Press Ctrl+C to stop.")

    # Automatically launch browser
    try:
        webbrowser.open(f"http://localhost:{port}/demos/index.html")
    except Exception:
        pass

    server = http.server.ThreadingHTTPServer(("0.0.0.0", port), FastHandler)
    server.daemon_threads = True
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped.")

if __name__ == '__main__':
    main()
