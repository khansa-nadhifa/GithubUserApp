package com.khansa.percobaanduasubmissiontiga

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        setPreferencesFromResource(R.xml.preferences, s)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val alarm = sharedPreferences.getBoolean(key, true)
        val alarmManager = AlarmReceiver()

        if (alarm) {
            alarmManager.setRepeatingAlarm(requireActivity(), "09:00", AlarmReceiver.EXTRA_MESSAGE)
        } else {
            alarmManager.cancelAlarm(requireActivity(), AlarmReceiver.ID_REPEATING)
        }
    }
}