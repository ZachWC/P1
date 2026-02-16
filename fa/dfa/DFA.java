package fa.dfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fa.State;

/**
 * Deterministic finite automaton implementation.
 *
 * @author YourName
 */
public class DFA implements DFAInterface {

    /** States by name, insertion-ordered for deterministic toString output. */
    private Map<String, DFAState> states;

    /** Alphabet symbols, insertion-ordered. */
    private Set<Character> sigma;

    /** Start state (q0). */
    private DFAState start;

    /**
     * Constructs an empty DFA.
     */
    public DFA() {
        this.states = new LinkedHashMap<>();
        this.sigma = new LinkedHashSet<>();
        this.start = null;
    }

    @Override
    public boolean addState(String name) {
        if (states.containsKey(name)) return false;
        states.put(name, new DFAState(name));
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        DFAState s = states.get(name);
        if (s == null) return false;
        s.setIsFinal(true);
        return true;
    }

    @Override
    public boolean setStart(String name) {
        DFAState s = states.get(name);
        if (s == null) return false;
        start = s;
        return true;
    }

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        if (start == null) return false;

        // Tests use "e" to represent epsilon (empty string)
        if (s.equals("e")) {
            return start.getIsFinal();
        }

        DFAState current = start;
        for (int i = 0; i < s.length(); i++) {
            char sym = s.charAt(i);
            if (!sigma.contains(sym)) return false;
            current = current.getTransition(sym);
            if (current == null) return false;
        }
        return current.getIsFinal();
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public State getState(String name) {
        return states.get(name);
    }

    @Override
    public boolean isFinal(String name) {
        DFAState s = states.get(name);
        return s != null && s.getIsFinal();
    }

    @Override
    public boolean isStart(String name) {
        DFAState s = states.get(name);
        return s != null && s == start;
    }

    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        DFAState from = states.get(fromState);
        DFAState to = states.get(toState);

        if (from == null || to == null) return false;
        if (!sigma.contains(onSymb)) return false;

        from.addTransition(onSymb, to);
        return true;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        DFA copy = new DFA();

        // copy alphabet in same order
        for (char c : sigma) copy.addSigma(c);

        // copy states in same order
        for (String name : states.keySet()) copy.addState(name);

        // copy start/finals
        for (String name : states.keySet()) {
            if (isFinal(name)) copy.setFinal(name);
            if (isStart(name)) copy.setStart(name);
        }

        // copy transitions, swapping just the two labels
        for (String name : states.keySet()) {
            DFAState original = states.get(name);

            for (char c : sigma) {
                DFAState dest = original.getTransition(c);
                if (dest == null) continue;

                char out = c;
                if (c == symb1) out = symb2;
                else if (c == symb2) out = symb1;

                copy.addTransition(name, dest.getName(), out);
            }
        }

        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Q
        sb.append("Q = { ");
        for (String name : states.keySet()) sb.append(name).append(" ");
        sb.append("}\n");

        // Sigma
        sb.append("Sigma = { ");
        for (char c : sigma) sb.append(c).append(" ");
        sb.append("}\n");

        // delta header
        sb.append("delta =\n");
        sb.append("\t");
        for (char c : sigma) sb.append("\t").append(c);
        sb.append("\n");

        // each state row
        for (String name : states.keySet()) {
            DFAState st = states.get(name);
            sb.append("\t").append(name);
            for (char c : sigma) {
                DFAState to = st.getTransition(c);
                sb.append("\t").append(to == null ? "" : to.getName());
            }
            sb.append("\n");
        }

        // q0
        sb.append("q0 = ").append(start == null ? "" : start.getName()).append("\n");

        // F
        sb.append("F = { ");
        for (String name : states.keySet()) {
            if (states.get(name).getIsFinal()) sb.append(name).append(" ");
        }
        sb.append("}");

        return sb.toString();
    }
}

