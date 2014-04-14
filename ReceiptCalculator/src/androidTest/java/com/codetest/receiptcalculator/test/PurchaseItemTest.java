package com.codetest.receiptcalculator.test;

import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.codetest.receiptcalculator.ItemListActivity;
import com.codetest.receiptcalculator.pojo.PurchaseItem;
import com.codetest.receiptcalculator.pojo.TotalItem;

import java.util.ArrayList;
import java.util.List;


public class PurchaseItemTest extends ActivityUnitTestCase<ItemListActivity> {

    public PurchaseItemTest(){
        super(ItemListActivity.class);
    }

    @MediumTest
    public void testParserHappy() throws Exception {

        String testInput = "1 book at 12.49";
        PurchaseItem item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "12.49");
        assertEquals(item.getPrice().floatValue(), 12.49f);
        assertEquals(item.getName(), "book");
        assertFalse(item.isImported());
        assertTrue(item.isExempt());

        testInput = "1 music CD at 14.99";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "14.99");
        assertEquals(item.getPrice().floatValue(), 14.99f);
        assertEquals(item.getName(), "music CD");
        assertFalse(item.isImported());
        assertFalse(item.isExempt());

        testInput = "1 chocolate bar at 0.85";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "0.85");
        assertEquals(item.getPrice().floatValue(), 0.85f);
        assertEquals(item.getName(), "chocolate bar");
        assertFalse(item.isImported());
        assertTrue(item.isExempt());

        testInput = "1 imported box of chocolates at 10.00";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "10.00");
        assertEquals(item.getPrice().floatValue(), 10f);
        assertEquals(item.getName(), "imported box of chocolates");
        assertTrue(item.isImported());
        assertTrue(item.isExempt());

        testInput = "1 imported bottle of perfume at 47.50";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "47.50");
        assertEquals(item.getPrice().floatValue(), 47.50f);
        assertEquals(item.getName(), "imported bottle of perfume");
        assertTrue(item.isImported());
        assertFalse(item.isExempt());

        testInput = "1 imported bottle of perfume at 27.99";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "27.99");
        assertEquals(item.getPrice().floatValue(), 27.99f);
        assertEquals(item.getName(), "imported bottle of perfume");
        assertTrue(item.isImported());
        assertFalse(item.isExempt());

        testInput = "1 bottle of perfume at 18.99";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "18.99");
        assertEquals(item.getPrice().floatValue(), 18.99f);
        assertEquals(item.getName(), "bottle of perfume");
        assertFalse(item.isImported());
        assertFalse(item.isExempt());

        testInput = "1 packet of headache pills at 9.75";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "9.75");
        assertEquals(item.getPrice().floatValue(), 9.75f);
        assertEquals(item.getName(), "packet of headache pills");
        assertFalse(item.isImported());
        assertTrue(item.isExempt());

        testInput = "1 box of imported chocolates at 11.25";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getCount(), 1);
        assertEquals(item.getPrice().toString(), "11.25");
        assertEquals(item.getPrice().floatValue(), 11.25f);
        assertEquals(item.getName(), "box of imported chocolates");
        assertTrue(item.isImported());
        assertTrue(item.isExempt());
    }

    @SmallTest
    public void testParserUnhappy() throws Exception {
        String testInput = "";
        PurchaseItem item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getError(), PurchaseItem.Error.eEMPTY);

        testInput = "music CD at 14.99";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getError(), PurchaseItem.Error.eCOUNT);

        testInput = "1 music CD 14.99";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getError(), PurchaseItem.Error.eGENERAL);

        testInput = "1 music CD at ";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getError(), PurchaseItem.Error.ePRICE);

        testInput = "1 asldfkjasdlkfjlsdkjf asldkfjasldjf asdf asdjfl asdkfj asf sad";
        item = PurchaseItem.parseItem(testInput);
        assertEquals(item.getError(), PurchaseItem.Error.ePRICE);
    }

    @SmallTest
    public void testRounding() throws Exception {
        String testStr = "54.625";
        assertEquals(54.65f, PurchaseItem.specialRound(testStr).floatValue());
        testStr = "32.1885";
        assertEquals(32.19f, PurchaseItem.specialRound(testStr).floatValue());
        testStr = "20.889";
        assertEquals(20.89f, PurchaseItem.specialRound(testStr).floatValue());
        testStr = "11.8125";
        assertEquals(11.85f, PurchaseItem.specialRound(testStr).floatValue());
    }

    @SmallTest
    public void testTaxTotal() throws Exception {
        List<PurchaseItem> items = new ArrayList<PurchaseItem>();
        items.add(PurchaseItem.parseItem("1 book at 12.49"));
        items.add(PurchaseItem.parseItem("1 music CD at 14.99"));
        items.add(PurchaseItem.parseItem("1 chocolate bar at 0.85"));
        TotalItem item = PurchaseItem.calculateTotalTax(items);
        assertEquals(item.getTotal().floatValue(), 29.83f);
        assertEquals(item.getTax().floatValue(), 1.50f);

        items.clear();
        items.add(PurchaseItem.parseItem("1 imported box of chocolates at 10.00"));
        items.add(PurchaseItem.parseItem("1 imported bottle of perfume at 47.50"));
        item = PurchaseItem.calculateTotalTax(items);
        assertEquals(item.getTotal().floatValue(), 65.15f);
        assertEquals(item.getTax().floatValue(), 7.65f);

        items.clear();
        items.add(PurchaseItem.parseItem("1 imported bottle of perfume at 27.99"));
        items.add(PurchaseItem.parseItem("1 bottle of perfume at 18.99"));
        items.add(PurchaseItem.parseItem("1 packet of headache pills at 9.75"));
        items.add(PurchaseItem.parseItem("1 box of imported chocolates at 11.25"));
        item = PurchaseItem.calculateTotalTax(items);
        assertEquals(item.getTotal().floatValue(), 74.68f);
        assertEquals(item.getTax().floatValue(), 6.70f);
    }

}
