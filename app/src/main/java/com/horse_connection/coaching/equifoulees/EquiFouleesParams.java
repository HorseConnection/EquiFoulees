package com.horse_connection.coaching.equifoulees;

/**
 * Créé par Ph. van Hauwaert le 10/10/2017.
 * <p>
 * ca-app-pub-4473740585617677~1839585815 (ID AdMob)
 */
class EquiFouleesParams {
    private final String[] pProfil;
    private String pTypeCalc;
    private String pLgFoul;
    private String pEntree;
    private boolean pReglage;
    private String[] pHautObst;
    private String[] pLargObst;

    public EquiFouleesParams(String pLgFoul, String[] pProfil, String[] pHautObst, String[] pLargObst) {
        this.pTypeCalc = "C";
        this.pLgFoul = pLgFoul;
        this.pProfil = pProfil;
        this.pEntree = "G";
        this.pReglage = false;
        this.pHautObst = pHautObst;
        this.pLargObst = pLargObst;
    }

    public String getpTypeCalc() {
        return pTypeCalc;
    }

    public void setpTypeCalc(String pTypeCalc) {
        this.pTypeCalc = pTypeCalc;
    }

    public String getpLgFoul() {
        return pLgFoul;
    }

    public void setpLgFoul(String pLgFoul) {
        this.pLgFoul = pLgFoul;
    }

    public String getpProfil0() {
        return pProfil[0];
    }

    public void setpProfil0(String pProfil) {
        this.pProfil[0] = pProfil;
    }

    public String getpProfil1() {
        return pProfil[1];
    }

    public void setpProfil1(String pProfil) {
        this.pProfil[1] = pProfil;
    }

    public String getpEntree() {
        return pEntree;
    }

    public void setpEntree(String pEntree) {
        this.pEntree = pEntree;
    }

    public boolean ispReglage() {
        return pReglage;
    }

    public void setpReglage(boolean pReglage) {
        this.pReglage = pReglage;
    }

    public String[] getpHautObst() {
        return pHautObst;
    }

    public void setpHautObst(String[] pHautObst) {
        this.pHautObst = pHautObst;
    }

    public String[] getpLargObst() {
        return pLargObst;
    }

    public void setpLargObst(String[] pLargObst) {
        this.pLargObst = pLargObst;
    }


}
