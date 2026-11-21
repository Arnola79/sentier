#!/bin/sh
# Régénère la version HTML autonome (bibliothèques chargées en ligne) à partir de www/index.html.
# Usage : tools/sync-html.sh [entrée] [sortie]   (par défaut www/index.html -> html/sentier.html)
set -e
cd "$(dirname "$0")/.."
IN="${1:-www/index.html}"
OUT="${2:-html/sentier.html}"
mkdir -p "$(dirname "$OUT")"
sed -e 's#href="leaflet/leaflet.css"#href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.css"#' \
    -e 's#src="leaflet/leaflet.js"#src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.js"#' \
    -e 's#const MAPLIBRE_JS = "maplibre/maplibre-gl.js";#const MAPLIBRE_JS = "https://cdn.jsdelivr.net/npm/maplibre-gl@5.24.0/dist/maplibre-gl.js";#' \
    -e 's#const MAPLIBRE_CSS = "maplibre/maplibre-gl.css";#const MAPLIBRE_CSS = "https://cdn.jsdelivr.net/npm/maplibre-gl@5.24.0/dist/maplibre-gl.css";#' \
    "$IN" > "$OUT"
if grep -q '"leaflet/\|"maplibre/' "$OUT"; then echo "Bibliothèque locale encore référencée dans $OUT" >&2; exit 1; fi
