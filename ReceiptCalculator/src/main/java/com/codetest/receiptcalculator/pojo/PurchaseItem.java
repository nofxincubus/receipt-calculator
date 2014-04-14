package com.codetest.receiptcalculator.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by cpark on 4/13/14.
 */
public class PurchaseItem {

    //variables
    private int count;
    private String name;
    private BigDecimal price;
    private boolean imported;
    private boolean exempt;
    private String originalString;
    private BigDecimal taxPrice;


    public enum Error {eEMPTY, eGENERAL, eCOUNT, ePRICE, eNONE};

    Error error = Error.eNONE;


    public static PurchaseItem parseItem(String originalString){
        PurchaseItem item = new PurchaseItem();
        item.originalString = originalString;
        //Empty String check
        if (item.originalString.isEmpty()){
            item.setError(Error.eEMPTY);
            return item;
        }

        //Parse string
        String []parsedArray = originalString.split(" ");

        //If length is less than three just return with general error
        if (parsedArray.length < 3){
            item.setError(Error.eGENERAL);
            return item;
        }

        //Parse item count
        try {
            item.setCount(Integer.parseInt(parsedArray[0]));
        } catch (NumberFormatException nfe){
            item.setError(Error.eCOUNT);
            return item;
        }

        //Parse item price
        try {
            item.setPrice(new BigDecimal(parsedArray[parsedArray.length - 1]));
        } catch (NumberFormatException nfe){
            item.setError(Error.ePRICE);
            return item;
        }

        //Parse at
        if (!parsedArray[parsedArray.length - 2].equalsIgnoreCase("at")) {
            item.setError(Error.eGENERAL);
            return item;
        }


        //Parse Name
        String name = "";
        for (int i = 1; i < parsedArray.length - 2; i++) {
            name = name + parsedArray[i] + " ";
        }
        name = name.trim();
        if (name.isEmpty()){
            item.setError(Error.eGENERAL);
            return item;
        }
        item.setName(name);

        ////Parse Exemption
        item.setExempt(false);
        //Books
        if (item.name.contains("book") || item.name.contains("novel") || item.name.contains("guide")){
            item.setExempt(true);
        }

        //Medical
        if (item.name.contains("pill") || item.name.contains("drug") || item.name.contains("medicine") ||
            item.name.contains("medication") || item.name.contains("prescription") ||
            item.name.contains("treatment") || item.name.contains("remedy")|| item.name.contains("bandage")){
            item.setExempt(true);
        }

        if (item.name.contains("chocolate")){
            item.setExempt(true);
        }

        //Food temporary food, hmm I'm wondering if there is better way to differentiate food
        if (item.name.contains("chocolate") || item.name.contains("food") || item.name.contains("fruits") ||
                item.name.contains("vegetables") || item.name.contains("meat")){
            item.setExempt(true);
        }


        //Parse Imported
        item.setImported(item.name.toLowerCase().contains("imported"));

        BigDecimal taxRate = BigDecimal.ONE;
        if (item.isImported()){
            taxRate = taxRate.add(BigDecimal.valueOf(0.05));
        }
        if (!item.isExempt()){
            taxRate = taxRate.add(BigDecimal.valueOf(0.10));
        }

        String taxPrice = item.getPrice().multiply(taxRate).toString();
        item.setTaxPrice(specialRound(taxPrice));

        return item;
    }

    public static BigDecimal specialRound(String taxPrice){
        if (taxPrice.indexOf(".") >= 0) {
            //4th digit less than 5
            if (Integer.parseInt(String.valueOf(taxPrice.charAt(taxPrice.indexOf(".")+2))) < 5 &&
                    Integer.parseInt(String.valueOf(taxPrice.charAt(taxPrice.indexOf(".")+2))) > 0){
                //Do the round up to 5 thing
                taxPrice = taxPrice.substring(0,taxPrice.indexOf(".")+2) + "5";
            } else { // All the other cases
                taxPrice = (new BigDecimal(taxPrice)).setScale(2, RoundingMode.CEILING).toString();
            }
        }
        return new BigDecimal(taxPrice);
    }

    public static TotalItem calculateTotalTax(List<PurchaseItem> purchaseItems){
        TotalItem totalItem = new TotalItem();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        for (PurchaseItem item:purchaseItems){
            total = total.add(item.getTaxPrice());
            tax = tax.add(item.getTaxPrice().subtract(item.getPrice()));
        }
        totalItem.setTax(tax);
        totalItem.setTotal(total);
        return totalItem;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public boolean isExempt() {
        return exempt;
    }

    public void setExempt(boolean exempt) {
        this.exempt = exempt;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }
}
