package com.example.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest =chain.request();

                        Request newRequest = originalRequest.newBuilder().header("Interceptor-Header","xyz").build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

//        Gson gson=new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi= retrofit.create(JsonPlaceHolderApi.class);
//        getposts();
//        getComments();
        createPost();
//          updatePost();
//            deletePost();
    }


    private void getposts() {

/*        Map<String,String> parameters = new HashMap<>();

        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

          Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);*/
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,6},null,null);

/*        ArrayList<Post> list=new ArrayList<>();
        Post post=new Post(1,"New Title","New Text");
        list.add(post);
        post=new Post(2,"New Title","New Text");
        list.add(post);
        post=new Post(3,"New Title","New Text");
        list.add(post);*/

//        Call<List<Post>> call =jsonPlaceHolderApi.creatListePost(list);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


//                response.body().get(0).getText();

                if(!response.isSuccessful()){
                    textViewResult.setText("code:"+response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts){
                    String content="";
                    content += "ID:"+post.getId()+"\n";
                    content += "User ID:"+post.getUserId()+"\n";
                    content += "Title:"+post.getTitle()+"\n";
                    content += "Text:"+post.getText()+"\n\n";
                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    

    private void getComments() {

//        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);


        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("/posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText("code:"+response.code());
                    return;
                }

                List<Comment> comments =response.body();

                for (Comment comment : comments){
                    String content="";
                    content += "ID: "+comment.getId()+"\n";
                    content += "Post ID: "+comment.getPostId()+"\n";
                    content += "Name: "+comment.getName()+"\n";
                    content += "Email: "+comment.getEmail()+"\n";
                    content += "Text: "+comment.getText()+"\n\n";
                    textViewResult.append(content);
                }



            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }

    private  void createPost(){



        Post post=new Post(23,"New Title","New Text");

//        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New Text");

        Map<String,String> fields = new HashMap<>();
        fields.put("userId","25");
        fields.put("title","New Title");
//        Call<Post> call = jsonPlaceHolderApi.createPost(fields);


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText("code:"+response.code());
                    return;
                }

                Post postResponse = response.body();

                String content="";
                content += "Code: "+response.code()+"\n";
                content += "ID: "+postResponse.getId()+"\n";
                content += "Post ID: "+postResponse.getUserId()+"\n";
                content += "Name: "+postResponse.getTitle()+"\n";
                content += "Email: "+postResponse.getText()+"\n\n";

                textViewResult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });


    }


    private void updatePost(){
        Post post = new Post(12,null,"New Text");


        Map<String,String> headers = new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghi");

            Call<Post> call = jsonPlaceHolderApi.patchPost(headers,5,post);
//        Call<Post> call = jsonPlaceHolderApi.putPost("abc",5,post);
//        Call<Post> call = jsonPlaceHolderApi.putPost(5,post);
//        Call<Post> call = jsonPlaceHolderApi.patchPost(5,post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText("code:"+response.code());
                    return;
                }

                Post postResponse = response.body();

                String content="";
                content += "Code: "+response.code()+"\n";
                content += "ID: "+postResponse.getId()+"\n";
                content += "Post ID: "+postResponse.getUserId()+"\n";
                content += "Name: "+postResponse.getTitle()+"\n";
                content += "Email: "+postResponse.getText()+"\n\n";

                textViewResult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void deletePost(){
        Call<Void> call =jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code:"+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText("Code:"+t.getMessage());
            }
        });

    }



}