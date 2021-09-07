import org.junit.Assert;
import org.junit.Test;
import org.light.serialize.core.util.IdentityIntMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021/05/16
 */
public class IdentityIntMapTest {

    @Test
    public void test() {
        IdentityIntMap<String> map = new IdentityIntMap<>();

        List<String> keys = new ArrayList<>(100);
        for (int i = 0; i < 10000; i+= 3) {
            keys.add("" + i);
        }

        for (String key : keys) {
            map.put(key, Integer.valueOf(key));
        }
        System.out.println(map);
        for (String key : keys) {
            Assert.assertEquals(map.get(key, -1), Integer.valueOf(key).intValue());
        }

    }

}
