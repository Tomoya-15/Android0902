package jp.ac.meijou.android.s231205113;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Optional;

import jp.ac.meijou.android.s231205113.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    private ActivityMain3Binding binding;
    private final ActivityResultLauncher<Intent> getActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case RESULT_OK -> {
                        // OKボタンが押されたとき
                        Optional.ofNullable(result.getData())
                                .map(data -> data.getStringExtra("ret"))
                                .map(ret -> "Result: " + ret)
                                .ifPresent(text -> binding.textview.setText(text));
                    }

                    case RESULT_CANCELED -> {
                        // Cancelボタンが押されたとき
                        binding.textview.setText("Result: Canceled");
                    }

                    default -> {
                        // 設定外のことが起きた
                        binding.textview.setText("Result: Unknown(" + result.getResultCode() + ")");
                    }
                }

            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button2.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.button3.setOnClickListener(view -> {
            var intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo: 35, 139"));
            startActivity(intent);
        });

        binding.button4.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            intent.putExtra("text", binding.edittext.getText().toString());
            startActivity(intent);
        });

        // 起動ボタン
        binding.button5.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            // MainActivity から返ってきたときにgetActivityResultで起動する
            getActivityResult.launch(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}