package com.phonecompany.billing;

/**
 *
 * @author ourie
 */
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TelephoneBillCalculatorImplTest {

    private final TelephoneBillCalculator calculator = new TelephoneBillCalculatorImpl();

    @Test
    public void testCalculate() { 
        String phoneLog = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n" +
                "420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00";
        BigDecimal result = calculator.calculate(phoneLog);
        assertEquals(new BigDecimal("1.5"), result);
    }

    @Test
    public void testFreeNumber() { 
        String phoneLog = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n" +
                "420774577453,14-01-2020 08:59:20,14-01-2020 09:10:00\n" +
                "420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00";
        BigDecimal result = calculator.calculate(phoneLog);
        assertEquals(new BigDecimal("6.2"), result);
    }
}
