package com.muradit.projectx.Model.others;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagesNumber {
    private DatabaseReference databaseReference;
    private MutableLiveData<Integer>numberOfMessages;
    private MutableLiveData<Integer>numberOfMessagesForRoom;

    public MessagesNumber() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Messages");
        numberOfMessages=new MutableLiveData<>();
        numberOfMessagesForRoom=new MutableLiveData<>();
    }

    public void getMessages(final String ID){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int n=0;
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("receiverId").getValue().toString().equals(ID)){
                        if(key.child("status").getValue().toString().equals("unseen"))
                            n++;
                    }
                }
                numberOfMessages.setValue(n);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getMessagesForRoom(final String roomId, final String ID){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int n=0;
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("roomId").getValue().toString().equals(roomId)) {
                        if (key.child("receiverId").getValue().toString().equals(ID)) {
                            if (key.child("status").getValue().toString().equals("unseen"))
                                n++;
                        }
                    }
                }
                numberOfMessagesForRoom.setValue(n);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<Integer> getNumberOfMessages(){
        return numberOfMessages;
    }
    public LiveData<Integer> getNumberOfMessagesForRoom(){
        return numberOfMessagesForRoom;
    }
}
