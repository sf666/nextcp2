package nextcp.handcoded;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a generated DTO field as nullable.
 *
 * Hand-written and located outside the generated "nextcp.dto" package, so it is neither
 * overwritten by the code generator nor exported as a TypeScript interface (the
 * typescript-generator only scans "nextcp.dto.*", see nextcp2-modelgen/pom.xml).
 *
 * The typescript-generator maven plugin is configured (pom.xml -&gt; optionalAnnotations)
 * to emit an optional TypeScript property for every field carrying this annotation, e.g.
 * "discogsReleaseId?: number;" which has the type "number | undefined".
 *
 * RUNTIME retention is required because the plugin reads the annotation via reflection
 * from the compiled DTO classes.
 *
 * A field is marked nullable by appending '?' to its type in dto.yaml, e.g.
 * "discogsReleaseId: Long?".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Nullable
{
}
