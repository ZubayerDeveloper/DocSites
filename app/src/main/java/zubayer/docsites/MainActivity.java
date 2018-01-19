package zubayer.docsites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.karan.churi.PermissionManager.PermissionManager;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.*;
import static android.widget.Toast.makeText;

public class MainActivity extends Activity {
	private AdView mAdView;
	Button residency, admission, posting, regiBCS,
			regiDept, regiSenior, resultBCS, resultDEPT,
			resultSenior, bsmmuNotice, forms, homelinks, bcpsNotice,
			bcpsGuideline, fcpsPart1, deputation,gadgettes;
	TextView errorMessage, text, items,bsmmuTitle;
	AlertDialog Dialog, checkinternet;
	AlertDialog.Builder builder;
//	HtmlParser back;
//	bcpsParser bcps;
	View m;
	ListView list;
	MyAdapter my;
	ProgressBar progressBar;
	String btxt, newline, url, paramUrl, paramTagForText, paramTagForLink, paramLink,
            updateMessage,parseVersionCode,pdfFilter,driveViewer;
	int position, i, textMin, textMax, a = 0, linkBegin, linkEnd, aa,versionCode=8 ;
	ArrayList<String> buttonTexts = new ArrayList<String>();
	ArrayList<String> urls = new ArrayList<String>();
    MenuItem menuitem;
//    PermissionManager permissionManager;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		permissionManager=new PermissionManager() {
//            @Override
//            public boolean checkAndRequestPermissions(Activity activity) {
//                return super.checkAndRequestPermissions(activity);
//            }
//
//            @Override
//            public ArrayList<statusArray> getStatus() {
//                return super.getStatus();
//            }
//
//            @Override
//            public List<String> setPermission() {
//                return super.setPermission();
//            }
//
//            @Override
//            public void checkResult(int requestCode, String[] permissions, int[] grantResults) {
//                super.checkResult(requestCode, permissions, grantResults);
//            }
//
//            @Override
//            public void ifCancelledAndCanRequest(Activity activity) {
//                super.ifCancelledAndCanRequest(activity);
//
//            }
//
//            @Override
//            public void ifCancelledAndCannotRequest(Activity activity) {
//                checkinternet = builder.create();
//                checkinternet.setButton("Allow permission", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            permissionManager.checkAndRequestPermissions(MainActivity.this);
//                        }
//                    }
//                });
//                checkinternet.setButton3("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//                try {
//                    checkinternet.setMessage("You need to allow permission to download files from internet");
//                } catch (Exception e) {
//                }
                checkinternet.show();
//            }
//
//            @Override
//            public int hashCode() {
//                return super.hashCode();
//            }
//
//            @Override
//            public boolean equals(Object obj) {
//                return super.equals(obj);
//            }
//
//            @Override
//            protected Object clone() throws CloneNotSupportedException {
//                return super.clone();
//            }
//
//            @Override
//            public String toString() {
//                return super.toString();
//            }
//
//            @Override
//            protected void finalize() throws Throwable {
//                super.finalize();
//            }
//        };
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            permissionManager.checkAndRequestPermissions(this);
//        }
//        UpdateChecker check=new UpdateChecker();
//        check.execute();
//		mAdView = (AdView) findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mAdView.loadAd(adRequest);
//
//		Calligrapher font = new Calligrapher(this);
//		font.setFont(this, "kalpurush.ttf", true);
//
//		residency = (Button) findViewById(R.id.Web);
//		admission = (Button) findViewById(R.id.admission);
//		posting = (Button) findViewById(R.id.posting);
//		regiBCS = (Button) findViewById(R.id.regi1);
//		regiDept = (Button) findViewById(R.id.regi2);
//		regiSenior = (Button) findViewById(R.id.regi3);
//		errorMessage = (TextView) findViewById(R.id.errorMessage);
//		resultBCS = (Button) findViewById(R.id.resul1);
//		resultDEPT = (Button) findViewById(R.id.result2);
//		resultSenior = (Button) findViewById(R.id.result3);
//		bsmmuNotice = (Button) findViewById(R.id.depa);
//		forms = (Button) findViewById(R.id.forms);
//		homelinks = (Button) findViewById(R.id.homeLinks);
//		bcpsGuideline = (Button) findViewById(R.id.guideline);
//		bcpsNotice = (Button) findViewById(R.id.bcpsNotice);
//		fcpsPart1 = (Button) findViewById(R.id.fcpsPartOne);
//		deputation = (Button) findViewById(R.id.deputation);
//		gadgettes=(Button)findViewById(R.id.gadgette);
//		bsmmuTitle=(TextView)findViewById(R.id.bsmmu);
//		items = (TextView) findViewById(R.id.items);
//
//		driveViewer="https://docs.google.com/viewer?url=";
//		m = getLayoutInflater().inflate(R.layout.listview, null);
//		list = (ListView) m.findViewById(R.id.ListView);
//		list.setBackgroundColor(Color.parseColor("#FFFFFF"));
//		my = new MyAdapter(MainActivity.this, buttonTexts,urls);
//		progressBar = (ProgressBar) m.findViewById(R.id.progressBar);
//		builder = new AlertDialog.Builder(MainActivity.this);
//        Dialog = builder.create();
//        Dialog.setButton("Close", new DialogInterface.OnClickListener() {
//            public void onClick(final DialogInterface dialog, int id) {
//                try {
//                    buttonTexts.clear();
//                    urls.clear();
//                    url = null;
//                    back.cancel(true);
//                    bcps.cancel(true);
//                    buttonTexts.clear();
//                    urls.clear();
//                    url = null;
//                }catch (Exception e){}
//
//            }
//        });
//        Dialog.setView(m);
//        list.setAdapter(my);
//        Dialog.setCancelable(false);
//		residency.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bsmmu.edu.bd";
//				paramTagForText = "a";
//				paramTagForLink = "a";
//				paramLink = "abs:href";
//				textMin = 146;
//				linkBegin = 146;
//				textMax = 153;
//				linkEnd = 153;
//				position = 15;
//				newline = "★★★";
//				Dialog.show();
//                Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//
//			}
//		});
//		bsmmuNotice.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bsmmu.edu.bd";
//				paramTagForText = "h3";
//				paramTagForLink = "h3 a";
//				paramLink = "abs:href";
//				textMin = 0;
//				textMax = 34;
//				linkBegin = 0;
//				linkEnd = 34;
//				position = 15;
//				newline = "★Administrative notice★";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		admission.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bsmmu.edu.bd";
//				paramTagForText = "a";
//				paramTagForLink = "a";
//				paramLink = "abs:href";
//				textMin = 155;
//				linkBegin = 155;
//				textMax = 159;
//				linkEnd = 159;
//				position = 5;
//				newline = "★★★";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		bcpsGuideline.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				bcps = new bcpsParser();
//				back = new HtmlParser();
//				paramUrl = "http://www.bcpsbd.org/notice.php";
//				paramTagForText = "a";
//				paramTagForLink = "a";
//				paramLink = "abs:href";
//				textMin = 2;
//				linkBegin = 2;
//				textMax = 4;
//				linkEnd = 4;
//				position = 15;
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				bcps.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		fcpsPart1.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bcpsbd.org/result/";
//				paramTagForText = "a";
//				paramTagForLink = "a";
//				paramLink = "abs:href";
//				textMin = 5;
//				linkBegin = 5;
//				textMax = 45;
//				linkEnd = 45;
//				position = 47;
//				newline = "★★★";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		posting.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//			    back = new HtmlParser();
//				paramUrl = "http://dghs.gov.bd/index.php/bd/?option=com_content&view=article&layout=edit&id=570";
//				paramTagForText = "#system a";
//				paramTagForLink = "#system a";
//				paramLink = "abs:href";
//				textMin = 0;
//				linkBegin = 0;
//				textMax = 4;
//				linkEnd = 4;
//				position = 5;
//				newline = "★★★";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		homelinks.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://dghs.gov.bd/index.php/bd/";
//				paramTagForText = "#system a";
//				paramTagForLink = "#system a";
//				paramLink = "abs:href";
//				textMin = 0;
//				linkBegin = 0;
//				textMax = 17;
//				linkEnd = 17;
//				position = 19;
//				newline = "★★★";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		regiBCS.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//			    pdfFilter="http://bpsc.teletalk.com.bd";
//			    browser(pdfFilter);
////				volumeParser = new VolumrParser();
////				parentUrl = "http://bpsc.teletalk.com.bd";
////				paramTagForText = "a";
////				paramTagForLink = "a";
////				paramLink = "href";
////				textMin = 0;
////				linkBegin = 0;
////				textMax = 12;
////				linkEnd = 12;
////				position = 14;
////				newline = "";
////				Dialog.show();
////				Dialog.setCancelable(false);
////				volumeParser.execute();
////				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		regiDept.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://dept.bpsc.gov.bd";
//				paramTagForText = "h5";
//				paramTagForLink = "h5 a";
//				paramLink = "href";
//				textMin = 1;
//				linkBegin = 0;
//				textMax = 7;
//				linkEnd = 7;
//				position = 8;
//				newline = "";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		regiSenior.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://snsc.bpsc.gov.bd";
//				paramTagForText = "a";
//				paramTagForLink = "a";
//				paramLink = "href";
//				textMin = linkBegin = 15;
//				textMax = linkEnd = 20;
//				position = 7;
//				newline = "wah";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		resultBCS.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://bpsc.gov.bd/site/view/psc_exam/BCS%20Examination/বিসিএস-পরীক্ষা";
//				paramTagForText = "tr";
//				paramTagForLink = "tr a";
//				paramLink = "href";
//				textMin = 1;
//				textMax = 122;
//				linkBegin = 0;
//				linkEnd = 123;
//				position = 0;
//				newline = "★ Click to download pdf:";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		resultDEPT.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Departmental%20Examination/বিভাগীয়-পরীক্ষা";
//				paramTagForText = "tr";
//				paramTagForLink = "tr td a";
//				paramLink = "href";
//				textMin = 1;
//				textMax = 26;
//				linkBegin = 0;
//				linkEnd = 26;
//				position = 28;
//				newline = "★ Click to download pdf:";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		resultSenior.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://www.bpsc.gov.bd/site/view/psc_exam/Senior%20Scale%20Examination/সিনিয়র-স্কেল-পরীক্ষা";
//				paramTagForText = "tr";
//				paramTagForLink = "tr td a";
//				paramLink = "href";
//				textMin = 1;
//				textMax = 38;
//				linkBegin = 0;
//				linkEnd = 38;
//				position = 40;
//				newline = "★ Click to download pdf:";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		forms.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				back = new HtmlParser();
//				paramUrl = "http://bpsc.gov.bd";
//				paramTagForText = "a";
//				paramTagForLink = "#box-5 a";
//				paramLink = "abs:href";
//				textMin = 77;
//				linkBegin = 0;
//				textMax = 81;
//				linkEnd = 7;
//				position = 9;
//				newline = "★ Click to dowload pdf file:";
//				Dialog.show();
//				Dialog.setCancelable(false);
//				back.execute();
//				progressBar.setVisibility(View.VISIBLE);
//			}
//		});
//		deputation.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//			    buttonTexts.add(deputation.getText().toString());
//			    urls.add("http://www.mohfw.gov.bd/index.php?option=com_docman&task=doc_download&gid=3189&lang=en");
//                Dialog.setView(m);
//                list.setAdapter(my);
//			    Dialog.show();
//                Dialog.setCancelable(false);
//                progressBar.setVisibility(View.GONE);
//			}
//		});
//		gadgettes.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                startActivity(intent);
//			}
//		});
//		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View m, int position, long l) {
//				try {
//					for (int i = 0; i < urls.size(); i++) {
//						if (position == i) {
//						    pdfFilter=urls.get(position);
//							browser(pdfFilter);
//						}
//					}
//				} catch (Exception e) {
//				}
//			}
//		});
//	}
//	public void executableTag(String Url, String TagForText, String tagForLink, String Attr, int begin, int end, int lBegin, int lEnd) {
//
//		paramUrl = Url;
//		paramTagForLink = tagForLink;
//		paramTagForText = TagForText;
//		paramLink = Attr;
//		textMin = begin;
//		textMax = end;
//		try {
//			Document doc = Jsoup.connect(Url).get();
//            Elements links = doc.select(TagForText);
//			Elements hrefs = doc.select(tagForLink);
//			for (i = begin; i < end; i++) {
//				aa = i;
//				Element link = links.get(aa);
//				btxt = link.text();
//				buttonTexts.add(btxt);
//			}
//			for (i = linkBegin; i < linkEnd; i++) {
//				aa = i;
//				Element li = hrefs.get(aa);
//				url = li.attr(Attr);
//				urls.add(url);
//
//			}
//			buttonTexts.add(position, newline);
//			urls.add(position, newline);
//		} catch (Exception e) {
//		}
//	}
//
//	public void bsmmuhome(View v) {
//		pdfFilter="http://www.bsmmu.edu.bd";
//		browser(pdfFilter);
//	}
//
//	public void hrm(View v) {
//        pdfFilter="http://hrm.dghs.gov.bd/auth/signin";
//		browser(pdfFilter);
//	}
//
//	public void bcpshome(View v) {
//        pdfFilter="http://www.bcpsbd.org";
//		browser(pdfFilter);
//	}
//
//	public void bcpsNotice(View v) {
//        pdfFilter="http://www.bcpsbd.org/notice.php";
//		browser(pdfFilter);
//	}
//
//	public void fcpsPart1Regi(View v) {
//        pdfFilter="http://www.bcpsbd.org/p1_reg/index.php";
//		browser(pdfFilter);
//	}
//
//	public void bpschome(View v) {
//        pdfFilter="http://www.bpsc.gov.bd";
//		browser(pdfFilter);
//	}
//
//	public void pds(View v) {
//        pdfFilter="http://103.247.238.123:88";
//		browser(pdfFilter);
//	}
//
//	public void NirpataHome(View v) {
//        pdfFilter="http://dghs.gov.bd/index.php/bd/";
//		browser(pdfFilter);
//
//	}
//
//	public void mohfhome(View v) {
//        pdfFilter="http://www.mohfw.gov.bd/index.php?option=com_content&view=frontpage&Itemid=1&lang=en";
//		browser(pdfFilter);
//	}
//
//	public void fcpsResults(View v) {
//        pdfFilter="http://www.bcpsbd.org/result/";
//		browser(pdfFilter);
//	}
//
//	class HtmlParser extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... params) {
//			executableTag(paramUrl, paramTagForText, paramTagForLink, paramLink, textMin, textMax, linkBegin, linkEnd);
//			return null;
//		}
//
//        @Override
//        protected void onCancelled() {
//		    buttonTexts.clear();
//		    urls.clear();
//            super.onCancelled();
//        }
//
//        @Override
//		protected void onPostExecute(Void b) {
//			super.onPostExecute(b);
//			if (url != null) {
//				progressBar.setVisibility(View.GONE);
//				list.setAdapter(my);
////                Toast toast= makeText(MainActivity.this,"long press to share link", Toast.LENGTH_SHORT);
////                toast.setGravity(Gravity.CENTER,0,0);
////                toast.show();
//			} else {
//                checkinternet = builder.create();
//                checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, int id) {
//                        buttonTexts.clear();
//                        urls.clear();
//                        url = null;
//                        Dialog.dismiss();
//                    }
//                });
//                checkinternet.setMessage("Check your network connection");
//                checkinternet.show();
//				progressBar.setVisibility(View.GONE);
//			}
//		}
//	}
//
	class bcpsParser extends AsyncTask<Void, Void, Void> {

