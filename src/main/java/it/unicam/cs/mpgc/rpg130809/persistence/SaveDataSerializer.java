package it.unicam.cs.mpgc.rpg130809.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Serializzatore JSON del salvataggio di gioco.
 * <p>
 * Il formato su disco non è noto ad altre classi del
 * progetto: l'unica operazione esposta è la conversione
 * {@link SaveData} <-> {@code String}, così un eventuale cambio di schema
 * o di libreria resta confinato in questa classe.
 * </p>
 */
public final class SaveDataSerializer {

    private final Gson gson;

    /**
     * Crea il serializzatore con la configurazione standard di Gson.
     */
    public SaveDataSerializer() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }

    /**
     * Serializza un salvataggio in un JSON.
     *
     * @param data il salvataggio da serializzare
     * @return la rappresentazione JSON del salvataggio
     * @throws IllegalArgumentException if the data is null
     */
    public String toJson(SaveData data) {
        if (data == null) {
            throw new IllegalArgumentException("SaveData cannot be null");
        }
        return gson.toJson(data);
    }

    /**
     * Deserializza una stringa JSON in un salvataggio.
     * <p>
     * Se la stringa non corrisponde a un salvataggio valido (contento
     * incompleto, formato imprevisto), i campi mancanti restano ai valori
     * di default ed è compito del chiamante validare il salvataggio.
     * </p>
     *
     * @param json la stringa JSON da deserializzare
     * @return il salvataggio ricostruito
     * @throws com.google.gson.JsonSyntaxException if the JSON is malformed
     * @throws IllegalArgumentException if the json is null or blank
     */
    public SaveData fromJson(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("JSON cannot be null or blank");
        }
        return gson.fromJson(json, SaveData.class);
    }
}
