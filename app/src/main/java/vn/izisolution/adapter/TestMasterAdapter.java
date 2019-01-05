package vn.izisolution.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ToanNMDev on 3/16/2018.
 */

public abstract class TestMasterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public abstract void setType();

    public class TestMasterAdapterChild extends TestMasterAdapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public void setType() {

        }
    }

}
