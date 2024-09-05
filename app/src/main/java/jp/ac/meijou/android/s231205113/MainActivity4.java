package jp.ac.meijou.android.s231205113;

import android.os.Bundle;
import android.view.PixelCopy;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Optional;

import jp.ac.meijou.android.s231205113.databinding.ActivityMain4Binding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity4 extends AppCompatActivity {

    private final OkHttpClient okHttpClient = new OkHttpClient(); // HTTP通信するクライアント
    private final Moshi moshi = new Moshi.Builder().build(); // JSONをjavaのオブジェクトに変換
    private final JsonAdapter<Gist> gistJsonAdapter = moshi.adapter(Gist.class); // JSONをGistクラスに変換するAdapter
    private ActivityMain4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        var request = new Request.Builder()
                .url("https://mura.github.io/meijou-android-sample/gist.json")
                .build(); // Request.Builderクラスでアクセス先を作成

        // enqueue()でサブスレッドで通信処理を行う
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 通信に失敗したら呼ばれる
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 通信に成功したら呼ばれる
                var gist = gistJsonAdapter.fromJson(response.body().source()); // gistJsonAdapterを使ってレスポンスボディをGistクラスに変換

                // 中身の取り出し
                Optional.ofNullable(gist)
                        .map(g -> g.files.get("OkHttp.txt")) // filesの中からOkhttp.txtを取り出し
                        .ifPresent(gistFile -> {
                            // UIスレッド以外で更新するとクラッシュするので，UIスレッドで実行させる
                            runOnUiThread(() -> binding.text.setText(gistFile.content));
                        });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}