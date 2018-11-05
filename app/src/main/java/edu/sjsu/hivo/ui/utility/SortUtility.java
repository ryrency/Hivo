package edu.sjsu.hivo.ui.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.hivo.ui.SortActivity;

public class SortUtility {

    private static final int PICK_SORT_REQUEST = 2;  // The request code


    private Activity mainActivity;

    public SortUtility(Activity ac){
        mainActivity = ac;
    }

    public void getSortData(ImageView sortImg,
                              TextView sortText){
        sortImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent sortIntent = new Intent(context , SortActivity.class);
                mainActivity.startActivityForResult(sortIntent,PICK_SORT_REQUEST);
            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent filterIntent = new Intent(context , SortActivity.class);
                mainActivity.startActivityForResult(filterIntent,PICK_SORT_REQUEST);
            }
        });
    }


    public String applySortData(Intent data, String extension){

        String sortOptionSelected = data.getStringExtra("SORT_OPTION");

        return sortOptionSelected;

    }

}
