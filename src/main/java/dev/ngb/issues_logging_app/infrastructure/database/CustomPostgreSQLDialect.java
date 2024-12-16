package dev.ngb.issues_logging_app.infrastructure.database;


import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        String pattern = "(?1 @@ ?2)";
        functionRegistry.registerPattern(SqlFunction.TSVECTOR_MATCH, pattern);
    }
}
