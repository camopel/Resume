package edu.sjsu.yduan.resume;

/**
 * Created by yduan on 3/11/2017.
 */
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.content.Context;
import java.io.Serializable;

public class RecyclerViewHolder extends ViewHolder implements View.OnClickListener{
    public TextView vOrg=null;
    public TextView vTitle=null;
    public TextView vTime=null;
    public ImageView vLogo=null;
    public Serializable mData;
    //private int key;
    public RecyclerViewHolder(View view){
        super(view);
        //key = k;
        //if(key==R.string.Time_View) {
            vOrg =(TextView)view.findViewById(R.id.item_org);
            vTitle =(TextView)view.findViewById(R.id.item_title);
            vTime=(TextView)view.findViewById(R.id.item_time);
            vLogo=(ImageView)view.findViewById(R.id.item_logo);
        //}
        view.setOnClickListener(this);
    }
    public void bind(Serializable o){
        mData=o;
        //if(key==R.string.Time_View) {
            ResumeItem edu = (ResumeItem)mData;
            Picasso.with(vLogo.getContext()).load(edu.logoUrl).into(vLogo);
            vOrg.setText(edu.org);
            vTitle.setText(edu.title);
            vTime.setText(edu.getTime());
        //}
    }
    @Override
    public void onClick(View v){
        Context context=itemView.getContext();
        Intent intent= new Intent(context,ListItemDetailActivity.class);
        String skey=context.getString(R.string.Time_View);
        intent.putExtra(skey,mData);
        context.startActivity(intent);
    }

    @Override
    public String toString(){
        return super.toString()+" "+mData.toString();
    }
}