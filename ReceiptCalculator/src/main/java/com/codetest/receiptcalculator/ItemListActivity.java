package com.codetest.receiptcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ItemListActivity extends FragmentActivity {

    public static int SAMPLE_INPUT_ONE = 1;
    public static int SAMPLE_INPUT_TWO = 2;
    public static int SAMPLE_INPUT_THREE = 3;

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
            ReceiptFragment receiptFragment = new ReceiptFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(receiptFragment,receiptFragment.getTag())
                    .commit();

        } else {
            ((Button)findViewById(R.id.calculateReceipt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ItemListActivity.this,ItemDetailActivity.class);
                    startActivity(intent);
                }
            });
            ((Button)findViewById(R.id.calculateReceipt)).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(ItemListActivity.this, getString(R.string.calculate_receipt), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    //Menu creation for adding new items and testing inputs
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Main menu controls
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_item:
                ((ItemListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list)).openAlertDialog();
                break;
            case R.id.action_input_one:
                ((ItemListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list)).setSampleInput(SAMPLE_INPUT_ONE);
                break;
            case R.id.action_input_two:
                ((ItemListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list)).setSampleInput(SAMPLE_INPUT_TWO);
                break;
            case R.id.action_input_three:
                ((ItemListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list)).setSampleInput(SAMPLE_INPUT_THREE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
