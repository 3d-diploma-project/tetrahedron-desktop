# Tetrahedron (desktop)

### To run the project locally

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

### To build executable file
#### Pre requirements
Only fo Windows: 
- Install [WiX 3](https://wixtoolset.org/docs/wix3/). 
- Add it to the PATH. Path example: "C:\Program Files (x86)\WiX Toolset v3.14\bin"

#### How to build
- Build a project using maven
 ```shell
    mvn clean package
 ```
- Run a script
  - Windows: [installer-win.bat](tools/installer-win.bat)
  - MacOS: [installer-mac.sh](tools/installer-mac.sh)

#### Signing/Notarizing
- You can sign and notarize macOS bundle if you have an Apple Developer account:
  - Run script: [notarize.sh](tools/notarize.sh)
- You can sign Windows installer if you have Azure Trusted Signing Certificate Profile:
  - - Run script: [sign.bat](tools/sign.bat)
