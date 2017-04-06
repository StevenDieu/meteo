package com.fges.meteo.ui.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fges.meteo.R;
import com.fges.meteo.data.manager.BeerManagerV2;
import com.fges.meteo.data.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.hello)
    TextView mTextView;

    @BindView(R.id.myLinear)
    LinearLayout mLinearLayout;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        context = this;
        mTextView.setText("Ca marche");

        BeerManagerV2.getInstance().getCategories(new MyCallback() {
            @Override
            public List<Category> onSuccess(List<Category> listCategory) {
                TextView textView;
                for (int i = 0; i < listCategory.size(); i++) {
                    textView = new TextView(context);
                    textView.setText(listCategory.get(i).getName());
                    mLinearLayout.addView(textView);
                }
                return listCategory;
            }

            @Override
            public void onError(String error) {
                mTextView.setText(error);
            }
        });
    }

    public interface MyCallback {
        List<Category> onSuccess(List<Category> listCategory);
        void onError(String error);
    }
}