
import com.pauware.pauware_engine._Core.*;
import com.pauware.pauware_engine._Exception.*;
import com.pauware.pauware_engine._Java_EE.*;
import java.util.Scanner;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sberger
 */
public class MAE_voiture {

    /**
     * @param args the command line arguments
     */
    voiture _voiture;
    AbstractStatechart _Arret;
    AbstractStatechart _MoinsVite;
    AbstractStatechart _PlusVite;
    AbstractStatechart _Maximum;
    AbstractStatechart _Marche;
    AbstractStatechart_monitor _MAE;
    
    public void init_structure(){
        _voiture = new voiture();
    }
    public void init_behavior() throws Statechart_state_based_exception, Statechart_exception{
        _Arret = new Statechart("Arret");
        _Arret.inputState();
        _Arret.doActivity(this, "vitesseNulle");
        
        _MoinsVite = new Statechart("MoinsVite");
        _MoinsVite.doActivity(this, "vitesseDec");
                
        _PlusVite = new Statechart("PlusVite");
        _PlusVite.inputState();
        _PlusVite.doActivity(this, "vitesseInc");

        
        _Maximum = new Statechart("Maximum");
        _Maximum.doActivity(this, "vitesseMax");
        

        _Marche = (_MoinsVite.xor(_PlusVite.xor(_Maximum))).name("Marche");  
                
        _MAE = new Statechart_monitor(_Arret.xor(_Marche), "MAE voiture", AbstractStatechart_monitor.Show_on_system_out, null);
        
    }
    
    public void start() throws Statechart_transition_based_exception, Statechart_exception{
        _MAE.fires("accelerer", _Arret, _Marche);
        _MAE.fires("ralentir", _Marche, _Arret, this, "vitesse_equal_10");
        _MAE.fires("ralentir", _Marche, _MoinsVite, this, "vitesse_different_10");
        _MAE.fires("accelerer", _Marche, _PlusVite, this, "vitesse_different_90");
        _MAE.fires("accelerer", _Marche, _Maximum, this, "vitesse_egal_90");
        _MAE.start();
    }
    
    public void stop() throws Statechart_exception {
        _MAE.stop();
    }
    
    public MAE_voiture() throws Statechart_exception {
        init_structure();
        init_behavior();
    }
    public void vitesseInc(){
        System.out.println("actionAccelerer");
        _voiture.set_vitesse(_voiture.get_vitesse()+10);
    }
    public void vitesseDec(){
        _voiture.set_vitesse(_voiture.get_vitesse()-10);
    }
    public void vitesseNulle(){
        System.out.println("vitesse nulle");
        _voiture.set_vitesse(0);
    }
    public void vitesseMax(){
        _voiture.set_vitesse(100);
        _voiture.set_maxAtteint(true);
    }
    
    public boolean vitesse_equal_10(){
        return _voiture.get_vitesse() == 10;
    }
    public boolean vitesse_egal_90(){
        return _voiture.get_vitesse() >= 90;
    }
    public boolean vitesse_different_10(){
        return _voiture.get_vitesse() != 10;
    }
    public boolean vitesse_different_90(){
        return _voiture.get_vitesse() < 90;
    }
    public void evtInc() throws Statechart_exception {
        System.out.println("incccccc");
        _MAE.run_to_completion("accelerer");
    }
    public void evtDec() throws Statechart_exception {
        _MAE.run_to_completion("ralentir");
    }
    public static void main(String[] args) {
        try {
            MAE_voiture ms = new MAE_voiture();
            ms.start();
            
            Scanner sc = new Scanner(System.in);
            String choice;
            do {
                System.out.println("Enter '+', '-', or 'end': ");
                System.out.println("Vitesse : "+ms._voiture.get_vitesse());
                choice = sc.nextLine();
                
                if (choice.equals("+")) {
                    System.out.println("+++++++++++");
                    ms.evtInc();
                }
                
                if (choice.equals("-")) {
                    ms.evtDec();
                }
            }
            while(! choice.equals("end"));
            
            ms.stop();
            System.exit(0);
        
        } catch (Throwable t) {
            t.printStackTrace();
        } 
    }
    
}
