package com.swahilibox.mobile.vision.qrcode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class ContactsAdapterFavshist extends RecyclerView.Adapter<ContactsAdapterFavshist.MyViewHolderf>
        implements Filterable {
    private Context context;
    private List<Contactfavshist> contactList;
    private List<Contactfavshist> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolderf extends RecyclerView.ViewHolder {
        public TextView campaign, description, location, reg_date;
        public ImageView thumbnail;


        public MyViewHolderf(View view) {
            super(view);
            campaign = view.findViewById(R.id.campaign);
            description = view.findViewById(R.id.description);
            location = view.findViewById(R.id.location);
            reg_date = view.findViewById(R.id.regdate);
            thumbnail = view.findViewById(R.id.thumbnailf);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapterFavshist(Context context, List<Contactfavshist> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }
    @SuppressWarnings("unchecked")
    @Override
    public MyViewHolderf onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item_favshist, parent, false);

        return new MyViewHolderf(itemView);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(MyViewHolderf holder, final int position) {
        final Contactfavshist contact = contactListFiltered.get(position);
        holder.campaign.setText(contact.getCampaign());
        holder.description.setText(contact.getDescription());
        holder.location.setText(contact.getLocation());
        holder.reg_date.setText(contact.getDriveDate()
        );

        Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contactfavshist> filteredList = new ArrayList<>();
                    for (Contactfavshist row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCampaign().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contactfavshist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contactfavshist contact);

    }
}
