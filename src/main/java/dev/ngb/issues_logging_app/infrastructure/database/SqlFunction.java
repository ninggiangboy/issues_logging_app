package dev.ngb.issues_logging_app.infrastructure.database;

public class SqlFunction {
    public static final String TSVECTOR_MATCH = "tsvector_match";
    public static final String TSVECTOR_MATCH_PATTERN = "to_tsvector(?1) @@ plainto_tsquery(?2)";
}
