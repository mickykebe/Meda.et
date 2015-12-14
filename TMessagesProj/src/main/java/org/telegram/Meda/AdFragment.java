package org.telegram.Meda;

import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;

/**
 * Created by Micky on 12/11/2015.
 */
public class AdFragment extends BaseFragment implements SetParent {

    public AdFragment(Bundle args) {
        super(args);
    }

    public void setParentLayout(ActionBarLayout layout) {
        if (parentLayout != layout) {
            parentLayout = layout;
            setFragmentViewParentLayout();
            if(bannerAdView != null){
                ViewGroup parent = (ViewGroup) bannerAdView.getParent();
                if(parent != null){
                    try {
                        parent.removeView(bannerAdView);
                    } catch (Exception e) {
                        FileLog.e("tmessages", e);
                    }
                }
                if (parentLayout != null && parentLayout.getContext() != bannerAdView.getContext()) {
                    bannerAdView = null;
                }
            }
            setActionBarParentLayout();
            if(parentLayout != null && bannerAdView == null){
                bannerAdView = new AdView(parentLayout.getContext());

                bannerAdView.setAdSize(AdSize.FULL_BANNER);
                bannerAdView.setAdUnitId(parentLayout.getContext().getString(R.string.ad_unit_banner));
                AdRequest adRequest = new AdRequest.Builder().
                        addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
                        addTestDevice("44D2C4A97E8DF359C698EAF1CBBA317C")
                        .build();
                bannerAdView.loadAd(adRequest);
            }
        }
    }
}
