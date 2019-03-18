package labs.walmart.dev_sena.checkout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class AddCartActivity extends AppCompatActivity {
    Context m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getIntent().hasExtra("FIREBASE_UID")) {
            startActivity(new Intent(this, PhoneAuthActivity.class));
            finish();
        }
        setContentView(R.layout.activity_add_cart);

        Objects.requireNonNull(getActionBar()).setTitle("Add Cart Details");

        m = this;
        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(m,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
