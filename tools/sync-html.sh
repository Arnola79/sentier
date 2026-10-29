#!/bin/sh
# Régénère html/sentier.html (version autonome, Leaflet chargé en ligne) à partir de www/index.html
set -e
cd "$(dirname "$0")/.."
mkdir -p html
sed -e 's#href="leaflet/leaflet.css"#href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.css"#' \
    -e 's#src="leaflet/leaflet.js"#src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/leaflet.min.js"#' \
    www/index.html > html/sentier.html
