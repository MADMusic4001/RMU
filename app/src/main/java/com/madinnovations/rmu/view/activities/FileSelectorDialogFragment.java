/**
 * Copyright (C) 2015 MadMusic4001
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.adapters.FileSelectorAdapter;
import com.madinnovations.rmu.view.utils.FileInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * UI for file selection dialog
 */
public class FileSelectorDialogFragment extends DialogFragment {
	private static final String FILE_SELECTOR_FILTER = "fs_extension_filter";
	private String						extension = ".rmu";
	private ArrayList<FileInfo>			fileList;
	private File 						path = Environment.getExternalStorageDirectory();
	private String						chosenFile;
	private FileSelectorDialogListener	listener;

	public interface FileSelectorDialogListener {
		void onFileSelected(String fileName);
	}

	private void loadFileList() {
		if (path.exists()) {
			final FilenameFilter filter = new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					return filename.endsWith(extension) || sel.isDirectory();
				}
			};
			String[] tempFileList = path.list(filter);
			fileList = new ArrayList<>(tempFileList.length + 1);
			if(path.getParent() != null) {
				fileList.add(new FileInfo("..", true));
			}
			for(String fileName : tempFileList) {
				File file = new File(path, fileName);
				fileList.add(new FileInfo(fileName, file.isDirectory()));
			}
			Log.d(this.getClass().getName(),
				  fileList.size() + " files loaded");
		}
		else {
			fileList = new ArrayList<>(0);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (FileSelectorDialogListener) context;
		}
		catch (ClassCastException ex) {
			throw new ClassCastException(
					context.getClass().getName() + " must implement "
							+ "FileSelectorDialogFragment.FileSelectorDialogListener.");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		extension = getArguments().getString(FILE_SELECTOR_FILTER);
		loadFileList();
		final ArrayAdapter<FileInfo> filesListAdapter = new FileSelectorAdapter(getActivity());

		filesListAdapter.addAll(fileList);
		filesListAdapter.notifyDataSetChanged();

		return builder.setTitle(String.format(getString(R.string.alert_fs_title),
				extension,
				path.getAbsolutePath()))
				.setSingleChoiceItems(filesListAdapter,
						-1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								FileInfo fileInfo = fileList.get(which);
								File file = new File(path + File.separator + fileInfo.getFileName());
								if (file.isDirectory()) {
									path = file;
									FileSelectorDialogFragment.this.getDialog().setTitle(
											String.format(getString(R.string.alert_fs_title),
													FileSelectorDialogFragment.this.extension,
													path.getAbsolutePath()));
									loadFileList();
									filesListAdapter.clear();
									filesListAdapter.addAll(fileList);
									filesListAdapter.notifyDataSetChanged();
									chosenFile = null;
								} else {
									for (FileInfo aFileInfo : fileList) {
										if (aFileInfo != fileInfo && aFileInfo.isSelected()) {
											aFileInfo.setSelected(false);
										} else if (aFileInfo == fileInfo) {
											fileInfo.setSelected(!fileInfo.isSelected());
											if (fileInfo.isSelected()) {
												chosenFile = path + File.separator + fileInfo.getFileName();
											} else {
												chosenFile = null;
											}
										}
									}
									filesListAdapter.notifyDataSetChanged();
								}
							}
						})
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog1, int which) {
								listener.onFileSelected(chosenFile);
							}
						})
				.create();
	}
}
