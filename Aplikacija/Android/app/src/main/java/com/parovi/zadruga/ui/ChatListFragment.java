package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.ChatResumesAdapter;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.data.ChatResume;
import com.parovi.zadruga.databinding.FragmentChatListBinding;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.ChatsViewModel;
import com.quickblox.chat.model.QBChatDialog;

import java.util.List;


public class ChatListFragment extends Fragment implements ChatResumesAdapter.ChatListListener {
    private ChatViewModel model;
    private FragmentChatListBinding binding;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatListBinding.inflate(inflater, container, false);

        model = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        MutableLiveData<CustomResponse<?>> isLoggedIn = new MutableLiveData<>();
        ZadrugaRepository.getInstance(requireActivity().getApplication()).loginUser(isLoggedIn, new User("vuk.bibic@gmail.com", "novaaasifraaaa"));
        isLoggedIn.observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    model.connectToChatServer();
                }
            }
        });


        model.observeIsConnected().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    model.addOnGlobalMessageReceived();
                    model.loadAllChats();
                }
            }
        });
        ChatResumesAdapter adapter = new ChatResumesAdapter(this);
        model.getChats().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    adapter.setChats((List<Chat>)customResponse.getBody());
            }
        });
        binding.rvChatList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.rvChatList.setLayoutManager(layoutManager);
        return binding.getRoot();
    }

    @Override
    public void onChatResumeSelected(QBChatDialog chat) {
        model.setActiveChat(chat);
        Navigation.findNavController(binding.getRoot()).navigate(ChatListFragmentDirections.actionChatListFragmentToChatMessagesFragment());
    }
}