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

package com.exorath.plugin.game.cakewars.team;

import com.exorath.exoHUD.HUDText;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toonsev on 8/24/2017.
 */
public class TeamText implements HUDText{
    private CWTeam cwTeam;
    private BehaviorSubject<List<TextComponent>> subject = BehaviorSubject.create();

    public TeamText(CWTeam cwTeam) {
        this.cwTeam = cwTeam;
        update();
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return subject;
    }

    protected void update(){
        List<TextComponent> components = new ArrayList<>(1);
        TextComponent teamText = new TextComponent(cwTeam.getName());
        components.add(teamText);
        if(cwTeam.isPlaying()){
            if(cwTeam.isCakeAlive()){
                TextComponent bracketOpen = new TextComponent( " [");
                bracketOpen.setColor(ChatColor.WHITE);
                TextComponent heart = new TextComponent("❤");
                heart.setColor(ChatColor.GREEN);
                TextComponent bracketClosed = new TextComponent("]");
                bracketClosed.setColor(ChatColor.WHITE);
                components.add(bracketOpen);
                components.add(heart);
                components.add(bracketClosed);
            }else {
                TextComponent bracketOpen = new TextComponent( " [");
                bracketOpen.setColor(ChatColor.WHITE);
                TextComponent cross = new TextComponent("✘");
                cross.setColor(ChatColor.RED);
                TextComponent bracketClosed = new TextComponent("]");
                bracketClosed.setColor(ChatColor.WHITE);
                components.add(bracketOpen);
                components.add(cross);
                components.add(bracketClosed);
            }
        }else {
            teamText.setText(ChatColor.stripColor(teamText.getText()));
            teamText.setColor(ChatColor.GRAY);
        }
        subject.onNext(components);

    }
}
