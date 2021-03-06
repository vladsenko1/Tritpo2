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
package com.amazonaws.mobile.samples.mynotes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amazonaws.mobile.samples.mynotes.Injection;
import com.amazonaws.mobile.samples.mynotes.models.ShoppingList;
import com.amazonaws.mobile.samples.mynotes.models.ResultCallback;
import com.amazonaws.mobile.samples.mynotes.repository.ShoppsRepository;

public class ShoppDetailViewModel extends ViewModel {
    private String noteId;
    private ShoppingList note;
    private MutableLiveData<String> mTitle;
    private MutableLiveData<String> mContent;
    private ShoppsRepository notesRepository;

    public ShoppDetailViewModel() {
        this.notesRepository = Injection.getNotesRepository();
        this.mTitle = new MutableLiveData<>();
        this.mContent = new MutableLiveData<>();
    }

    public void setNoteId(final String noteId) {
        this.noteId = noteId;
        notesRepository.get(noteId, (ShoppingList result) -> {
            if (result != null)
                mTitle.postValue(result.getTitle());
                mContent.postValue(result.getContent());
                this.noteId = result.getNoteId();
        });
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }

    public LiveData<String> getContent() {
        return mContent;
    }

    public synchronized void create(String title, String content, ResultCallback<ShoppingList> callback) {
        notesRepository.create(title, content, (ShoppingList result) -> {
            if (result != null) {
                noteId = result.getNoteId();
                note = result;
                callback.onResult(result);
            }
        });
    }

    public synchronized void update(String title, String content) {
        if (note == null) {
            note = new ShoppingList();
            note.setNoteId(noteId);
        }
        note.setTitle(title);
        note.setContent(content);
        notesRepository.update(note, (ShoppingList result) -> {
            /* Do Nothing */
        });
    }
}
