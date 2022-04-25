package com.psyala.pojo;

public enum Dungeon {
    NECROTIC_WAKE("NW", "Necrotic Wake"),
    SPIRES_OF_ASCENSION("SOA", "Spires of Ascension"),
    DE_OTHER_SIDE("DOS", "De Other Side"),
    MISTS_OF_TIRNA_SCITHE("MISTS", "Mists of Tirna Scithe"),
    PLAGUEFALL("PF", "Plaguefall"),
    THEATRE_OF_PAIN("TOP", "Theatre of Pain"),
    SANGUINE_DEPTHS("SD", "Sanguine Depths"),
    HALLS_OF_ATONEMENT("HOA", "Halls of Atonement"),
    SOLEAHS_GAMBIT("GMBT", "Tazavesh: Soleah's Gambit"),
    STREETS_OF_WONDER("STRT", "Tazavesh: Streets of Wonder"),
    ;

    private final String acronym;
    private final String fullName;

    Dungeon(String acronym, String fullName) {
        this.acronym = acronym;
        this.fullName = fullName;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getFullName() {
        return fullName;
    }
}
