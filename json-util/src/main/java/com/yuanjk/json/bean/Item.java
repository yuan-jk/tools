package com.yuanjk.json.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yuanjk.json.serializer.ItemDeserializer;

/**
 * @author yuanjk
 * @version 21/4/9
 */
@JsonDeserialize(using = ItemDeserializer.class)
public class Item {
    public int id;
    public String itemName;
    public User owner;

    public Item() {
    }

    public Item(int id, String itemName, User owner) {
        this.id = id;
        this.itemName = itemName;
        this.owner = owner;
    }
}
