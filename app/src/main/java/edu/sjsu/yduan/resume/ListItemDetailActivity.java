package edu.sjsu.yduan.resume;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.net.Uri;
import android.content.Intent;
public class ListItemDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ResumeItem mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem_detail);
        //ActionBar ab = getSupportActionBar();
        String key = this.getString(R.string.Time_View);
        mData = (ResumeItem) getIntent().getSerializableExtra(key);

        ImageView vLogo = (ImageView)findViewById(R.id.detail_logo);
        Picasso.with(this).load(mData.logoUrl).into(vLogo);
        //ab.setTitle(mData.school);
        TextView vSchool = (TextView)findViewById(R.id.detail_org);
        vSchool.setText(mData.org);
        TextView vTime = (TextView)findViewById(R.id.detail_time);
        vTime.setText(mData.getTime());
        TextView vMajor = (TextView)findViewById(R.id.detail_title);
        vMajor.setText(mData.title);
        TextView vMinor = (TextView)findViewById(R.id.detail_desc);
        vMinor.setText(mData.desc);
        TextView vAddress = (TextView)findViewById(R.id.detail_addr);
        vAddress.setText(mData.address);
        ImageView vNext = (ImageView)findViewById(R.id.detail_next);
        vNext.setImageResource(R.drawable.ic_action_next);
        vAddress.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+mData.address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
    /*Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
 try {
        List<Address> address = geoCoder.getFromLocationName(locationName, 1);
        double latitude = address.get(0).getLatitude();
        double longitude = address.get(0).getLongitude();
    } catch (IOException e) {
        e.printStackTrace();
    }*/
}
