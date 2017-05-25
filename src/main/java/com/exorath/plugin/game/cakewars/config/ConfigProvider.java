package com.exorath.plugin.game.cakewars.config;

import com.google.gson.JsonObject;

/**
 * Created by toonsev on 5/23/2017.
 */
public interface ConfigProvider {
    JsonObject getKitPackageJson();
}
