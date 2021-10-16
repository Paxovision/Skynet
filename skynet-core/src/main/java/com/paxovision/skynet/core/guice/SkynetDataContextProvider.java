package com.paxovision.skynet.core.guice;

import com.google.inject.Provider;
import com.paxovision.skynet.core.data.SkynetDataContext;

public class SkynetDataContextProvider implements Provider<SkynetDataContext> {
    @Override
    public SkynetDataContext get() {
        return SkynetDataContext.getInstance();
    }
}