package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.UnknownKeyException;
import weka.core.Attribute;
import weka.core.Instance;

/**
 * The 'state' is the attributes (i.e. text) from the bug reports
 * 
 * @author John Anvik (2017)
 *
 */

public class BugTriageState implements MutableState {

    public static final String INSTANCE = "Instance";

    private static BugTriageDomain domain;
    private Instance report;

    public BugTriageState(BugTriageDomain d) {
	BugTriageState.domain = d;
    }

    public BugTriageState(Instance r) {
	this.report = r;
    }

    public State copy() {
	return new BugTriageState(report);	
    }

    public Object get(Object key) {

	switch ((String) key) {
	case INSTANCE:
	    return report;
	default:
	    throw new UnknownKeyException(key);
	}
    }

    public List<Object> variableKeys() {
	List<Object> keys = new ArrayList<Object>();
	keys.add(INSTANCE);
	return keys;
    }

    public MutableState set(Object key, Object value) {
	switch ((String) key) {
	case INSTANCE:
	    report = (Instance) value;
	    break;
	default:
	    throw new UnknownKeyException(key);
	}

	return this;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	Enumeration<Attribute> attributes = report.enumerateAttributes();
	while (attributes.hasMoreElements()) {
	    Attribute a = attributes.nextElement();
	    sb.append(a.name() + ": " + a.weight() +"\n");
	}
	return sb.toString();
    }

    public Instance getReport() {
	return report;
    }

}