		String URl = "http://www.bcpsbd.org/notice.php";
		String tagtext = "a";
		String taglink = "a";
		String Attrs = "onClick";
		String uRl = "";
		String prelingk = "http://www.bcpsbd.org/";

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Document doc = Jsoup.connect(URl).get();
				Elements links = doc.select(tagtext);
				Elements hrefs = doc.select(taglink);
				for (i = 5; i < hrefs.size(); i++) {
					aa = i;
					Element link = links.get(aa);
					btxt = link.text();
					buttonTexts.add(btxt);
				}
				for (i = 5; i < 100; i++) {
					aa = i;
					Element li = hrefs.get(aa);
					uRl = li.attr(Attrs);
					urls.add(stringmaker());
					//imageArray.add(url);
					//buttonTexts.add(stringmaker());
				}
			} catch (Exception e) {
			}
			return null;
		}

        @Override
        protected void onCancelled() {
		    buttonTexts.clear();
		    urls.clear();
            super.onCancelled();
        }

        public String stringmaker() {
			ArrayList<String> chars = new ArrayList<String>();
			int start = 0;
			int end = 0;
			int i = 0;
			char ch;
			String s = uRl;
			String build;
			String newstring = "";
			String finalString = "";
			String prelink = "http://www.bcpsbd.org/";
			String finalLink = "";
			for (i = 0; i < s.length(); i++) {
				ch = s.charAt(i);
				build = Character.toString(ch);
				chars.add(build);
			}
			for (int ii = 0; ii < s.length(); ii++) {
				if (chars.get(ii).equals("(")) {
					start = ii + 2;
				}
				if (chars.get(ii).equals(")")) {
					end = ii - 1;
				}
			}
			for (int ia = 0; ia < chars.size(); ia++) {
				newstring += chars.get(ia);
			}
			finalString = newstring.substring(start, end);
			finalLink = prelink + finalString;
			return finalLink;
		}

		@Override
		protected void onPostExecute(Void b) {
			super.onPostExecute(b);
			if (uRl != null) {
				progressBar.setVisibility(View.GONE);
				list.setAdapter(my);
			} else {
				checkinternet = builder.create();
				checkinternet.setButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						buttonTexts.clear();
						urls.clear();
						Dialog.dismiss();
						btxt = null;
						//imageArray.clear();
					}
				});
				try {
					checkinternet.setMessage("Check your network connection");
				} catch (Exception e) {
				}
				checkinternet.setCancelable(false);
				checkinternet.show();
			}
		}
	}
