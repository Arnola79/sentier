# Sentier

Application de randonnée : calcul d'itinéraire entre un départ et une arrivée (BRouter), profil altimétrique, export GPX pour montre Garmin.

- Interface web : `www/index.html` (Leaflet embarqué dans `www/leaflet`).
- Version HTML autonome : `html/sentier.html`, un seul fichier à ouvrir dans un navigateur (Leaflet chargé en ligne). Mis à jour automatiquement à chaque build.
- Application Android : Capacitor, dossier `android/`.
- APK et version HTML : construits par GitHub Actions à chaque push sur `main`, publiés dans la release `apk-latest` (`sentier.apk` et `sentier.html`).

Pour modifier l'app, change `www/index.html`, puis pousse sur `main`. Le nouvel APK est prêt quelques minutes plus tard.

## Version iPhone (Cloudflare Pages)

Le dossier `html/` est publié par Cloudflare Pages : aucune commande de build, répertoire de sortie `html`. L'adresse racine redirige vers `/sentier` (fichier `html/_redirects`).
