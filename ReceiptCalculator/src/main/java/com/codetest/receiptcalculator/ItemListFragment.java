package com.codetest.receiptcalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.codetest.receiptcalculator.pojo.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends ListFragment implements AdapterView.OnItemLongClickListener {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public static List<PurchaseItem> purchaseList;
    PurchaseAdapter purchaseAdapter;

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchaseAdapter = new PurchaseAdapter(getActivity());
        setSampleInput(ItemListActivity.SAMPLE_INPUT_ONE);
        setListAdapter(purchaseAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemLongClickListener(this);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        openAlertDialog(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void setSampleInput(int x){
        purchaseList = new ArrayList<PurchaseItem>();
        if (x == ItemListActivity.SAMPLE_INPUT_ONE){
            String []array = getResources().getStringArray(R.array.input_one);
            for (int i = 0;i < array.length;i++){
                purchaseList.add(PurchaseItem.parseItem(array[i]));
            }
        } else if (x == ItemListActivity.SAMPLE_INPUT_TWO){
            String []array = getResources().getStringArray(R.array.input_two);
            for (int i = 0;i < array.length;i++){
                purchaseList.add(PurchaseItem.parseItem(array[i]));
            }
        } else if (x == ItemListActivity.SAMPLE_INPUT_THREE){
            String []array = getResources().getStringArray(R.array.input_three);
            for (int i = 0;i < array.length;i++){
                purchaseList.add(PurchaseItem.parseItem(array[i]));
            }
        }
        purchaseAdapter.notifyDataSetChanged();
    }

    public class PurchaseAdapter extends BaseAdapter{
        LayoutInflater inflater;
        public PurchaseAdapter(Activity activity){
            inflater = activity.getLayoutInflater();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            ((TextView)convertView).setText(purchaseList.get(position).getOriginalString());
            return convertView;
        }

        @Override
        public int getCount() {
            return purchaseList.size();
        }

        @Override
        public Object getItem(int position) {
            return purchaseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public PurchaseItem.Error addPurchaseItem(String itemString){
        PurchaseItem item = PurchaseItem.parseItem(itemString);
        if (item.getError() == PurchaseItem.Error.eNONE) {
            purchaseList.add(item);
            purchaseAdapter.notifyDataSetChanged();
        }
        return item.getError();
    }

    public void openAlertDialog() {
        openAlertDialog(-1);
    }

    private void openAlertDialog(final int index){
        View view = getActivity().getLayoutInflater().inflate(R.layout.edittext_dialog,null);
        final EditText purchaseEditText = (EditText)view.findViewById(R.id.purchaseEditText);
        if (index != -1){
            purchaseEditText.setText(purchaseList.get(index).getOriginalString());
        }
        AlertDialog.Builder dialogBuilder=  new  AlertDialog.Builder(getActivity())
            .setTitle(R.string.type_purchase)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!purchaseEditText.getText().toString().isEmpty()) {
                                if (index != -1) {
                                    PurchaseItem item = PurchaseItem.parseItem(purchaseEditText.getText().toString());
                                    if (item.getError() == PurchaseItem.Error.eNONE) {
                                        purchaseList.set(index, item);
                                    } else {
                                        setErrorDialog(item.getError());
                                    }
                                } else {
                                    PurchaseItem.Error error = addPurchaseItem(purchaseEditText.getText().toString());
                                    if (error == PurchaseItem.Error.eNONE) {
                                        return;
                                    } else {
                                        setErrorDialog(error);
                                    }
                                }
                            } else {
                                setErrorDialog(PurchaseItem.Error.eEMPTY);
                            }
                            dialog.dismiss();
                        }
                    }
            )
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }
            );
        dialogBuilder.setView(view);
        dialogBuilder.create().show();
    }

    private void setErrorDialog(PurchaseItem.Error error){
        String errorMsg = "";
        if (error == PurchaseItem.Error.eGENERAL){
            errorMsg = getString(R.string.error_general);
        } else if (error == PurchaseItem.Error.ePRICE){
            errorMsg = getString(R.string.error_price);
        } else if (error == PurchaseItem.Error.eCOUNT){
            errorMsg = getString(R.string.error_count);
        } else if (error == PurchaseItem.Error.eEMPTY){
            errorMsg = getString(R.string.error_empty);
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(errorMsg)
                .setPositiveButton(android.R.string.ok,null);
        dialogBuilder.create().show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_message)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        purchaseList.remove(position);
                        purchaseAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogBuilder.create().show();
        return false;
    }
}
