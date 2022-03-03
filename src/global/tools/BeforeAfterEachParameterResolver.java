package global.tools;

// Adapted from https://code-case.hashnode.dev/how-to-pass-parameterized-test-parameters-to-beforeeachaftereach-method-in-junit5

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.engine.execution.BeforeEachMethodAdapter;
import org.junit.jupiter.engine.extension.ExtensionRegistry;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

public class BeforeAfterEachParameterResolver implements BeforeEachMethodAdapter, ParameterResolver {
    private ParameterResolver parameterisedTestParameterResolver = null;

    @Override
    public void invokeBeforeEachMethod(ExtensionContext context, ExtensionRegistry registry) {
        Optional<ParameterResolver> resolverOptional = registry.getExtensions(ParameterResolver.class)
                .stream()
                .filter(parameterResolver -> parameterResolver.getClass().getName().contains("ParameterizedTestParameterResolver"))
                .findFirst();
        if (resolverOptional.isEmpty()) {
            throw new IllegalStateException("ParameterizedTestParameterResolver missed in the registry. Probably it's not a Parameterized Test");
        } else {
            parameterisedTestParameterResolver = resolverOptional.get();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isExecutedOnBeforeOrAfterMethod(parameterContext)) {
            ParameterContext pContext = getMappedContext(parameterContext, extensionContext);
            return parameterisedTestParameterResolver.supportsParameter(pContext, extensionContext);
        }
        return false;
    }

    private MappedParameterContext getMappedContext(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new MappedParameterContext(
                parameterContext.getIndex(),
                extensionContext.getRequiredTestMethod().getParameters()[parameterContext.getIndex()],
                Optional.of(parameterContext.getTarget()));
    }


    private boolean isExecutedOnBeforeOrAfterMethod(ParameterContext parameterContext) {
        return Arrays.stream(parameterContext.getDeclaringExecutable().getDeclaredAnnotations())
                .anyMatch(this::isBeforeEachOrAfterEachAnnotation);
    }

    private boolean isBeforeEachOrAfterEachAnnotation(Annotation annotation) {
        return annotation.annotationType() == BeforeEach.class || annotation.annotationType() == AfterEach.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterisedTestParameterResolver.resolveParameter(getMappedContext(parameterContext, extensionContext), extensionContext);
    }
}
