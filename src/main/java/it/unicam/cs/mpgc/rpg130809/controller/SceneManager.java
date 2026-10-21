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
 * Implementa il design pattern <b>Singleton</b> e fornisce i metodi per caricare
 * i file FXML e per passare da una schermata all'altra. È utilizzato dai
 * controller del package
 * {@code it.unicam.cs.mpgc.rpg130809.controller.ui}, che delegano qui ogni
 * transizione invece di gestire direttamente lo {@link Stage}.
 * </p>
 * <p>
 * Il foglio di stile condiviso viene applicato a ogni scena creata, così i file
 * FXML non devono referenziarlo con percorsi relativi.
 * </p>
 *
 * @see it.unicam.cs.mpgc.rpg130809.CardJourneyApplication
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
     * <p>
     * L'istanza viene creata alla prima invocazione e riutilizzata per tutta la
     * durata dell'applicazione.
     * </p>
     *
     * @return l'istanza condivisa di {@link SceneManager}
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Inizializza lo SceneManager con lo {@link Stage} dell'applicazione.
     * <p>
     * Imposta il titolo della finestra e disabilita il ridimensionamento.
     * </p>
     *
     * @param stage lo stage primario dell'applicazione
     */
    public void initialize(Stage stage) {
        this.mainStage = stage;
        mainStage.setTitle(GameConstants.GAME_TITLE);
        mainStage.setResizable(false);
    }

    /**
     * Effettua la transizione alla scena specificata caricando il relativo file
     * FXML.
     * <p>
     * Il caricamento avviene tramite {@link FXMLLoader}; la nuova scena riceve
     * il foglio di stile condiviso e viene impostata sullo stage principale.
     * </p>
     *
     * @param fxmlFile il percorso relativo della risorsa FXML da caricare
     * @throws RuntimeException if the FXML resource cannot be loaded
     */
    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = createLoader(fxmlFile);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getStylesheetUrl());
            mainStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + fxmlFile, e);
        }
    }

    /**
     * Restituisce l'URL assoluto del foglio di stile condiviso.
     * <p>
     * Il foglio viene applicato a ogni scena da qui, invece di essere
     * referenziato con un percorso relativo nei file FXML. In questo modo il
     * caricamento è indipendente da dove si trovano i file FXML e funziona anche
     * quando l'applicazione è impacchettata in un JAR.
     * </p>
     *
     * @return l'URL del foglio di stile
     * @throws IllegalStateException if the stylesheet resource is missing
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
     * @param fxmlPath il percorso del file FXML
     * @return un'istanza configurata di {@link FXMLLoader}
     */
    private FXMLLoader createLoader(String fxmlPath) {
        return new FXMLLoader(SceneManager.class.getResource(fxmlPath));
    }
}
