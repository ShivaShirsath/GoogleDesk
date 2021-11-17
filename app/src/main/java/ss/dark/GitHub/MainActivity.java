package ss.dark.GitHub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	private WebView webView;
	private WebSettings webSettings;

	private DrawerLayout drawer_layout;
	private NavigationView left_nav, right_nav;

	private boolean
	DesktopMode = false, 
	ForceDark = true,
	JavaScriptEnabled = true,
	BuiltInZoomControls = true,
	DisplayZoomControls = false,
	JavaScriptCanOpenWindowsAutomatically = true,
	LoadsImagesAutomatically = true,
	LoadWithOverviewMode = true,
	UseWideViewPort = true,
	AllowContentAccess = true,
	DomStorageEnabled = true;

	private String
	git = "https://github.com/", 
	user = "ShivaShirsath",
	tab = "?tab=",
	link = git + user, 
	CM;

	private ValueCallback<Uri> UM;
	private ValueCallback<Uri[]> UMA;
	private long backPressedTime = 0;
	private int focus = 0;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

			setContentView(R.layout.activity_main);
			webView = findViewById(R.id.WebView);
			webSettings = webView.getSettings();

			loadAll();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	private void loadAll() {
		if (
			((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null 
			|| !
			((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected()
			) {
			showDialog("No Internet Connection !");
		} else if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected()) {
			setDrawers();
			refreshWebView(link);
		}
	}
	private void setDrawers() {
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left_nav = (NavigationView) findViewById(R.id.left_nav);
		right_nav = (NavigationView) findViewById(R.id.right_nav);

		left_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem item) {
					if ((!item.getTitle().equals(user)) && item.getItemId() == R.id.item_user)
						item.setTitle(user);
					switch (item.getItemId()) {
						case R.id.item_user:
							link = git + item.getTitle();
							break;
						case R.id.item_newRepo:
							link = git + String.valueOf(item.getTitle()).toLowerCase();
							break;
						case R.id.item_repo:
							link = git + user + tab + String.valueOf(item.getTitle()).toLowerCase();
							break;
						case R.id.item_project:
							link = git + user + tab + String.valueOf(item.getTitle()).toLowerCase();
							break;
						case R.id.item_package:
							link = git + user + tab + String.valueOf(item.getTitle()).toLowerCase();
							break;
						case R.id.item_settings:
							link = git + String.valueOf(item.getTitle()).toLowerCase();
							break;
						default:
							Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
					}
					refreshWebView(link);

					drawer_layout.closeDrawers();
					return true;
				}
			});

		right_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem item) {
					switch (item.getItemId()) {
						case R.id.item_desktop:
							DesktopMode = ! DesktopMode;
							item.setTitle((DesktopMode ? "Desktop" : "Mobile") + " Mode");
							break;
						case R.id.item_dark:
							ForceDark = ! ForceDark;
							item.setTitle((ForceDark ? "Dark" : "Light") + " Mode");
							break;
						case R.id.item_open_in_chrome:
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl())).setPackage("com.android.chrome"));
							break;
						case R.id.item_reload:
							webView.reload();
							break;
						case R.id.item_help:
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.github.com")).setPackage("com.android.chrome"));
							break;
						case R.id.item_agent:
							Toast.makeText(MainActivity.this, webView.getSettings().getUserAgentString(), Toast.LENGTH_LONG).show();
							break;
						default:
							Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
					}
					refreshWebView(webView.getUrl());

					drawer_layout.closeDrawers();
					return true;
				}
			});
	}

	private void refreshWebView(String url) {

		webSettings.setJavaScriptEnabled(JavaScriptEnabled);
		
		webSettings.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
		//webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Mobile Safari/537.36");
		/*webSettings.setUserAgentString(
		 webSettings.getUserAgentString()
		 .replace(
		 webSettings.getUserAgentString()
		 .substring(
		 webSettings.getUserAgentString().indexOf("("),
		 webSettings.getUserAgentString().indexOf(")") + 1
		 ),
		 DesktopMode 
		 ? "(Macintosh; Intel Mac OS X 11_2_3)" /*(X11; Linux x86_64)*//*(Windows NT 10.0; Win64; x64)*
		 : "(iPhone; CPU iPhone OS 14_4 like Mac OS X)"
		 )
		 );*/ // For Desktop side toggle
        //webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(BuiltInZoomControls);
		webSettings.setDisplayZoomControls(DisplayZoomControls);

		webSettings.setJavaScriptCanOpenWindowsAutomatically(JavaScriptCanOpenWindowsAutomatically);
		webSettings.setLoadsImagesAutomatically(LoadsImagesAutomatically);

		webView.setInitialScale(1);
		webSettings.setLoadWithOverviewMode(LoadWithOverviewMode);
		webSettings.setUseWideViewPort(UseWideViewPort);

		webSettings.setAllowContentAccess(AllowContentAccess);
		webSettings.setDomStorageEnabled(DomStorageEnabled);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			webSettings.setForceDark(
				(getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES 
				? WebSettings.FORCE_DARK_ON
				: WebSettings.FORCE_DARK_OFF
			);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(0);
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		webView.setWebViewClient(new WebViewClient() {
				@Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
				}
				@Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url.contains("github")) {
						if (url.contains("raw")) {
							setDownload(url);
						} else {
							view.loadUrl(url);
						}
					} else {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					}
					return true;
				}
			});

		webView.setWebChromeClient(new WebChromeClient() {
				//For Android 3.0+
				public void openFileChooser(ValueCallback<Uri> uploadMsg) {
					fileChooser(uploadMsg);
				}
				// For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
					fileChooser(uploadMsg);
				}
				//For Android 4.1+
				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
					fileChooser(uploadMsg);
				}
				//For Android 5.0+
				public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
					if (UMA != null) {
						UMA.onReceiveValue(null);
					}
					UMA = filePathCallback;
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
						File photoFile = null;
						try {
							@SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
							String imageFileName = "img_" + timeStamp + "_";
							File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
							photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
							takePictureIntent.putExtra("PhotoPath", CM);
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
						}
						if (photoFile != null) {
							CM = "file:" + photoFile.getAbsolutePath();
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
						} else {
							takePictureIntent = null;
						}
					}
					Intent[] intentArray;
					if (takePictureIntent != null) {
						intentArray = new Intent[]{takePictureIntent};
					} else {
						intentArray = new Intent[0];
					}
					Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
					chooserIntent.putExtra(Intent.EXTRA_INTENT, fileChooser(null));
					chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an Action");
					chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
					startActivityForResult(chooserIntent, 1);
					return true;
				}
			});
		webView.loadUrl(url);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Uri[] results = null;
			//Check if response is positive
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == 1) {
					if (null == UMA) {
						return;
					}
					if (intent == null) {
						//Capture Photo if no image available
						if (CM != null) {
							results = new Uri[]{Uri.parse(CM)};
						}
					} else {
						String dataString = intent.getDataString();
						if (dataString != null) {
							results = new Uri[]{Uri.parse(dataString)};
						}
					}
				}
			}
			UMA.onReceiveValue(results);
			UMA = null;
		} else {
			if (requestCode == 1) {
				if (null == UM) return;
				Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
				UM.onReceiveValue(result);
				UM = null;
			}
		}
	}
	@Override public void onBackPressed() {
		if (drawer_layout.isDrawerOpen(left_nav) || drawer_layout.isDrawerOpen(right_nav)) {
			drawer_layout.closeDrawers();
		} else if (webView.canGoBack()) {
			webView.goBack();
		} else if (backPressedTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
			finish();
			return;
		} else {
			Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
		}
		backPressedTime = System.currentTimeMillis();
	}
	@Override public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshWebView(webView.getUrl());
	}
	@Override protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		webView.saveState(outState);
	}
	@Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		webView.restoreState(savedInstanceState);
	}
	@Override public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Toast.makeText(MainActivity.this, hasFocus ? "Welcome" + (focus == 0 ?"" : " Back") : "Bye…" , Toast.LENGTH_LONG).show();
		focus ++;
	}

	public Intent fileChooser(ValueCallback<Uri> uploadMsg) {
		UM = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		if (uploadMsg != null)
			MainActivity.this.startActivityForResult(Intent.createChooser(intent, "File Chooser"), 1);
		return intent;
	}

	public void setDownload(String url) {
		((DownloadManager) getSystemService(DOWNLOAD_SERVICE)).enqueue(
			new DownloadManager.Request(Uri.parse(url))
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) /*Notify client once download is completed.*/
			.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, // This is Download Directory
				url.substring(
					url.indexOf(".com/") + 5,
					(url.indexOf(".com/") + 5) + url.substring(url.indexOf(".com/") + 5).indexOf("/")
				) + "/" + // This is Folder Name 
				url.substring(url.lastIndexOf("/") + 1) // This is File Name
			)
		);
		Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
	}
	public void showDialog(String title) {
		AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle(title)
			.setCancelable(false).setNegativeButton("Exit", new DialogInterface.OnClickListener(){
				@Override public void onClick(DialogInterface dialog, int which) {
					finish();
					dialog.dismiss();
				}
			})
			.setPositiveButton("Rel⟳ad", new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					loadAll();
					dialog.dismiss();
				}
			}).create();
		dialog.show();
	}
}
