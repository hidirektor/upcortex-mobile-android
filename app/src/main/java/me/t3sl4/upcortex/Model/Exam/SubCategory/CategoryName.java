package me.t3sl4.upcortex.Model.Exam.SubCategory;

public enum CategoryName {
    KISA_SURELI_BELLEK("Kısa Süreli Bellek"),
    UZUN_SURELI_BELLEK("Uzun Süreli Bellek"),
    GORSEL_BELLEK("Görsel Bellek"),
    ISLEMSel_BELLEK("İşlemsel Bellek");

    private final String displayName;

    CategoryName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoryName fromString(String text) {
        for (CategoryName category : CategoryName.values()) {
            if (category.displayName.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category name: " + text);
    }
}