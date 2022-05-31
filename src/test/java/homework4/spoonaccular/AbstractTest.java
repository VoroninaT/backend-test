package homework4.spoonaccular;

import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTest {

    protected void assertJson(Object expected, Object actually) {
        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(Option.IGNORING_ARRAY_ORDER));
    }

    protected  String getResource(String name) throws  Exception {
        String resource = "/" + getClass().getSimpleName() + "/" + name;
        InputStream inputStream = getClass().getResourceAsStream(resource);
        assert inputStream != null;
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
