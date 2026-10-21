package it.unicam.cs.mpgc.rpg130809.model.game;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;

/**
 * Carica e registra i font usati dalla UI.
 * <p>
 * JavaFX non gestisce in modo affidabile la direttiva {@code @font-face} nei
 * file css quando l'applicazione viene eseguita da un JAR o da una
 * directory diversa. Il font viene quindi caricato all'avvio dell'applicazione
 * </p>
 */
public final class FontManager {

    public static final String UI_FONT_FAMILY = "Jersey 20";
    private static final String FONT_RESOURCE = "/fonts/Jersey20-Regular.ttf";
    private static boolean loaded;

    private FontManager() {}

    /**
     * Carica il font se non è già stato caricato.
     * <p>
     * Se la risorsa manca o non è valida, l'applicazione continua a funzionare
     * con il font di fallback del sistema.
     * </p>
     */
    public static void loadFonts() {
        if (loaded) {
            return;
        }

        try (InputStream fontStream = FontManager.class.getResourceAsStream(FONT_RESOURCE)) {
            if (fontStream == null) {
                System.err.println("Font non trovato: " + FONT_RESOURCE);
                return;
            }

            Font font = Font.loadFont(fontStream, 10);
            if (font == null) {
                System.err.println("Impossibile caricare il font: " + FONT_RESOURCE);
                return;
            }

            loaded = true;

            if (!UI_FONT_FAMILY.equals(font.getFamily())) {
                System.err.println("Font caricato: '" + font.getFamily() + "', atteso: '" + UI_FONT_FAMILY + "'");
            }
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del font: " + e.getMessage());
        }
    }

    /**
     * @return {@code true} se il font dell'interfaccia è stato caricato
     */
    public static boolean isLoaded() {
        return loaded;
    }
}
