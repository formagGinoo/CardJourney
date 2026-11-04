package it.unicam.cs.mpgc.rpg130809.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Gestore dell'accesso fisico al file di salvataggio.
 * <p>
 * Incapsula il percorso del file e la logica di I/O: la scrittura è
 * <b>atomica</b> (prima si scrive un file temporaneo {@code save.json.tmp},
 * poi lo si rinomina sul file definitivo), così in caso di chiusura forzata
 * del processo a metà scrittura il file precedente resta integro e non può
 * trovarsi in uno stato corrotto o parzialmente scritto.
 * </p>
 * <p>
 * La classe non conosce il contenuto dei dati, lavora solo su JSON..
 * </p>
 *
 * @see SaveManager
 */
public class SaveFileHandler {

    private static final String SAVE_FILE_NAME = "save.json";
    private static final String TEMP_SUFFIX = ".tmp";

    private final Path saveDirectory;
    private final Path saveFile;
    private final Path tempFile;

    /**
     * Crea il gestore del file di salvataggio nella cartella specificata.
     *
     * @param saveDirectory la cartella destinata a contenere il salvataggio
     * @throws IllegalArgumentException if the directory is null
     */
    public SaveFileHandler(Path saveDirectory) {
        if (saveDirectory == null) {
            throw new IllegalArgumentException("Save directory cannot be null");
        }
        this.saveDirectory = saveDirectory;
        this.saveFile = saveDirectory.resolve(SAVE_FILE_NAME);
        this.tempFile = saveDirectory.resolve(SAVE_FILE_NAME + TEMP_SUFFIX);
    }

    /**
     * Scrive il contenuto sul file di salvataggio in modo atomico.
     * <p>
     * Il contenuto viene prima scritto su {@code save.json.tmp} e scritto su
     * disco, poi il file temporaneo viene rinominato sul file definitivo. Se
     * il filesystem non supporta la rinomina atomica, viene eseguita una
     * copia con sostituzione, che mantiene comunque lo stesso livello di
     * protezione garantito dalla scrittura in due passaggi.
     * </p>
     *
     * @param content il contenuto JSON da scrivere
     * @throws IOException if reading or writing fails
     * @throws IllegalArgumentException if the content is null
     */
    public void write(String content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        Files.createDirectories(saveDirectory);
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);
        try {
            Files.move(tempFile, saveFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tempFile, saveFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Legge il contenuto JSON del file di salvataggio.
     *
     * @return il contenuto del file
     * @throws IOException if reading fails
     */
    public String read() throws IOException {
        return Files.readString(saveFile, StandardCharsets.UTF_8);
    }

    /**
     * Indica se esiste un file di salvataggio leggibile.
     *
     * @return {@code true} se il file di salvataggio esiste
     */
    public boolean exists() {
        return Files.isRegularFile(saveFile);
    }

    /**
     * Elimina il file di salvataggio, se presente.
     * <p>
     * Usato a partita vinta o quando una nuova partita sostituisce quella
     * precedente. Rimuove anche un eventuale file temporaneo rimasto da una
     * scrittura interrotta.
     * </p>
     *
     * @throws IOException if deletion fails
     */
    public void delete() throws IOException {
        Files.deleteIfExists(saveFile);
        Files.deleteIfExists(tempFile);
    }

    /**
     * Restituisce il percorso completo del file di salvataggio.
     *
     * @return il percorso del file di salvataggio
     */
    public Path getSaveFilePath() {
        return saveFile;
    }
}
