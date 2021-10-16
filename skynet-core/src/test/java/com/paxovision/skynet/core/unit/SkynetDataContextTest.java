package com.paxovision.skynet.core.unit;

import com.google.inject.Inject;
import com.paxovision.skynet.core.data.SkynetDataContext;
import com.paxovision.skynet.core.framework.ScriptBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SkynetDataContextTest extends ScriptBase {

    @Inject
    SkynetDataContext skynetDataContext;

    @Test
    public void instanceTest(){
        Assertions.assertThat(skynetDataContext).isNotNull();
    }

    @Test
    public void dataSaveTest(){
        skynetDataContext.put("key1","My Value");
        String value  = skynetDataContext.get("key1");
        Assertions.assertThat(value).isEqualTo("My Value");
    }

}
