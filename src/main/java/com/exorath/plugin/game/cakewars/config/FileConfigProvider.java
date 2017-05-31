/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.plugin.game.cakewars.config;

import com.exorath.plugin.game.cakewars.Main;
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
