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

package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.InventoryMenu;
import com.exorath.exomenus.MenuItem;
import com.exorath.exomenus.Size;

/**
 * Load the menu from the flavor config:
 * shop:
 *   weapons:
 *     ironsword: //itemstack
 *       type: item
 *       name: Iron Sword
 *       material: IRON_SWORD
 *       slot: 0
 *       cost:
 *         iron: 20
 *
 * Created by toonsev on 3/18/2017.
 */
public class ShopMenu {
    private InventoryMenu menu;

    public ShopMenu(){
        this.menu = new InventoryMenu("Item Store", Size.ONE_LINE, new MenuItem[9], null);

    }
    public void addShopDirectory(ShopDirectory directory){
        menu.setItem(directory.getSlot(), directory);
    }

    public InventoryMenu getMenu() {
        return menu;
    }
}