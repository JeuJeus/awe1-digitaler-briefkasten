# Anleitung

## Kompilieren und Starten des Programmes
Das Programm kann mit Hilfe des Maven Wrappers kompiliert werden:
- unter Linux/ Mac OS Systemen mit:
    ```chmod +x mvnw & ./mvnw clean compile package```
- unter Windows mit: 
    ```.\mvnw.cmd clean compile package  ```

Danach kann die Zieldatei aus dem Projekt Root ausgeführt werden.
- ```java -jar target/digitaler-briefkasten-1.0.0-FINAL.jar``` (Die Versionsnummer kann abweichen!)

Alternativ lässt sich das Programm auch direkt starten:
- ```./mvnw spring-boot:run ``` (respektive die Windows Notation)

Nach dem erfolgreichen Start ist die Oberfläche unter :
````http://localhost:8080```` erreichbar.

## Test Zugangsdaten
Grundsätzlich exisiteren drei verschiedene Arten von Accounts:
- Administrator
- Spezialist
- User

Zur Nutzung des Systems ist es einfach möglich einen neuen *User-Account* anzulegen.

Ein *Administrator-Account* kann falls nicht bereits vorhanden mithilfe der *HelperScriptsNoTests*-Klasse angelegt werden.

Ein *Spezialist-Account* kann falls nicht bereits vorhanden entweder mithilfe der der *HelperScriptsNoTests*-Klasse oder durch den Administrator in dessen *Aminpanel* angelegt werden. 
 
### Admin-Credentials 
- **Username:** admin
- **Passwort:** hierKönnteIhreWerbungStehen

### Specialist-Credentials 
- **Username:** SpeziusMaximus_$JEWEILIGERNAME
- **Passwort:** boringProphet
