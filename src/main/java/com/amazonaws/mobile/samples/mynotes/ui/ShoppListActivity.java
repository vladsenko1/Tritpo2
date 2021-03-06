/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazonaws.mobile.samples.mynotes.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.amazonaws.mobile.samples.mynotes.ShoppingListApp;
import com.amazonaws.mobile.samples.mynotes.R;
import com.amazonaws.mobile.samples.mynotes.models.ShoppingList;
import com.amazonaws.mobile.samples.mynotes.viewmodels.ShoppingListViewModel;

public class ShoppListActivity extends AppCompatActivity {
    /**
     * If the device is running in two-pane mode, then this is set to true.  In two-pane mode,
     * the UI is a side-by-side, with the list on the left and the details on the right.  In one
     * pane mode, the list and details are separate pages.
     */
    private boolean twoPane = false;

    /**
     * The view model
     */
    private ShoppingListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ShoppingListViewModel.class);
        setContentView(R.layout.activity_shoppinglist_list);
        if (findViewById(R.id.note_detail_container) != null) twoPane = true;

        // Configure the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());

        // Add an item click handler to the floating action button for adding a note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> loadNoteDetailFragment("new"));

        // Create the adapter that will be used to load items into the recycler view
        final ShoppListAdapter adapter = new ShoppListAdapter((ShoppingList item) -> loadNoteDetailFragment(item.getNoteId()));

        // Create the swipe-to-delete handler
        SwipeToDelete swipeHandler = new SwipeToDelete(this, (ShoppingList item) -> viewModel.removeNote(item.getNoteId()));
        ItemTouchHelper swipeToDelete = new ItemTouchHelper(swipeHandler);

        RecyclerView note_list = findViewById(R.id.note_list);
        swipeToDelete.attachToRecyclerView(note_list);
        note_list.setAdapter(adapter);

        // Ensure the note list is updated whenever the repository is updated
        viewModel.getNotesList().observe(this, adapter::submitList);
    }

    /**
     * Loads the note details the right way, depending on if this is two-pane mode.
     *
     * @param noteId the ID of the note to load
     */
    private void loadNoteDetailFragment(String noteId) {
        if (twoPane) {
            Fragment fragment = new ShoppDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putString(ShoppingListApp.ITEM_ID, noteId);
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ShoppDetailActivity.class);
            intent.putExtra(ShoppingListApp.ITEM_ID, noteId);
            startActivity(intent);
        }
    }
}
