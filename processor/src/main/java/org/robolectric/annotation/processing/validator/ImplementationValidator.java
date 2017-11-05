package org.robolectric.annotation.processing.validator;

import com.google.common.collect.ImmutableSet;
import javax.lang.model.element.Name;
import org.robolectric.annotation.processing.RobolectricModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.util.Set;

/**
 * Validator that checks usages of {@link org.robolectric.annotation.Implementation}.
 */
public class ImplementationValidator extends FoundOnImplementsValidator {
  public static final Set<String> METHODS_ALLOWED_TO_BE_PUBLIC = ImmutableSet.of(
      "toString",
      "hashCode",
      "equals"
  );

  public ImplementationValidator(RobolectricModel model, ProcessingEnvironment env) {
    super(model, env, "org.robolectric.annotation.Implementation");
  }

  @Override
  public Void visitExecutable(ExecutableElement elem, TypeElement parent) {
    Set<Modifier> modifiers = elem.getModifiers();
    if (!METHODS_ALLOWED_TO_BE_PUBLIC.contains(elem.getSimpleName().toString())) {
      if (modifiers.contains(Modifier.PRIVATE)
          || modifiers.contains(Modifier.PUBLIC)
          || !modifiers.contains(Modifier.PROTECTED)) {
        message(Kind.ERROR, "@Implementation methods should be protected");
      }
    }

    // TODO: Check that it has the right signature
    return null;
  }
}
