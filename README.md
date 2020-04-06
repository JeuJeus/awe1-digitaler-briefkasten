# Vorgaben - "Meine Idee Initiative"
Versicherungsunternehmen will Innerbetriebliches Vorschlagswesen modernisieren.
Weg vom Briefkasten zur Webanwendung.

Build Status:
![Git CI - mvnw](https://github.com/JeuJeus/awe1-digitaler-briefkasten/workflows/Git%20CI%20-%20mvnw/badge.svg)
 
 ---
 
 ## Muss
 - registrierte und nicht registrierte Nutzer
 - Möglichkeit der Registrierung
 - Registrierter Mitarbeiter kann sich anmelden
 
 
 - angemeldeter Nutzer kann neue Idee erfassen
 - angemeldeter Nutzer kann eigene Ideen auflisten
 - angemeldeter Nutzer kann Ideen bearbeiten bis eingereicht wurde
 
 
 - (nicht-) registrierte Nutzer können Ideen auflisten
 - (nicht-) registrierte Nutzer können Ideen filtern
 
 
 - eingereichte Ideen werden (automatisch) dem passenden Spezialisten zugeordnet
 - Spezialist kann Ideen ansehen
 - Spezialist kann Ideen ablehnen
 - Spezialist kann Ideen annehmen
 - Spezialist kann Ideen in Ideenspeicher überführen
 - Spezialist kann Ideen von Ideenspeicher zurückführen
 - Bewertung von Spezialisten soll für alle Dokumentiert und sichtbar sein
 - Spezialist soll ToDo haben 
 
 ## Kann
 
 - Zu einer Idee <=3 Dokumente hochladen
 - Profilfoto möglich für registrierte Nutzer
 - Pdf als Übersicht eingerichter Ideen erstellen -> (Latex???)
 - verwaltender Admin
 - Mailbenachrichtigung neuer Ideen für Spezialisten
 - Alle Nutzer (auch unregistriert) können Kontaktformular nutzen
 
- Rest API Anbindung veröffentlichter Ideen
- Rest API Anbindung Details einer Idee
- Rest API Anbindung filtern der Detailsliste

---

## Zusatzinfos Unternehmen
- Sparten: KFZ, Unfall, Krankenversicherung, Rechtsschutz, Lebensversicherungt, Rentenversicherung, Haftpflicht, Hausrat, Wohngebäude
- Vertriebskanäle: Stationär in Geschäften, Versicherungsmakler, Koop. mit Kreditinstituten, Direktversicherung
- Zielgruppen: Kinder/Jugendliche, Familien, Singles, Paare, Personen 50+, Gewerbebetreibende


- **Handlungsfelder: Kostensparen, Ertragssteigerun, Zukunftsfähigkeit**
- **Größe: 3000 Mitarbeiter -> 80% Nutzer des Systems = 2400**


- Kategorien von Ideen: 
    
    Idee von der beide Erben mit gemeinsamen Kategorien?
    
    - Produktideen: 
        - Text-Beschreibung
        - Erfasser
        - Erfassungsdatum
        - Produktsparte (1!)
        - Vertriebsweg (optional >=1)
        - Zielgruppe (>=1)
        - Existiert vergleichbares bei Mitbewerber (Bool?)
        - Vorteile (0-3)
     - Interne Ideen:
        - Text-Beschreibung
        - Erfasser
        - Erfassungsdatum
        - Handlungsfeld (!1)
        - Vorteile (0-3)
        
- Infrastruktur:
    - eigenes Rechenzentrum
    - eigene IT
    - **Wir als Dienstleister zur ausgelagerten Entwicklung**
    - **Weiterentwicklung/Support intern**

- **Technologiestack:**
    - Java >=8
    - relationale DB, noSQL
    
- Qualitätssicherung: 
    - Vollständigkeit
    - Korrektheit
    - Stabilität
    - Benutzbarkeit
    - Sicherheit 

