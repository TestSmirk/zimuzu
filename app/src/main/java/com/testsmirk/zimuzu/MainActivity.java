package com.testsmirk.zimuzu;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testsmirk.zimuzu.base.BaseActivity;
import com.testsmirk.zimuzu.utils.Util;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.coder.MD5Coder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends BaseActivity {
    private static final String TAG = "BaseActivity";
    @BindView(R.id.tv_main_click)
    TextView textView;
    public interface GitHubService {
        @GET(Util.APP_MOVIE_RESOURCE)
        Call<ResponseBody> listRepos(@Query("cid") String user, @Query("accesskey")String accesskey, @Query("timestamp") String timestamp, @Query("client") String client);
    }
    @OnClick(R.id.tv_main_click)
    public void submit(View view) {
        long time = System.currentTimeMillis();
        Log.d(TAG, "submit: "+ time);
        Log.d(TAG, "submit() called with: view = [" + MD5Coder.getMD5Code(Util.ZIMUZU_CID+"$$"+Util.ZIMUZU_ACCESSKEY+"&&"+ time) + "]");;
        String md5Code = MD5Coder.getMD5Code(Util.ZIMUZU_CID + "$$" + Util.ZIMUZU_ACCESSKEY + "&&" + time);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASEURL)
                .build();
        GitHubService gitHubService = retrofit.create(GitHubService.class);
        Call<ResponseBody> responseBodyCall = gitHubService.listRepos(Util.ZIMUZU_CID, md5Code, String.valueOf(time), 2 + "");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResponse() called with: call = [" + call.request().url() + "], response = [" + response.body().string() + " "+response.body().source()+"]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        textView.setText("123");
    }
}
