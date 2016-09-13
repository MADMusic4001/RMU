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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.FileSelectorAdapter;
import com.madinnovations.rmu.view.di.modules.CampaignFragmentModule;
import com.madinnovations.rmu.view.utils.FileInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * UI for file selection dialog
 */
public class FileSelectorDialogFragment extends DialogFragment {
	private static final String FILE_SELECTOR_FILTER = "fs_extension_filter";
	private static final String PARENT_DIR = "..";
	private String						extension = ".rmu";
	private ArrayList<FileInfo>			fileList;
	private File 						path;
	private String						chosenFile;
	private FileSelectorDialogListener	listener;
	@Inject
	protected FileRxHandler               fileRxHandler;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		extension = getArguments().getString(FILE_SELECTOR_FILTER);
		loadFileList();
		final ArrayAdapter<FileInfo> filesListAdapter = new FileSelectorAdapter(getActivity());

		if(fileList != null && !fileList.isEmpty()) {
			filesListAdapter.addAll(fileList);
		}
		else {
			filesListAdapter.clear();
		}
		filesListAdapter.notifyDataSetChanged();

		final TextView titleView = new TextView(getActivity());
		titleView.setText(String.format(getString(R.string.alert_fs_title),
										extension,
										path.getAbsolutePath()));

		return builder.setCustomTitle(titleView)
				.setSingleChoiceItems(filesListAdapter,
									  -1,
									  new DialogInterface.OnClickListener() {
										  @Override
										  public void onClick(DialogInterface dialogInterface, int which) {
											  FileInfo fileInfo = fileList.get(which);
											  File file;
											  if(fileInfo.getFileName().equals(PARENT_DIR)) {
												  file = path.getParentFile();
											  }
											  else {
												  file = new File(path + File.separator + fileInfo.getFileName());
											  }
											  if (file.isDirectory()) {
												  path = file;
												  titleView.setText(String.format(getString(R.string.alert_fs_title),
																				  FileSelectorDialogFragment.this.extension,
																				  path.getAbsolutePath()));
												  titleView.invalidate();
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

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((CampaignActivity)activity).getActivityComponent().
				newCampaignFragmentComponent(new CampaignFragmentModule(this)).injectInto(this);
		path = fileRxHandler.getImportExportDir();
		try {
			listener = (FileSelectorDialogListener) activity;
		}
		catch (ClassCastException ex) {
			throw new ClassCastException(
					activity.getClass().getName() + " must implement "
							+ "FileSelectorDialogFragment.FileSelectorDialogListener.");
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((CampaignActivity)getActivity()).getActivityComponent().
				newCampaignFragmentComponent(new CampaignFragmentModule(this)).injectInto(this);
		path = fileRxHandler.getImportExportDir();
		try {
			listener = (FileSelectorDialogListener) context;
		}
		catch (ClassCastException ex) {
			throw new ClassCastException(
					context.getClass().getName() + " must implement "
							+ "FileSelectorDialogFragment.FileSelectorDialogListener.");
		}
	}

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
			if(tempFileList != null) {
				fileList = new ArrayList<>(tempFileList.length + 1);
				if (path.getParent() != null) {
					fileList.add(new FileInfo("..", true));
				}
				for (String fileName : tempFileList) {
					File file = new File(path, fileName);
					fileList.add(new FileInfo(fileName, file.isDirectory()));
				}
			}
		}
		else {
			fileList = new ArrayList<>(0);
		}
	}
}
