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


public class ContactsAdapterFavs extends RecyclerView.Adapter<ContactsAdapterFavs.MyViewHolderf>
        implements Filterable {
    private Context context;
    private List<Contactfavs> contactList;
    private List<Contactfavs> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolderf extends RecyclerView.ViewHolder {
        public TextView donor_id, donor_name, reg_date;
        public ImageView thumbnail;


        public MyViewHolderf(View view) {
            super(view);
            donor_name = view.findViewById(R.id.donor_name);
            donor_id = view.findViewById(R.id.donor_id);
            reg_date = view.findViewById(R.id.phoneno);
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


    public ContactsAdapterFavs(Context context, List<Contactfavs> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }
    @SuppressWarnings("unchecked")
    @Override
    public MyViewHolderf onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item_favs, parent, false);

        return new MyViewHolderf(itemView);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(MyViewHolderf holder, final int position) {
        final Contactfavs contact = contactListFiltered.get(position);
        holder.donor_id.setText(contact.getDonor_id());
        holder.donor_name.setText(contact.getDonor_name());
        holder.reg_date.setText(contact.getPhoneno()+" | "+contact.getReg_date());

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
                    List<Contactfavs> filteredList = new ArrayList<>();
                    for (Contactfavs row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDonor_name().toLowerCase().contains(charString.toLowerCase()) || row.getDonor_id().contains(charSequence)) {
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
                contactListFiltered = (ArrayList<Contactfavs>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contactfavs contact);

    }
}
