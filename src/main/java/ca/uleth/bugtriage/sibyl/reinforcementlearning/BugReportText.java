package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.UnknownKeyException;
import weka.core.Attribute;
import weka.core.Instance;

public class BugReportText implements ObjectInstance, MutableState {

    public static final String TEXT = "Text";
    public static final String BR_TEXT = "BugReportText";
    
    protected String text;
    private String name;
    
    private final static List<Object> keys = Arrays.<Object>asList(TEXT);
    
    public BugReportText(String s) {
	this.text = s;
    }

    public BugReportText(String n, String t) {
	this.name = n;
	this.text = t;
    }

    public State copy() {
	return new BugReportText(text);
    }

    public Object get(Object key) {

	switch ((String) key) {
	case TEXT:
	    return text;
	default:
	    throw new UnknownKeyException(key);
	}
    }

    public List<Object> variableKeys() {	
	return keys;
    }

    public MutableState set(Object key, Object value) {
	switch ((String) key) {
	case TEXT:
	    text = (String) value;
	    break;
	default:
	    throw new UnknownKeyException(key);
	}

	return this;
    }

    @Override
    public String toString() {
	return text;
    }

    @Override
    public String className() {
	return BR_TEXT;
    }

    @Override
    public String name() {
	return this.name;
    }

    @Override
    public ObjectInstance copyWithName(String objectName) {
	return new BugReportText(objectName, text);
    }
}