//
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu, menu);
//
//		MenuItem newi=menu.findItem(R.id.newinapp);
//		MenuItem share=menu.findItem(R.id.share);
//		 menuitem=menu.findItem(R.id.item2);
//		 newi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		SharedPreferences settings=getSharedPreferences("setting",0);
//		boolean checked=settings.getBoolean("checked",false);
//		menuitem.setChecked(checked);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//            case R.id.newinapp:
//				urls.add(null);
//                buttonTexts.add(getString(R.string.newinapp));
//				Dialog.setCancelable(false);
//				Dialog.show();
//				progressBar.setVisibility(View.GONE);
//                break;
//			case R.id.share:
//				Intent intent= new Intent(Intent.ACTION_SEND);
//				intent.setType("text/plain");
//				intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=zubayer.docsites");
//				startActivity(Intent.createChooser(intent,"Share using.."));
//				break;
//			case R.id.about:
//				urls.add(null);
//				buttonTexts.add(getString(R.string.about));
//				Dialog.show();
//				progressBar.setVisibility(View.GONE);
//
//			break;
//			case R.id.item2: if (item.getItemId()==R.id.item2) {
//				item.setChecked(!item.isChecked());
//				SharedPreferences settings = getSharedPreferences("setting", 0);
//				SharedPreferences.Editor editor = settings.edit();
//				editor.putBoolean("checked", item.isChecked());
//				editor.commit();
//			}
//			break;
//			case R.id.rate: Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites"));
//			startActivity(i);
//		}
//			return super.onOptionsItemSelected(item);
//
//	}
//    @Override
//    public void onBackPressed() {
//         boolean pressed=false;
//        if(!pressed) {
//            checkinternet = builder.create();
//            checkinternet.setButton("Send mail", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//					Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+"zubayer.developer@gmail.com"));
//					i.putExtra(Intent.EXTRA_SUBJECT,"Subject: links/problems");
//					i.putExtra(Intent.EXTRA_TEXT,"Write here your suggestion for more links/problems");
//					startActivity(i);
//                }
//            });
//            checkinternet.setButton3("Exit", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//					finish();
//                }
//            });
//            try {
//                checkinternet.setMessage(getText(R.string.exit));
//            } catch (Exception e) {
//            }
//            checkinternet.show();
//            pressed=true;
//        }else {
//            super.onBackPressed();
//        }
//    }
//    public  void browser(String inurl){
//        final String uurl=inurl;
//		if(pdfFilter.contains("pdf")){
//            try {
//                checkinternet = builder.create();
//            checkinternet.setMessage("Download pdf file? or view first");
//			checkinternet.setButton("Download", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//                    if (menuitem.isChecked()==false) {
//                        Intent intent = new Intent(MainActivity.this, Browser.class);
//                        intent.putExtra("value", uurl);
//                        startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uurl));
//                        startActivity(intent);
//                    }
//				}
//			});
//			checkinternet.setButton3("View first", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//				    Intent intents = new Intent(Intent.ACTION_VIEW,Uri.parse(driveViewer+uurl));
//                    startActivity(intents);
//				}
//
//			});
//                checkinternet.show();
//			} catch (Exception e) {
//			}
//
//		}else {
//		    try {
//                if (menuitem.isChecked() == false) {
//                    Intent intent = new Intent(MainActivity.this, Browser.class);
//                    intent.putExtra("value", inurl);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inurl));
//                    startActivity(intent);
//                }
//            }catch (Exception e){}
//        }
//
//	}
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionManager.checkResult(requestCode, permissions, grantResults);
//    }
//    class UpdateChecker extends AsyncTask<Void,Void,Void>
//    {
//        @Override
//        protected Void doInBackground(Void...params)
//        {
//            try{
//                Document doc =Jsoup.connect("https://drzubayerahmed.wordpress.com/2017/11/29/26/?preview=true").get();
//                Elements links=doc.select("p");
//                Element link=links.get(0);
//                Element message=links.get(2);
//                parseVersionCode=link.text();
//                updateMessage=message.text();
//            }catch(Exception e){
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void b)
//        {
//            super.onPostExecute(b);
//            if(parseVersionCode!=null){
//                Integer parseint=Integer.parseInt(parseVersionCode);
//                if(parseint>versionCode){
//                    SharedPreferences prefs=getSharedPreferences("updateDocSite",Context.MODE_PRIVATE);
//                    prefs.edit().putBoolean("yesno",true).commit();
//                    prefs.edit().putString("updateMessage",updateMessage).commit();
//                    checkUpdate();
//                }else  if(parseint==versionCode){
//                    SharedPreferences prefs=getSharedPreferences("updateDocSite",Context.MODE_PRIVATE);
//                    prefs.edit().putBoolean("yesno",false).commit();
//                    myToast("Your app is uptodate");
//                    checkUpdate();
//                }else if(parseint<versionCode){
//                    SharedPreferences prefs=getSharedPreferences("updateDocSite",Context.MODE_PRIVATE);
//                    prefs.edit().putBoolean("yesno",false).commit();
//                    myToast("Your app is up to date");
//                    checkUpdate();
//                }
//            }
//            else{
//                checkUpdate();
//            }
//        }
//    }
//    public  void checkUpdate(){
//        try {
//            SharedPreferences prefs = getSharedPreferences("updateDocSite", Context.MODE_PRIVATE);
//            boolean b = prefs.getBoolean("yesno", false);
//            updateMessage=prefs.getString("updateMessage",null);
//            if (b == true) {
//                checkinternet = builder.create();
//                checkinternet.setButton("Update", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface da, int but) {
//                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zubayer.docsites"));
//                        startActivity(i);
//                    }
//                });
//                checkinternet.setMessage(updateMessage);
//                checkinternet.setCancelable(false);
//                checkinternet.show();
//            }
//        }catch (Exception e){}
//    }
//    public void myToast(String toasttText){
//        Toast toast = makeText(MainActivity.this, toasttText, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
    }
}



