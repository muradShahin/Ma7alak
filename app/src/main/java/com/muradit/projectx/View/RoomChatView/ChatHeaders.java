package com.muradit.projectx.View.RoomChatView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muradit.projectx.Model.ChatRoom;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterRooms;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.RoomChatViewModel.ChatRoomViewModel;

import java.util.List;


public class ChatHeaders extends Fragment {

     private RecyclerView recyclerView;
     private ChatRoomViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.messages));
        View v= inflater.inflate(R.layout.fragment_chat_headers, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        viewModel=new ChatRoomViewModel(getActivity());
        Flags.insideChat=false;
        recyclerView=v.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        getAllChatRooms();

    }

    private void getAllChatRooms(){
        viewModel.getAllChatRooms().observe(getViewLifecycleOwner(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                RecyclerAdapterRooms recAdapter = new RecyclerAdapterRooms(getActivity(), chatRooms);
                recyclerView.setAdapter(recAdapter);

            }
        });
    }
}