# 📌 CardJourney

CardJourney è un gioco RPG dove il giocatore esplora un mondo fantastico, affrontando nemici con il suo mazzo di carte. 
Durante il viaggio il giocatore può acquistare nuove carte per arricchire il suo mazzo e fare più danno ai nemici.

![](https://i.imgur.com/VqRn60N.png)
---

## 🚀 Come eseguire il progetto
### Prerequisiti
- Java 25 (LTS)
- Gradle
### Istruzioni
```bash
git clone https://github.com/formagGinoo/CardJourney.git
cd CardJourney
```
### Build del progetto
```bash
./gradlew build
```
### Esecuzione
```bash
./gradlew run
```
---

## Funzionalità presenti
- Sistema di movimenti del giocatore (WASD) oppure con le frecce direzionali
- Sistema di combattimento a turni con le carte
- Sistema di acquisto carte e ripristino della salute del giocatore
- Sistema di salvataggio e caricamento della partita
- Sistema per la riproduzione di varie traccie musicali a seconda della situazione (es. menu principale, esplorazione, negozio)
---

## 🤖 Uso di strumenti di AI
Strumenti di AI sono stati utilizzati per supportare lo sviluppo del progetto, in particolare:
* Utilizzato Copilot per:
    * autocompletamento di metodi semplici
    * generazione di codice ripetitivo (es. getter/setter)
    * utilizzata la funzione di code review dopo un analisi autonoma del codice per individuare possibili problemi con i principi SOLID e CLEAN CODE
* Utilizzato Gemini per:
    * assistenza con algoritmi
    * assistenza su come poter migliorare il codice dopo una prima implementazione umana
    * come poter utilizzare al meglio JavaFX, assistenza con i file .fxml e .css
    * generazione degli asset del progetto (sfondi, icone, file audio)
---

## ⚠️ Nota
Seppur la maggior parte degli asset del gioco siano stati generati con strumenti di AI, gli sprites
delle carte sono state prese da [siddhu.itch.io](https://siddhu.itch.io/card-set-playing-cards)


