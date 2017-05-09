package com.browser2app.pse.psebankdemo;

import android.app.Application;

import com.browser2app.khenshin.Khenshin;
import com.browser2app.khenshin.KhenshinInterface;
import com.browser2app.khenshin.activities.KhenshinApplication;

public class DemoBank extends Application implements KhenshinApplication {

	private KhenshinInterface khenshin;

	@Override
	public KhenshinInterface getKhenshin() {
		return khenshin;
	}

	public DemoBank() {
		super();
		khenshin = new Khenshin.KhenshinBuilder()
				.setApplication(this)
				.setAutomatonAPIUrl("https://b2a.pse.com.co/api/automata/")
				.setCerebroAPIUrl("https://b2a.pse.com.co/api/automata/")
				.setMainButtonStyle(Khenshin.CONTINUE_BUTTON_IN_FORM)
				.setAllowCredentialsSaving(true)
				.setHideWebAddressInformationInForm(true)
				.setAutomatonTimeout(90)
				.build();
	}
}
