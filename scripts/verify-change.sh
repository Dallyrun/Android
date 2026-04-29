#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

usage() {
    cat <<'USAGE'
Usage:
  ./scripts/verify-change.sh docs
  ./scripts/verify-change.sh feature <name>
  ./scripts/verify-change.sh core
  ./scripts/verify-change.sh ui
  ./scripts/verify-change.sh build-logic
  ./scripts/verify-change.sh data
  ./scripts/verify-change.sh all

Examples:
  ./scripts/verify-change.sh feature run
  ./scripts/verify-change.sh docs
USAGE
}

require_file() {
    local path="$1"
    if [[ ! -e "$path" ]]; then
        echo "Missing required path: $path" >&2
        exit 1
    fi
}

require_feature_name() {
    if [[ $# -lt 1 || -z "${1:-}" ]]; then
        echo "Feature gate requires a feature name." >&2
        usage
        exit 1
    fi
}

run_gradle() {
    ./gradlew "$@" --no-daemon
}

gate="${1:-}"
shift || true

case "$gate" in
    docs)
        require_file "AGENTS.md"
        require_file "CLAUDE.md"
        require_file ".agent-harness/README.md"
        require_file ".agent-harness/validation-gates.md"
        require_file ".agent-harness/feedback-loops.md"
        require_file ".github/pull_request_template.md"
        echo "Docs gate passed."
        ;;
    feature)
        require_feature_name "$@"
        feature_name="$1"
        require_file "feature/${feature_name}/build.gradle.kts"
        run_gradle ":feature:${feature_name}:test" ":app:assembleDebug"
        ;;
    core)
        run_gradle test ":app:assembleDebug"
        ;;
    ui)
        run_gradle test ":app:assembleDebug"
        ;;
    build-logic)
        run_gradle build lint --continue
        ;;
    data)
        run_gradle test ":app:assembleDebug"
        ;;
    all)
        run_gradle build
        ;;
    ""|-h|--help|help)
        usage
        ;;
    *)
        echo "Unknown validation gate: $gate" >&2
        usage
        exit 1
        ;;
esac
