#!/usr/bin/env python3
"""
Safe utility script for /pyrun:
- No network access
- No filesystem access
- Only hashes the provided text arguments
"""

import argparse
import hashlib
import json
import sys


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Hash text using a safe built-in algorithm.")
    parser.add_argument("text", nargs="*", help="Text to hash (tokens will be joined with spaces)")
    parser.add_argument("--algo", choices=["sha256", "sha512"], default="sha256", help="Hash algorithm")
    return parser


def main() -> int:
    parser = build_parser()
    args = parser.parse_args()

    payload = " ".join(args.text).strip()
    if not payload:
        print(json.dumps({
            "ok": False,
            "error": "No text provided",
            "usage": "pyrun hash_text.py --algo sha256 hello world"
        }))
        return 1

    hasher = hashlib.new(args.algo)
    hasher.update(payload.encode("utf-8"))

    result = {
        "ok": True,
        "algorithm": args.algo,
        "input_length": len(payload),
        "digest": hasher.hexdigest(),
    }
    print(json.dumps(result, ensure_ascii=False))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
