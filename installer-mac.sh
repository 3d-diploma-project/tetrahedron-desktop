mkdir tmp tmp/app
cp target/tetrahedron-desktop-1.0.0-SNAPSHOT-jar-with-dependencies.jar tmp/app/Tetrahedron.jar

jlink --strip-debug --no-man-pages --no-header-files --compress zip-6 \
  --add-modules java.base,java.scripting,java.desktop,jdk.unsupported,jdk.unsupported.desktop --output tmp/jre

jpackage --input tmp/app --main-jar Tetrahedron.jar --main-class org.cmps.tetrahedron.Tetrahedron --runtime-image tmp/jre \
  --name Tetrahedron --vendor "CMPS, KhPI" --app-version 1.0.0 --icon src/main/resources/logo.icns \
  --type app-image --dest dist

rm -rf tmp