package jp.ac.meijou.android.s231205113;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.s231205113.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PrefDataStore prefDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefDataStore = PrefDataStore.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.button.setOnClickListener(view -> {
            var text = binding.editTextText.getText().toString();
            binding.text.setText(R.string.text2);
        });

        binding.editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが更新される直前に呼ばれる
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字を1つ入力されたときに呼ばれる
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // テキストが更新されたあとに呼ばれる
                binding.text.setText(editable.toString());
            }
        });

        binding.saveButton.setOnClickListener(view -> {
            var text = binding.editTextText.getText().toString();
            prefDataStore.setString("name", text);
        });

        binding.deleteButton.setOnClickListener(view -> {
            prefDataStore.setString("name", "");
        });

        // Okボタン
        binding.buttonOk.setOnClickListener(view -> {
            var intent = new Intent();
            intent.putExtra("ret", "Ok");
            setResult(RESULT_OK, intent);
            finish();
        });

        // Cancelボタン
        binding.buttonCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String text = getIntent().getStringExtra("text");
        binding.text.setText(text);
//        prefDataStore.getString("name")
//                .ifPresent(name -> binding.text.setText(name)); // .ifPresent()は「getString("name")の結果がnullでないときに，引数の処理を実行する」というOptionalのメソッド
    }

    @Override
    protected void onStop() {
        super.onStop();
        var text = binding.editTextText.getText().toString();
        prefDataStore.setString("name", text);
    }
}