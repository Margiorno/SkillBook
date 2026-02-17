package com.mz.identity.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handle(ExpiredGoogleTokenException ex, DataFetchingEnvironment env) {
        log.warn("Exception: {}", ex.getMessage());
        return buildError(ex, env, ErrorType.UNAUTHORIZED);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(InvalidGoogleTokenException ex, DataFetchingEnvironment env) {
        log.warn("Exception: {}", ex.getMessage());
        return buildError(ex, env, ErrorType.UNAUTHORIZED);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(UnsupportedLoginMethodException ex, DataFetchingEnvironment env) {
        log.warn("Exception: {}", ex.getMessage());
        return buildError(ex, env, ErrorType.BAD_REQUEST);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGenericException(Exception ex, DataFetchingEnvironment env) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return buildError(ex, env, ErrorType.INTERNAL_ERROR);
    }

    private GraphQLError buildError(Throwable ex, DataFetchingEnvironment env, ErrorType errorType) {
        return buildError(ex, env, errorType, Collections.emptyMap());
    }

    private GraphQLError buildError(Throwable ex, DataFetchingEnvironment env, ErrorType errorType, Map<String, Object> additionalExtensions) {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("errorCode", ex.getClass().getSimpleName());
        extensions.putAll(additionalExtensions);

        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .errorType(errorType)
                .extensions(extensions)
                .build();
    }
}
