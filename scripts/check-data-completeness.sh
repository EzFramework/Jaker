#!/usr/bin/env bash
set -euo pipefail

# Check that each locale directory under jaker-data-*/src/main/resources/data
# contains a minimal set of dataset files (non-empty).

EXPECTED=(
  names.txt
  cities.txt
  phones.txt
  streets.txt
  company_names.txt
  domains.txt
  creditcards.txt
)

missing=0

echo "Checking data completeness for jaker-data modules..."

for module in jaker-data-*; do
  base="$module/src/main/resources/data"
  if [ -d "$base" ]; then
    for locale_dir in "$base"/*; do
      if [ -d "$locale_dir" ]; then
        echo "Checking $locale_dir"
        for f in "${EXPECTED[@]}"; do
          if [ ! -s "$locale_dir/$f" ]; then
            echo "  MISSING or empty: $locale_dir/$f"
            missing=1
          fi
        done
      fi
    done
  fi
done

if [ "$missing" -ne 0 ]; then
  echo
  echo "Data completeness check failed: missing files found."
  exit 1
fi

echo "All checked locales have the required files." 
