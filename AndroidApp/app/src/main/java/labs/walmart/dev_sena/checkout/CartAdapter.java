package labs.walmart.dev_sena.checkout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private static HashMap<HashMap,Integer> mCart;
    CartAdapter(HashMap<HashMap,Integer> cart){
        mCart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.checkout_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name="null",type="null"; double price = 0; int pos = 0,quantity=0;
        for(HashMap item:mCart.keySet()){
            if(pos==i) {
                name = (String) item.get("name");
                type = (String) item.get("quantity");
                price = Double.parseDouble(item.get("price").toString());
                quantity = mCart.get(item);
            }
            pos++;
        }

        viewHolder.bind(name,type,price,quantity);
    }

    @Override
    public int getItemCount() {
        return mCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
        }

        void bind(String name,String type, double price,int quantity){
            ((TextView)itemView.findViewById(R.id.item_name)).setText(name);
            ((TextView)itemView.findViewById(R.id.item_type)).setText(type);
            ((TextView)itemView.findViewById(R.id.item_price)).setText(Double.toString(price));
            ((TextView)itemView.findViewById(R.id.item_qty)).setText(quantity+"");
        }
    }
}
