package edu.sjsu.yduan.resume;

import android.util.Log;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class ResumeItem implements Serializable{
    public String org;
    public String title;
    //public String minor;
    //public String gpa;
    public String desc;
    public String beg;
    public String end;
    public String logoUrl;
    public String address;
    ResumeItem(JSONObject jo){
        String TAG = ResumeItem.class.getSimpleName();
        try{
            org = jo.getString("org");
            title = jo.getString("title");
            //minor = jo.getString("minor");
            //gpa = jo.getString("gpa");
            desc = jo.getString("desc");
            logoUrl = jo.getString("image");
            beg = jo.getString("beg");
            end = jo.getString("end");
            address = jo.getString("addr");
        }catch (JSONException e) {
            Log.d(TAG, "on JSON Failure:"+e.getMessage());
        }
    }
    @Override
    public String toString() {
        return org+","+title+","+desc;
    }
    public String getTime(){
        return beg+"-"+end;
    }
}