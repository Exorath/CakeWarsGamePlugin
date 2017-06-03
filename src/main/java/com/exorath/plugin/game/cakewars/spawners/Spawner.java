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

package com.exorath.plugin.game.cakewars.spawners;

import com.exorath.exoHUD.DisplayProperties;
import com.exorath.exoHUD.HUDText;
import com.exorath.exoHUD.locations.row.HologramLocation;
import com.exorath.exoHUD.removers.NeverRemover;
import com.exorath.exoHUD.texts.CompositeText;
import com.exorath.exoHUD.texts.PlainText;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.Main;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.omg.CORBA.COMM_FAILURE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by toonsev on 3/17/2017.
 */
public class Spawner {
    private Location location;
    private SpawnerType type;
    private boolean started = false;
    private boolean showHolo = true;

    private HologramLocation hologram;

    public Spawner(Location location, SpawnerType type) {
        this.location = location;
        this.type = type;
        if(showHolo) {
            hologram = new HologramLocation(location.clone().add(0, 1.5d, 0));
            addTypeLineToHologram();
        }
    }

    private void addTypeLineToHologram(){
        String nameLine = type.getName() == null ? ChatColor.GREEN + type.getMaterial().name() : type.getName();
        Stream<TextComponent> textComponentStream = Arrays.asList(TextComponent.fromLegacyText(nameLine)).stream().map(component -> (TextComponent) component);
        hologram.addText(PlainText.components(textComponentStream.collect(Collectors.toList())), DisplayProperties.create(0, NeverRemover.never()));
    }

    public boolean start() {
        if (started)
            return false;
        started = true;
        if (type.getInterval() > 20)
            new Countdown().runTaskTimer(Main.getInstance(), 0, 20);
        else
            new QuickDropper().runTaskTimer(Main.getInstance(), 0, type.getInterval());
        return true;
    }

    public Location getLocation() {
        return location;
    }


    private class QuickDropper extends BukkitRunnable {
        @Override
        public void run() {
            if (!started)
                return;
            if (BaseGameAPI.getInstance().getStateManager().getState() != State.STARTED)
                return;
            if (location.getWorld() != null) {
                location.getWorld().dropItem(location, new SpawnerItemStack(type.getMaterial()));
            }
            else
                System.out.println("Spawner: no world found");
        }
    }



    private class Countdown extends BukkitRunnable implements HUDText {
        private PublishSubject<List<TextComponent>> subject = PublishSubject.create();
        private long remaining = type.getInterval();

        public Countdown() {
            hologram.addText(this, DisplayProperties.create(-1, NeverRemover.never()));
        }

        @Override
        public void run() {
            if (!started)
                return;
            if (BaseGameAPI.getInstance().getStateManager().getState() != State.STARTED)
                return;
            if (remaining <= 0) {
                if (location.getWorld() != null) {
                    location.getWorld().dropItem(location, new ItemStack(type.getMaterial()));
                } else
                    System.out.println("Spawner: no world found");
                if (!subject.hasComplete() && showHolo)
                    updateSpawned();
                remaining = type.getInterval();

            } else {
                if (!subject.hasComplete() && showHolo)
                    update();
                remaining -= 20l;
            }

        }

        private void updateSpawned() {
            TextComponent component = new TextComponent("Spawned!");
            component.setColor(ChatColor.GREEN);
            subject.onNext(Arrays.asList(new TextComponent[]{component}));
        }

        private void update() {
            Long remainingSec = remaining / 20l;
            TextComponent component = new TextComponent("Spawning in ");
            component.setColor(ChatColor.AQUA);
            TextComponent count = new TextComponent(remainingSec.toString());
            count.setColor(ChatColor.GRAY);
            TextComponent seconds = new TextComponent(" Seconds");
            seconds.setColor(ChatColor.AQUA);
            subject.onNext(Arrays.asList(new TextComponent[]{component, count, seconds}));
        }

        @Override
        public Observable<List<TextComponent>> getTextObservable() {
            return subject;
        }
    }
}
