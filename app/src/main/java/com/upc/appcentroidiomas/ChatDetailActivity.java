package com.upc.appcentroidiomas;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upc.appcentroidiomas.adapter.ChatRecyclerAdapter;
import com.upc.appcentroidiomas.api.ApiContants;
import com.upc.appcentroidiomas.api.OrderApi;
import com.upc.appcentroidiomas.api.ProductChatApi;
import com.upc.appcentroidiomas.data.LoginDataSource;
import com.upc.appcentroidiomas.data.LoginRepository;
import com.upc.appcentroidiomas.data.model.ChatMessageResponse;
import com.upc.appcentroidiomas.data.model.LoggedInUser;
import com.upc.appcentroidiomas.data.model.NewMessageModel;
import com.upc.appcentroidiomas.data.model.NewMessageResponse;
import com.upc.appcentroidiomas.data.model.OrderResponse;
import com.upc.appcentroidiomas.data.model.UserInformationModel;
import com.upc.appcentroidiomas.models.ChatMessageModel;
import com.upc.appcentroidiomas.utils.AndroidUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatDetailActivity extends AppCompatActivity {
    UserInformationModel otherUser;
    ChatRecyclerAdapter adapter;

    ArrayList<ChatMessageModel> chatMessages = new ArrayList<>();

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;
    ImageButton orderCreateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        orderCreateBtn = findViewById(R.id.order_create_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);

        if (otherUser.imageUrl != null) {
            AndroidUtil.setProfilePic(this, Uri.parse(otherUser.imageUrl), imageView);
        }

        backBtn.setOnClickListener((v) -> {
            onBackPressed();
        });
        otherUsername.setText(otherUser.displayName);

        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        orderCreateBtn.setOnClickListener((v -> {
            showOrderConfirmationDialog();
        }));

        //getOrCreateChatroomModel();
        setupChatRecyclerView();
    }

    void sendMessageToUser(String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductChatApi productChatApi = retrofit.create(ProductChatApi.class);
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), ChatDetailActivity.this).getLoggedUser();

        NewMessageModel newMessage = new NewMessageModel();
        newMessage.userIdFrom = loggedInUser.getUserId();
        newMessage.userIdTo = otherUser.id;
        newMessage.messageContent = message;

        Call<NewMessageResponse> call = productChatApi.SendNewMessage(newMessage);
        call.enqueue(new Callback<NewMessageResponse>() {
            @Override
            public void onResponse(Call<NewMessageResponse> call, Response<NewMessageResponse> response) {
                if (response.isSuccessful()) {
                    messageInput.setText("");
                    //sendNotification(message);//TODO IMPLEMENT REALTIME NOTIFICATION
                    refreshChat();
                    Toast.makeText(ChatDetailActivity.this, "Mensaje enviado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChatDetailActivity.this, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewMessageResponse> call, Throwable t) {
                Toast.makeText(ChatDetailActivity.this, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }

    void setupChatRecyclerView() {
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();

        String url = ApiContants.BASE_URL + "ProductChatMessage/GetMessages/" + loggedInUser.getUserId() + "/" + otherUser.id;

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    chatMessages.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        ChatMessageModel _chatMessage = new ChatMessageModel();
                        _chatMessage.setSenderId(object.getInt("senderId"));
                        _chatMessage.setMessage(object.getString("message"));

                        try {
                            SimpleDateFormat format = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                            }
                            format.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date = format.parse(object.getString("timestamp"));
                            _chatMessage.setTimestamp(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Maneja el error de parseo aquí
                        }

                        chatMessages.add(_chatMessage);
                    }

                    if (chatMessages.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No se encontraron mensajes", Toast.LENGTH_LONG).show();
                    }

                    adapter = new ChatRecyclerAdapter(getApplicationContext(), chatMessages);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    manager.setReverseLayout(true);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                    //adapter.startListening();
                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            recyclerView.smoothScrollToPosition(0);
                        }
                    });

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(peticion);
    }


    public void refreshChat(){
        LoggedInUser loggedInUser = LoginRepository.getInstance(new LoginDataSource(), this.getApplicationContext()).getLoggedUser();
        String url = ApiContants.BASE_URL + "ProductChatMessage/GetMessages/" + loggedInUser.getUserId() + "/" + otherUser.id;

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    chatMessages.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        ChatMessageModel _chatMessage = new ChatMessageModel();
                        _chatMessage.setSenderId(object.getInt("senderId"));
                        _chatMessage.setMessage(object.getString("message"));

                        try {
                            SimpleDateFormat format = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                            }
                            format.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date = format.parse(object.getString("timestamp"));
                            _chatMessage.setTimestamp(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Maneja el error de parseo aquí
                        }

                        chatMessages.add(_chatMessage);
                    }

                    if (chatMessages.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No se encontraron mensajes", Toast.LENGTH_LONG).show();
                    }

                    adapter.setMessages(chatMessages);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(peticion);
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Estás seguro de generar la orden de compra?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call method to create order
                createOrder();
                //after send the message go to orders created main list
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createOrder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderApi orderApi = retrofit.create(OrderApi.class);

        int productChatMessageId = 18;
        Call<OrderResponse> call = orderApi.create(productChatMessageId);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChatDetailActivity.this, "La orden de compra fue creada satisfactoriamente!.", Toast.LENGTH_SHORT).show();
                    // Navigate to OrderFragment
                    OrderFragment orderFragment = new OrderFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main_screen, orderFragment)
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(ChatDetailActivity.this, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(ChatDetailActivity.this, "Ha sucedido un error, intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }
}