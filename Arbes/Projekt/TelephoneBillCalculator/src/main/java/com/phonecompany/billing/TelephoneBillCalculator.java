package com.phonecompany.billing;

/**
 *
 * @author ourie
 */
import java.math.BigDecimal;

public interface TelephoneBillCalculator {
    BigDecimal calculate(String phoneLog);
}
