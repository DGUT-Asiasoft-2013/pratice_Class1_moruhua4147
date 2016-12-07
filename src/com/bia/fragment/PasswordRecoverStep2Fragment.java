package com.bia.fragment;

import com.bia.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import inputcells.BaseInputCellFragment;
import inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep2Fragment extends BaseInputCellFragment {

	View view;
	SimpleTextInputCellFragment flagInputVerify;
	SimpleTextInputCellFragment flagInputPassword;
	SimpleTextInputCellFragment flagInputPasswordRepeat;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.fragment_password_recover_step2, null);
		}
		flagInputVerify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);
		flagInputPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		flagInputPasswordRepeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		return view;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		flagInputVerify.setLabelText("验证码");
		flagInputVerify.setHintText("请输入验证码");
		flagInputPassword.setLabelText("新密码");
		flagInputPassword.setHintText("输入新密码");
		flagInputPasswordRepeat.setLabelText("重复输入");
		flagInputPasswordRepeat.setHintText("再次输入新密码");
	}
}
