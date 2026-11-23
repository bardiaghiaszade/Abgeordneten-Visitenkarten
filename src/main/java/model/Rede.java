package model;

/**
 * Rede eines Abgeordneten
 */
public interface Rede {

    // id des zugehörigen Abgeordneten
    int getAbgeordnetenId();

    // datum der Rede (TT.MM.JJJJ)
    String getDatum();

    // tagesordnungspunkt
    String getTop();

    // wahlperiode
    int getWp();

    // redetext
    String getContent();
}