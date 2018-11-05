package edu.sjsu.hivo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.sjsu.hivo.R;

public class SortActivity extends AppCompatActivity {


        private ListView lvCheckBox;
        private TextView btnSortOrder;
        private Button applySort;
        private String[] sortOptions = {"Price","Beds","Baths","Square Feet"};
        int selectedItem = Integer.MIN_VALUE;
        String sortOptionSelected ="";


    @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sort);

            btnSortOrder = (TextView)findViewById(R.id.sortOrder);
            applySort = (Button)findViewById(R.id.apply_sort_button);

            lvCheckBox = (ListView)findViewById(R.id.lvCheckBox);
            lvCheckBox.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lvCheckBox.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, sortOptions));



            btnSortOrder.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {

                }
            });

            lvCheckBox.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView adapterView, View view, int position, long arg3)
                {
                    if(selectedItem == position)
                    {
                        selectedItem = Integer.MIN_VALUE;
                    }
                    else
                    {
                        selectedItem = position;

                    }
                    Toast.makeText(SortActivity.this, " selected Item"+selectedItem, Toast.LENGTH_SHORT).show();
                }
            });

            applySort.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(selectedItem == Integer.MIN_VALUE)
                            Toast.makeText(SortActivity.this, "Please select an Option", Toast.LENGTH_SHORT).show();
                        else {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("SORT_OPTION", sortOptions[selectedItem]);
                            setResult(FilterActivity.RESULT_OK, resultIntent);
                            finish();
                        }
                }
            });
        }
    }
