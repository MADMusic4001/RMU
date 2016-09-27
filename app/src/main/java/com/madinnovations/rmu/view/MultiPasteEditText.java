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
package com.madinnovations.rmu.view;

import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/26/2016.
 */
public class MultiPasteEditText extends EditText {
	private Pattern pattern;
	private MultiPasteEditText nextMultiPaste;

	/**
	 * @see EditText#EditText(Context)
	 */
	public MultiPasteEditText(Context context) {
		super(context);
	}

	/**
	 * @see EditText#EditText(Context, AttributeSet)
	 */
	public MultiPasteEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @see EditText#EditText(Context, AttributeSet, int)
	 */
	public MultiPasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
			String[] results = text.toString().split("\\s");
			MultiPasteEditText nextEditText = this;
			boolean changed = false;
			for(String result : results) {
				Matcher matcher = pattern.matcher(result);
				if(matcher.matches()) {
					changed = true;
					nextEditText.setText(result);
					nextEditText = nextEditText.getNextMultiPaste();
					if(nextEditText == null) {
						break;
					}
				}
				else {
					break;
				}
			}
			// TODO: Save item
//			if(changed) {
//				saveItem();
//			}
		}
	}

	// Getters and setters
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public MultiPasteEditText getNextMultiPaste() {
		return nextMultiPaste;
	}
	public void setNextMultiPaste(MultiPasteEditText nextMultiPaste) {
		this.nextMultiPaste = nextMultiPaste;
	}
}
