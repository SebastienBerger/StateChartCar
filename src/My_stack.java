// Code from Franck Barbier and modified by Eric Cariou

import com.pauware.pauware_engine._Core.*;
import com.pauware.pauware_engine._Exception.*;
import com.pauware.pauware_engine._Java_EE.*;
import java.util.Scanner;

public class My_stack implements Manageable {

    // business data: a stack of String
    java.util.Stack<String> _stack;
    
    // UML state machine
    AbstractStatechart _Empty;
    AbstractStatechart _Only_one;
    AbstractStatechart _More_than_one;
    AbstractStatechart _Not_empty;
    AbstractStatechart_monitor _My_stack_state_machine;

    // business initialization
    private void init_structure() throws Statechart_exception {
        _stack = new java.util.Stack();
    }

    // creation of the state machine
    private void init_behavior() throws Statechart_exception {
        _Empty = new Statechart("Empty");
        _Empty.inputState();
        _Empty.stateInvariant(this, "size_is_equal_to_0");
        
        _Only_one = new Statechart("Only one");
        _Only_one.stateInvariant(this, "size_is_equal_to_1");
        
        _More_than_one = new Statechart("More than one");
        _More_than_one.stateInvariant(this, "size_is_greater_than_1");
        
        _Not_empty = (_Only_one.xor(_More_than_one)).name("Not empty");
        _Not_empty.allowedEvent("peek", this, "actionPeek");

        // PauWare viewer:
        //com.pauware.pauware_view.PauWare_view pv = null;
        // uncomment the following line for launching the viewer
        //pv = new com.pauware.pauware_view.PauWare_view();

        // creation of the state machine
        _My_stack_state_machine = new Statechart_monitor(_Empty.xor(_Not_empty), "My stack", AbstractStatechart_monitor.Show_on_system_out, null);
     
        _My_stack_state_machine.allowedEvent("empty", this, "actionEmpty");
        _My_stack_state_machine.allowedEvent("search", this, "actionSearch", new Object[]{new Object()}); // this allowed event is overridden in the 'public void search(Object o) throws Statechart_exception' method below
    }
    
    // add transitions and start the state machine
    public void start() throws Statechart_exception {
      
        // creation of the transitions between states
        _My_stack_state_machine.fires("pop", _Only_one, _Empty, true, this, "actionPop");
        _My_stack_state_machine.fires("pop", _More_than_one, _Only_one, this, "size_is_equal_to_2", this, "actionPop");
        _My_stack_state_machine.fires("pop", _More_than_one, _More_than_one, this, "size_is_not_equal_to_2", this, "actionPop");

        _My_stack_state_machine.fires("push", _Empty, _Only_one, true, this, "actionPush", new Object[]{new Object()}); // this transition is overridden in the 'public void push(Object item) throws Statechart_exception' method below
        _My_stack_state_machine.fires("push", _Only_one, _More_than_one, true, this, "actionPush", new Object[]{new Object()}); // this transition is overridden in the 'public void push(Object item) throws Statechart_exception' method below
        _My_stack_state_machine.fires("push", _More_than_one, _More_than_one, true, this, "actionPush", new Object[]{new Object()}); // this transition is overridden in the 'public void push(Object item) throws Statechart_exception' method below

        _My_stack_state_machine.start();
    }

    public void stop() throws Statechart_exception {
        _My_stack_state_machine.stop();
    }

    public My_stack() throws Statechart_exception {
        init_structure();
        init_behavior();
    }

    //-----------
    // UML events
    //-----------
    
    public void evtEmpty_() throws Statechart_exception {
        _My_stack_state_machine.run_to_completion("empty", AbstractStatechart_monitor.Compute_invariants);
    }

    public void evtPeek() throws Statechart_exception {
        _My_stack_state_machine.run_to_completion("peek", AbstractStatechart_monitor.Compute_invariants);
    }

    public void evtPop() throws Statechart_exception {
        _My_stack_state_machine.run_to_completion("pop", AbstractStatechart_monitor.Compute_invariants);
    }

    public void evtPush(String item) throws Statechart_exception {
        // transitions are redefined because the "item" parameter must be passed for the execution of "actionPush"
        _My_stack_state_machine.fires("push", _Empty, _Only_one, true, this, "actionPush", new Object[]{item});
        _My_stack_state_machine.fires("push", _Only_one, _More_than_one, true, this, "actionPush", new Object[]{item});
        _My_stack_state_machine.fires("push", _More_than_one, _More_than_one, true, this, "actionPush", new Object[]{item});
        _My_stack_state_machine.run_to_completion("push", AbstractStatechart_monitor.Compute_invariants);
    }

    public void evtSearch(String item) throws Statechart_exception {
        _My_stack_state_machine.allowedEvent("search", this, "_search", new Object[]{item});
        _My_stack_state_machine.run_to_completion("search", AbstractStatechart_monitor.Compute_invariants);
    }
    
    //---------------------------------------------------------
    // UML actions (called by reflection; they must be 'public')
    //----------------------------------------------------------
    
    public boolean actionEmpty() {
        return _stack.empty();
    }

    public String actionPeek() {
        String val = _stack.peek();
        System.out.println(" --> value read: "+val);
        return val;
    }

    public String actionPop() {
        String val = _stack.pop();
        System.out.println(" --> value read: "+val);
        return val;
    }

    public Object actionPush(String item) {
        return _stack.push(item);
    }

    public int actionSearch(String o) {
        return _stack.search(o);
    }

    //-----------
    // UML guards
    //-----------
    
    public boolean size_is_equal_to_2() {
        return _stack.size() == 2;
    }

    public boolean size_is_not_equal_to_2() {
        return _stack.size() != 2;
    }

    //---------------
    // UML invariants
    //---------------
    
    public boolean size_is_equal_to_0() {
        return _stack.size() == 0;
    }

    public boolean size_is_equal_to_1() {
        return _stack.size() == 1;
    }

    public boolean size_is_greater_than_1() {
        return _stack.size() > 1;
    }

    //-------------------------
    // State machine management
    //-------------------------
    
    public String async_current_state() {
        return _My_stack_state_machine.async_current_state();
    }

    public String current_state() {
        return _My_stack_state_machine.current_state();
    }

    public boolean in_state(String name) {
        return _My_stack_state_machine.in_state(name);
    }

    public void to_state(String name) throws Statechart_exception {
        _My_stack_state_machine.to_state(name);
    }

    public String name() {
        return _My_stack_state_machine.name();
    }

    public String verbose() {
        return _My_stack_state_machine.verbose();
    }

    public void reset() throws Statechart_exception {
        try {
            while (true) {
                _stack.pop();
            }
        } catch (java.util.EmptyStackException ese) {
            to_state("Empty");
        }
    }

    public static void main(String args[]) {
        try {
            My_stack ms = new My_stack();
            ms.start();
            
            Scanner sc = new Scanner(System.in);
            String choice;
            do {
                System.out.print("Enter 'pop', 'push', 'peek' or 'end': ");
                choice = sc.nextLine();
                
                if (choice.equals("push")) {
                    System.out.print("Enter the value: ");
                    String value = sc.nextLine();
                    ms.evtPush(value);
                }
                
                if (choice.equals("pop")) {
                    ms.evtPop();
                }
                
                if (choice.equals("peek")) {
                    ms.evtPeek();
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
