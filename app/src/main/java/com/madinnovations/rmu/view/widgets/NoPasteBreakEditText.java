/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.widgets;

import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.madinnovations.rmu.view.RMUApp;

/**
 * EditText subclass that strips line breaks from text being pasted into an EditText
 */
public class NoPasteBreakEditText extends EditText {

	/**
	 * @see EditText#EditText(Context)
	 */
	public NoPasteBreakEditText(Context context) {
		super(context);
	}

	/**
	 * @see EditText#EditText(Context, AttributeSet)
	 */
	public NoPasteBreakEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @see EditText#EditText(Context, AttributeSet, int)
	 */
	public NoPasteBreakEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		boolean consumed = super.onTextContextMenuItem(id);
		switch (id) {
			case android.R.id.paste:
				onTextPaste();
				break;
		}
		return consumed;
	}

	public void onTextPaste() {
		ClipboardManager clipboardManager = RMUApp.getClipboardManager();
		if(clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType(
				ClipDescription.MIMETYPE_TEXT_PLAIN)) {
			CharSequence text = clipboardManager.getPrimaryClip().getItemAt(0).getText();
			String newString = text.toString().replace("\n", " ");
			setText(newString);
		}
	}
}
