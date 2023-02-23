package thedivazo.conditionhandler.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.script.*;
import java.io.Reader;
import java.io.StringReader;

public class CountPoint {

    private static final ScriptEngine JSEngine = new ScriptEngineManager().getEngineByName("graal.js");

    @Getter
    @Setter
    protected ActionType actionType;

    @Getter
    protected CompiledScript codeJS;

    public CountPoint(ActionType actionType, String codeJS) throws ScriptException {
        this.actionType = actionType;
        this.codeJS = ((Compilable)JSEngine).compile(codeJS);
    }

    public CountPoint(ActionType actionType, Reader codeJS) throws ScriptException {
        this.actionType = actionType;
        this.codeJS = ((Compilable)JSEngine).compile(codeJS);
    }


    public double execute() throws ScriptException {
        if(codeJS.eval() instanceof Double result) return result;
        else throw new ScriptException("Not valid the result. The result must be a number type <number>");
    }

}
