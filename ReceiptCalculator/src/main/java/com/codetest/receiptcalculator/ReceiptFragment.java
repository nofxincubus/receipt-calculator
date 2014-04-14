package com.codetest.receiptcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codetest.receiptcalculator.pojo.PurchaseItem;
import com.codetest.receiptcalculator.pojo.TotalItem;

public class ReceiptFragment extends Fragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    ReceiptAdapter receiptAdapter;
    TotalItem totalItem;

    public ReceiptFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiptAdapter = new ReceiptAdapter(getActivity());
        totalItem = PurchaseItem.calculateTotalTax(ItemListFragment.purchaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        ListView listView = (ListView) view.findViewById(R.id.receiptListView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        TextView taxTextView = (TextView) view.findViewById(R.id.taxTextView);
        TextView totalTextView = (TextView) view.findViewById(R.id.totalTextView);
        taxTextView.setText("Sales Taxes: " + totalItem.getTax().toString());
        totalTextView.setText("Total: " + totalItem.getTotal().toString());
        listView.setAdapter(receiptAdapter);
        return view;
    }

    public class ReceiptAdapter extends BaseAdapter{
        LayoutInflater inflater;
        public ReceiptAdapter(Activity activity){
            inflater = activity.getLayoutInflater();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            PurchaseItem item = ItemListFragment.purchaseList.get(position);
            ((TextView)convertView).setText(item.getCount() + " " + item.getName() + ": " + item.getTaxPrice().toString());
            return convertView;
        }

        @Override
        public int getCount() {
            return ItemListFragment.purchaseList.size();
        }

        @Override
        public Object getItem(int position) {
            return ItemListFragment.purchaseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
