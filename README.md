# Sentier

Application de randonnée : calcul d'itinéraire entre un départ et une arrivée (BRouter), profil altimétrique, export GPX pour montre Garmin.

- Interface web : `www/index.html` (Leaflet embarqué dans `www/leaflet`).
- Version HTML autonome : `html/sentier.html`, un seul fichier à ouvrir dans un navigateur (Leaflet chargé en ligne). Régénéré avec `tools/sync-html.sh` à chaque modification de `www/index.html` (le build signale un écart).
- Application Android : Capacitor, dossier `android/`.
- APK et version HTML : construits par GitHub Actions à chaque push sur `main`, publiés dans la release `apk-latest` (`sentier.apk` et `sentier.html`). La version web est aussi en ligne : https://arnola79.github.io/sentier/

Pour modifier l'app, change `www/index.html`, puis pousse sur `main`. Le nouvel APK est prêt quelques minutes plus tard.

## Version iPhone (Cloudflare Pages)

Le dossier `html/` est publié par Cloudflare Pages : aucune commande de build, répertoire de sortie `html`. L'adresse racine redirige vers `/sentier` (fichier `html/_redirects`).

## Mise à jour de l'APK

Au démarrage, l'app lit `apk.json` sur GitHub Pages (numéro de build et lien de l'APK). Si le numéro est plus récent que celui installé, elle propose « Installer » : téléchargement de l'APK puis installateur Android.
