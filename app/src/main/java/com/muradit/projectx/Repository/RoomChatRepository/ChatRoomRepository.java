package com.muradit.projectx.Repository.RoomChatRepository;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.ChatRoom;
import com.muradit.projectx.Model.Messages;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentStoreChat;
import com.muradit.projectx.Model.others.CurrentStoreChatStatic;
import com.muradit.projectx.Model.others.CurrentUserChat;
import com.muradit.projectx.Model.others.CurrentUserChatStatic;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.MessageNeeds;
import com.muradit.projectx.Model.others.NotificationsSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<MessageNeeds> messageNeeds;
    private MutableLiveData<List<Messages>> messages;
    private MutableLiveData<List<ChatRoom>> rooms;
    private MutableLiveData<Boolean>created;
    private Context context;

    public ChatRoomRepository(Context context) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        this.context = context;
        messageNeeds=new MutableLiveData<>();
        messages=new MutableLiveData<>();
        rooms=new MutableLiveData<>();
        created=new MutableLiveData<>();
    }



    public void createRoom(){
        final ChatRoom room=new ChatRoom();
        room.setCustomerId(CurrentUserInfo.id);
        room.setStoreId(CurrentStore.id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag=false;
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("storeId").getValue().toString().equals(CurrentStore.id)
                    && key.child("customerId").getValue().toString().equals(CurrentUserInfo.id)){
                        CurrentChatRoom.roomId=key.getKey();
                        CurrentChatRoom.storeId=key.child("storeId").getValue().toString();
                        CurrentChatRoom.userId=key.child("customerId").getValue().toString();
                        MessageNeeds mn=new MessageNeeds();
                        mn.setExist(true);
                        mn.setRoomId(key.getKey());
                        messageNeeds.setValue(mn);

                       flag=true;
                       break;
                    }
                }
                if(!flag){

                    String key=databaseReference.push().getKey();
                    databaseReference.child(key).setValue(room)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Room created", Toast.LENGTH_SHORT).show();

                                }
                            });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void checkIfRoomExist(){

         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 for(DataSnapshot key:dataSnapshot.getChildren()) {
                     if(key.child("storeId").getValue().toString().equals(CurrentStore.id)
                             && key.child("customerId").getValue().toString().equals(CurrentUserInfo.id)) {
                         CurrentChatRoom.roomId=key.getKey();
                         CurrentChatRoom.storeId=key.child("storeId").getValue().toString();
                         CurrentChatRoom.userId=key.child("customerId").getValue().toString();
                         MessageNeeds mn=new MessageNeeds();
                         mn.setExist(true);
                         mn.setRoomId(key.getKey());
                         messageNeeds.setValue(mn);
                         break;
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
    public void checkIfRoomExistStore(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot key:dataSnapshot.getChildren()) {
                    if(key.getKey().equals(CurrentChatRoom.roomId)) {
                        CurrentChatRoom.roomId=key.getKey();
                        CurrentChatRoom.storeId=key.child("storeId").getValue().toString();
                        CurrentChatRoom.userId=key.child("customerId").getValue().toString();
                        MessageNeeds mn=new MessageNeeds();
                        mn.setExist(true);
                        mn.setRoomId(key.getKey());
                        messageNeeds.setValue(mn);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void checkIfRoomExistCust(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot key:dataSnapshot.getChildren()) {
                    if((key.getKey().equals(CurrentChatRoom.roomId))) {
                        CurrentChatRoom.roomId=key.getKey();
                        CurrentChatRoom.storeId=key.child("storeId").getValue().toString();
                        CurrentChatRoom.userId=key.child("customerId").getValue().toString();
                        MessageNeeds mn=new MessageNeeds();
                        mn.setExist(true);
                        mn.setRoomId(key.getKey());
                        messageNeeds.setValue(mn);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void sendMessage(String content,  String key) {
        String senderId,receiverId;
        final String senderName,receiverName,senderEmail,receiverEmail;

        if(Flags.Customer_flag){
            senderId=CurrentUserInfo.id;
            senderName=CurrentUserInfo.name;
            senderEmail=CurrentUserInfo.email;
            if(Flags.Flag_Room) {
                receiverId=CurrentStoreChatStatic.id;
                receiverName=CurrentStoreChatStatic.name;
                receiverEmail=CurrentStoreChatStatic.ownerEmail;
            }else{
                receiverId = CurrentStore.id;
                receiverName = CurrentStore.name;
                receiverEmail=CurrentStore.ownerEmail;
            }
        }else{
            senderId=CurrentStore.id;
            senderName=CurrentStore.name;
            receiverId=CurrentUserChatStatic.id;
            receiverName=CurrentUserChatStatic.name;
            senderEmail=CurrentStore.email;
            receiverEmail=CurrentUserChatStatic.email;
        }



        /*
        if(Flags.Flag_Room) {
           receiverId=CurrentStoreChatStatic.id;
           receiverName=CurrentStoreChatStatic.name;
        }else{
                receiverId = CurrentStore.id;
                receiverName = CurrentStore.name;
        }
*/




        final Messages messages=new Messages(senderName,receiverName,content,"2020/7/7",key,senderId,receiverId,"unseen");

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Messages");
         final String keyMessage="ms-"+databaseReference.push().getKey()+(int)(Math.random()*1000);

         databaseReference.child(keyMessage).setValue(messages)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              sendNotification(keyMessage,receiverEmail,senderName);
                          }
                      }, 2000);
                  }
              });

    }

    private void getAllMessages(){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Messages> messagesList=new ArrayList<>();
                for (DataSnapshot node:dataSnapshot.getChildren()){
                    if(node.child("roomId").getValue().toString().equals(CurrentChatRoom.roomId)) {
                        Messages message = new Messages(node.child("sender").getValue().toString(),
                                node.child("receiver").getValue().toString(),
                                node.child("content").getValue().toString(),
                                node.child("time").getValue().toString(),
                                node.child("roomId").getValue().toString(),
                                node.child("senderId").getValue().toString(),
                                node.child("receiverId").getValue().toString(),
                                node.child("status").getValue().toString()
                                );
                        messagesList.add(message);
                        Map<String,Object> updateStatus=new HashMap<>();
                        if(Flags.Customer_flag && Flags.insideChat) {
                            if (node.child("receiverId").getValue().toString().equals(CurrentUserInfo.id))
                                updateStatus.put("status", "seen");

                        }else if(!(Flags.Customer_flag) && Flags.insideChat){
                            if (node.child("receiverId").getValue().toString().equals(CurrentStore.id))
                                updateStatus.put("status", "seen");
                        }
                        databaseReference.child(node.getKey()).updateChildren(updateStatus);

                    }
                }
                messages.setValue(messagesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void getChatRooms(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatRoom> roomsList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(CurrentStore.id.equals(key.child("storeId").getValue().toString())){
                        ChatRoom room=new ChatRoom();
                        room.setStoreId(key.child("storeId").getValue().toString());
                        room.setCustomerId(key.child("customerId").getValue().toString());
                        room.setRoomId(key.getKey());

                        roomsList.add(room);
                    }
                }
                rooms.setValue(roomsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getChatRoomsCust(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatRoom> roomsList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(CurrentUserInfo.id.equals(key.child("customerId").getValue().toString())){
                        ChatRoom room=new ChatRoom();
                        room.setStoreId(key.child("storeId").getValue().toString());
                        room.setCustomerId(key.child("customerId").getValue().toString());
                        room.setRoomId(key.getKey());

                        roomsList.add(room);
                    }
                }
                rooms.setValue(roomsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendNotification(final String keyMessage, final String receiverEmail, final String senderName){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot node:dataSnapshot.getChildren()) {
                    if (node.child("roomId").getValue().toString().equals(CurrentChatRoom.roomId)){
                        if(node.getKey().equals(keyMessage))
                            if(node.child("status").getValue().toString().equals("unseen")){
                                NotificationsSender ns=new NotificationsSender("spec");
                                ns.sendNotification(receiverEmail,senderName +" "+node.child("content").getValue().toString());
                                break;
                            }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public LiveData<MessageNeeds> getMessageStatus(){
        return messageNeeds;
    }
    public LiveData<List<Messages>> getChatMessages(){
        getAllMessages();
        return messages;
    }

    public LiveData<List<ChatRoom>> getRooms(){
        if(Flags.Customer_flag)
            getChatRoomsCust();
        else
           getChatRooms();

        return rooms;

    }
    public LiveData<Boolean> finishedCreation(){return  created;}


}
