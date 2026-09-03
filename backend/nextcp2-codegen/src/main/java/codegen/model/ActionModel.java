package codegen.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * One action of a service. Arguments are held by name and sorted, so a regenerated file only
 * differs when something was actually added.
 */
public class ActionModel {

	private final String name;
	private final Map<String, VariableModel> input = new TreeMap<>();
	private final Map<String, VariableModel> output = new TreeMap<>();

	public ActionModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<String, VariableModel> getInput() {
		return input;
	}

	public Map<String, VariableModel> getOutput() {
		return output;
	}

	public List<VariableModel> getInputList() {
		return List.copyOf(input.values());
	}

	public List<VariableModel> getOutputList() {
		return List.copyOf(output.values());
	}

	public void add(boolean isInput, VariableModel variable) {
		merge(isInput ? input : output, variable);
	}

	/**
	 * Adds everything the other model knows. Nothing is ever removed: a device that supports less
	 * than the one seen before must not shrink what the generated code can do.
	 *
	 * @return names of arguments whose java type disagrees; the type already stored wins.
	 */
	public List<String> mergeFrom(ActionModel other) {
		List<String> conflicts = new java.util.ArrayList<>();
		conflicts.addAll(mergeAll(input, other.input, "input"));
		conflicts.addAll(mergeAll(output, other.output, "output"));
		return conflicts;
	}

	private List<String> mergeAll(Map<String, VariableModel> target, Map<String, VariableModel> source, String kind) {
		List<String> conflicts = new java.util.ArrayList<>();
		for (VariableModel variable : source.values()) {
			VariableModel known = target.get(variable.getName());
			if (known != null && !known.getJavaType().equals(variable.getJavaType())) {
				conflicts.add(String.format("%s.%s %s: %s <> %s", name, variable.getName(), kind, known.getJavaType(),
					variable.getJavaType()));
			}
			merge(target, variable);
		}
		return conflicts;
	}

	private static void merge(Map<String, VariableModel> target, VariableModel variable) {
		VariableModel known = target.get(variable.getName());
		target.put(variable.getName(), known == null ? variable : known.mergedWith(variable));
	}
}
