package com.browser2app.pse.psebankdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.browser2app.khenshin.KhenshinConstants;
import com.browser2app.khenshin.LogWrapper;
import com.browser2app.khenshin.activities.StartPaymentActivity;

public class BrowsableActivity extends AppCompatActivity {

	private static final String TAG = BrowsableActivity.class.getSimpleName();
	private static final int START_PAYMENT_REQUEST_CODE = 306;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	}

	@Override
	public void onResume() {
		super.onResume();
		startPayment();
	}

	private void startPayment() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setupApp();
				if (getIntent().getData() != null && getIntent().getData().getLastPathSegment() != null) {

					final String automatonRequestId = getIntent().getData().getLastPathSegment();
					LogWrapper.d(TAG, "automatonRequestId: " + automatonRequestId);

					if (automatonRequestId != null) {
						Intent intent = new Intent(BrowsableActivity.this, StartPaymentActivity.class);
						intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_REQUEST_ID, automatonRequestId);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						setIntent(new Intent());
						startActivityForResult(intent, START_PAYMENT_REQUEST_CODE);
					}

				} else {
					Intent intent = new Intent(BrowsableActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}
			private void setupApp() {
				Configuration config = new Configuration(getBaseContext().getResources().getConfiguration());
				getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
			}


		}, 1000L);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String exitUrl = null;
		if (requestCode == START_PAYMENT_REQUEST_CODE && data != null) {
			setIntent(new Intent());
			exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
			Intent intent = new Intent(BrowsableActivity.this, MainActivity.class);
			if (exitUrl != null && !exitUrl.isEmpty()) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(exitUrl));
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

}
