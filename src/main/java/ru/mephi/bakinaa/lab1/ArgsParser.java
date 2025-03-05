package ru.mephi.bakinaa.lab1;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;
import ru.mephi.bakinaa.lab1.validation.RegExValidator;
import ru.mephi.bakinaa.lab1.validation.flex.FlexValidator;
import ru.mephi.bakinaa.lab1.validation.smc.SmcValidator;

public class ArgsParser {
    public Params parseArgs(String[] args) {
        Params params = new Params();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-benchmark": {
                    params.benchmark = true;
                } break;
                case "-analyzer": {
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("Invalid usage of -analyzer flag. Use -analyzer <smc|regex|flex>");
                    String analyzerName = args[i + 1];
                    switch (analyzerName) {
                        case "smc": {
                            params.validator = new SmcValidator();
                        } break;
                        case "regex": {
                            params.validator = new RegExValidator();
                        } break;
                        case "flex": {
                            params.validator = new FlexValidator();
                        } break;
                        default: {
                            throw new IllegalArgumentException("Invalid usage of -analyzer flag. Use -analyzer <smc|regex|flex>");
                        }
                    }
                    i++;
                } break;
                case "-file": {
                    if (i + 2 >= args.length) {
                        throw new IllegalArgumentException("Invalid usage of -file flag. Use -file <source file> <target file>");
                    }
                    params.fileMode = true;
                    params.sourceFileName = args[i + 1];
                    params.targetFileName = args[i + 2];
                    i += 2;
                } break;
                default: {
                    throw new IllegalArgumentException("Invalid argument");
                }
            }
        }
        return params;
    }

    public static class Params{
        public boolean benchmark = false;
        public boolean fileMode = false;
        public String sourceFileName = null;
        public String targetFileName = null;
        public NfsValidator validator = new RegExValidator();
    }
}
