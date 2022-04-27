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

    public static Dungeon fromAcronym(String acronym) {
        if (acronym.equalsIgnoreCase(NECROTIC_WAKE.getAcronym())) return NECROTIC_WAKE;
        if (acronym.equalsIgnoreCase(SPIRES_OF_ASCENSION.getAcronym())) return SPIRES_OF_ASCENSION;
        if (acronym.equalsIgnoreCase(DE_OTHER_SIDE.getAcronym())) return DE_OTHER_SIDE;
        if (acronym.equalsIgnoreCase(MISTS_OF_TIRNA_SCITHE.getAcronym())) return MISTS_OF_TIRNA_SCITHE;
        if (acronym.equalsIgnoreCase(PLAGUEFALL.getAcronym())) return PLAGUEFALL;
        if (acronym.equalsIgnoreCase(THEATRE_OF_PAIN.getAcronym())) return THEATRE_OF_PAIN;
        if (acronym.equalsIgnoreCase(SANGUINE_DEPTHS.getAcronym())) return SANGUINE_DEPTHS;
        if (acronym.equalsIgnoreCase(HALLS_OF_ATONEMENT.getAcronym())) return HALLS_OF_ATONEMENT;
        if (acronym.equalsIgnoreCase(SOLEAHS_GAMBIT.getAcronym())) return SOLEAHS_GAMBIT;
        if (acronym.equalsIgnoreCase(STREETS_OF_WONDER.getAcronym())) return STREETS_OF_WONDER;
        throw new IndexOutOfBoundsException();
    }
}
