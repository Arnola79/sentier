# Sentier

Application de randonnée : calcul d'itinéraire entre un départ et une arrivée (BRouter), profil altimétrique, export GPX pour montre Garmin.

- Interface web : `www/index.html` (Leaflet embarqué dans `www/leaflet`).
- Application Android : Capacitor, dossier `android/`.
- APK et version HTML : construits par GitHub Actions à chaque push sur `main`, publiés dans la release `apk-latest` (`sentier.apk` et `sentier.html`).

Pour modifier l'app, change `www/index.html`, puis pousse sur `main`. Le nouvel APK est prêt quelques minutes plus tard.
