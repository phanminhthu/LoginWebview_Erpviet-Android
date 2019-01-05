package vn.izisolution.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.izisolution.R;
import vn.izisolution.views.FontTextView;

/**
 * Created by ToanNMDev on 3/16/2018.
 */

public class MasterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<? extends Object> data;
    private Type type;

    public MasterAdapter(Context context, ArrayList<? extends Object> data, Type type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    public enum Type {
        Type_1(0),
        Type_2(1);

        private int type;
        private Type(int type){
            this.type = type;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (type == Type.Type_1){
            final ViewHolder_1 holder = (ViewHolder_1) viewHolder;
            final String item = (String)data.get(position);
            holder.title.setText(item);
        } else {
            final ViewHolder_2 holder = (ViewHolder_2) viewHolder;
            int resource = (Integer) data.get(position);
            holder.imageView.setImageResource(resource);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return type == Type.Type_1
                ? new ViewHolder_1(LayoutInflater.from(context).inflate(R.layout.item_1, parent, false))
                : new ViewHolder_2(LayoutInflater.from(context).inflate(R.layout.item_2, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ViewHolder_1 extends RecyclerView.ViewHolder{

        private FontTextView title;

        public ViewHolder_1(View itemView) {
            super(itemView);

            title = (FontTextView) itemView.findViewById(R.id.title);
        }
    }

    private class ViewHolder_2 extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ViewHolder_2(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
