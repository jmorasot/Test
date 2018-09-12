package com.example.manuelmora.gastos.view.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.model.Transaction;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author Juan Mora
 * @since 11/09/2018 13:18
 *
 * Class to manage the list of MainActivity's transactions.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionHolder> {

    private List<Transaction> mTransactionList;

    public TransactionsAdapter() {
        mTransactionList = new RealmList<>();
        update();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View transactionView = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_transaction, parent, false);
        return new TransactionHolder(transactionView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        holder.bindElements(mTransactionList.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }

    /**
     * Fill the transactions list to show it in MainActivity
     *
     * @return The transactions list
     */
    public List<Transaction> update() {
        Realm realm = Realm.getDefaultInstance();
        mTransactionList = realm.copyFromRealm(realm.where(Transaction.class).findAll());
        realm.close();
        notifyDataSetChanged();
        return mTransactionList;
    }

    /**
     * Class to bind the view's components to show the transactions.
     *
     */

    class TransactionHolder extends RecyclerView.ViewHolder {
        ImageView mTypeImageView;
        TextView mConceptTextView;
        TextView mAmountTextView;

        TransactionHolder(View itemView) {
            super(itemView);
            mTypeImageView = itemView.findViewById(R.id.item_transaction_type);
            mConceptTextView = itemView.findViewById(R.id.item_transaction_concept);
            mAmountTextView = itemView.findViewById(R.id.item_transaction_amount);
        }

        void bindElements(Transaction transaction) {
            // Evaluate the transaction's type to set the image to display and the background's color
            if (transaction.type.equals(Transaction.DEBT_TYPE)){
                mTypeImageView.setBackgroundColor(Color.rgb(230, 17, 17));
                mTypeImageView.setImageResource(R.drawable.ic_debt);
            } else {
                mTypeImageView.setImageResource(R.drawable.ic_payment);
            }
            mConceptTextView.setText(transaction.concept);
            mAmountTextView.setText(String.format(Locale.getDefault(), "$%1$,.2f", transaction.amount));
        }
    }
}
