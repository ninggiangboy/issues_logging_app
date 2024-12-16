package dev.ngb.issues_logging_app.application.validator;

import dev.ngb.issues_logging_app.application.dto.auth.LoginRequest;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidatorFactory {

    private final Map<Class<?>, ValidatorChain<?>> validators = new HashMap<>();

    public ValidatorFactory(List<ValidatorChain<?>> validatorChains) {
        for (ValidatorChain<?> validatorChain : validatorChains) {
            validators.put(validatorChain.support(), validatorChain);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ValidatorChain<T> getValidator(Class<T> clazz) {
        ValidatorChain<?> validator = validators.get(clazz);
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for class: " + clazz.getName());
        }
        return (ValidatorChain<T>) validator;
    }
}
