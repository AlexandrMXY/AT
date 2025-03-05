package ru.mephi.bakinaa.lab1.validation.smc;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;

public class SmcValidator implements NfsValidator {
    @Override
    public String getServerName(String string) {
        if (string == null)
            return null;

        var context = new SmcNfsValidationFSMContext();
        var sm = new SmcNfsValidationFSM(context);
        // TODO проверить проверку длинны
        for (char c : string.toCharArray())
            sm.nextChar(c);
        sm.end();

        if (sm.getState().getId() == SmcNfsValidationFSM.ValidatorMap_Invalid_STATE_ID)
            return null;
        if (context.namesCnt < 2)
            return null;
        return context.serverName;
    }
}
