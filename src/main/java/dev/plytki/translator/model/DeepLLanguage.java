package dev.plytki.translator.model;

public enum DeepLLanguage {

    BG("Bulgarian"),
    CS("Czech"),
    DA("Danish"),
    DE("German"),
    EL("Greek"),
    EN("English"),
    ES("Spanish"),
    ET("Estonian"),
    FI("Finnish"),
    FR("French"),
    HU("Hungarian"),
    ID("Indonesian"),
    IT("Italian"),
    JA("Japanese"),
    KO("Korean"),
    LT("Lithuanian"),
    LV("Latvian"),
    NB("Norwegian (Bokmål)"),
    NL("Dutch"),
    PL("Polish"),
    PT("Portuguese"),
    RO("Romanian"),
    RU("Russian"),
    SK("Slovak"),
    SL("Slovenian"),
    SV("Swedish"),
    TR("Turkish"),
    UK("Ukrainian"),
    ZH("Chinese");

    private final String name;

    DeepLLanguage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}