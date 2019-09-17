package tactical.twerk.abilitychartview;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AbilityChartView mPpv;
    private Button mBtnSwitch;
    private Button mBtnLess,mBtnMore;
    private EditText mEtSides;
    private int mSides = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPpv = findViewById(R.id.ppv);
        mBtnSwitch =  findViewById(R.id.btnSwitch);
        mBtnLess = findViewById(R.id.btn_less);
        mBtnMore = findViewById(R.id.btn_more);
        mEtSides = findViewById(R.id.et_number);
        mBtnLess.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (mSides==-1 || mSides<=3){
                    return;
                }
                mSides -=1;
                mEtSides.setText(String.valueOf(mSides));
            }
        });
        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (mSides==-1){
                    mSides = 3;
                }else{
                    mSides +=1;
                }
                mEtSides.setText(String.valueOf(mSides));
            }
        });
        mEtSides.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void afterTextChanged(Editable editable) {
                if (null==editable || TextUtils.isEmpty(editable.toString()) ||Integer.valueOf(editable.toString())<3){
                    mSides=-1;
                }else {
                    mSides = Integer.valueOf(editable.toString());
                }
            }
        });

        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                int sides = mSides >=3 ?mSides:random.nextInt(5)+10;
                float[] mProgressValues = new float[sides];
                for (int i = 0; i < sides; i++) {
                    mProgressValues[i] = random.nextFloat();
                    if (random.nextInt() % 3 == 0) {
                        mProgressValues[i] = 1;
                    }
                }
                String []mLabels = new String[sides];
                for (int i = 0; i < sides; i++) {
                    mLabels[i] = "label" + i;
                }

                mPpv.initial(sides,mProgressValues,mLabels);
                mPpv.animateProgress();
            }
        });

    }


}
