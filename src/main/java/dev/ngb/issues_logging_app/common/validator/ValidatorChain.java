package dev.ngb.issues_logging_app.common.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ValidatorChain<T> {

    @Getter
    private final Map<String, List<String>> errors = new HashMap<>();
    private final List<Rule<T>> rules = new ArrayList<>();
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected ValidatorChain() {
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * Returns the class that this validator supports.
     *
     * @return the class that this validator supports
     */
    public Class<T> support() {
        return type;
    }

    /**
     * Creates a new RuleBuilder for a specific field.
     *
     * @param fieldName the name of the field to validate
     * @param extractor a function to extract the field value from the object
     * @return a new RuleBuilder instance
     */
    protected <F> RuleBuilder<T, F> ruleFor(String fieldName, Function<T, F> extractor) {
        return new RuleBuilder<>(fieldName, extractor, this);
    }

    /**
     * Adds a global rule to the list of rules.
     *
     * @param predicate    the predicate to test
     * @param errorMessage the error message to display
     */
    protected void ruleFor(String fieldName, Predicate<T> predicate, String errorMessage) {
        rules.add(new Rule<>(fieldName, predicate, errorMessage));
    }

    private Map<String, List<String>> validate(T object) {
        errors.clear();
        rules.forEach(rule -> {
            if (!rule.predicate.test(object)) {
                errors.computeIfAbsent(rule.fieldName, k -> new ArrayList<>()).add(rule.errorMessage);
            }
        });
        return errors;
    }

    /**
     * Checks if the given object is valid.
     *
     * @param object the object to check
     * @return true if the object is valid, false otherwise
     */
    public boolean isNotValid(T object) {
        return !validate(object).isEmpty();
    }

    /**
     * Adds a new rule to the list of rules.
     *
     * @param rule the rule to add
     */
    private void addRule(Rule<T> rule) {
        rules.add(rule);
    }

    @RequiredArgsConstructor
    protected static class RuleBuilder<T, F> {
        private final String fieldName;
        private final Function<T, F> extractor;
        private final ValidatorChain<T> validator;

        public RuleBuilder<T, F> require(Predicate<F> predicate, String errorMessage) {
            validator.addRule(new Rule<>(
                    fieldName,
                    object -> predicate.test(extractor.apply(object)),
                    errorMessage
            ));
            return this;
        }
    }

    private record Rule<T>(String fieldName, Predicate<T> predicate, String errorMessage) {
    }
}