package com.muradit.projectx.View.RoomChatView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.muradit.projectx.Model.Messages;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentStoreChatStatic;
import com.muradit.projectx.Model.others.CurrentUserChatStatic;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.MessageNeeds;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterChats;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.RoomChatViewModel.ChatRoomViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.muradit.projectx.View.Reviews.ReviewsFragment.hideKeyboardFrom;


public class ChatRoom extends Fragment {

    private ChatRoomViewModel viewModel;
    private RecyclerView recyclerView;
    private EditText messageContent;
    private ImageView sendBtn;
    private int LastPos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_chat_room, container, false);
        if(Flags.Flag_Room){
            if(Flags.Customer_flag){
                ((MainActivity)getActivity()).setActionBarTitle(CurrentStoreChatStatic.name);
            }else{
                ((MainActivity)getActivity()).setActionBarTitle(CurrentUserChatStatic.name);
            }
        }else{
            ((MainActivity)getActivity()).setActionBarTitle(CurrentStore.name);

        }


        init(v);
        return v;
    }

    private void init(View v) {
        viewModel=new ChatRoomViewModel(getActivity());
        recyclerView=v.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        Flags.insideChat=true;
       if(!Flags.Flag_Room) {
           viewModel.CreateRoomMessage();



       }else {
           viewModel.checkIfExistFromRoom();

       }
       viewModel.getMessageStatus().observe(getViewLifecycleOwner(), new Observer<MessageNeeds>() {
           @Override
           public void onChanged(MessageNeeds messageNeeds) {
               if(messageNeeds.isExist()){
                   prepareChatRoom();
               }
           }
       });




        messageContent=v.findViewById(R.id.messageContent);
        sendBtn=v.findViewById(R.id.sendBtn);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              viewModel.sendMessage(messageContent.getText().toString());

              messageContent.setText("");
              messageContent.setHint(getString(R.string.SendMessageStr));
              hideKeyboardFrom(getActivity(),v);
                ((LinearLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(false);
                recyclerView.scrollToPosition(LastPos);



            }
        });


    }
    private void prepareChatRoom() {

            viewModel.getAllMessages().observe(getViewLifecycleOwner(), new Observer<List<Messages>>() {
                @Override
                public void onChanged(List<Messages> messages) {
                    LastPos = messages.size() - 1;
                    RecyclerAdapterChats recChat = new RecyclerAdapterChats(getActivity(), messages);
                    recyclerView.setAdapter(recChat);
                    recyclerView.scrollToPosition(LastPos);

                }
            });


    }


}