package com.lu.magic.frame.xpreference;

import com.lu.magic.frame.xp.bean.ContractResponse;
import com.lu.magic.frame.xp.util.KxGson;

import org.junit.Test;

/**
 * @author Lu
 * @date 2025/7/13 0:17
 * @description
 */
public class GsonJavaTest {

    @Test
    public void testGson() {
        ContractResponse<Boolean> response = KxGson.getGSON().fromJson("            { \n" +
                "               \"responseId\": \"112\",\n" +
                "               \"data\": true\n" +
                "            }           ", KxGson.getType(ContractResponse.class, Boolean.TYPE)
        );

        System.out.println(response);
    }
}
