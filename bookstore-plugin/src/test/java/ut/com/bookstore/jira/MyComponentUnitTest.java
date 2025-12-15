package ut.com.bookstore.jira;

import org.junit.Test;
import com.bookstore.jira.api.MyPluginComponent;
import com.bookstore.jira.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}