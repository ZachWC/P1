package fa.dfa;

import java.util.LinkedHashMap;
import java.util.Map;

import fa.State;

public class DFAState extends State{

    private boolean isFinal;

    private Map<Character, DFAState> transitions;

    /**
     * Constructor for DFAState
     * @param name the name of the state
     */
    public DFAState(String name) {
        super(name);
        this.isFinal = false;
        this.transitions = new LinkedHashMap<>();

    }

    //GETTERS

    public boolean getIsFinal() {
        return isFinal;
    }

    public Map<Character, DFAState> getTransitions() {
        return transitions;
    }

    public DFAState getTransition(char symbol) {
        return transitions.get(symbol);
    }

    //SETTERS

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public void addTransition(char symbol, DFAState state) {
        transitions.put(symbol, state);
    }
}