package com.muradit.projectx.ViewModel.RoomChatViewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.muradit.projectx.Model.ChatRoom;
import com.muradit.projectx.Model.Messages;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.MessageNeeds;
import com.muradit.projectx.Repository.RoomChatRepository.ChatRoomRepository;

import java.util.List;

public class ChatRoomViewModel {
    private ChatRoomRepository repository;
    private Context context;

    public ChatRoomViewModel(Context context) {
        repository=new ChatRoomRepository(context);
        this.context=context;
    }

    public void CreateRoomMessage(){
        repository .createRoom();
    }

    public void checkIfExist(){

            repository.checkIfRoomExist();
    }
    public void checkIfExistFromRoom(){
        if(Flags.Customer_flag)
            repository.checkIfRoomExistCust();
        else
            repository.checkIfRoomExistStore();
        
    }

    public void sendMessage(final String content){

        repository.getMessageStatus().observe((LifecycleOwner) context, new Observer<MessageNeeds>() {
            @Override
            public void onChanged(MessageNeeds messageNeeds) {
                if(messageNeeds.isExist()) {

                    repository.sendMessage(content, messageNeeds.getRoomId());
                    repository.getMessageStatus().removeObserver(this);


                }
                }
        });
    }

    public LiveData<List<Messages>> getAllMessages(){
        return repository.getChatMessages();
    }

    public LiveData<List<ChatRoom>> getAllChatRooms(){
        return repository.getRooms();
    }
    public LiveData<MessageNeeds> getMessageStatus(){return repository.getMessageStatus();}
    public LiveData<Boolean> finishedCreation(){return repository.finishedCreation();}



}
