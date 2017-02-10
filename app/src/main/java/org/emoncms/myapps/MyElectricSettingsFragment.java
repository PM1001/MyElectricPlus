package org.emoncms.myapps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyElectricSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    static final String TAG = "MESETTINGSFRAGMENT";

    private String emoncmsProtocol;
    private String emoncmsURL;
    private String emoncmsAPIKEY;
    ListPreference powerFeedPreference;
    ListPreference powerFeed2Preference;
    ListPreference kWhFeedPreference;
    ListPreference geyserFeedPreference;
    ListPreference geyserFlagsFeedPreference;
    ListPreference collectorFeedPreference;
    ListPreference insideFeedPreference;
    ListPreference outsideFeedPreference;
    ListPreference voltageFeedPreference;
    ListPreference frequencyFeedPreference;
    Handler mHandler = new Handler();
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.me_preferences);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        loadValues();
        powerFeedPreference = (ListPreference) this.findPreference("myelectric_power_feed");
        powerFeed2Preference = (ListPreference) this.findPreference("myelectric_power_feed2");
        kWhFeedPreference = (ListPreference) this.findPreference("myelectric_kwh_feed");
        geyserFeedPreference = (ListPreference) this.findPreference("myelectric_geyser_feed");
        collectorFeedPreference = (ListPreference) this.findPreference("myelectric_collector_feed");
        geyserFlagsFeedPreference = (ListPreference) this.findPreference("myelectric_geyserflags_feed");
        insideFeedPreference = (ListPreference) this.findPreference("myelectric_tempinside_feed");
        outsideFeedPreference = (ListPreference) this.findPreference("myelectric_tempoutside_feed");
        voltageFeedPreference = (ListPreference) this.findPreference("myelectric_voltage_feed");
        frequencyFeedPreference = (ListPreference) this.findPreference("myelectric_frequency_feed");
        updateFeedList();
    }

    @Override
    public void onActivityCreated(Bundle savesInstanceState) {
        super.onActivityCreated(savesInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.me_settings_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("emoncms_url") || key.equals("emoncms_apikey") || key.equals("emoncms_usessl"))
        {
            loadValues();
            updateFeedList();
        }
    }

    void loadValues() {
        emoncmsProtocol = sp.getBoolean("emoncms_usessl", false) ? "https://" : "http://";
        emoncmsURL = sp.getString("emoncms_url", "");
        emoncmsAPIKEY = sp.getString("emoncms_apikey", "");
    }

    private void updateFeedList() {
        if (!emoncmsURL.equals("") && !emoncmsAPIKEY.equals(""))
            mHandler.post(mRunnable);
    }

    private Runnable mRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            String url = String.format("%s%s/feed/list.json?apikey=%s", emoncmsProtocol, emoncmsURL, emoncmsAPIKEY);

            JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                    (url, new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {

                            List<String> powerEntryList = new ArrayList<>();
                            List<String> powerEntryValueList = new ArrayList<>();

                            powerEntryList.add("AUTO");
                            powerEntryValueList.add("-1");

                            List<String> power2EntryList = new ArrayList<>();
                            List<String> power2EntryValueList = new ArrayList<>();

                            power2EntryList.add("AUTO");
                            power2EntryValueList.add("-1");

                            List<String> kwhFeedEntryList = new ArrayList<>();
                            List<String> kwhFeedEntryValueList = new ArrayList<>();

                            kwhFeedEntryList.add("AUTO");
                            kwhFeedEntryValueList.add("-1");

                            List<String> geyserFeedEntryList = new ArrayList<>();
                            List<String> geyserFeedEntryValueList = new ArrayList<>();

                            geyserFeedEntryList.add("AUTO");
                            geyserFeedEntryValueList.add("-1");

                            List<String> geyserFlagsFeedEntryList = new ArrayList<>();
                            List<String> geyserFlagsFeedEntryValueList = new ArrayList<>();

                            geyserFlagsFeedEntryList.add("AUTO");
                            geyserFlagsFeedEntryValueList.add("-1");

                            List<String> collectorFeedEntryList = new ArrayList<>();
                            List<String> collectorFeedEntryValueList = new ArrayList<>();

                            collectorFeedEntryList.add("AUTO");
                            collectorFeedEntryValueList.add("-1");

                            List<String> insideFeedEntryList = new ArrayList<>();
                            List<String> insideFeedEntryValueList = new ArrayList<>();

                            insideFeedEntryList.add("AUTO");
                            insideFeedEntryValueList.add("-1");

                            List<String> outsideFeedEntryList = new ArrayList<>();
                            List<String> outsideFeedEntryValueList = new ArrayList<>();

                            outsideFeedEntryList.add("AUTO");
                            outsideFeedEntryValueList.add("-1");

                            List<String> voltageFeedEntryList = new ArrayList<>();
                            List<String> voltageFeedEntryValueList = new ArrayList<>();

                            voltageFeedEntryList.add("AUTO");
                            voltageFeedEntryValueList.add("-1");

                            List<String> frequencyFeedEntryList = new ArrayList<>();
                            List<String> frequencyFeedEntryValueList = new ArrayList<>();

                            frequencyFeedEntryList.add("AUTO");
                            frequencyFeedEntryValueList.add("-1");

                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject row;
                                try
                                {
                                    row = response.getJSONObject(i);

                                    String id = row.getString("id");
                                    String name = row.getString("name");
                                    int engineType = row.getInt("engine");


                                    if (engineType == 2 ||
                                        engineType == 5 ||
                                        engineType == 6)
                                    {
                                        powerEntryList.add(name);
                                        powerEntryValueList.add(id);
                                        power2EntryList.add(name);
                                        power2EntryValueList.add(id);
                                        kwhFeedEntryList.add(name);
                                        kwhFeedEntryValueList.add(id);
                                        geyserFeedEntryList.add(name);
                                        geyserFeedEntryValueList.add(id);
                                        geyserFlagsFeedEntryList.add(name);
                                        geyserFlagsFeedEntryValueList.add(id);
                                        collectorFeedEntryList.add(name);
                                        collectorFeedEntryValueList.add(id);
                                        insideFeedEntryList.add(name);
                                        insideFeedEntryValueList.add(id);
                                        outsideFeedEntryList.add(name);
                                        outsideFeedEntryValueList.add(id);
                                        voltageFeedEntryList.add(name);
                                        voltageFeedEntryValueList.add(id);
                                        frequencyFeedEntryList.add(name);
                                        frequencyFeedEntryValueList.add(id);
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            CharSequence powerEntries[] = powerEntryList.toArray(new CharSequence[powerEntryList.size()]);
                            CharSequence powerEntryValues[] = powerEntryValueList.toArray(new CharSequence[powerEntryValueList.size()]);
                            CharSequence power2Entries[] = power2EntryList.toArray(new CharSequence[power2EntryList.size()]);
                            CharSequence power2EntryValues[] = power2EntryValueList.toArray(new CharSequence[power2EntryValueList.size()]);
                            CharSequence kwhFeedEntries[] = kwhFeedEntryList.toArray(new CharSequence[kwhFeedEntryList.size()]);
                            CharSequence kwhFeedEntryValues[] = kwhFeedEntryValueList.toArray(new CharSequence[kwhFeedEntryValueList.size()]);
                            CharSequence geyserFeedEntries[] = geyserFeedEntryList.toArray(new CharSequence[geyserFeedEntryList.size()]);
                            CharSequence geyserFeedEntryValues[] = geyserFeedEntryValueList.toArray(new CharSequence[geyserFeedEntryValueList.size()]);
                            CharSequence geyserFlagsFeedEntries[] = geyserFlagsFeedEntryList.toArray(new CharSequence[geyserFlagsFeedEntryList.size()]);
                            CharSequence geyserFlagsFeedEntryValues[] = geyserFlagsFeedEntryValueList.toArray(new CharSequence[geyserFlagsFeedEntryValueList.size()]);
                            CharSequence collectorFeedEntries[] = collectorFeedEntryList.toArray(new CharSequence[collectorFeedEntryList.size()]);
                            CharSequence collectorFeedEntryValues[] = collectorFeedEntryValueList.toArray(new CharSequence[collectorFeedEntryValueList.size()]);
                            CharSequence insideFeedEntries[] = insideFeedEntryList.toArray(new CharSequence[insideFeedEntryList.size()]);
                            CharSequence insideFeedEntryValues[] = insideFeedEntryValueList.toArray(new CharSequence[insideFeedEntryValueList.size()]);
                            CharSequence outsideFeedEntries[] = outsideFeedEntryList.toArray(new CharSequence[outsideFeedEntryList.size()]);
                            CharSequence outsideFeedEntryValues[] = outsideFeedEntryValueList.toArray(new CharSequence[outsideFeedEntryValueList.size()]);
                            CharSequence voltageFeedEntries[] = voltageFeedEntryList.toArray(new CharSequence[voltageFeedEntryList.size()]);
                            CharSequence voltageFeedEntryValues[] = voltageFeedEntryValueList.toArray(new CharSequence[voltageFeedEntryValueList.size()]);
                            CharSequence frequencyFeedEntries[] = frequencyFeedEntryList.toArray(new CharSequence[frequencyFeedEntryList.size()]);
                            CharSequence frequencyFeedEntryValues[] = frequencyFeedEntryValueList.toArray(new CharSequence[frequencyFeedEntryValueList.size()]);

                            if (powerEntries.length > 1 && powerEntryValues.length > 1)
                            {
                                powerFeedPreference.setEntries(powerEntries);
                                powerFeedPreference.setEntryValues(powerEntryValues);
                                powerFeedPreference.setEnabled(true);
                            }
                            if (power2Entries.length > 1 && power2EntryValues.length > 1)
                            {
                                powerFeed2Preference.setEntries(power2Entries);
                                powerFeed2Preference.setEntryValues(power2EntryValues);
                                powerFeed2Preference.setEnabled(true);
                            }
                            if (kwhFeedEntries.length > 1 && kwhFeedEntryValues.length > 1)
                            {
                                kWhFeedPreference.setEntries(kwhFeedEntries);
                                kWhFeedPreference.setEntryValues(kwhFeedEntryValues);
                                kWhFeedPreference.setEnabled(true);
                            }
                            if (geyserFeedEntries.length > 1 && geyserFeedEntryValues.length > 1)
                            {
                                geyserFeedPreference.setEntries(geyserFeedEntries);
                                geyserFeedPreference.setEntryValues(geyserFeedEntryValues);
                                geyserFeedPreference.setEnabled(true);
                            }
                            if (geyserFlagsFeedEntries.length > 1 && geyserFlagsFeedEntryValues.length > 1)
                            {
                                geyserFlagsFeedPreference.setEntries(geyserFlagsFeedEntries);
                                geyserFlagsFeedPreference.setEntryValues(geyserFlagsFeedEntryValues);
                                geyserFlagsFeedPreference.setEnabled(true);
                            }
                            if (collectorFeedEntries.length > 1 && collectorFeedEntryValues.length > 1)
                            {
                                collectorFeedPreference.setEntries(collectorFeedEntries);
                                collectorFeedPreference.setEntryValues(collectorFeedEntryValues);
                                collectorFeedPreference.setEnabled(true);
                            }
                            if (insideFeedEntries.length > 1 && insideFeedEntryValues.length > 1)
                            {
                                insideFeedPreference.setEntries(insideFeedEntries);
                                insideFeedPreference.setEntryValues(insideFeedEntryValues);
                                insideFeedPreference.setEnabled(true);
                            }
                            if (outsideFeedEntries.length > 1 && outsideFeedEntryValues.length > 1)
                            {
                                outsideFeedPreference.setEntries(outsideFeedEntries);
                                outsideFeedPreference.setEntryValues(outsideFeedEntryValues);
                                outsideFeedPreference.setEnabled(true);
                            }
                            if (voltageFeedEntries.length > 1 && voltageFeedEntryValues.length > 1)
                            {
                                voltageFeedPreference.setEntries(voltageFeedEntries);
                                voltageFeedPreference.setEntryValues(voltageFeedEntryValues);
                                voltageFeedPreference.setEnabled(true);
                            }
                            if (frequencyFeedEntries.length > 1 && frequencyFeedEntryValues.length > 1)
                            {
                                frequencyFeedPreference.setEntries(frequencyFeedEntries);
                                frequencyFeedPreference.setEntryValues(frequencyFeedEntryValues);
                                frequencyFeedPreference.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            powerFeedPreference.setEnabled(false);
                            powerFeed2Preference.setEnabled(false);
                            kWhFeedPreference.setEnabled(false);
                            geyserFeedPreference.setEnabled(false);
                            collectorFeedPreference.setEnabled(false);
                            insideFeedPreference.setEnabled(false);
                            outsideFeedPreference.setEnabled(false);
                            voltageFeedPreference.setEnabled(false);
                            frequencyFeedPreference.setEnabled(false);

                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.error)
                                    .setMessage(R.string.feed_download_error_message)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });
            jsArrayRequest.setTag(TAG);
            HTTPClient.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);
        }
    };
}