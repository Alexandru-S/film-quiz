package com.glassbyte.film_quiz2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Alex on 28/11/15.
 */

/**
 * My Sql Adapter Java Class intended
 *  to help the MySqlDB class
 *  populate a list within
 *  a layout
 */
public class MySqlDBHelper extends RecyclerView.Adapter<MySqlDBHelper.StockViewHolder>{


    List<MySqlDBList> stocks;
    public MySqlDBHelper(List<MySqlDBList> stocks){
        this.stocks = stocks;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        StockViewHolder pvh = new StockViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position)
    {
        holder.stockName.setText(stocks.get(position).getName());
        holder.stockPrice.setText(stocks.get(position).getPrice());
        Ion.with(holder.stockPhoto).error(R.mipmap.ic_launcher).load(stocks.get(position).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView stockName;
        TextView stockPrice;
        ImageView stockPhoto;

        StockViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            stockName = (TextView)itemView.findViewById(R.id.person_name);
            stockPrice = (TextView)itemView.findViewById(R.id.person_age);
            stockPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }
}