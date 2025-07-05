# Tetrahedron (desktop)

The English version and developer instructions are below.

## Про проєкт
Tetrahedron - програма для візуалізації даних на 3D моделях.
Розроблена для візуалізації результатів розрахунків проведених методом скінченних елементів.
В програмі підтримується один скінченний елемент - тетраедр, від нього і походить назва програми.

### Дизайн
[Проєкт у Figma](https://www.figma.com/design/qRI4mkLqebFSf43ICyF4mn/TETRAHEDRON?node-id=0-1&p=f&t=CPYtHWevupWzcIn4-0)

### Розробники проєкту

#### Дизайнер
- [Саченко Тетяна (ІКМ-221А)](https://github.com/SachenkoTanya)

#### Розробники програмного забезпечення
- [Вадим Старчак (ІКМ-221А)](https://github.com/VadimST04)
- [Гурген Авагян (ІКМ-221А)](https://github.com/GurgenAvagyan)

#### Ментори
- [Татарінова Оксана. Завідувачка кафедрою Комп’ютерного Моделювання Процесів та Систем, НТУ "ХПІ"](https://web.kpi.kharkov.ua/cmps/uk/tatarinova-oksana-andriyivna/)
- [Бородін Марія. Аспірант кафедри Комп’ютерного Моделювання Процесів та Систем, НТУ "ХПІ"](https://web.kpi.kharkov.ua/cmps/uk/golovna/vikladatskij-sklad/borodin-mariya-anatoliyivna/)

## About
Tetrahedron — a program for visual data analysis.
It allows you to load files to build models, apply and analyze any data on it (for example, stress or displacement)

### Design
[Figma project](https://www.figma.com/design/qRI4mkLqebFSf43ICyF4mn/TETRAHEDRON?node-id=0-1&p=f&t=CPYtHWevupWzcIn4-0)

### Project developers

#### Designer
- [Tetiana Sachenko](https://github.com/SachenkoTanya)

#### Software Developers
- [Vadym Starchak](https://github.com/VadimST04)
- [Hurhen Avahian](https://github.com/GurgenAvagyan)

#### Mentors
- [Tatarinova Oksana. Head of the Department of Computer Modeling of Processes and Systems, NTU "KhPI"](https://web.kpi.kharkov.ua/cmps/en/main/magistral-staff/tatarinova-oksana/)
- [Mariia Borodin. Ph.D. student at the Department of Computer Modeling of Processes and Systems, NTU "KhPI"](https://web.kpi.kharkov.ua/cmps/en/magistral-staff/borodin-mariia/)

## Running the project locally

### Prerequisites
- Java JDK 21 (e.g. [Temurin](https://adoptium.net/temurin/))
- Maven (can be found [here](https://maven.apache.org/download.cgi))

**NOTE:** If you are using IntelliJ IDEA, Maven is built-in and Java could be installed from the IDE.

### Building and running with Maven
- Run the following command to build the project:
```shell
  mvn package
```
- Run a built jar-file
```shell
  java -jar target/tetrahedron-desktop-4.2.0-SNAPSHOT-jar-with-dependencies.jar
```

### Running in IntelliJ IDEA
- Open a cloned project
- Launch main() in `src/main/java/org/cmps/tetrahedron/Launcher.java`

## Building an OS bundle/Installer

### Prerequisites
Only for Windows: 
- Install [WiX 3](https://wixtoolset.org/docs/wix3/). 
- Add it to the PATH. Path example: "C:\Program Files (x86)\WiX Toolset v3.14\bin"

### How to build
Run a script
- Windows: [installer-win.bat](tools/installer-win.bat)
- macOS: [installer-mac.sh](tools/installer-mac.sh)

### Signing/Notarizing
- You can sign and notarize a macOS bundle if you have an Apple Developer account:
  - You should have a Developer ID certificate in your Keychain and notarytool authenticated with App Store Connect
  - In [notarize.sh](tools/notarize.sh) set `CERT_NAME` to the name of your Developer ID certificate
  - Run: [notarize.sh](tools/notarize.sh)
- You can sign a Windows Installer if you have Azure Trusted Signing Certificate Profile:
  - You should have Azure CLI installed and authenticated with your account
  - In [signing.json](tools/signing.json) set `CodeSigningAccountName` and `CertificateProfileName`
  - Run: [sign.bat](tools/sign.bat)
