package com.sophra.pdf_merge;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Files> items = new ArrayList<Files>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        TextView Storage;
        CheckBox item_checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.list_name);
            Storage = itemView.findViewById(R.id.list_storage);
            item_checkbox = itemView.findViewById(R.id.item_checkbox);
            item_checkbox.setChecked(false);

            item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.v("testwe", "" + getAdapterPosition() + b);

                    if(MainActivity.ischecked[getAdapterPosition()] == false)
                    {
                        MainActivity.ischecked[getAdapterPosition()] = true;
                        MainActivity.mergeList.add(MainActivity.pdfList.get(getAdapterPosition()));
                        Log.v("testwe", "머지리스트 : " + MainActivity.mergeList);
                    }
                    else
                    {
                        MainActivity.ischecked[getAdapterPosition()] = false;
                        //Log.v("testwe", "실행함" + MainActivity.mergeList.get(getAdapterPosition()));

                        Log.v("testwe", "머지리스트 : " + MainActivity.mergeList);
                        
                        //여기 수정해야함
                        for(int j = 0; j < MainActivity.mergeList.size(); j++){

                            if(MainActivity.mergeList.get(j).equals(MainActivity.pdfList.get(getAdapterPosition())))
                            {
                                MainActivity.mergeList.remove(j);
                                Log.v("testwe", "실행함");
                                Log.v("testwe", "머지리스트 : " + MainActivity.mergeList);
                            }
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
