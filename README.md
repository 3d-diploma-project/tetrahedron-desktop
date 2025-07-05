# Tetrahedron (desktop)

The English version and developer instructions are below.

# Про проєкт
Tetrahedron - програма для візуалізації даних на 3D моделях.
Розроблена для візуалізації результатів розрахунків проведених методом скінченних елементів.
В програмі підтримується один скінченний елемент - тетраедр, від нього і походить назва програми.

## Дизайн
[Проєкт у Figma](https://www.figma.com/design/qRI4mkLqebFSf43ICyF4mn/TETRAHEDRON?node-id=0-1&p=f&t=CPYtHWevupWzcIn4-0)

## Розробники проєкту

#### Дизайнер
- [Саченко Тетяна (ІКМ-221А)](https://github.com/SachenkoTanya)

#### Розробники програмного забезпечення
- [Вадим Старчак (ІКМ-221А)](https://github.com/VadimST04)
- [Гурген Авагян (ІКМ-221А)](https://github.com/GurgenAvagyan)

#### Ментори
- [Татарінова Оксана. Завідувачка кафедрою Комп’ютерного Моделювання Процесів та Систем, НТУ "ХПІ"](https://web.kpi.kharkov.ua/cmps/uk/tatarinova-oksana-andriyivna/)
- [Бородін Марія. Аспірант кафедри Комп’ютерного Моделювання Процесів та Систем, НТУ "ХПІ"](https://web.kpi.kharkov.ua/cmps/uk/golovna/vikladatskij-sklad/borodin-mariya-anatoliyivna/)

# About
Tetrahedron - a program for visual data analysis.
It allows you to load files to build models, apply and analyse any data on it (for example stress or displacement)

## Design
[Figma project](https://www.figma.com/design/qRI4mkLqebFSf43ICyF4mn/TETRAHEDRON?node-id=0-1&p=f&t=CPYtHWevupWzcIn4-0)

## Project developers

#### Designer
- [Tetiana Sachenko](https://github.com/SachenkoTanya)

#### Software Developers
- [Vadym Starchak](https://github.com/VadimST04)
- [Hurhen Avahian](https://github.com/GurgenAvagyan)

#### Mentors
- [Tatarinova Oksana. Head of the Department of Computer Modeling of Processes and Systems, NTU "KhPI"](https://web.kpi.kharkov.ua/cmps/en/main/magistral-staff/tatarinova-oksana/)
- [Mariia Borodin. Ph.D. student at the Department of Computer Modeling of Processes and Systems, NTU "KhPI"](https://web.kpi.kharkov.ua/cmps/en/magistral-staff/borodin-mariia/)

# How to run the project locally
- Download the Java JDK 21 and install: [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
- Maven is required for dependency management and project build
  - **NOTE:** If you are using IntelliJ IDEA, Maven is built-in, so you don’t need to install it separately.
  - You can download Maven here: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
  - Run the following command to build the project:
    ```shell
    mvn clean install
    ```
- Download the plugin `Lombok` in Intelij Idea for the development environment to recognize the syntax (it does not affect the operation of the program)

    ![image](https://github.com/user-attachments/assets/fce03f62-2e13-436e-8c02-59319ea6c558)
- Launch the program in file `src/main/java/org/cmps/tetrahedron/Tetrahedron.java`

# How to build executable file
## Pre requirements
Only fo Windows: 
- Install [WiX 3](https://wixtoolset.org/docs/wix3/). 
- Add it to the PATH. Path example: "C:\Program Files (x86)\WiX Toolset v3.14\bin"

## How to build
- Build a project using maven
 ```shell
    mvn clean package
 ```
- Run a script
  - Windows: [installer-win.bat](tools/installer-win.bat)
  - MacOS: [installer-mac.sh](tools/installer-mac.sh)

## Signing/Notarizing
- You can sign and notarize macOS bundle if you have an Apple Developer account:
  - Run script: [notarize.sh](tools/notarize.sh)
- You can sign Windows installer if you have Azure Trusted Signing Certificate Profile:
  - - Run script: [sign.bat](tools/sign.bat)
