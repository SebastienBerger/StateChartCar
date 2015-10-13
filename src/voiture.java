/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sberger
 */
public class voiture {
    private int _vitesse;
    private boolean _maxAtteint;
    public voiture(){
        _vitesse = 0;
        _maxAtteint = false;
    }
    public void set_vitesse(int vitesse){
        _vitesse = vitesse;
    }
    public void set_maxAtteint(boolean bool){
        _maxAtteint = bool;
    }
    public int get_vitesse(){
        return _vitesse;
    }
    public boolean get_maxAtteint(){
        return _maxAtteint;
    }
}
