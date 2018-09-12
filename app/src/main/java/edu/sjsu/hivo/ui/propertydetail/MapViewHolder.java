package edu.sjsu.hivo.ui.propertydetail;

import edu.sjsu.hivo.model.ListPropertyResponse;
import edu.sjsu.hivo.ui.propertydetail.PropertyViewHolder;

import android.support.v4.app.Fragment;
import android.view.View;
import edu.sjsu.hivo.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapViewHolder extends PropertyViewHolder  {

    //private MapView mapView;
    private SupportMapFragment mapFragment;

    public MapViewHolder(View view) {
        super(view);
        //mapFragment = (SupportMapFragment)view.findViewById(R.id.map);

    }

    @Override
    public void bindProperty(final ListPropertyResponse property) {
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                LatLng latLng = new LatLng(Double.valueOf(property.getLatitude()), Double.valueOf(property.getLongitude()));
//                googleMap.addMarker(new MarkerOptions().position(latLng)
//                        .title("Singapore"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            }
//        });
//    }
//

    }
}
