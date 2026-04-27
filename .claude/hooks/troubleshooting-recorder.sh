#!/usr/bin/env bash
# Stop hook: spawn a Claude sub-agent to scan the just-finished session and
# append entries to .claude/TROUBLESHOOTING.md if any troubleshooting / error fix
# / performance improvement happened.
#
# Fire-and-forget — does not block the user. Sub-agent writes asynchronously.
# Lockfile prevents recursion when the sub-agent itself triggers a Stop event.

set -euo pipefail

input=$(cat)
cwd=$(printf '%s' "$input" | jq -r '.cwd // empty' 2>/dev/null || echo "")
transcript=$(printf '%s' "$input" | jq -r '.transcript_path // empty' 2>/dev/null || echo "")

[[ -z "$cwd" || -z "$transcript" || ! -f "$transcript" ]] && exit 0
[[ -f "$cwd/.claude/TROUBLESHOOTING.md" ]] || exit 0

lock="$cwd/.claude/logs/.troubleshooting-recorder.lock"
[[ -f "$lock" ]] && exit 0

mkdir -p "$cwd/.claude/logs"
touch "$lock"

prompt=$(cat <<'EOP'
You are a Stop-hook sub-agent for the Dallyrun Android project. Your only job is
to decide whether the just-finished session deserves a TROUBLESHOOTING.md entry,
and if so, prepend it.

Read the session transcript at the path in the env var $TRANSCRIPT_PATH (JSONL,
one message per line). Then read .claude/TROUBLESHOOTING.md to learn the format
and to avoid duplicating an entry that already exists.

Record an entry ONLY for clearly qualifying events:
- Troubleshooting: a build/runtime error or non-obvious bug was diagnosed and resolved
- Error fix: a meaningful defect was repaired (NOT typos / cleanups / refactors)
- Performance: a measurable performance improvement was applied (include before/after numbers)

For each qualifying event, prepend ONE entry directly under the line "## Entries"
in .claude/TROUBLESHOOTING.md. Use today's UTC date (YYYY-MM-DD). Use the format:

## YYYY-MM-DD · <Troubleshooting | Error fix | Performance> · <한줄 제목>

**Symptom:** the error/observation (key stack trace or message)
**Root cause:** what was actually wrong
**Fix:** how it was resolved
**Reference:** PR/commit/file links if known

Body language: Korean, concise, factual. Reference any PR/commit numbers found
in the transcript. If nothing qualifies, exit silently with no file changes.

Do NOT log: routine code edits, refactors, doc updates, dependency cleanups,
unused-import removal, work that didn't actually fix anything. When in doubt,
do NOT record — false negatives are cheap, false positives pollute the log.
EOP
)

# Fire-and-forget background sub-agent. Always release the lock on exit.
(
  trap 'rm -f "$lock"' EXIT
  cd "$cwd"
  TRANSCRIPT_PATH="$transcript" claude \
    -p \
    --permission-mode acceptEdits \
    --output-format text \
    "$prompt" \
    >> .claude/logs/troubleshooting-recorder.log 2>&1
) </dev/null >/dev/null 2>&1 &
disown

exit 0
