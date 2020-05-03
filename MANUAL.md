# Anleitung

## Kompilieren und Starten des Programmes
Das Programm kann mit Hilfe des Maven Wrappers kompiliert werden:
- unter Linux/ Mac OS Systemen mit:
    ```chmod +x mvnw & ./mvnw clean compile package```
- unter Windows mit: 
    ```.\mvnw.cmd clean compile package  ```

Danach kann die Zieldatei welche im Unterordner **target** liegt ausgeführt werden.
- ```java -jar digitaler-briefkasten-1.0.0-FINAL.jar``` (Die Versionsnummer kann abweichen!)

Nach dem erfolgreichen Start ist die Oberfläche unter :
````http://localhost:8080```` erreichbar.

## Test Zugangsdaten
Zur Nutzung des Systems ist es einfach möglich einen neuen Account anzulegen.
Ein Administrator-Account kann falls nicht bereits vorhanden mithilfe der *HelperScriptsNoTests*-Klasse angelegt werden.
### Admin-Credentials 
- **Username:** admin
- **Password:** hierKönnteIhreWerbungStehen