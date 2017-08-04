package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.UnknownKeyException;
import weka.core.Instance;

public class BugReportTerm implements ObjectInstance, MutableState {

	public static final String TERM = "Term";
	public static final String INSTANCE = "Instance";
	public static final String BR_TERM = "BugReportTerm";

	private String name; // term name
	private final static List<Object> keys = Arrays.<Object> asList(TERM);
	
	private Instance instance;

	public BugReportTerm(String n, Instance i) {
		this.name = n;
		this.instance = i;
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object key) {
		switch ((String) key) {
		case TERM:
			return name;
		case INSTANCE:
			return instance;
		default:
			throw new UnknownKeyException(key);
		}
	}

	@Override
	public MutableState set(Object key, Object value) {
		switch ((String) key) {
		case TERM:
			this.name = (String) value;
			break;
		case INSTANCE:
			this.instance = (Instance) value;
			break;
		default:
			throw new UnknownKeyException(key);
		}
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
	public ObjectInstance copyWithName(String termName) {
		return new BugReportTerm(termName, instance);
	}

	@Override
	public State copy() {
		return new BugReportTerm(name, instance);
	}
	
	@Override
	public String toString() {
		return (String)this.get(TERM);
	}
}
