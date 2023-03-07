package thedivazo.parserexpression.interpreter.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface WrapperObject<T1> {
    T1 getObject();

    Set<WrapperMethod<?>> getMethods();
    boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType);
    boolean hasMethod(String nameMethod, Collection<?> methodArguments);

    @Nullable
    Object executeMethod(String nameMethod, Collection<?> methodArguments);
}
