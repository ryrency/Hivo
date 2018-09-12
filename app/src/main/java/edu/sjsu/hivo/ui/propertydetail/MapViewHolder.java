package edu.sjsu.hivo.ui.propertydetail;

import edu.sjsu.hivo.model.ListPropertyResponse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import edu.sjsu.hivo.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapViewHolder extends PropertyViewHolder implements OnMapReadyCallback  {

    private GoogleMap googleMap;
    private MapView mapView;
    private Context context;
    private ListPropertyResponse property;
    private TextView directions;
    private  LatLng currentLocation;

    public MapViewHolder(View view, Context context) {
        super(view);
        this.context = context;
        directions = view.findViewById(R.id.directions);
        mapView = (MapView) view.findViewById(R.id.mapview);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context.getApplicationContext());
        this.googleMap = googleMap;
        updateMap();

    }


    private void updateMap() {
        currentLocation = new LatLng(property.getLatitude(), property.getLongitude());
        if (googleMap != null && property != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            //markerOptions.title("i'm here");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void bindProperty(final ListPropertyResponse property) {
        this.property = property;
        updateMap();
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo: " + property.getLatitude()+ ", " + property.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
    }
}
