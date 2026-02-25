#!/usr/bin/env python3
import json
import platform
import sys
from datetime import datetime, timezone


def main() -> int:
    payload = {
        "message": "Hello from Python",
        "utc_time": datetime.now(timezone.utc).isoformat(),
        "python": platform.python_version(),
        "args": sys.argv[1:],
    }
    print(json.dumps(payload, ensure_ascii=False))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
