package tactical.twerk.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    PolygonProgressView mPpv;
    Button mBtnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPpv = (PolygonProgressView) findViewById(R.id.ppv);
        mBtnSwitch = (Button) findViewById(R.id.btnSwitch);

        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                int mSides = random.nextInt(5)+10;
                float[] mProgressValues = new float[mSides];
                for (int i = 0; i < mSides; i++) {
                    mProgressValues[i] = random.nextFloat();
                    if (random.nextInt() % 3 == 0) {
                        mProgressValues[i] = 1;
                    }
                }
                String []mLabels = new String[mSides];
                for (int i = 0; i < mSides; i++) {
                    mLabels[i] = "label" + i;
                }

                mPpv.initial(mSides,mProgressValues,mLabels);
                mPpv.animateProgress();
            }
        });

    }
}
