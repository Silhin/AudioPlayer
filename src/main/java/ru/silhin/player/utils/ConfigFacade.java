package ru.silhin.player.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigFacade {

    private String musicFolderUrl;
    private final String fileName;

    public ConfigFacade(final String fileName) {
        this.fileName = fileName;
    }

    public void read() {
        try {
            BufferedInputStream reader = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(this.fileName)));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode parser = objectMapper.readTree(reader);

            musicFolderUrl = parser.path("music_folder").asText();
            reader.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void write(String newMusicFolder) {
        try {
            FileWriter writer = new FileWriter(Objects.requireNonNull(getClass().getResource(this.fileName)).getFile());

            Map<String, Object> customer = new HashMap<>();
            customer.put("music_folder", newMusicFolder);
            ObjectMapper mapper = new ObjectMapper();

            writer.write(mapper.writeValueAsString(customer));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMusicFolderUrl() {
        return this.musicFolderUrl;
    }

}
