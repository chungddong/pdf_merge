package com.sophra.pdf_merge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MergelistAdapter extends RecyclerView.Adapter<MergelistAdapter.ViewHolder> {

    private ArrayList<Files> items = new ArrayList<Files>();

    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public MergelistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_second, parent, false);
        return new MergelistAdapter.ViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    public void setFileList(ArrayList<Files> list){
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Storage;
        Button btn_delete;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            Name = itemView.findViewById(R.id.list_name);
            Storage = itemView.findViewById(R.id.list_storage);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }

        void onBind(Files files)
        {
            Name.setText(files.getName());
            Storage.setText(files.getStorage());
        }
    }
}
