/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.custom;

import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;
import android.util.Log;

import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import android.os.UserHandle;
import android.content.Context;
import android.os.ServiceManager;

public class ThemesUtils {

    public static final String PACKAGE_SYSTEMUI = "com.android.systemui";

    private static OverlayManager mOverlayService;
    public static final String TAG = "ThemesUtils";

   // Switch themes
    private static final String[] SWITCH_THEMES = {
        "com.android.system.switch.aosp", // 0
        "com.android.system.switch.contained", // 1
        "com.android.system.switch.telegram", // 2
        "com.android.system.switch.md2", // 3
        "com.android.system.switch.retro", // 4
        "com.android.system.switch.oos", // 5
        "com.android.system.switch.android12", // 6
    };

    public static final String[] STATUSBAR_HEIGHT = {
            "com.gnonymous.gvisualmod.sbh_m", // 1
            "com.gnonymous.gvisualmod.sbh_l", // 2
            "com.gnonymous.gvisualmod.sbh_xl", // 3
    };

    public static final String[] UI_RADIUS = {
            "com.gnonymous.gvisualmod.urm_r", // 1
            "com.gnonymous.gvisualmod.urm_m", // 2
            "com.gnonymous.gvisualmod.urm_l", // 3
    };
    public static void updateSwitchStyle(IOverlayManager om, int userId, int switchStyle) {
        if (switchStyle == 1) {
            stockSwitchStyle(om, userId);
        } else {
            try {
                om.setEnabled(SWITCH_THEMES[switchStyle],
                        true, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Can't change switch theme", e);
            }
        }
    }

// Method to detect whether an overlay is enabled or not
    public static boolean isThemeEnabled(String packageName) {
        mOverlayService = new OverlayManager();
        try {
            List<OverlayInfo> infos = mOverlayService.getOverlayInfosForTarget("android",
                    UserHandle.myUserId());
            for (int i = 0, size = infos.size(); i < size; i++) {
                if (infos.get(i).packageName.equals(packageName)) {
                    return infos.get(i).isEnabled();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class OverlayManager {
        private final IOverlayManager mService;

        public OverlayManager() {
            mService = IOverlayManager.Stub.asInterface(
                    ServiceManager.getService(Context.OVERLAY_SERVICE));
        }

        public void setEnabled(String pkg, boolean enabled, int userId)
                throws RemoteException {
            mService.setEnabled(pkg, enabled, userId);
        }

        public List<OverlayInfo> getOverlayInfosForTarget(String target, int userId)
                throws RemoteException {
            return mService.getOverlayInfosForTarget(target, userId);
        }
    }

    public static void stockSwitchStyle(IOverlayManager om, int userId) {
        for (int i = 0; i < SWITCH_THEMES.length; i++) {
            String switchtheme = SWITCH_THEMES[i];
            try {
                om.setEnabled(switchtheme,
                        false /*disable*/, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
