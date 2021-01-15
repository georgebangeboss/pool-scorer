package com.example.pool.gui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pool.R;
import com.example.pool.action.EventAction;

import java.util.HashMap;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<EventAction> allEvents;
    private FragmentManager fm;
    private Context context;
    private HashMap<Integer, ViewHolder> holderHashMap = new HashMap<>();

    public EventAdapter(List<EventAction> allEvents, FragmentManager fm, Context context) {
        this.allEvents = allEvents;
        this.fm = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            holder.mEventTV.setText("THE KICK OFF");
        }else{
            holder.mEventTV.setText(allEvents.get(position).getActionString());
        }
        if (!holderHashMap.containsKey(position)) {
            holderHashMap.put(position, holder);
        }
        if (allEvents != null) {
            holder.mCardView.setOnClickListener(v -> {
                if (position == 0) {
                    //TODO add warning sound effect
                } else {
                    resetAllEventsBackground(position);
                    EventAction eventAction = allEvents.get(position);
                    //show the dialog box
                    EventEditFragmentDialog eventEditFragmentDialog = new EventEditFragmentDialog(eventAction, position);
                    eventEditFragmentDialog.show(fm, "Edit Event Dialog Fragment");
                }
            });
        }
    }

    private void resetAllEventsBackground(int position) {
        for (int i = 0; i < getItemCount(); i++) {
            ViewHolder vh = holderHashMap.get(i);
            if (i == position) {
                vh.mEventTV.setBackground(context.getDrawable(R.drawable.corner_curve_selected_event));
            } else {
                vh.mEventTV.setBackground(context.getDrawable(R.drawable.corner_curve_events));
            }

        }
    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mEventTV;
        private CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.event_card_view);
            mEventTV = itemView.findViewById(R.id.events_text_view);
        }
    }

}
