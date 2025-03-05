package ru.mephi.bakinaa.lab1;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataAnalyzer {
    private final NfsValidator validator;

    public DataAnalyzer(NfsValidator validator) {
        this.validator = validator;
    }

    public Result analyze(InputStream is) {
        Scanner scanner = new Scanner(is);

        Map<String, Integer> res = new HashMap<>();
        int invalidCnt = 0;

        while (scanner.hasNextLine()) {
            String server = validator.getServerName(scanner.nextLine());
            if (server == null)
                invalidCnt++;
            else
                incValueInMap(res, server);
        }

        return new Result(invalidCnt, res);
    }

    public void analyze(InputStream is, OutputStream os) {
        Result res = analyze(is);

        PrintStream ps = new PrintStream(os);
        ps.printf("<Invalid>: %d\n", res.invalidCnt);
        res.validCnt.forEach((serverName, cnt) -> {
            ps.printf("%s: %d\n", serverName, cnt);
        });
    }

    private void incValueInMap(Map<String, Integer> map, String key) {
        map.compute(key, (k, v) -> {
            if (v == null)
                return 1;
            return v + 1;
        });
    }

    public record Result(
            int invalidCnt,
            Map<String, Integer> validCnt
    ) {

    }
}
