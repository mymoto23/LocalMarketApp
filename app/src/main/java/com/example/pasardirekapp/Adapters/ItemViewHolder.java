package com.example.pasardirekapp.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CardView cardView;
    TextView itemName;
    TextView description;
    Button btnDelete;

    ItemClickListener itemClickListener;
    ItemClickListener onAnotherItemClickListener;

    ItemViewHolder(View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cardView);
        itemName = itemView.findViewById(R.id.tvName);
        description = itemView.findViewById(R.id.tvDescription);
        btnDelete = itemView.findViewById(R.id.btnDelete);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}

