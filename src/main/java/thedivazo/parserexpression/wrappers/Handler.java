package thedivazo.parserexpression.wrappers;

import java.util.HashMap;
import java.util.Map;

public abstract class Handler<T> {

     public interface Method {}

     @FunctionalInterface
     public interface EmptyMethod<V> extends Method {
          V execute();
     }

     @FunctionalInterface
     public interface UnaryMethod<V, R> extends Method {
          V execute(R input);
     }
     protected final String name;
     protected final T input;

     protected Handler(String nameHandler, T input) {
          this.name = nameHandler;
          this.input = input;
     }

     protected final Map<String, Method> methods = new HashMap<>();

//     //public Object callMethod(String methodName) {
//          return methods.get(methodName).execute();
//     }
}
