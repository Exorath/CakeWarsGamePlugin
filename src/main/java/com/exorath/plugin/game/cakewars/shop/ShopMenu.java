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