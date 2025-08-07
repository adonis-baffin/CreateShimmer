package com.adonis.createshimmer.config;

import com.adonis.createshimmer.common.CSCommon;
import plus.dragons.createdragonsplus.config.StressConfig;

public class CSStressConfig extends StressConfig {
    public CSStressConfig() {
        super(CSCommon.ID);
    }

    @Override
    protected int getVersion() {
        return 1;
    }
}
