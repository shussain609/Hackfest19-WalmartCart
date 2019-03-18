package labs.walmart.dev_sena.checkout;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    public static String cart;
    HashMap value;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView emptyCart;
    TextView totalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                progressBar = findViewById(R.id.indeterminateBar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        emptyCart = findViewById(R.id.empty_cart);
        totalView = findViewById(R.id.total_view);
        totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),PaymentActivity.class));
                finish();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("carts");
        final String TAG = "Firebase";
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try{
                value = (HashMap)dataSnapshot.getValue();
                updateRecyclerView();
                Log.d(TAG, "Value is: " + value.toString());}
                catch (Exception ignored){}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(){
        try {
            Log.d("Test", value.values().toString());
            Object z = value.values().iterator().next();
            for (Object o : value.values()) {
                z = o;
            }
            double total = 0;
            HashMap cart = (HashMap) ((HashMap) z).get("checkout");
            HashMap<HashMap,Integer> finalCart = new HashMap<>();
            for(Object x:cart.values()){
                HashMap xh = (HashMap)x;
                total += Double.parseDouble(xh.get("price").toString());
                if(finalCart.containsKey(xh)){
                    int count = finalCart.get(xh) + 1;
                    finalCart.remove(xh);
                    finalCart.put(xh,count);
                }
                else{
                    finalCart.put(xh,1);
                }
            }
            CartAdapter adapter = new CartAdapter(finalCart);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            emptyCart.setVisibility(View.GONE);
            totalView.setText("Total : "+total);
            totalView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e("MSG",e.getMessage());
            totalView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }
}
