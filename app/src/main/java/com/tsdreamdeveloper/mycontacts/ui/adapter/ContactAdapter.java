/*
 * Copyright 2019 TSDream Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsdreamdeveloper.mycontacts.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tsdreamdeveloper.mycontacts.R;
import com.tsdreamdeveloper.mycontacts.mvp.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Seisembayev
 * @since 22.07.2019
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {

    /**
     * The interface that receives onClick item.
     */
    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    /**
     * New contacts list
     */
    private List<Contact> contactList;

    /**
     * New filtered contact list matching request
     */
    private List<Contact> contactListFiltered;

    public ContactAdapter() {
        this.contactList = new ArrayList<>();
        this.contactListFiltered = contactList;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dummy_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact item = contactListFiltered.get(position);
        String name = item.getName() + " " + item.getPhone();
        holder.mNameTV.setText(name);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameTV;

        ViewHolder(View view) {
            super(view);
            mNameTV = view.findViewById(R.id.tv_name);
        }
    }

    public void setItems(List<Contact> contacts) {
        this.contactList.addAll(contacts);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.contactList.clear();
        notifyDataSetChanged();
    }

    public void setFilteredItems(List<Contact> contacts) {
        this.contactListFiltered = contacts;
        notifyDataSetChanged();
    }

    public List<Contact> getFilteredItems() {
        return contactListFiltered;
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
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}