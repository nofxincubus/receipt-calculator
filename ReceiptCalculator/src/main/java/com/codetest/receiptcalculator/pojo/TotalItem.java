package com.codetest.receiptcalculator.pojo;

import java.math.BigDecimal;

/**
 * Created by cpark on 4/14/14.
 */
public class TotalItem {
    private BigDecimal tax;
    private BigDecimal total;

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
