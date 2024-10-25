package me.t3sl4.upcortex.Model.Package;

public class PackageItem {
    private String name;
    private String description;
    private int orderCount;
    private int imageResource;

    public PackageItem(String name, String description, int orderCount, int imageResource) {
        this.name = name;
        this.description = description;
        this.orderCount = orderCount;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getImageResource() {
        return imageResource;
    }
}