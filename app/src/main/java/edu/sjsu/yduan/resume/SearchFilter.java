package edu.sjsu.yduan.resume;
import android.widget.Filter;
import java.util.ArrayList;
public class SearchFilter extends Filter {
    RecycleViewAdapter adapter;
    public SearchFilter(RecycleViewAdapter adp){
        adapter = adp;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        if(adapter==null) return null;
        FilterResults filterResults = new FilterResults();
        if (constraint!=null && constraint.length()>0) {
            ArrayList<ResumeItem> tempList = new ArrayList<>();
            for (ResumeItem item : adapter.fullDataList) {
                if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    tempList.add(item);
                }
            }
            filterResults.count = tempList.size();
            filterResults.values = tempList;
        }
        else{
            filterResults.count = adapter.fullDataList.size();
            filterResults.values = adapter.fullDataList;
        }
        return filterResults;
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if(results!=null) {
            adapter.filtedDataList = (ArrayList<ResumeItem>) results.values;
            adapter.notifyDataSetChanged();
        }
    }
}
