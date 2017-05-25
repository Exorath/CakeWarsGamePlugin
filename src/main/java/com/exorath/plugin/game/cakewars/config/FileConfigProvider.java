package com.exorath.plugin.game.cakewars.config;

import com.exorath.plugin.kit.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by toonsev on 5/23/2017.
 */
public class FileConfigProvider implements ConfigProvider{
    private static final Gson GSON = new Gson();
    private JsonObject config;

    public FileConfigProvider(File file) throws FileNotFoundException {
        this.config = GSON.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
    }

    @Override
    public JsonObject getKitPackageJson() {
        if(!config.has("kitPackage"))
            Main.terminate("The config does not contain a kitPackage object");
        return config.get("kitPackage").getAsJsonObject();
    }
}
