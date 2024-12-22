@echo off

call mkdir tmp tmp\app
call copy target\tetrahedron-desktop-1.0-SNAPSHOT-jar-with-dependencies.jar tmp\app\Tetrahedron.jar

call jlink --strip-debug --no-man-pages --no-header-files --compress zip-6 ^
    --add-modules java.base,java.scripting,java.desktop,jdk.unsupported,jdk.unsupported.desktop --output tmp\jre

call jpackage --input tmp\app --main-jar Tetrahedron.jar --main-class org.cmps.tetrahedron.Tetrahedron --runtime-image tmp\jre ^
    --name Tetrahedron --vendor "CMPS, KhPI" --icon src/main/resources/logo.ico ^
    --type msi --win-dir-chooser --win-menu --win-shortcut --win-shortcut-prompt --dest dist

call rmdir /S /Q tmp