package org.slackyogi.model;

import org.slackyogi.model.enums.ToolType;

public class Tool extends Product {
    ToolType type;
    double mass;

    public Tool(String name, double price, double mass, ToolType type) {
        super(name, price);
        this.mass = mass;
    }


}
