package dev.ngb.issues_logging_app.infrastructure.database;


import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class FullTextSearchPostgreSQLDialect extends PostgreSQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        functionRegistry.registerPattern(SqlFunction.TSVECTOR_MATCH, SqlFunction.TSVECTOR_MATCH_PATTERN);
    }
}
