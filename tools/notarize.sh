#!/bin/bash

set -e

CERT_NAME=""
APP_VERSION="6.1.0"

detach_tetrahedron_volumes() {
  df | grep Tetrahedron | awk '{print $1}' | while read volume; do
    hdiutil detach "$volume" -quiet
  done
}

cd ../dist

detach_tetrahedron_volumes
hdiutil attach Tetrahedron-$APP_VERSION.dmg -nobrowse -quiet
cp -rf /Volumes/Tetrahedron/Tetrahedron.app Tetrahedron.app
detach_tetrahedron_volumes

(mkdir Tetrahedron.app/Contents/app/Tetrahedron \
  && cd Tetrahedron.app/Contents/app/Tetrahedron \
  && jar xf ../Tetrahedron.jar > /dev/null)

find Tetrahedron.app/Contents/app/Tetrahedron -name "*.dylib" -exec \
  codesign -s "$CERT_NAME" --force {} \;

rm Tetrahedron.app/Contents/app/Tetrahedron.jar
jar cf Tetrahedron.app/Contents/app/Tetrahedron.jar -C Tetrahedron.app/Contents/app/Tetrahedron . > /dev/null
rm -r Tetrahedron.app/Contents/app/Tetrahedron

jpackage --type app-image --app-image Tetrahedron.app \
  --mac-sign --mac-package-signing-prefix cmps.tetrahedron. --mac-entitlements ../tools/Tetrahedron.entitlements

rm Tetrahedron-$APP_VERSION.dmg
jpackage --type dmg --app-image Tetrahedron.app --app-version $APP_VERSION --icon ../src/main/resources/logo.icns
rm -rf Tetrahedron.app

xcrun notarytool submit Tetrahedron-$APP_VERSION.dmg --keychain-profile "notarytool-password" --wait
xcrun stapler staple Tetrahedron-$APP_VERSION.dmg