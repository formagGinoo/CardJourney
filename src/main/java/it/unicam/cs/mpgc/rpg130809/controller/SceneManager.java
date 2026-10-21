package it.unicam.cs.mpgc.rpg130809.controller;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Gestore centralizzato delle scene dell'applicazione.
 * <p>
 * Implementa il design pattern <b>Singleton</b> e fornisce metodi per il caricamento
 * e la transizione tra le diverse scene.
 * </p>
 */
public class SceneManager {

    private static final String STYLESHEET_PATH = "/css/style.css";

    private static SceneManager instance;
    private Stage mainStage;

    /**
     * Costruttore privato conforme al pattern Singleton.
     */
    private SceneManager() {
    }

    /**
     * Restituisce l'unica istanza attiva di {@link SceneManager}.
     *
     * @return L'istanza di SceneManager
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Inizializza lo SceneManager con lo {@link Stage} dell'applicazione.
     * Imposta il titolo e disabilita il ridimensionamento della finestra.
     *
     * @param stage Lo stage primario dell'applicazione
     */
    public void initialize(Stage stage) {
        this.mainStage = stage;
        mainStage.setTitle(GameConstants.GAME_TITLE);
        mainStage.setResizable(false);
    }

    /**
     * Effettua la transizione alla scena specificata caricando il relativo file FXML.
     *
     * @param fxmlFile Il percorso relativo della risorsa FXML da caricare
     * @throws RuntimeException Se il caricamento del file FXML fallisce
     */
    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = createLoader(fxmlFile);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getStylesheetUrl());
            mainStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento della scena: " + fxmlFile, e);
        }
    }

    /**
     * Restituisce l'URL assoluto del foglio di stile condiviso.
     * <p>
     * Il foglio viene applicato a ogni scena da qui, invece di essere referenziato
     * con un percorso relativo nei file FXML. In questo modo il caricamento è
     * indipendente da dove si trovano i file FXML e funziona anche quando
     * l'applicazione è impacchettata in un JAR.
     * </p>
     *
     * @return l'URL del foglio di stile
     */
    private String getStylesheetUrl() {
        URL stylesheet = SceneManager.class.getResource(STYLESHEET_PATH);
        if (stylesheet == null) {
            throw new IllegalStateException("Stylesheet not found at: " + STYLESHEET_PATH);
        }
        return stylesheet.toExternalForm();
    }

    /**
     * Crea un {@link FXMLLoader} per la risorsa FXML richiesta.
     *
     * @param fxmlPath Il percorso del file FXML
     * @return Un'istanza configurata di FXMLLoader
     */
    private FXMLLoader createLoader(String fxmlPath) {
        return new FXMLLoader(SceneManager.class.getResource(fxmlPath));
    }
}
