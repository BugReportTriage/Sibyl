package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;

public class BugReportTerm extends BugReportText {

    public static final String TERM = "Term";
    public static final String BR_TERM = "BugReportTerm";

    private String name;
    private final static List<Object> keys = Arrays.<Object>asList(TEXT, TERM);

    private String term;

    public BugReportTerm(String name, String text) {
	super(name, text);
    }

    public BugReportTerm(String name, String text, String term) {
	super(name, text);
	this.term = term;
    }

    @Override
    public List<Object> variableKeys() {
	return keys;
    }

    @Override
    public Object get(Object key) {
	if (key.equals(TERM))
	    return this.term;
	return super.get(key);
    }

    @Override
    public MutableState set(Object key, Object value) {
	if (key.equals(TERM))
	    this.term = (String) value;
	else
	    super.set(key, value);
	return this;
    }

    @Override
    public String name() {
	return name;
    }

    @Override
    public String className() {
	return BR_TERM;
    }

    @Override
    public ObjectInstance copyWithName(String objectName) {
	return new BugReportTerm(objectName, text, term);
    }

    @Override
    public State copy() {
	return new BugReportTerm(name, text, term);
    }
}
